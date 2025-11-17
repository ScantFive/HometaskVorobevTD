package com.mipt.hw5;

public interface CustomList<A> extends Iterable<A> {
  void add(A element);

  A get(int index);

  A remove(int index);

  int size();

  boolean isEmpty();
}
