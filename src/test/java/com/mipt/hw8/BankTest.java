package com.mipt.hw8;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTest {

  private Bank bank;
  private BankAccount account1;
  private BankAccount account2;

  @BeforeEach
  void setUp() {
    bank = new Bank();
    account1 = new BankAccount(1000);
    account2 = new BankAccount(1000);
  }

  @Test
  void testValidation() {
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, account2, 100));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(account1, null, 100));

    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(account1, account2, -100));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(account1, account2, 0));
  }

  @Test
  void testConcurrentTransfersNoDeadlock() throws InterruptedException {
    int threadCount = 10;
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);
    AtomicInteger successCount = new AtomicInteger(0);

    for (int i = 0; i < threadCount; i++) {
      new Thread(() -> {
        try {
          startLatch.await();

          for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
              bank.sendToAccount(account1, account2, 1);
            } else {
              bank.sendToAccount(account2, account1, 1);
            }
            successCount.incrementAndGet();
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          endLatch.countDown();
        }
      }).start();
    }

    startLatch.countDown();
    endLatch.await();

    assertEquals(threadCount * 10, successCount.get());
    assertEquals(2000, account1.getBalance() + account2.getBalance());
  }

  @Test
  void testInsufficientFunds() {
    BankAccount poorAccount = new BankAccount(50);

    assertThrows(IllegalArgumentException.class,
      () -> bank.sendToAccount(poorAccount, account2, 100));
  }

  @Test
  void testSingleTransfer() {
    bank.sendToAccount(account1, account2, 300);

    assertEquals(700, account1.getBalance());
    assertEquals(1300, account2.getBalance());
  }

  @Test
  void testDeadlock() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(2);
    AtomicBoolean deadlockOccurred = new AtomicBoolean(false);

    Thread thread1 = new Thread(() -> {
      synchronized (account1) {
        System.out.println("Поток 1 заблокировал account1");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
        synchronized (account2) {
          System.out.println("Поток 1 заблокировал account2");
          account1.withdraw(100);
          account2.deposit(100);
        }
      }
      latch.countDown();
    });

    Thread thread2 = new Thread(() -> {
      synchronized (account2) {
        System.out.println("Поток 2 заблокировал account2");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
        synchronized (account1) {
          System.out.println("Поток 2 заблокировал account1");
          account2.withdraw(100);
          account1.deposit(100);
        }
      }
      latch.countDown();
    });

    thread1.start();
    thread2.start();

    boolean completed = latch.await(2, TimeUnit.SECONDS);

    if (!completed) {
      deadlockOccurred.set(true);
      System.out.println("DEADLOCK! Потоки заблокированы");
      thread1.interrupt();
      thread2.interrupt();
    }

    assertTrue(deadlockOccurred.get(), "Deadlock должен был произойти!");
  }

}