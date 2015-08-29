package TestMessageDigest;

import java.io.*;
import java.util.*;

/**
 * @version 1.12 2007-06-22
 * @author Cay Horstmann
 */
public class TextFileTest
{
   public static void main(String[] args)
   {
      Employee[] staff = new Employee[3];

      staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
      staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

      try
      {
         // save all employee records to the file employee.dat
         PrintWriter out = new PrintWriter("employee.dat");   //这个语句就会在相对路径下，新建一个文件 employee.dat, 这个相对路径在Book文件夹下
         
         /* File aFile=new File("d:\\workspace\\Book\\src\\afile.txt");    //这个注释掉的语句会在src文件夹里面新建一个afile.txt
                                                                          //但是注意路径的分割符必须是\\
          PrintWriter out1=new PrintWriter(aFile) ;    */ 
         
          writeData(staff, out);
          out.close();                  // 如果把close注释掉整个程序就会出错了。会出错在，第34行 就是in就读不进来

         // retrieve all records into a new array
         Scanner in = new Scanner(new FileReader("employee.dat"));
         Employee[] newStaff = readData(in);
         in.close();

         // print the newly read employee records
         /*for (Employee e : newStaff)
            System.out.println(e);*/            //System.out 这个对象的println才是输出到控制台的，如果
                                              //注释掉的话，就不会输出到控制台了。
      }
      catch (IOException exception)
      {
         exception.printStackTrace();
      }
  
	String aString=System.getProperty("user.dir");    // 这个语句就是测试当前相对路径的起点在哪里
	System.out.println(aString);
	String sString=System.getProperty("file.separator");
	System.out.println(sString);
   }

   /**
    * Writes all employees in an array to a print writer
    * @param employees an array of employees
    * @param out a print writer
    */
   private static void writeData(Employee[] employees, PrintWriter out) throws IOException
   {
      // write number of employees
      out.println(employees.length);

      for (Employee e : employees)
         e.writeData(out);
   }

   /**
    * Reads an array of employees from a scanner
    * @param in the scanner
    * @return the array of employees
    */
   private static Employee[] readData(Scanner in)
   {
      // retrieve the array size
      int n = in.nextInt();
      in.nextLine(); // consume newline

      Employee[] employees = new Employee[n];
      for (int i = 0; i < n; i++)
      {
         employees[i] = new Employee();
         employees[i].readData(in);
      }
      return employees;
   }
}










class Employee
{
   public Employee()
   {
   }

   public Employee(String n, double s, int year, int month, int day)
   {
      name = n;
      salary = s;
      GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
      hireDay = calendar.getTime();
   }

   public String getName()
   {
      return name;
   }

   public double getSalary()
   {
      return salary;
   }

   public Date getHireDay()
   {
      return hireDay;
   }

   public void raiseSalary(double byPercent)
   {
      double raise = salary * byPercent / 100;
      salary += raise;
   }

   public String toString()
   {
      return getClass().getName() + "[name=" + name + ",salary=" + salary + ",hireDay=" + hireDay
            + "]";
   }

   /**
    * Writes employee data to a print writer
    * @param out the print writer
    */
   public void writeData(PrintWriter out)
   {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(hireDay);
      out.println(name + "|" + salary + "|" + calendar.get(Calendar.YEAR) + "|"
            + (calendar.get(Calendar.MONTH) + 1) + "|" + calendar.get(Calendar.DAY_OF_MONTH));
   }

   /**
    * Reads employee data from a buffered reader
    * @param in the scanner
    */
   public void readData(Scanner in)
   {
      String line = in.nextLine();
      String[] tokens = line.split("\\|");
      name = tokens[0];
      salary = Double.parseDouble(tokens[1]);
      int y = Integer.parseInt(tokens[2]);
      int m = Integer.parseInt(tokens[3]);
      int d = Integer.parseInt(tokens[4]);
      GregorianCalendar calendar = new GregorianCalendar(y, m - 1, d);
      hireDay = calendar.getTime();
   }

   private String name;
   private double salary;
   private Date hireDay;
}
