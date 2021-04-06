import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.*;

// not tested
public class Bookstore {
    public static void main(String[] args) {
        run();
    }

    // not tested
    private static void OrUpdate() {
        Scanner in = new Scanner(System.in);
        String input = "0";
        String userin;

        Connection con = Connect.connect();
        Statement stmt = null;
        ResultSet resultSet = null;
        Statement upstmt = null;
        ResultSet upresultSet = null;
        System.out.print("Please enter the order ID: ");
        while (true) {
            try {
                input = in.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid order ID!!");
                continue;
            }
            int len = input.length();
            if (len == 8) {
                break;
            } else {
                System.out.println("Invalid order ID!!");
            }

        }
        String psql = "SELECT O.shipping_status, S.quantity FROM orders O , ordering S WHERE O.order_id = %s AND S.order_id = O.order_id";
        String aUserInputOrderID = input;
        String sql = String.format(psql, aUserInputOrderID);
        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(sql);
            int quantity = 0;
            String status = "";
            while (resultSet.next()) {
                status = resultSet.getString("shipping_status");
                quantity += resultSet.getInt("quantity");
            }
            System.out.println(
                    "the shipping status of " + input + " is " + status + " and " + quantity + " books ordered");
            con.close();

            if (status.equals("N")) {
                if (quantity >= 1) {
                    while (true) {
                        System.out.print("Are you sure to update the shipping status? (Yes=Y)");
                        try {
                            userin = in.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid input!!");
                            continue;
                        }
                        if (!userin.equals("Y") && !userin.equals("N")) {
                            System.out.println("Invalid input!!");
                        } else if (userin.equals("Y")) {
                            con = Connect.connect();

                            String update = "UPDATE orders SET shipping_status = 'Y' WHERE order_id = %s";
                            String upsql = String.format(update, input);
                            try {
                                upstmt = con.createStatement();
                                upstmt.executeUpdate(upsql);
                                System.out.print("Update shiping status");
                            } catch (SQLException e) {
                                System.out.println("SQLException: " + e.getMessage());
                                System.out.println("SQLState: " + e.getSQLState());
                                System.out.println("VendorError: " + e.getErrorCode());
                            }
                            break;
                        } else if (userin.equals("N")) {
                            break;
                        }
                    }
                } else {
                    System.out.println("You can't update the status!");
                }

            } else if (status.equals("Y")) {
                System.out.println("You can't update the status!");
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    stmt = null;
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    resultSet = null;
                }

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                con = null;
            }
        }
    }

    // not tested
    private static void OrQuery() {
        Scanner in = new Scanner(System.in);
        String input = null;
        int total = 0;
        Connection con = Connect.connect();
        Statement stmt = null;
        ResultSet resultSet = null;
        System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
        while (true) {
            input = in.nextLine();
            if (input.matches("\\d{4}-\\d{2}")) {
                String psql = "SELECT * FROM orders WHERE shipping_status = 'Y' AND o_date REGEXP '^%s'";// Not sure can
                                                                                                         // we use
                                                                                                         // REGEXP like
                                                                                                         // this
                String aUserInputODate = input;
                String sql = String.format(psql, aUserInputODate);
                try {
                    stmt = con.createStatement();
                    resultSet = stmt.executeQuery(sql);
                    int i = 1;

                    while (resultSet.next()) {
                        System.out.println("");
                        System.out.println("");
                        System.out.println("Record : " + i);
                        System.out.println("order_id : " + resultSet.getString("order_id"));
                        System.out.println("customer_id : " + resultSet.getString("customer_id"));
                        System.out.println("date : " + resultSet.getDate("o_date"));
                        System.out.println("chage : " + resultSet.getInt("charge"));// i know i type the wrong spelling,
                                                                                    // but the spec spell like this, so
                                                                                    // i followed
                        total += resultSet.getInt("charge");
                        i++;
                    }
                } catch (SQLException e) {
                    System.out.println("SQLException: " + e.getMessage());
                    System.out.println("SQLState: " + e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                } finally {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("Total charges of the month is " + total);
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            stmt = null;
                        }
                    }
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            resultSet = null;
                        }

                    }
                }
                break;
            } else {
                System.out.println("Invalid input !!");
                continue;
            }

        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                con = null;
            }
        }
    }

    private static void Npop(Scanner in) {
        try{

            Connection sm = Philip.connect();
            Statement Philip = sm.createStatement();
    
            System.out.println("Please input the N popular books number: ");
    
            int number = -1;
            int numberCounter = 0;
            int bookCounter = 0;
            int loopCounter = 0 ;
    
            // Get the total number of different book records
            String mysql1 = String.format(
                "Select * " + 
                "from ordering "+
                "order by ISBN"
            );
            String isbnA = "";
            String isbnB = "";
            ResultSet rs1 = Philip.executeQuery(mysql1);
            if(!rs1.isBeforeFirst()) System.out.println("There are no record in table book");
            else{while(rs1.next()){
                    isbnA = rs1.getString("ISBN");  
                    if(loopCounter == 0) bookCounter++;
                    else if(isbnA.equals(isbnB)) continue;
                    else {
                        bookCounter++;
                    }
                    isbnB = isbnA;
                    loopCounter++;
                }
            }
            
            // Continue scanning
            while(number==-1 || number>bookCounter || number == 0){
                // System.out.println(bookCounter);
                number = in.nextInt();
                if(number>bookCounter) System.out.println("The number you picked is more than the total number of books.");
                else if(numberCounter>0) System.out.println("Please input the N popular books number: ");
                else if(number == 0) System.out.println("Please enter a number greater than 0");
                else {numberCounter++;
                    break;
                }
            }
    
            //To get the List of the ISBN we need to display
            String mysql2 = String.format(
                "select * "+ 
                "from ordering "+
                "order by ISBN"
            );
            
            //To create array to store ISBN 
            String isbnArr[]=new String[1000];// create Array  storing ISBN
            int qtyArr[]=new int[1000];// create Array  storing qty
            String isbnPrevious ="";
            int refNum = 0;
            int loopNum = 0;
            
            ResultSet rs2 = Philip.executeQuery(mysql2);
            if(!rs2.isBeforeFirst()) System.out.println("There are no record in the table book");
            else{ while(rs2.next()){
                String isbnNow = rs2.getString("ISBN");
                
                
                if(loopNum==0 || isbnNow.equals(isbnPrevious)){ //ISBN still the same
                    isbnArr[refNum] = rs2.getString("ISBN");
                    // System.out.println(isbnArr[refNum]+"Ref Num: "+ refNum);
                    qtyArr[refNum] += rs2.getInt("quantity");
                    loopNum++;
                    isbnPrevious = isbnNow;
                }
                else{
                    refNum++;
                    isbnArr[refNum] = rs2.getString("ISBN");
                    // System.out.println(isbnArr[refNum]+"Ref Num: "+ refNum);
                    qtyArr[refNum] += rs2.getInt("quantity");
                    isbnPrevious = isbnNow;
                }
    
                }
    
                //Bubble sort the ISBN and quantity to descending order or ascending order
                int i,j;
                for(i=0;i<refNum;i++){
                    for(j=0;j<refNum-i;j++){
                    if(qtyArr[j]>qtyArr[j+1]){
                        //swap qtyArr
                        int temp = qtyArr[j];
                        qtyArr[j] = qtyArr[j+1];
                        qtyArr[j+1] = temp;
    
    
                        //swap isbnArr
                        String temp2 = isbnArr[j];
                        isbnArr[j] = isbnArr[j+1];
                        isbnArr[j+1] = temp2;
                    }
                }
                }
                // To get the N most popular
                int x = 0 ;
                String isbnArrFilter[]=new String[1000];// create Array  storing ISBN new
                int qtyArrFilter[]=new int[1000];// create Array  storing qty new
                // for(int m = 0;m<refNum+1;m++) System.out.println(isbnArr[m]);
                // for(int m = 0;m<refNum+1;m++) System.out.println(qtyArr[m]);
                // System.out.println("sad");
                isbnArrFilter[0] = isbnArr[refNum]; 
                qtyArrFilter[0] = qtyArr[refNum];
                int countFilter = 1;
                // System.out.println(isbnArrFilter[0]+"Ref Num: "+ 0);
                // System.out.println(qtyArrFilter[0]+" Ref Num: "+ 0);
    
                // Filtering
                while(x<number-1 && (refNum>=countFilter)){ // Need to check this logic
                    if(qtyArr[refNum-countFilter] == qtyArr[refNum-countFilter+1]){ // in original array - same as previous?
                        qtyArrFilter[countFilter] = qtyArr[refNum-countFilter];
                        isbnArrFilter[countFilter]= isbnArr[refNum-countFilter];
                        // System.out.println("Pass system 1");
                        if(!(qtyArr[refNum-countFilter] == qtyArr[refNum-countFilter-1]))x++;
                        countFilter++;
                    }
                    else if (!((refNum-countFilter) == 0)){ //is it the end?
                        if(qtyArr[refNum-countFilter] == qtyArr[refNum-countFilter-1]){ // in original array - same as next?
                            qtyArrFilter[countFilter] = qtyArr[refNum-countFilter];
                            isbnArrFilter[countFilter] = isbnArr[refNum-countFilter];
                            countFilter++;
                            // System.out.println("Pass system 2");
                        }
                        else {
                            qtyArrFilter[countFilter] = qtyArr[refNum-countFilter];
                            isbnArrFilter[countFilter] = isbnArr[refNum-countFilter];
                            countFilter++;
                            x++;
                            // System.out.println("Pass system 3");
                        }
                    }
                    else {
                        qtyArrFilter[countFilter] = qtyArr[refNum-countFilter];
                        isbnArrFilter[countFilter] = isbnArr[refNum-countFilter];
                        countFilter++;
                        x++;
                        // System.out.println("Pass system 4");
                    }
                    
                  
                }
                // System.out.println("sad");
                // for(int m = 0;m<countFilter;m++) System.out.println(isbnArrFilter[m]);
                // printing out
                System.out.println("ISBN               Title            Copies");
                for(int a =0;a<countFilter;a++){
                    // query for title
                    String sqlquery = String.format(
                        "select * "+
                        "from book "+
                        "where ISBN = \"%s\" ", isbnArrFilter[a]
                    );
                    
                    ResultSet sqlquery1 = Philip.executeQuery(sqlquery);
                    sqlquery1.next();
                    if(qtyArrFilter[a]!=0)
                    System.out.println(isbnArrFilter[a]+ "      " +sqlquery1.getString("title")+"      "+ qtyArrFilter[a] );
    
    
                    
                }
    
            }
            }catch(Exception err){
                System.err.println(err);
            }
    }
    public static void run(){
        Scanner in = new Scanner(System.in);
        run: while (true) {
            int input = 0;
            System.out.println("----------------------------------");
            System.out.println("1. Order Update.");
            System.out.println("2. Order Query.");
            System.out.println("3. N most Popular Book Query.");
            System.out.println("4. Back to main menu.");
            System.out.println(" ");
            System.out.print("What is your choice??..");

            try {
                input = in.nextInt();
                System.out.println("");
            }

            catch (Exception e) {
                input = in.nextInt();
            }

            finally {
                switch (input) {
                case 1:
                    OrUpdate();
                    break;
                case 2:
                    OrQuery();
                    break;
                case 3:
                    Npop(in);
                    break;
                case 4:
                    break run;
                default:
                    System.out.println("Invaild choice.");
                    break;
                }
            }
        }
    }
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

class Connect {
    public static Connection connect() {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db19";// uncomplete address, need change later
        String dbUsername = "Group19";
        String dbPassword = "CSCI3170";// may change later on

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
