package com.mipt.hw5;
import java.util.Objects;

public class Student {
  private final int id;
  private final String name;
  private double grade;

  public Student(int id, String name, double grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public void setGrade(double grade) {
    this.grade = grade;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getGrade() {
    return grade;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Student student = (Student) object;
    return id == student.id && name.equals(student.name) && Double.compare(student.grade, grade) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, grade);
  }
}
