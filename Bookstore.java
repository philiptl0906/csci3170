import java.util.*;
import java.io.*;
import java.sql.*;

    // not tested
public class Bookstore{
    public static void run(Scanner in){
        
        run: while(true){
            int input= 0;
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

            catch(Exception e){
                input = in.next();
            }

            finally{
                switch(input){
                    case 1:
                        OrUpdate(in);
                        break;
                    case 2:
                        OrQuery(in);
                        break;
                    case 3: 
                        Npop();
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
//not tested
    private static void OrUpdate(Scanner in){
        int input = 0;
        System.out.print("Please enter the order ID: ");
        while(true){
            try{
                input = in.nextInt();
            }
            catch(Exception e){
                System.out.println("Invalid order ID!!");
                continue;
            }
            String token = input.toString();
            int len = token.length();
            if(len == 8 ){
                break;
            }else{
                System.out.println("Invalid order ID!!");
            }

        }
        String psql = "SELECT O.shipping_status, S.quantity FROM orders O , ordering S WHERE order_id = %s";
        String aUserInputOrderID = token;
        String sql = String.format(psql, aUserInputOrderID);
        Connection con = Connect.connect();
        try{
            Statement stmt = con.createStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            int quantity = 0;
            while(resultSet.next()){
            String status = resultSet.getString("shipping_status");
            quantity += resultSet.getInt("quantity");
            }
            System.out.println("the shipping status of " + token +" is " + status + " and " + quantity + " books ordered");
            
            if(status == "N"){
                if(quantity >= 1){
                    while(true){
                        System.out.print("Are you sure to update the shipping status? (Yes=Y)");
                        try{
                            String userin = in.nextLine();
                    } catch(Exception e){
                            System.out.println("Invalid input!!");
                            continue;
                    }
                    if(userin != 'Y' && userin !='N'){
                        System.out.println("Invalid input!!");
                    }else if(userin == 'Y'){
                        
                        psql = "UPDATE orders SET shipping_status = 'Y' WHERE order_id = %s";
                        sql = String.format(psql, aUserInputOrderID);
                        try{
                            stmt = con.createStatement(sql);
                            resultSet = stmt.executeQuery();
                        System.out.print("Update shiping status");
                        }catch(SQLException e){
                            System.out.println("SQLException: " + e.getMessage());
                            System.out.println("SQLState: " + e.getSQLState());
                            System.out.println("VendorError: " + e.getErrorCode());
                        }
                        break;
                        }else if(userin == 'N'){
                        break;
                        }
                    }         
                }else{
                    System.out.println("You can't update the status!");
                }

            }else{
                 System.out.println("You can't update the status!");
            }
                
            
            

        }catch(SQLException e){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());

        }finally{
            if(stmt != null){
                try{
                    stmt.close();
                }catch(SQLException e){
                    stmt= null;
                }
            }
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    resultSet = null;
                }

            }
        }
        if(con != null){
            try{
                con.close();
            }catch(SQLException e){
                con = null;
            }
        }    
    }
//not tested
    private static void OrQuery(Scanner in){
        String input = '';
        System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
        input = in.nextLine();
        if(input.matchs("\\d{4}-\\d{2}")){
            String psql = "SELECT * FROM orders WHERE shipping_status = 'Y', o_date REGEXP '^%s'";
            String aUserInputODate = input;
            String sql = String.format(psql, aUserInputODate);
            Connection con = Connect.connect();
            try{
                Statement stmt = con.createStatement(sql);
                ResultSet resultSet = stmt.executeQuery();
                int i=1;
                int total = 0;
                while(resultSet.next()){
                    System.out.println("");
                    System.out.println("");
                    System.out.println("Record : " + i);
                    System.out.println("order_id : " + resultSet.getString("order_id"));
                    System.out.println("customer_id : " + resultSet.getString("customer_id"));
                    System.out.println("date : "+ resultSet.getDate("o_date"));
                    System.out,println("chage : "+ resultSet.getInt("charge"));
                    total += resultSet.getInt("charge");
                }catch(SQLException e){
                    System.out.println("SQLException: " + e.getMessage());
                    System.out.println("SQLState: " + e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                }finally{
                    System.out.println("");
                    System.out.println("");
                    System.out.println("Total charges of the month is "+ total);
                     if(stmt != null){
                        try{
                            stmt.close();
                        }catch(SQLException e){
                            stmt= null;
                        }
                    }
                    if(resultSet != null){
                        try{
                            resultSet.close();
                        }catch(SQLException e){
                            resultSet = null;
                        }

                    }
                }
            }
            if(con != null){
            try{
                con.close();
            }catch(SQLException e){
                con = null;
            }
        }

        }else{
            System.out.printlf("Invalid input !!");
        }
        
    }

    private static void Npop(){

    }

    
}
