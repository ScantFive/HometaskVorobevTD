package com.mipt.hw4;

public class Calculator<T extends Number> {
  public double sum(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() + b.doubleValue();
  }

  public double subtract(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() - b.doubleValue();
  }

  public double multiply(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() * b.doubleValue();
  }

  public double divide(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    if (b.doubleValue() == 0.0) {
      return Double.NaN;
    }
    return a.doubleValue() / b.doubleValue();
  }

  public static void main(String[] args) {
    // пример использования
    final Calculator<Integer> intCalc = new Calculator<>();
    final double result = intCalc.sum(5, 3); // 8.0
    System.out.println(result);
    final Calculator<Double> doubleCalc = new Calculator<>();
    final double div = doubleCalc.divide(10.0, 4.0); // 2.5
    System.out.println(div);
    System.out.println(doubleCalc.sum(null, null));
    System.out.println(doubleCalc.subtract(null, null));
    System.out.println(doubleCalc.multiply(null, null));
    System.out.println(doubleCalc.divide(null, null));
    System.out.println(doubleCalc.divide(2.0, 0.0));
    System.out.println(doubleCalc.divide(6.0, 2.0));
    System.out.println(doubleCalc.multiply(2.0, 0.0));
    System.out.println(doubleCalc.multiply(2.0, 5.0));
    System.out.println(doubleCalc.sum(2.0, 5.0));
    System.out.println(doubleCalc.subtract(2.0, 5.0));

  }
}