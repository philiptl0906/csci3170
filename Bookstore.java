import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.Integer.*;

// not tested
public class Bookstore {
    public static void run(Scanner in) {

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
                input = in.next();
            }

            finally {
                switch (input) {
                case 1:
                    OrUpdate(in);
                    break;
                case 2:
                    OrQuery(in);
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
    private static void OrUpdate(Scanner in) {
        int input = 0;
        String userin;
        String token;
        Connection con = Connect.connect();
        Statement stmt = null;
        ResultSet resultSet = null;
        System.out.print("Please enter the order ID: ");
        while (true) {
            try {
                input = in.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid order ID!!");
                continue;
            }
            token = input.toString();
            int len = token.length();
            if (len == 8) {
                break;
            } else {
                System.out.println("Invalid order ID!!");
            }

        }
        String psql = "SELECT O.shipping_status, S.quantity FROM orders O , ordering S WHERE O.order_id = %s AND S.order_id = O.order_id";
        String aUserInputOrderID = input.toString();
        String sql = String.format(psql, aUserInputOrderID);
        try {
            stmt = con.createStatement(sql);
            resultSet = stmt.executeQuery();
            int quantity = 0;
            String status = "";
            while (resultSet.next()) {
                status = resultSet.getString("shipping_status");
                quantity += resultSet.getInt("quantity");
            }
            System.out.println(
                    "the shipping status of " + input + " is " + status + " and " + quantity + " books ordered");

            if (status == "N") {
                if (quantity >= 1) {
                    while (true) {
                        System.out.print("Are you sure to update the shipping status? (Yes=Y)");
                        try {
                            userin = in.nextLine();
                        } catch (Exception e) {
                            System.out.println("Invalid input!!");
                            continue;
                        }
                        if (userin != "Y" && userin != "N") {
                            System.out.println("Invalid input!!");
                        } else if (userin == "Y") {

                            psql = "UPDATE orders SET shipping_status = 'Y' WHERE order_id = %s";
                            sql = String.format(psql, aUserInputOrderID);
                            try {
                                stmt = con.createStatement(sql);
                                resultSet = stmt.executeQuery();
                                System.out.print("Update shiping status");
                            } catch (SQLException e) {
                                System.out.println("SQLException: " + e.getMessage());
                                System.out.println("SQLState: " + e.getSQLState());
                                System.out.println("VendorError: " + e.getErrorCode());
                            }
                            break;
                        } else if (userin == "N") {
                            break;
                        }
                    }
                } else {
                    System.out.println("You can't update the status!");
                }

            } else {
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
    private static void OrQuery(Scanner in) {
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
                    stmt = con.createStatement(sql);
                    resultSet = stmt.executeQuery();
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
                System.out.printlf("Invalid input !!");
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
        }

        System.out.println("");
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
            stmt = con.createStatement(sql);
            resultSet = stmt.executeQuery();
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
