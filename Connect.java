import java.sql.*;

public class Connect{
    public static Connection connect(){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db19";// uncomplete address, need change later
        String dbUsername = "Group19";
        String dbPassword = "CSCI3170";// may change later on

        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }
}