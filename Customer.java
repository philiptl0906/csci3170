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
        orderCreation();
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
                            orderAltering();
                            inputIsValid = true;
                            break;
                        case 4:
                            orderQuery();
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
     * Customer book search - Complete
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

        //Getting ORDER_ID (+1)
        String grid = String.format(
        "select order_id "+
        "from orders "+
        "order by order_id DESC"
        );
        Statement gg = con.createStatement();
        ResultSet rem = gg.executeQuery(grid);
        rem.next();
        String order_id = Integer.toString(Integer.parseInt(rem.getString("order_id")) +1 );
        
        // Date format
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        

        List<String> isbnList = new ArrayList<String>();
        Boolean loop = false; // for the ISBN loop
        String isbn =""; 

        //Entering Customer ID
        System.out.print("Please enter your CustomerID??");
        String cid = sc.nextLine();

        //Checking ISBN

        //Getting all ISBN lists into isbnList
        String isbnchecker = String.format(
        "SELECT ISBN " +
        "from book "
        );
        Statement stisbnchecker = con.createStatement();
        ResultSet rsetisbnChecker = stisbnchecker.executeQuery(isbnchecker);
        while(rsetisbnChecker.next()){
            isbnList.add(rsetisbnChecker.getString("ISBN"));
        };

        // UI for ISBN 
        System.out.println("What books do you want to order??");
        System.out.println("Input ISBN and then quantity.");
        System.out.println("You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
        Boolean initializeOrders = false;
        //Loop for ISBN
        while(!loop){
            System.out.println("Please enter the book's ISBN:");
            isbn = sc.nextLine();
            isbn = String.valueOf(isbn);
            // System.out.println(isbn);
            String string1 = "L";
            String string2 = "l";
            String string3 = "F";
            String string4 = "f";
            //check cases

            // case 1 - L 
            if (isbn.equals(string1) || isbn.equals(string2)){
                // System.out.println("Went through L");
                
                // show the existing order
                String show = String.format(
                    "select ISBN, quantity " +
                    "from ordering " +
                    "where order_id =\"%s\" ",order_id
                );
                Statement showSt = con.createStatement();
                ResultSet stres = showSt.executeQuery(show);
                if(!stres.isBeforeFirst()){
                    System.out.println("No orders of this Order_id"); // this order_id still has no orderings yet
                }else {
                    System.out.println("ISBN            Number:");
                    while(stres.next()){
                        System.out.println(stres.getString("ISBN")+"  "+stres.getInt("quantity"));
                    }
                }
            }


            // case 2 - F
            if(isbn.equals(string3) || isbn.equals(string4)){
                // System.out.println("Went through F");
                int countPurchase = 0; //No of copies of all book ordered
                int totalBookPrice = 0; //// Total book price : Sum(unit price * qty)
                // calculate charge: 
                // Total Book Price + Shippin price 
                // Shipping price = No.of copies of all book ordered (keep count) * Unit shipping charge($10) + Handling charge($10)
                String sqlquery = String.format(
                    "select ISBN, quantity "+
                    "from ordering "+
                    "where order_id=\"%s\" ", order_id
                );
                Statement stt = con.createStatement();
                ResultSet rset1 = stt.executeQuery(sqlquery);
                if(!rset1.isBeforeFirst()){
                    //skip
                }else{
                    while(rset1.next()){
                        countPurchase += rset1.getInt("quantity");
                        String sqlquery2 = String.format(
                            "select unit_price "+
                            "from book " +
                            "where ISBN = \"%s\" ", rset1.getString("ISBN")
                        );
                        Statement stt2 = con.createStatement();
                        ResultSet rset2 = stt2.executeQuery(sqlquery2);
                        rset2.next();
                        totalBookPrice += (rset2.getInt("unit_price") * rset1.getInt("quantity"));
                    }
                }
                int charge = totalBookPrice + (countPurchase*10 +10);
                // add order to orders
                String insert = String.format(
                    "update orders " +
                    "set charge = %d " +
                    "where order_id =\"%s\" ",charge,order_id
                );
                Statement insert2 = con.createStatement();
                insert2.executeUpdate(insert);
                // System.out.println("Yo mama fat");
                loop = true;
                continue;
            }

            // case 3 - correctly inputted ISBN - Ask for quantity
            if(isbnList.contains(isbn)){
                
                // Prompt for quantity
                System.out.println("Please enter the quantity of the order: ");
                int qty = sc.nextInt();

                int copies = 0; // this is used to check for no.of available copies

                // check if qty is available
                String sql2 = String.format(
                    "select no_of_copies "+ 
                    "from book "+
                    "where ISBN =\"%s\" ",isbn
                );
                Statement st1 = con.createStatement();
                ResultSet r2 = st1.executeQuery(sql2);
                r2.next();
                copies = r2.getInt("no_of_copies");
                
                // The checking part
                if (copies >= qty){
                    //Step 1: Create empty data in orders
                    //create empty order (without the charge first) - Update when 'F'
                    if(!initializeOrders){
                    String emptyOrders = String.format(
                        "insert into orders " +
                        "values(\"%s\",\"%s\",\"%s\",%d,\"%s\") ",order_id, date,"N",0,cid
                    );
                    Statement empty = con.createStatement();
                    empty.executeUpdate(emptyOrders);
                    initializeOrders = true;
                    }
                    //Step 2: update to ordering
                    //add to ordering
                    String sqlxx = String.format(
                        "insert into ordering (order_id,ISBN,quantity) "+
                        "values (\"%s\",\"%s\",%d)",order_id,isbn,qty
                    );
                    Statement insert1 = con.createStatement();
                    insert1.executeUpdate(sqlxx);
                } else {
                    System.out.println("Quantity ordered exceed number of available quantity");
                }
        }
    }
} catch(Exception err) {
        System.err.println(err);
    }

}
private static void orderAltering(){
    try{
        System.out.println(" Please enter the OrderID that you want to change:");
        String oid = sc.nextLine();

        //Add copies to order 
        String array[]={};// create Array 
        Boolean inputValid = false;
        //Get the order and orders:
        String sql1 = String.format(
            "select * "+
            "from orders " +
            "where order_id = \"%s\" ",oid
        );
        Statement st1 = con.createStatement();
        ResultSet rs1 = st1.executeQuery(sql1);
        String x = rs1.getString("shipping_status"); // shipping status
        int counter = 1; // initialize counter
        if(!rs1.isBeforeFirst()){
            System.out.println("There is no order for this order_id");
        } else {
            rs1.next()
            System.out.print("order_id: "+oid);
            System.out.print("shipping: "+rs1.getString("shipping_status"));
            System.out.print("charge: "+rs1.getInt("charge"));
            System.out.print("customerID= "+rs1.getString("customer_id"));

            // Select all with same order ID from ordering
            String sql2 = String.format(
                "Select * "+
                "from ordering "+
                "where order_id = \"%s\" ",oid
            );
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(sql2);
            if(!rs2.isBeforeFirst()){
                System.out.println("There is no book here");
            }else {
                while(rs2.next()){
                    System.out.println("book no: "+ counter);
                    System.out.print("ISBN = "+ rs2.getString("ISBN"));
                    System.out.print("quantity= " +rs2.getString("quantity"));
                    array[counter] = rs2.getString("ISBN");
                    counter++;
                }
                int number = 0;
                while(!inputValid){
                System.out.println("Which book you want to alter (input book no.): ");
                number = sc.nextInt(); //scanning for the number
                if(number <= counter )
                    inputValid = true;
                }
                System.out.println("input add or remove");
                String decision = sc.nextLine();
                
                if(decision.equals("add")){
                // check if valid number 
                
                //valid number
                System.out.println("How many copies do you want to add?");
                int addCopies = sc.nextInt();

                //get the ISBN of the number.

                // check of number of available copies
                String noc = String.format(
                    "select no_of_copies "+
                    "from book " +
                    "where ISBN = \"%s\" ",array[number]
                );
                Statement noc1 = con.createStatement();
                ResultSet nocst = noc1.executeQuery(noc);
                nocst.next();
                if(nocst.getInt("no_of_copies")>addCopies && x.equals("N")){
                    // succesful
                    // ADD the copies to the ordering
                    String sql4 = String.format(
                        "update ordering " +
                        "set quantity = %d " +
                        "where order_id =\"%s\" ",addCopies,oid
                    );
                    Statement up1 = con.createStatement();
                    up1.executeUpdate(sql4);

                    // Decrement from the available book list
                    int newQty = nocst.getInt("no_of_copies") - addCopies;
                    String sql5 = String.format(
                        "update book " +
                        "set no_of_copies = %d " +
                        "where ISBN =\"%s\" ",newQty,array[number]
                    );
                    Statement up2 = con.createStatement();
                    up2.executeUpdate(sql5);


                } else {
                    if(nocst.getInt("no_of_copies")< addCopies)
                    System.out.println("There are no sufficient number of books");
                    if(!x.equals("N"))
                    System.out.println("The books in the order are shipped");
                }
                }
                
                // remove
                


        }

         
        }

        //Remove copies to order



    } 
    catch(Exception err){
        System.err.println(err);
    }
}


// private static String largestOrderID(){
//     String sql = String.format(
//         "select order_id "+
//         "from orders "+
//         "DESC"
//     );
//     Statement st = con.createStatement();
//     ResultSet rs = st.executeQuery(sql);
//     rs.next();
//     return rs.getString("order_id");
// }


static public class Philip{
  
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
}

