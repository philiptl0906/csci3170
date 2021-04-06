import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.*;

// not tested
public class Bookstore {
    public static void main(String[] args) {
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
        int input = 0;
        System.out.print("Please input the N popular books number: ");

        while (true) {
            try {
                input = in.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input!!");
                continue;
            }
            break;
        }
        System.out.println(" ");
        System.out.println("ISBN            Title           copies");
        String psql = "SELECT b.ISBN, b.title, t.Total_no FROM book b, Top_result t WHERE b.ISBN = (SELECT ISBN FROM(SELECT ISBN, TOP %d Total_no FROM (SELECT ISBN, Sum(quantity) AS Total_no FROM ordering GROUP BY ISBN)Total_result ORDER BY Total_no)Top_result)";// Not
                                                                                                                                                                                                                                                                       // sure
                                                                                                                                                                                                                                                                       // is
                                                                                                                                                                                                                                                                       // this
                                                                                                                                                                                                                                                                       // correct
        int aUserInput = input;
        String sql = String.format(psql, aUserInput);
        Connection con = Connect.connect();
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ISBN") + "   " + resultSet.getString("title") + "  "
                        + resultSet.getInt("Total_no"));
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