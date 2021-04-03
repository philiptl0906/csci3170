import java.util.*;
import java.io.*;
import java.sql.*;

public class System {

  public static void service(Scanner keyboard) throws Exception {

    // Scanner keyboard = new Scanner(System.in);
    // help
    services: while (true) {
      System.out.println("<This is the system interface.>");
      System.out.println("-------------------------------");
      System.out.println("1. Create Table.");
      System.out.println("2. Delete Table.");
      System.out.println("3. Insert Data.");
      System.out.println("4. Set System Date.");
      System.out.println("5. Back to main menu.\n");
      System.out.println("Please enter your choice??..");

      int input = 0;
      try {
        input = keyboard.nextInt();
      } catch (Exception e) {
        keyboard.next();
      } finally {
        switch (input) {
          case 1:
            createTable();
            break;
          case 2:
            deleteTable();
            break;
          case 3:
            insertData(keyboard);
            break;
          case 4:
            setDate();
            break;
          case 5:
            break services;
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
        "CREATE TABLE IF NOT EXISTS customer(customer_id varchar(10) PRIMARY KEY, name varchar(50) NOT NULL, shipping_address varchar(200) NOT NULL, credit_card_no varchar(19) NOT NULL);",
        "CREATE TABLE IF NOT EXISTS orders(order_id varchar(8) PRIMARY KEY, o_date DATE NOT NULL, shipping_status varchar(1) NOT NULL, charge integer NOT NULL, customer_id varchar(10) NOT NULL, CHECK(charge>=0));",
        "CREATE TABLE IF NOT EXISTS ordering(order_id varchar(8) NOT NULL, ISBN varchar(13) NOT NULL, quantity integer NOT NULL, CHECK(quantity>=0), PRIMARY KEY(order_id, ISBN), FOREIGN KEY(order_id) REFERENCES orders(order_id), FOREIGN KEY(call_number, copy_number) REFERENCES copy(call_number, copy_number));",
        "CREATE TABLE IF NOT EXISTS book_author(ISBN varchar(13) NOT NULL, author_name varchar(50) NOT NULL, PRIMARY KEY(ISBN, author_name), FOREIGN KEY(ISBN) REFERENCES book(ISBN));" };
    Connection con = LoadServer.connect();
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
    String[] deleteTables = { "book", "cusomter", "orders", "ordering", "book_author" };
    Connection con = LoadServer.connect();
    System.out.print("\rProcessing...");
    for (int i = 0; i < deleteTables.length; i++) {
      try (PreparedStatement delete = con.prepareStatement("DROP TABLE IF EXISTS " + deleteTables[i])) {
        delete.executeUpdate();
      } catch (SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
      } finally {
      }
    }
    System.out.print("Done! Tables are deleted!\n");
    con.close();
  }

  // **** doing ******
  private static void insertData(Scanner keyboard) throws Exception {
    // if all data is sucessfully loaded, then this variable remains true.If not
    // then false.
    boolean success = true;

    Connection con = LoadServer.connect();
    System.out.println("Please enter the folder path");
    String path = keyboard.next();

    System.out.print("Processing... ");
    String filename = path + "/book.txt";
    File file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split(",");
        int seats = Integer.parseInt(values[2]);
        try (PreparedStatement insert = con.prepareStatement(
            "INSERT INTO Vehicle VALUES ('" + values[0] + "', '" + values[1] + "', " + seats + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());

          success = false;
        } finally {
        }
      }
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println(e);
      success = false;
    }

    filename = path + "/drivers.csv";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split(",");
        int id = Integer.parseInt(values[0]);
        int drivingYears = Integer.parseInt(values[3]);
        try (PreparedStatement insert = con.prepareStatement("INSERT INTO Driver VALUES (" + id + ", '" + values[1]
            + "', '" + values[2] + "', " + drivingYears + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
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
      System.out.println(e);
      success = false;
    }

    filename = path + "/passengers.csv";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split(",");
        int id = Integer.parseInt(values[0]);
        try (PreparedStatement insert = con
            .prepareStatement("INSERT INTO Passenger VALUES (" + id + ", '" + values[1] + "');")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
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
      System.out.println(e);
      success = false;
    }

    filename = path + "/taxi_stops.csv";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split(",");
        int location_x = Integer.parseInt(values[1]);
        int location_y = Integer.parseInt(values[2]);
        try (PreparedStatement insert = con.prepareStatement(
            "INSERT INTO Taxi_Stop VALUES ('" + values[0] + "', " + location_x + ", " + location_y + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
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
      System.out.println(e);
      success = false;
    }

    filename = path + "/trips.csv";
    file = new File(filename);
    try {
      Scanner inputStream = new Scanner(file);
      while (inputStream.hasNext()) {
        String data = inputStream.nextLine();
        String[] values = data.split(",");
        int tid = Integer.parseInt(values[0]);
        int did = Integer.parseInt(values[1]);
        int pid = Integer.parseInt(values[2]);
        int fee = Integer.parseInt(values[7]);
        try (PreparedStatement insert = con.prepareStatement("INSERT INTO Trip VALUES (" + tid + ", " + did + ", " + pid
            + ", '" + values[3] + "', '" + values[4] + "', '" + values[5] + "', '" + values[6] + "', " + fee + ");")) {
          insert.executeUpdate();
          insert.close();
        } catch (SQLException e) {
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
      System.out.println(e);
      success = false;
    }

    if (success)
      System.out.print("Data is loaded!\n");
  }

  private static void setDate() throws Exception {
    System.out.println("Numbers of record in each table: ");

    String[] checkTables = { "Vehicle", "Passenger", "Driver", "Trip", "Request", "Taxi_Stop" };
    Connection con = LoadServer.connect();
    Statement stmt = null;
    ResultSet rs = null;
    for (int i = 0; i < checkTables.length; i++) {
      try {
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(*) AS TOTAL FROM " + checkTables[i] + ";");
        rs.next();
        System.out.println(checkTables[i] + " " + rs.getInt("TOTAL"));
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
    }
    con.close();

  }
}
