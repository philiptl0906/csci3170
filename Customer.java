import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.sql.*;
import java.util.*; 

public class Customer {
    static Scanner sc = new Scanner(System.in);
    static Connection con = Philip.connect();
    public static void main(String[] args) {
        System.out.println("Hello world!");
        orderQuery();
    }
    public static void run() { 

        boolean custApplicationIsRunning = true;

        while(custApplicationIsRunning) {
            printCustMenu();

            boolean inputIsValid = false;

            while(!inputIsValid) {
                try {
                    int custAction = sc.nextInt();
    
                    switch(custAction) {
                        case 1:
                            bookSearch(); 
                            inputIsValid = true;
                            break;
                        case 2:
                            orderCreation(); 
                            inputIsValid = true;
                            break;
                        case 3:
                            //orderAltering();
                            inputIsValid = true;
                            break;
                        case 4:
                            //orderQuery();
                            inputIsValid = true;
                            break;   
                        case 5:
                            custApplicationIsRunning = false;
                            inputIsValid = true;
                            break;
                        default:
                            System.err.println("INVALID INPUT");
                    }
                } catch (Exception err) {
                    System.err.println("INVALID INPUT");
                    sc.next();
                }
            }
        }
    }

    // print menu for Customer - done
    private static void printCustMenu() {
        System.out.println("<This is the customer interface.>");
        System.out.println("1. Book Search");
        System.out.println("2. Order Creation");
        System.out.println("3. Order Altering");
        System.out.println("4. Order Query");
        System.out.println("5. Back to main menu.");
        System.out.println("What is your choice??..");
    }

    /**
     * Customer book search - not completed yet
     */
    private static void printCustBSMenu() {
        System.out.println("What do u want to search??");
        System.out.println("1. ISBN");
        System.out.println("2. Book Title");
        System.out.println("3. Author Name");
        System.out.println("4. Exit");
        System.out.println("Your Choice?...");
        
    }
    private static void bookSearch() {

      boolean custBSApplicationIsRunning = true;

      while(custBSApplicationIsRunning) {
          printCustBSMenu(); 

          boolean inputIsValid = false;

          while(!inputIsValid) {
              try {
                  int custBSAction = sc.nextInt();
  
                  switch(custBSAction) {
                      case 1:
                          bookSearchISBN();
                          inputIsValid = true;
                          break;
                      case 2:
                          bookSearchBT();
                          inputIsValid = true;
                          break;
                      case 3:
                          bookSearchAN();
                          inputIsValid = true;
                          break; 
                      case 4:
                          custBSApplicationIsRunning = false;
                          inputIsValid = true;
                          break;
                      default:
                          System.err.println("INVALID INPUT");
                  }
              } catch (Exception err) {
                  System.err.println("INVALID INPUT");
                  sc.next();
              }
          }
      }
  }
  private static void bookSearchISBN() { // Search by ISBN - Complete, TESTED. debug: add for user input
    
    try {
      String isbncharacter = sc.nextLine();
      String mysqlStatement = String.format(
          "SELECT book.ISBN, book.title, book.unit_price, book.no_of_copies, book_author.author_name " + 
          "from book inner join book_author on book.ISBN = book_author.ISBN " +
          "where book.ISBN=\"%s\" " +
          "order by book.title, book.ISBN, book_author.author_name " , isbncharacter
      );
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(mysqlStatement);

      if(!rs.isBeforeFirst()) {
          System.out.println("No records found.");
      } else {
          rs.next();
          int counter =1;
              System.out.println("Record " + counter);
              System.out.println("ISBN: " + rs.getString("ISBN"));
              System.out.println("Book Title: " + rs.getString("title"));
              System.out.println("Unit Price: " +rs.getString("unit_price"));
              System.out.println("No Of Available: "+rs.getString("no_of_copies"));
              System.out.println("Authors: ");
              System.out.println(counter + ": " +rs.getString("author_name"));
              counter++;
              while(rs.next()){
              System.out.println(counter + ": " + rs.getString("author_name"));
              counter++;
          }
      }
  } catch(Exception err) {
      System.err.println(err);
  }
}
  private static void bookSearchBT() { // Search by book title - Complete and tested need debugging to get user input
    try {
        String bookTitle = sc.nextLine(); // request for user input
        String exactMatchBT = "";
        List<String> exactISBN = new ArrayList<String>();
        int counter =1;
        for(char c : bookTitle.toCharArray()) { // check if same or not ( Exact Match or not )
            if(c == '%' || c == '_'){
                // skip c 
            }
            else  exactMatchBT += c;
        }

        if (exactMatchBT != bookTitle) { // If there is wild card
            String mysqlStatement = String.format(
                "select b.ISBN, b.title, b.unit_price, b.no_of_copies " + 
                "from book b " +
                "where b.title=\"%s\" " +
                "order by b.title, b.ISBN ", exactMatchBT
            );
            Statement stmt = con.createStatement();
            ResultSet s1 = stmt.executeQuery(mysqlStatement);
        
            if(!s1.isBeforeFirst()) {
                //skip
            } else {
                while(s1.next()) {
                    int counterAuthor = 1;
                    String isbn = s1.getString("ISBN");
                    exactISBN.add(isbn);
                    System.out.println("Record " + counter);
                    System.out.println("ISBN: " + s1.getString("ISBN"));
                    System.out.println("Book Title: " + s1.getString("title"));
                    System.out.println("Unit Price: " +s1.getString("unit_price"));
                    System.out.println("No Of Available: "+s1.getString("no_of_copies"));
                    String sqlauthors = String.format(
                        "select author_name "+
                        "from book_author "+
                        "where ISBN=\"%s\" " +
                        "order by author_name", isbn
                    );
                    Statement stmtx = con.createStatement();
                    ResultSet lol = stmtx.executeQuery(sqlauthors);
                    while(lol.next()){
                        System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                        counterAuthor++;

                    };
                    counter++;
                }
            }
        } 
        
            String mysqlStatement = String.format(
                "select b.ISBN, b.title, b.unit_price, b.no_of_copies " + 
                "from book b " +
                "where b.title LIKE \"%s\" " +
                "order by b.title, b.ISBN ", bookTitle
            );
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(mysqlStatement);
  
        if(!rs.isBeforeFirst()) {
            System.out.println("No records found.");
        } else {
            while(rs.next()) {
                    int counterAuthor = 1;
                    String isbn = rs.getString("ISBN");
                    if(exactISBN.contains(isbn)) continue;
                    System.out.println("Record " + counter);
                    System.out.println("ISBN: " + rs.getString("ISBN"));
                    System.out.println("Book Title: " + rs.getString("title"));
                    System.out.println("Unit Price: " + rs.getString("unit_price"));
                    System.out.println("No Of Available: "+ rs.getString("no_of_copies"));
                    String sqlauthors = String.format(
                        "select author_name "+
                        "from book_author "+
                        "where ISBN=\"%s\" " +
                        "order by author_name", isbn
                    );
                    Statement stmtx = con.createStatement();
                    ResultSet lol = stmtx.executeQuery(sqlauthors);
                    while(lol.next()){
                        System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                        counterAuthor++;

                    };
                    counter++;
            }
        }
    } catch(Exception err) {
        System.err.println(err);
    }
  }
  private static void bookSearchAN() { // Done and tested - need debugging to prompt
    try {
        String authorName = sc.nextLine();
        String exactMatchAN = "";
        List<String> exactAN = new ArrayList<String>();
        int counter =1;
        for(char c : authorName.toCharArray()) {
            if(c == '%' || c == '_'){
                // skip c 
            }
            else  exactMatchAN += c;
        }
        if (exactMatchAN != authorName) { // If there is wild card
            String mysqlStatement = String.format(
                "select ISBN, author_name " + 
                "from book_author " +
                "where author_name=\"%s\" " +
                "order by ISBN ", exactMatchAN
            );
            Statement stmt = con.createStatement();
            ResultSet s1 = stmt.executeQuery(mysqlStatement);
        
            if(!s1.isBeforeFirst()) {
                //skip
            } else {
                while(s1.next()) {
                    int counterAuthor = 1;
                    String isbn = s1.getString("ISBN");
                    exactAN.add(s1.getString("ISBN"));
                    String sql1 = String.format(
                        "select ISBN, title, unit_price, no_of_copies "+
                        "from book " +
                        "where ISBN = \"%s\" "+
                        "order by title, ISBN", isbn
                    );
                    Statement stmtxx = con.createStatement();
                    ResultSet s1xx = stmtxx.executeQuery(sql1);
                    while(s1xx.next()){
                    System.out.println("Record " + counter);
                    System.out.println("ISBN: " + s1xx.getString("ISBN"));
                    System.out.println("Book Title: " + s1xx.getString("title"));
                    System.out.println("Unit Price: " +s1xx.getString("unit_price"));
                    System.out.println("No Of Available: "+s1xx.getString("no_of_copies"));
                    String sqlauthors = String.format(
                        "select author_name "+
                        "from book_author "+
                        "where ISBN=\"%s\" " +
                        "order by author_name", isbn
                    );
                    Statement stmtx = con.createStatement();
                    ResultSet lol = stmtx.executeQuery(sqlauthors);
                    while(lol.next()){
                        System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                        counterAuthor++;

                    };
                    counter++;
                }
                }
            }
        } 
        
            String mysqlStatement = String.format(
                "select ISBN, author_name " + 
                "from book_author " +
                "where author_name LIKE \"%s\" " +
                "order by ISBN ", authorName
            );
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(mysqlStatement);
  
        if(!rs.isBeforeFirst()) {
            System.out.println("No records found.");
        } else {
            while(rs.next()) {
                    int counterAuthor = 1;
                    String isbn = rs.getString("ISBN");
                    if(exactAN.contains(isbn)) continue;
                    exactAN.add(rs.getString("ISBN"));
                    String sql2 = String.format(
                        "select ISBN, title, unit_price, no_of_copies "+
                        "from book " +
                        "where ISBN = \"%s\" "+
                        "order by title, ISBN", isbn
                    );
                    Statement stmtxx2 = con.createStatement();
                    ResultSet s2xx = stmtxx2.executeQuery(sql2);
                    while(s2xx.next()){
                    System.out.println("Record " + counter);
                    System.out.println("ISBN: " + s2xx.getString("ISBN"));
                    System.out.println("Book Title: " + s2xx.getString("title"));
                    System.out.println("Unit Price: " + s2xx.getString("unit_price"));
                    System.out.println("No Of Available: "+ s2xx.getString("no_of_copies"));
                    String sqlauthors = String.format(
                        "select author_name "+
                        "from book_author "+
                        "where ISBN=\"%s\" " +
                        "order by author_name", isbn
                    );
                    Statement stmtx = con.createStatement();
                    ResultSet lol = stmtx.executeQuery(sqlauthors);
                    while(lol.next()){
                        System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                        counterAuthor++;

                    };
                    counter++;
            }
        }
        }
        
    } catch(Exception err) {
        System.err.println(err);
    }
  }

  //Ordering Query
  private static void orderQuery(){ // Done and tested
      try{
        System.out.println("Please Input Customer ID:");
        String cid = sc.nextLine();
        System.out.println("Please Input the Year:");
        String thisYear = sc.nextLine();
        int counter = 1;
          String mysqlStatement = String.format(
              "select order_id, o_date, shipping_status, charge "+
              "from orders "+
              "where year(o_date) = \"%s\" and customer_id = \"%s\" " +
              "order by order_id", thisYear, cid
          );
          Statement stmt = con.createStatement();
          ResultSet rs = stmt.executeQuery(mysqlStatement);
          if(!rs.isBeforeFirst()){
              System.out.println("Record not found");
          }else{
              while(rs.next()){
                System.out.println("Record: " + counter);
                System.out.println("OrderID :" +rs.getString("order_id"));
                System.out.println("OrderDate :" + rs.getDate("o_date"));
                System.out.println("Charge: " + rs.getInt("charge"));
                System.out.println("Shipping Status: " + rs.getString("shipping_status"));
                System.out.println("");
                counter++;
              };
          }
      }catch(Exception err) {
        System.err.println(err);
    }
  }

  // Creating orders
  private static void orderCreation() {
    try { 
        List<String> isbnList = new ArrayList<String>();
        Boolean inputValid = false;
        String isbn =""; //initialization
        System.out.print("Please enter your CustomerID??");
        String cid = sc.nextLine();
        String mysqlStatemen1 = String.format(
            "select order_id " +
            "from orders " +
            "where customer_id = \"%s\" ", cid
        );
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(mysqlStatemen1);
        if(!rs.isBeforeFirst()){
            System.out.println("Record not found");
        }else{
            while(rs.next()){
                String mysqlStatement2 = String.format(
                "select order_id, ISBN " +
                "from ordering " +
                "where order_ID =\"%s\" ",rs.getString("order_id")
            );
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(mysqlStatement2);
            if(!rs2.isAfterLast()){
                System.out.println("Record not found");
            }else{
                while(rs2.next()){
                    isbnList.add(rs2.getString("ISBN"));
                }
            }
            }
        }
        System.out.println("What books do you want to order??");
        System.out.println("Input ISBN and then quantity.");
        System.out.println("You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
        while(!inputValid){
        System.out.println("Please enter the book's ISBN:");
        isbn = sc.nextLine();
        //check cases
        // case 1
        if (isbn =="L" || isbn == "l"){
            // show the existing order
        }

        // case 2
        if(isbn =="F" || isbn == "f"){
            // Finish ordering -> while bigger loop
        }

        // case 3 - correctly inputted ISBN
        if(isbnList.contains(isbn)){
            inputValid = true; 
        }
        }
        // if Isbn entered correctly
        System.out.println("Please enter the quantity of the order: ");
        int qty = sc.nextInt();
        // check if the qty is available for the ISBN number
        

        
    } catch(Exception err) {
        System.err.println(err);
    }

  }
}
class Philip{
  
    public static Connection connect() {
      String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db19";
      String dbUsername = "Group19"; 
      String dbPassword = "CSCI3170";
      
      Connection con = null;
      try{
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
      } catch (ClassNotFoundException e){
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0);
      } catch (SQLException e){
        System.out.println(e);
      }
      return con;
    }
}
    
