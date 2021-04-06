import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemInter {
  static Scanner in = new Scanner(System.in);
  static Connection con = Julianna.connect();
  static Date sysDate;

  public static void main(String[] args) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date1 = "2000-02-01";
    sysDate = sdf.parse(date1);
    // System.out.println(sdf.format(sysDate));
    run: while (true) {
      System.out.println("<This is the system interface.>");
      System.out.println("-------------------------------");
      System.out.println("1. Create Table.");
      System.out.println("2. Delete Table.");
      System.out.println("3. Insert Data.");
      System.out.println("4. Set System Date.");
      System.out.println("5. Back to main menu.\n");
      System.out.print("Please enter your choice??..");

      int input = 0;
      try {
        input = in.nextInt();
      } catch (Exception e) {
        in.next();
      } finally {
        switch (input) {
          case 1:
            createTable();
            break;
          case 2:
            deleteTable();
            break;
          case 3:
            insertData(in);
            break;
          case 4:
            setDate(in, sysDate);
            break;
          case 5:
            break run;
          default:
            System.out.println("[ERROR] Invalid input. Please input [1-5].");
            break;
        }
      }
    }
  }

  private static void createTable() throws Exception {

    String[] createTables = {
        "CREATE TABLE IF NOT EXISTS book(ISBN varchar(13) PRIMARY KEY, title varchar(100) NOT NULL, unit_price integer NOT NULL, no_of_copies integer NOT NULL, CHECK(unit_price>=0 AND no_of_copies>=0));",
        "CREATE TABLE IF NOT EXISTS customer(customer_id varchar(10) PRIMARY KEY, cus_name varchar(50) NOT NULL, shipping_address varchar(200) NOT NULL, credit_card_no varchar(19) NOT NULL);",
        "CREATE TABLE IF NOT EXISTS orders(order_id varchar(8) PRIMARY KEY, o_date DATE NOT NULL, shipping_status varchar(1) NOT NULL, charge integer NOT NULL, customer_id varchar(10) NOT NULL, CHECK(charge>=0));",
        "CREATE TABLE IF NOT EXISTS ordering(order_id varchar(8) NOT NULL, ISBN varchar(13) NOT NULL, quantity integer NOT NULL, CHECK(quantity>=0), PRIMARY KEY(order_id, ISBN), FOREIGN KEY(order_id) REFERENCES orders(order_id));",
        "CREATE TABLE IF NOT EXISTS book_author(ISBN varchar(13) NOT NULL, author_name varchar(50) NOT NULL, PRIMARY KEY(ISBN, author_name), FOREIGN KEY(ISBN) REFERENCES book(ISBN));" };
    Connection con = Julianna.connect();
    System.out.print("Processing...");
    for (int i = 0; i < createTables.length; i++) {
      try (PreparedStatement create = con.prepareStatement(createTables[i])) {
        create.executeUpdate();
      } catch (SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
      } finally {
      }
    }
    System.out.print("Success! Tables are created.\n");
    con.close();

  }

  private static void deleteTable() throws Exception {
    String[] deleteTables = { "book", "customer", "orders", "ordering", "book_author" };
    Connection con = Julianna.connect();
    System.out.print("\rProcessing...");
    try (PreparedStatement changeCon = con.prepareStatement("SET foreign_key_checks = 0;")) {
      changeCon.executeUpdate();
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    }

    for (int i = 0; i < deleteTables.length; i++) {
      try (PreparedStatement delete = con.prepareStatement("DROP TABLE IF EXISTS " + deleteTables[i])) {
        delete.executeUpdate();
      } catch (SQLException e) {
        System.out.println("Delete Table name: " + deleteTables[i]);
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
      } finally {
      }
    }
    System.out.print("Success! Tables are deleted!\n");
    try (PreparedStatement changeOff = con.prepareStatement("SET foreign_key_checks = 1;")) {
      changeOff.executeUpdate();
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    }
    con.close();
  }

  // **** not test ******
  private static void insertData(Scanner in) throws Exception {
    // if all data is sucessfully loaded, then this variable remains true.If not
    // then false.
    boolean success = true;

    Connection con = Julianna.connect();
    System.out.println("Please enter the folder path");
    String path = in.next();

    System.out.print("Processing...");
    String filename = path + "/book.txt";
    File file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split("\\|");
        int unit_price = Integer.parseInt(values[2]);
        int no_of_copies = Integer.parseInt(values[3]);
        try (PreparedStatement insert = con.prepareStatement("INSERT INTO book VALUES ('" + values[0] + "', '"
            + values[1] + "', " + unit_price + "," + no_of_copies + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("[Error]: Book Insertion");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: File not found" + e);
      success = false;
    }

    filename = path + "/customer.txt";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split("\\|");
        try (PreparedStatement insert = con.prepareStatement("INSERT INTO customer VALUES ('" + values[0] + "', '"
            + values[1] + "', '" + values[2] + "', '" + values[3] + "');")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("[Error]: customer Insertion");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
          // System.out.print("\rProcessing... ");
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: File not found. " + e);
      success = false;
    }

    filename = path + "/orders.txt";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split("\\|");
        int charge = Integer.parseInt(values[3]);

        try (PreparedStatement insert = con.prepareStatement("INSERT INTO orders VALUES ('" + values[0] + "', '"
            + values[1] + "', '" + values[2] + "', " + charge + ", '" + values[3] + "');")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("[Error]: orders Insertion");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
          // System.out.print("\rProcessing... ");
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: File not found. " + e);
      success = false;
    }

    filename = path + "/ordering.txt";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split("\\|");
        int quantity = Integer.parseInt(values[2]);
        try (PreparedStatement insert = con.prepareStatement(
            "INSERT INTO ordering VALUES ('" + values[0] + "', '" + values[1] + "', " + quantity + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("[Error]: ordering Insertion");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
          // System.out.print("\rProcessing... ");
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: File not found. " + e);
      success = false;
    }

    filename = path + "/book_author.txt";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split("\\|");
        try (PreparedStatement insert = con
            .prepareStatement("INSERT INTO book_author VALUES ('" + values[0] + "', '" + values[1] + "');")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("[Error]: book_author Insertion");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
          // System.out.print("\rProcessing... ");
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("[Error]: File not found. " + e);
      success = false;
    }

    if (success)
      System.out.print("Data is loaded!\n");
    con.close();
  }

  // **** doing ******
  private static void setDate(Scanner in, Date sysDate) throws Exception {
    System.out.print("Please Input the date (YYYYMMDD): ");
    String year = "", month = "", day = "";
    String date = in.next();
    try {
      for (int i = 0; i < 4; i++) {
        year += date.charAt(i); // get the year
      }
      for (int i = 4; i < 6; i++) {
        month += date.charAt(i); // get the month
      }
      for (int i = 6; i < 8; i++) {
        day += date.charAt(i); // get the day
      }
    } catch (Exception e) {
      System.out.println("[Error]: the input should be a date");
      return;
    }

    String inputDate = year + "-" + month + "-" + day;
    Connection con = Julianna.connect();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = con.createStatement();
      rs = stmt.executeQuery("SELECT MAX(o_date) AS o_date FROM orders;");
      rs.next();

      String order_date = (rs.getString("o_date"));
      System.out.println("Latest date in orders: " + order_date);
      sysDate = rs.getDate("o_date"); // update the system date, if there is order made in the lastest "order date"

    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
        }
        rs = null;
      }

      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
        }
        stmt = null;
      }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date newDate = sdf.parse(inputDate);
    int compare_result = newDate.compareTo(sysDate);
    // check the input date is later than the latest date or not
    if (compare_result > 0) {
      sysDate = newDate; // the newest date used in the system
      System.out.println("Today is " + sdf.format(newDate));
    } else {
      System.out.println("[Error]: The date is not later than the lastest date in orders");
    }
    con.close();
  }
}

class Julianna {

  public static Connection connect() {
    String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db19";
    String dbUsername = "Group19";
    String dbPassword = "CSCI3170";

    Connection con = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
    } catch (ClassNotFoundException e) {
      System.out.println("[Error]: Java MySQL DB Driver not found!!");
      System.exit(0);
    } catch (SQLException e) {
      System.out.println(e);
    }
    return con;
  }
}