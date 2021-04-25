import java.util.Scanner;
import javax.naming.spi.DirStateFactory.Result;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.sql.*;
import java.util.*;

public class Customer {
    static Scanner sc = new Scanner(System.in);

    public static Date run(Date sysDate) {

        boolean custApplicationIsRunning = true;

        while (custApplicationIsRunning) {
            printCustMenu();

            boolean inputIsValid = false;

            while (!inputIsValid) {
                int custAction = 0;
                try {
                    custAction = sc.nextInt();
                } catch (Exception err) {
                    System.err.println("INVALID INPUT");
                    sc.next();
                } finally {

                    switch (custAction) {
                        case 1:
                            bookSearch();
                            inputIsValid = true;
                            break;
                        case 2:
                            orderCreation(sysDate);
                            inputIsValid = true;
                            break;
                        case 3:
                            orderAltering(sysDate);
                            inputIsValid = true;
                            break;
                        case 4:
                            orderQuery();
                            inputIsValid = true;
                            break;
                        case 5:
                            custApplicationIsRunning = false;
                            inputIsValid = true;
                            return sysDate;
                        // break;
                        default:
                            System.err.println("INVALID INPUT");
                    }
                }
            }

        }
        return sysDate;
    }

    // print menu for Customer - done
    private static void printCustMenu() {
        System.out.println("<This is the customer interface.>");
        System.out.println("1. Book Search");
        System.out.println("2. Order Creation");
        System.out.println("3. Order Altering");
        System.out.println("4. Order Query");
        System.out.println("5. Back to main menu.");
        System.out.print("What is your choice??..");
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
        System.out.print("Your Choice?...");

    }

    private static void bookSearch() {

        boolean custBSApplicationIsRunning = true;

        while (custBSApplicationIsRunning) {
            printCustBSMenu();

            boolean inputIsValid = false;

            while (!inputIsValid) {
                try {
                    int custBSAction = sc.nextInt();

                    switch (custBSAction) {
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

    private static Boolean listingAllISBN(String a) {
        try {
            List<String> aISBNLIST = new ArrayList<String>();
            String sql = String.format("Select * " + "from book ");

            ResultSet sqlr = Philip.executeQuery(sql);
            if (!sqlr.isBeforeFirst()) {
                System.out.println("There are no records in table book");
            } else {
                while (sqlr.next()) {
                    aISBNLIST.add(sqlr.getString("ISBN"));
                }
            }
            if (aISBNLIST.contains(a))
                return true;
            return false;
        } catch (Exception err) {
            System.err.println(err);
            return false;
        }
    }

    private static Boolean listingAllCustomer(String a) {
        try {
            List<String> aCUSTLIST = new ArrayList<String>();
            String sql = String.format("Select * " + "from customer ");

            ResultSet sqlr = Philip.executeQuery(sql);
            if (!sqlr.isBeforeFirst()) {
                System.out.println("There are no records in table book");
            } else {
                while (sqlr.next()) {
                    aCUSTLIST.add(sqlr.getString("customer_id"));
                }
            }
            if (aCUSTLIST.contains(a))
                return true;
            return false;
        } catch (Exception err) {
            System.err.println(err);
            return false;
        }
    }

    private static Boolean listingAllOrder(String a) {
        try {
            List<String> aCUSTLIST = new ArrayList<String>();
            String sql = String.format("Select * " + "from orders ");

            ResultSet sqlr = Philip.executeQuery(sql);
            if (!sqlr.isBeforeFirst()) {
                System.out.println("There are no records in table book");
            } else {
                while (sqlr.next()) {
                    aCUSTLIST.add(sqlr.getString("order_id"));
                }
            }
            if (aCUSTLIST.contains(a))
                return true;
            return false;
        } catch (Exception err) {
            System.err.println(err);
            return false;
        }
    }

    private static void bookSearchISBN() { // Search by ISBN - Complete, TESTED. debug: add for user input

        try {
            System.out.print("Input the ISBN: ");
            String isbncharacter = "";
            int countex = 0;
            while (!listingAllISBN(isbncharacter)) {
                isbncharacter = sc.nextLine();
                if (countex != 0 && !listingAllISBN(isbncharacter)) {
                    System.out.println("Invalid input");
                    System.out.println("Please input the correct ISBN:");
                }
                countex++;
            }
            String mysqlStatement = String.format(
                    "SELECT book.ISBN, book.title, book.unit_price, book.no_of_copies, book_author.author_name "
                            + "from book inner join book_author on book.ISBN = book_author.ISBN "
                            + "where book.ISBN=\"%s\" " + "order by book.title, book.ISBN, book_author.author_name ",
                    isbncharacter);

            ResultSet rs = Philip.executeQuery(mysqlStatement);

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo records found.");
            } else {
                rs.next();
                int counter = 1;
                System.out.println("\nRecord " + counter);
                System.out.println("ISBN: " + rs.getString("ISBN"));
                System.out.println("Book Title: " + rs.getString("title"));
                System.out.println("Unit Price: " + rs.getString("unit_price"));
                System.out.println("No Of Available: " + rs.getString("no_of_copies"));
                System.out.println("Authors: ");
                System.out.println(counter + ": " + rs.getString("author_name"));
                counter++;
                while (rs.next()) {
                    System.out.println(counter + ": " + rs.getString("author_name"));
                    counter++;
                }
            }
        } catch (Exception err) {
            System.err.println(err);
        }
    }

    private static void bookSearchBT() { // Search by book title - Complete and tested need debugging to get user input
        try {
            System.out.println("Input the book title");
            String bookTitle = "";
            while (bookTitle.equals("")) {
                bookTitle = sc.nextLine(); // request for user input
            }
            String exactMatchBT = "";
            List<String> exactISBN = new ArrayList<String>();
            int counter = 1;
            for (char c : bookTitle.toCharArray()) { // check if same or not ( Exact Match or not )
                if (c == '%' || c == '_') {
                    // skip c
                } else
                    exactMatchBT += c;
            }

            if (exactMatchBT != bookTitle) { // If there is wild card
                String mysqlStatement = String.format("select b.ISBN, b.title, b.unit_price, b.no_of_copies "
                        + "from book b " + "where b.title=\"%s\" " + "order by b.title, b.ISBN ", exactMatchBT);

                ResultSet s1 = Philip.executeQuery(mysqlStatement);

                if (!s1.isBeforeFirst()) {
                    // skip
                } else {
                    while (s1.next()) {
                        int counterAuthor = 1;
                        String isbn = s1.getString("ISBN");
                        exactISBN.add(isbn);
                        System.out.println("Record " + counter);
                        System.out.println("ISBN: " + s1.getString("ISBN"));
                        System.out.println("Book Title: " + s1.getString("title"));
                        System.out.println("Unit Price: " + s1.getString("unit_price"));
                        System.out.println("No Of Available: " + s1.getString("no_of_copies"));
                        String sqlauthors = String.format("select author_name " + "from book_author "
                                + "where ISBN=\"%s\" " + "order by author_name", isbn);
                        ResultSet lol = Philip.executeQuery(sqlauthors);
                        while (lol.next()) {
                            System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                            counterAuthor++;

                        }
                        ;
                        counter++;
                    }
                }
            }

            String mysqlStatement = String.format("select b.ISBN, b.title, b.unit_price, b.no_of_copies "
                    + "from book b " + "where b.title LIKE \"%s\" " + "order by b.title, b.ISBN ", bookTitle);

            ResultSet rs = Philip.executeQuery(mysqlStatement);

            if (!rs.isBeforeFirst()) {
                System.out.println("No records found.");
            } else {
                while (rs.next()) {
                    int counterAuthor = 1;
                    String isbn = rs.getString("ISBN");
                    if (exactISBN.contains(isbn))
                        continue;
                    System.out.println("Record " + counter);
                    System.out.println("ISBN: " + rs.getString("ISBN"));
                    System.out.println("Book Title: " + rs.getString("title"));
                    System.out.println("Unit Price: " + rs.getString("unit_price"));
                    System.out.println("No Of Available: " + rs.getString("no_of_copies"));
                    String sqlauthors = String.format(
                            "select author_name " + "from book_author " + "where ISBN=\"%s\" " + "order by author_name",
                            isbn);

                    ResultSet lol = Philip.executeQuery(sqlauthors);
                    while (lol.next()) {
                        System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                        counterAuthor++;

                    }
                    ;
                    counter++;
                }
            }
        } catch (Exception err) {
            System.err.println(err);
        }
    }

    private static void bookSearchAN() { // Done and tested - need debugging to prompt
        try {
            System.out.println("Please Input the Author name:");
            String authorName = "";
            while (authorName.equals("")) {
                authorName = sc.nextLine();
            }
            String exactMatchAN = "";
            List<String> exactAN = new ArrayList<String>();
            int counter = 1;
            for (char c : authorName.toCharArray()) {
                if (c == '%' || c == '_') {
                    // skip c
                } else
                    exactMatchAN += c;
            }
            if (exactMatchAN != authorName) { // If there is wild card
                String mysqlStatement = String.format("select ISBN, author_name " + "from book_author "
                        + "where author_name=\"%s\" " + "order by ISBN ", exactMatchAN);

                ResultSet s1 = Philip.executeQuery(mysqlStatement);

                if (!s1.isBeforeFirst()) {
                    // skip
                } else {
                    while (s1.next()) {
                        int counterAuthor = 1;
                        String isbn = s1.getString("ISBN");
                        exactAN.add(s1.getString("ISBN"));
                        String sql1 = String.format("select ISBN, title, unit_price, no_of_copies " + "from book "
                                + "where ISBN = \"%s\" " + "order by title, ISBN", isbn);

                        ResultSet s1xx = Philip.executeQuery(sql1);
                        while (s1xx.next()) {
                            System.out.println("Record " + counter);
                            System.out.println("ISBN: " + s1xx.getString("ISBN"));
                            System.out.println("Book Title: " + s1xx.getString("title"));
                            System.out.println("Unit Price: " + s1xx.getString("unit_price"));
                            System.out.println("No Of Available: " + s1xx.getString("no_of_copies"));
                            String sqlauthors = String.format("select author_name " + "from book_author "
                                    + "where ISBN=\"%s\" " + "order by author_name", isbn);

                            ResultSet lol = Philip.executeQuery(sqlauthors);
                            while (lol.next()) {
                                System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                                counterAuthor++;

                            }
                            ;
                            counter++;
                        }
                    }
                }
            }

            String mysqlStatement = String.format("select ISBN, author_name " + "from book_author "
                    + "where author_name LIKE \"%s\" " + "order by ISBN ", authorName);
            ResultSet rs = Philip.executeQuery(mysqlStatement);

            if (!rs.isBeforeFirst()) {
                System.out.println("No records found.");
            } else {
                while (rs.next()) {
                    int counterAuthor = 1;
                    String isbn = rs.getString("ISBN");
                    if (exactAN.contains(isbn))
                        continue;
                    exactAN.add(rs.getString("ISBN"));
                    String sql2 = String.format("select ISBN, title, unit_price, no_of_copies " + "from book "
                            + "where ISBN = \"%s\" " + "order by title, ISBN", isbn);
                    ResultSet s2xx = Philip.executeQuery(sql2);
                    while (s2xx.next()) {
                        System.out.println("Record " + counter);
                        System.out.println("ISBN: " + s2xx.getString("ISBN"));
                        System.out.println("Book Title: " + s2xx.getString("title"));
                        System.out.println("Unit Price: " + s2xx.getString("unit_price"));
                        System.out.println("No Of Available: " + s2xx.getString("no_of_copies"));
                        String sqlauthors = String.format("select author_name " + "from book_author "
                                + "where ISBN=\"%s\" " + "order by author_name", isbn);
                        ResultSet lol = Philip.executeQuery(sqlauthors);
                        while (lol.next()) {
                            System.out.println(counterAuthor + ": " + lol.getString("author_name"));
                            counterAuthor++;

                        }
                        ;
                        counter++;
                    }
                }
            }

        } catch (Exception err) {
            System.err.println(err);
        }
    }

    // Ordering Query
    private static void orderQuery() { // Done and tested
        try {
            System.out.println("Please Input Customer ID:");
            String cid = "";
            int counterx = 0;
            while (!listingAllCustomer(cid)) {
                cid = sc.nextLine();
                if (counterx != 0 && !listingAllCustomer(cid))
                    System.out.println("Invalid input, No such customer_id");
                counterx++;
            }
            System.out.println("Please Input the Year:");
            String thisYear = sc.nextLine();
            int counter = 1;
            String mysqlStatement = String.format(
                    "select order_id, o_date, shipping_status, charge " + "from orders "
                            + "where year(o_date) = \"%s\" and customer_id = \"%s\" " + "order by order_id",
                    thisYear, cid);

            ResultSet rs = Philip.executeQuery(mysqlStatement);
            if (!rs.isBeforeFirst()) {
                System.out.println("Record not found");
            } else {
                while (rs.next()) {
                    System.out.println("Record: " + counter);
                    System.out.println("OrderID :" + rs.getString("order_id"));
                    System.out.println("OrderDate :" + rs.getDate("o_date"));
                    System.out.println("Charge: " + rs.getInt("charge"));
                    System.out.println("Shipping Status: " + rs.getString("shipping_status"));
                    System.out.println("");
                    counter++;
                }
                ;
            }
        } catch (Exception err) {
            System.err.println(err);
        }
    }

    // Creating orders
    private static void orderCreation(Date sysDate) {
        try {

            // Getting ORDER_ID (+1)
            String grid = String.format("select order_id " + "from orders " + "order by order_id DESC");

            ResultSet rem = Philip.executeQuery(grid);
            rem.next();
            String order_id = String.format("%08d", Integer.valueOf(rem.getString("order_id")) + 1);

            // Date format
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());

            List<String> isbnList = new ArrayList<String>();
            Boolean loop = false; // for the ISBN loop
            String isbn = "";

            // Entering Customer ID
            System.out.print("Please enter your CustomerID??");
            String cid = "";
            int counterID = 0;
            while (!listingAllCustomer(cid)) {
                cid = sc.nextLine();
                if (!listingAllCustomer(cid) && counterID != 0)
                    System.out.println("Invalid input, there are no such Customer_id");
                counterID++;
            }

            // Checking ISBN

            // Getting all ISBN lists into isbnList
            String isbnchecker = String.format("SELECT ISBN " + "from book ");

            ResultSet rsetisbnChecker = Philip.executeQuery(isbnchecker);
            while (rsetisbnChecker.next()) {
                isbnList.add(rsetisbnChecker.getString("ISBN"));
            }
            ;

            // UI for ISBN
            System.out.println("What books do you want to order??");
            System.out.println("Input ISBN and then quantity.");
            System.out.println("You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
            Boolean initializeOrders = false;
            // Loop for ISBN
            
            
            while (!loop) {
                System.out.print("Please enter the book's ISBN: ");
                
                isbn = sc.nextLine();
                isbn = String.valueOf(isbn);
                // System.out.println(isbn);
                String string1 = "L";
                String string2 = "l";
                String string3 = "F";
                String string4 = "f";
                // check cases

                // case 1 - L
                if (isbn.equals(string1) || isbn.equals(string2)) {
                    // UI Basic deisgn
                    System.out.println("ISBN            Number:");
                    // show the existing order
                    String show = String.format("select ISBN, quantity " + "from ordering " + "where order_id =\"%s\" ",
                            order_id);

                    ResultSet stres = Philip.executeQuery(show);
                    if (!stres.isBeforeFirst()) {
                        // System.out.println("No orders of this Order_id"); // this order_id still has
                        // no orderings yet
                    } else {
                        while (stres.next()) {
                            System.out.println(stres.getString("ISBN") + "  " + stres.getInt("quantity"));
                        }
                    }
                }

                // case 2 - F
                if (isbn.equals(string3) || isbn.equals(string4)) {
                    String show = String.format("select ISBN, quantity " + "from ordering " + "where order_id =\"%s\" ",
                            order_id);

                    ResultSet stres = Philip.executeQuery(show);
                    if (!stres.isBeforeFirst()) {
                        continue; // this order_id
                                                                                                      // still has no
                                                                                                      // orderings yet
                    } else {
                        // System.out.println("Went through F");
                        int countPurchase = 0; // No of copies of all book ordered
                        int totalBookPrice = 0; //// Total book price : Sum(unit price * qty)
                        // calculate charge:
                        // Total Book Price + Shippin price
                        // Shipping price = No.of copies of all book ordered (keep count) * Unit
                        // shipping charge($10) + Handling charge($10)
                        String sqlquery = String.format(
                                "select ISBN, quantity " + "from ordering " + "where order_id=\"%s\" ", order_id);

                        ResultSet rset1 = Philip.executeQuery(sqlquery);
                        if (!rset1.isBeforeFirst()) {
                            // skip
                        } else {
                            while (rset1.next()) {
                                countPurchase += rset1.getInt("quantity");
                                String sqlquery2 = String.format(
                                        "select unit_price " + "from book " + "where ISBN = \"%s\" ",
                                        rset1.getString("ISBN"));

                                ResultSet rset2 = Philip.executeQuery(sqlquery2);
                                rset2.next();
                                totalBookPrice += (rset2.getInt("unit_price") * rset1.getInt("quantity"));
                            }
                        }
                        int charge = totalBookPrice + (countPurchase * 10 + 10);
                        // add order to orders
                        String insert = String.format("update orders " + "set charge = %d " + "where order_id =\"%s\" ",
                                charge, order_id);
                        Philip.executeUpdate(insert);
                        loop = true;
                        continue;
                    }
                }

                // case 3 - correctly inputted ISBN - Ask for quantity
                if (isbnList.contains(isbn)) {
                    Boolean inputQtyValid = false;
                    // Prompt for quantity
                    int qty = -1;
                    System.out.print("Please enter the quantity of the order: ");
                    while (!inputQtyValid) {
                        String qty1 = sc.next();
                        try {
                            qty = Integer.parseInt(qty1);
                        } catch (Exception e) {
                            System.out.println("[Error]: Invalid input. Please input a number");
                        }
                        if (qty > 0) {
                            inputQtyValid = true;
                        } else
                            System.out.println("Please input a number greater than 0");

                    }

                    int copies = 0; // this is used to check for no.of available copies

                    // check if qty is available
                    String sql2 = String.format("select no_of_copies " + "from book " + "where ISBN =\"%s\" ", isbn);

                    ResultSet r2 = Philip.executeQuery(sql2);
                    r2.next();
                    copies = r2.getInt("no_of_copies");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    // The checking part
                    if (copies >= qty) {
                        // Step 1: Create empty data in orders
                        // create empty order (without the charge first) - Update when 'F'
                        if (!initializeOrders) {
                            String emptyOrders = String.format(
                                    "insert into orders " + "values(\"%s\",\"%s\",\"%s\",%d,\"%s\") ", order_id,
                                    sdf.format(sysDate), "N", 0, cid);
                            Philip.executeUpdate(emptyOrders);
                            initializeOrders = true;
                        }
                        // Step 2: update to ordering
                        // add to ordering
                        String sqlxx = String.format(
                                "insert into ordering (order_id,ISBN,quantity) " + "values (\"%s\",\"%s\",%d)",
                                order_id, isbn, qty);

                        Philip.executeUpdate(sqlxx);

                        // decrease the number of copies of the book
                        String sqlxx1 = String.format("Select * " + "from book " + "where ISBN =\"%s\" ", isbn);
                        ResultSet sqlxx1r = Philip.executeQuery(sqlxx1);
                        sqlxx1r.next();
                        int aa1 = sqlxx1r.getInt("no_of_copies");

                        // Update
                        String sqlUp = String.format("update book " + "set no_of_copies= %d " + "where ISBN =\"%s\" ",
                                aa1 - qty, isbn);
                        Philip.executeUpdate(sqlUp);
                        // System.out.println("Updated no.of copies");
                    } else {
                        System.out.println("Quantity ordered exceed number of available quantity");
                    }
                }
                
            }
            
        } catch (Exception err) {
            System.err.println(err);
        }

    }

    private static void orderAltering(Date sysDate) { // Done checked and tested
        try {
            System.out.println(" Please enter the OrderID that you want to change:");
            String oid = "";
            int numberx = 0;
            while (!listingAllOrder(oid)) {
                oid = sc.nextLine();
                if (numberx != 0 && !listingAllOrder(oid))
                    System.out.println("There is no such order id. Please re-input");
                numberx++;
            }
            // Add copies to order
            String array[] = new String[1000];// create Array storing ISBN
            String array1[] = new String[1000]; // create Array storing order_id
            Boolean inputValid = false;
            // Get the order and orders:
            String sql1 = String.format("select * " + "from orders " + "where order_id = \"%s\" ", oid);
            ResultSet rs1 = Philip.executeQuery(sql1);
            int counter = 1; // initialize counter
            if (!rs1.isBeforeFirst()) {
                System.out.println("There is no order for this order_id");
            } else {
                rs1.next();

                String x = rs1.getString("shipping_status"); // shipping status

                // UI design
                System.out.print("order_id: " + oid + ", ");
                System.out.print("shipping: " + rs1.getString("shipping_status") + ", ");
                System.out.print("charge: " + rs1.getInt("charge") + ", ");
                System.out.println("customerID= " + rs1.getString("customer_id"));

                // Select all with same order ID from ordering
                String sql2 = String.format("Select * " + "from ordering " + "where order_id = \"%s\" ", oid);
                ResultSet rs2 = Philip.executeQuery(sql2);

                if (!rs2.isBeforeFirst()) {
                    System.out.println("There is no book here"); // no orders
                } else {
                    while (rs2.next()) {
                        System.out.print("book no: " + counter + ", ");
                        System.out.print("ISBN = " + rs2.getString("ISBN") + ", ");
                        System.out.println("quantity= " + rs2.getString("quantity") + ", ");
                        array[counter] = rs2.getString("ISBN");
                        array1[counter] = rs2.getString("order_id");
                        counter++;
                    }

                    int number = 0; // initialization

                    // Ensuring valid input
                    while (!inputValid) {
                        System.out.println("Which book you want to alter (input book no.): ");
                        String input = sc.next();
                        try {
                            number = Integer.parseInt(input);
                        } catch (Exception e) {
                            System.out.println("[Error]: Invalid input. Please input a positive number");
                            continue;
                        }
                        if (number <= counter - 1 && number != 0)
                            inputValid = true;
                    }

                    // Decision UI
                    String decision = "";
                    int counterDecision = 0;
                    System.out.println("input add or remove");
                    while (!(decision.equals("add") || decision.equals("remove"))) {
                        decision = sc.nextLine();
                        if (counterDecision != 0)
                            System.out.println("Invalid input. Please input add or remove: ");
                        counterDecision++;
                    }

                    if (decision.equals("add")) {
                        // valid number
                        Boolean inputQtyValid = false;
                        // Prompt for quantity
                        int addCopies = -1;
                        System.out.println("How many copies do you want to add?");
                        while (!inputQtyValid) {
                            String addCopies1 = sc.next();
                            try {
                                addCopies = Integer.parseInt(addCopies1);
                            } catch (Exception e) {
                                System.out.println("[Error]: Invalid input. Please input a number");
                            }
                            if (addCopies > 0) {
                                inputQtyValid = true;
                            } else
                                System.out.println("Please input a number greater than 0");

                        }

                        // get the ISBN of the number.
                        String noc = String.format("select * " + "from book " + "where ISBN = \"%s\" ", array[number]);
                        ResultSet nocst = Philip.executeQuery(noc);
                        nocst.next();

                        // check of number of available copies
                        if (nocst.getInt("no_of_copies") > addCopies && x.equals("N")) {
                            // succesful
                            System.out.println("Update is ok!");
                            String aa = String.format(
                                    "select * " + "from ordering " + "where order_id=\"%s\" and ISBN =\"%s\" ", oid,
                                    array[number]);
                            ResultSet aar = Philip.executeQuery(aa);
                            aar.next();

                            // ADD the copies to the ordering
                            String sql4 = String.format(
                                    "update ordering " + "set quantity = %d "
                                            + "where order_id =\"%s\" and ISBN= \"%s\" ",
                                    addCopies + aar.getInt("quantity"), oid, array[number]);

                            Philip.executeUpdate(sql4);

                            // Decrement from the available book list
                            int newQty = nocst.getInt("no_of_copies") - addCopies;
                            String sql5 = String.format(
                                    "update book " + "set no_of_copies = %d " + "where ISBN =\"%s\" ", newQty,
                                    array[number]);

                            Philip.executeUpdate(sql5);

                            System.out.println("Update done!");

                            // Update the charge - calculate the additional charge
                            // Additional Total Book Price : - Extra no.of copies(add_copies) * unit _price
                            int extraTBP = addCopies * nocst.getInt("unit_price");
                            int extraSC = addCopies * 10;
                            int extraCharge = extraTBP + extraSC;
                            String sqlu1 = String.format("Select * " + "from orders " + "where order_id = \"%s\" ",
                                    oid);
                            ResultSet sqlu1r = Philip.executeQuery(sqlu1);
                            sqlu1r.next();
                            int newCharge = extraCharge + sqlu1r.getInt("charge");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String sqluc1 = String.format("update orders " + "set charge = %d and o_date =\"%s\" "
                                    + "where order_id = \"%s\" ", newCharge, sdf.format(sysDate), oid);
                            try {
                                Philip.executeUpdate(sqluc1);
                                System.out.println("Updated Charge");
                            } catch (Exception e) {
                                System.out.println(e);
                            }

                        } else {
                            if (nocst.getInt("no_of_copies") < addCopies)
                                System.out.println("There are no sufficient number of books");
                            if (!x.equals("N"))
                                System.out.println("The books in the order are shipped");
                        }
                    }

                    // remove
                    if (decision.equals("remove")) {
                        Boolean inputQtyValid = false;
                        // Prompt for quantity
                        int deleteCopies = -1;
                        System.out.println("How many copies do you want to delete?");
                        while (!inputQtyValid) {
                            String deleteCopies1 = sc.next();
                            try {
                                deleteCopies = Integer.parseInt(deleteCopies1);
                            } catch (Exception e) {
                                System.out.println("[Error]: Invalid input. Please input a number");
                            }
                            if (deleteCopies > 0) {
                                inputQtyValid = true;
                            } else
                                System.out.println("Please input a number greater than 0");

                        }

                        // get the quantity in the orders.
                        String deleteQty = String.format(
                                "SELECT quantity " + "from ordering " + "where order_id= \"%s\" and ISBN= \"%s\" ", oid,
                                array[number]);
                        ResultSet dell = Philip.executeQuery(deleteQty);
                        dell.next();
                        // check if valid
                        if (dell.getInt("quantity") < deleteCopies) {
                            System.out.println("You want to delete more than what's there"); // not valid
                        } else { // valid
                            if (x.equals("N")) { // not shipped yet
                                // delete copies in quantity
                                int newCopies = dell.getInt("quantity") - deleteCopies;
                                String sql6 = String.format(
                                        "update ordering " + "set quantity = %d "
                                                + "where order_id =\"%s\" and ISBN= \"%s\" ",
                                        newCopies, oid, array[number]);
                                Philip.executeUpdate(sql6);

                                // Get the number of available copies
                                String sql8 = String.format("Select * " + "from book " + "where ISBN= \"%s\" ",
                                        array[number]);
                                ResultSet sqll = Philip.executeQuery(sql8);
                                sqll.next();

                                int newQty = sqll.getInt("no_of_copies") + deleteCopies;

                                // increment no_of available copies
                                String sql7 = String.format(
                                        "update book " + "set no_of_copies = %d " + "where ISBN =\"%s\" ", newQty,
                                        array[number]);
                                Philip.executeUpdate(sql7);

                                System.out.println("Update done!");

                                String nox = String.format("Select * " + "from book " + "where ISBN =\"%s\" ",
                                        array[number]);

                                ResultSet noxs = Philip.executeQuery(nox);
                                noxs.next();

                                // update charge
                                int lessTBP = deleteCopies * noxs.getInt("unit_price");
                                int lessSP = deleteCopies * 10;
                                int lessCharge = lessTBP + lessSP;
                                String sqlu1 = String.format("Select * " + "from orders " + "where order_id = \"%s\" ",
                                        oid);

                                ResultSet sqlu1r = Philip.executeQuery(sqlu1);
                                sqlu1r.next();
                                int newCharge = sqlu1r.getInt("charge") - lessCharge;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String sqluc1 = String.format("update orders " + "set charge = %d and o_date =\"%s\" "
                                        + "where order_id = \"%s\" ", newCharge, sdf.format(sysDate), oid);

                                Philip.executeUpdate(sqluc1);

                                System.out.println("Updated Charge");

                            }
                        }
                    }

                }

            }

            // print updated

        } catch (Exception err) {
            System.err.println(err);
        }
    }

    static public class Philip {

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

        public static ResultSet executeQuery(String mysqlStatement) throws SQLException, SQLTimeoutException {
            Connection connection = Philip.connect();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(mysqlStatement);
            return resultSet;
        }

        public static void executeUpdate(String mysqlStatement) throws SQLException, SQLTimeoutException {
            Connection connection = Philip.connect();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(mysqlStatement);

        }
    }

}
