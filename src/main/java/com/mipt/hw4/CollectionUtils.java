package com.mipt.hw4;
import java.util.*;

public class CollectionUtils {
  public static <T> List<T> mergeLists(List<? extends T> list1,
                                       List<? extends T> list2) {
    List<T> newList = new ArrayList<>();
    newList.addAll(list1);
    newList.addAll(list2);
    return newList;
  }

  public static <T> void addAll(List<? super T> destination,
                                List<? extends T> source) {
    destination.addAll(source);
  }

  public static void main(String[] args) {
    final List<Integer> list1 = Arrays.asList(1, 2, 3);
    final List<Double> list2 = Arrays.asList(4.5, 5.6);
    final List<Number> merged = CollectionUtils.mergeLists(list1, list2);
    System.out.println(merged);

    final List<Object> destination = new ArrayList<>();
    CollectionUtils.addAll(destination, list1);
    System.out.println(destination);

  }
}