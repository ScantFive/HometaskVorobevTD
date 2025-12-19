package com.mipt.hw8;

public class Bank {

  public void sendAccountToDeadlock(BankAccount sender, BankAccount recipient, int moneyAmount) {
    if ((sender == null) || (recipient == null)) throw new IllegalArgumentException();
    if (moneyAmount <= 0) throw new IllegalArgumentException();

    synchronized (sender) {
      synchronized (recipient) {
        sender.withdraw(moneyAmount);
        recipient.deposit(moneyAmount);
      }
    }
  }

  public void sendToAccount(BankAccount sender, BankAccount recipient, int moneyAmount) {
    if ((sender == null) || (recipient == null)) throw new IllegalArgumentException();
    if (moneyAmount <= 0) throw new IllegalArgumentException();

    BankAccount first = (sender.getId().compareTo(recipient.getId())) < 0 ? sender : recipient;
    BankAccount second = (sender.getId().compareTo(recipient.getId())) < 0 ? recipient : sender;

    synchronized (first) {
      synchronized (second) {
        sender.withdraw(moneyAmount);
        recipient.deposit(moneyAmount);
      }
    }
  }
}
