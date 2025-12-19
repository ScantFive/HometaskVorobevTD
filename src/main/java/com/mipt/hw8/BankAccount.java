package com.mipt.hw8;

import java.util.UUID;

public class BankAccount {

  private final UUID id;
  private int balance;

  public BankAccount(int startBalance) {
    this.id = UUID.randomUUID();
    balance = startBalance;
  }

  public UUID getId() {
    return id;
  }

  public double getBalance() {
    return balance;
  }

  public void deposit(int moneyAmount) {
    if (moneyAmount <= 0) throw new IllegalArgumentException();
    balance += moneyAmount;
  }

  public void withdraw(int moneyAmount) {
    if (moneyAmount <= 0) throw new IllegalArgumentException();
    if (moneyAmount > balance) throw new IllegalArgumentException("Недостаточно средств");
    balance -= moneyAmount;
  }
}
