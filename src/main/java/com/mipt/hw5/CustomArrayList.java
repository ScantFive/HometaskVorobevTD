package com.mipt.hw5;

import java.util.Iterator;


public class CustomArrayList<A> implements CustomList<A> {
  private static final double RATIO = 1.5;
  private Object[] array;
  private int capacity = 8;
  private int size;

  public CustomArrayList() {
    array = new Object[capacity];
    size = 0;
  }

  @Override
  public void add(A element) {
    if (element == null) {
      throw new NullPointerException();
    }
    if (size >= capacity) {
      capacity *= RATIO;
      Object[] newArray = new Object[capacity];
      for (int i = 0; i < size; i++) {
        newArray[i] = array[i];
      }
      array = newArray;
    }
    array[size] = element;
    size++;
  }

  @Override
  public A get(int index) {
    if (index < 0 || index >= size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return (A) array[index];
  }

  @Override
  public A remove(int index) {
    if (index >= size || index < 0) {
      throw new ArrayIndexOutOfBoundsException();
    }
    A removedElement = (A) array[index];
    Object[] newArray = new Object[capacity];
    for (int i = 0; i < index; i++) {
      newArray[i] = array[i];
    }
    for (int i = index + 1; i < size; i++) {
      newArray[i - 1] = array[i];
    }
    array = newArray;
    array[size - 1] = null;
    size--;
    return removedElement;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public Iterator<A> iterator() {
    return new CustomArrayListIterator();
  }

  private class CustomArrayListIterator implements Iterator<A> {
    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < size;
    }

    public A next() {
      if (hasNext()) {
        throw new ArrayIndexOutOfBoundsException();
      }
      A element = (A) array[index];
      index++;
      return element;
    }
  }
}
