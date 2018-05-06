/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp;

/**
 *
 * @author Troels
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

class Employee implements Comparable {
  private String name;

  private double salary;

  Employee(String name, double salary) {
    this.name = name;
    this.salary = salary;
  }

  String getName() {
    return name;
  }

  double getSalary() {
    return salary;
  }

  public String toString() {
    return "Name = " + getName() + ", Salary = " + getSalary();
  }

  public int compareTo(Object o) {
    if (!(o instanceof Employee))
      throw new ClassCastException();

    Employee e = (Employee) o;

    return name.compareTo(e.getName());
  }

  static class SalaryComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      if (!(o1 instanceof Employee) || !(o2 instanceof Employee))
        throw new ClassCastException();

      Employee e1 = (Employee) o1;
      Employee e2 = (Employee) o2;

      return (int) (e1.getSalary() - e2.getSalary());
    }
  }
}

class UtilDemo4 {
  public static void main(String[] args) {
    String[] names = { "A", "B", "C", "D" };

    double[] salaries = { 2.0, 5.0, 6.0, 4.0 };

    List l = new ArrayList();

    for (int i = 0; i < names.length; i++)
      l.add(new Employee(names[i], salaries[i]));

    Collections.sort(l);

    ListIterator liter = l.listIterator();

    while (liter.hasNext())
      System.out.println(liter.next());

    Collections.sort(l, new Employee.SalaryComparator());

    liter = l.listIterator();

    while (liter.hasNext())
      System.out.println(liter.next());
  }
}
