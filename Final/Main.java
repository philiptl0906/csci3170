import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    static Date sysDate;

    public static void main(String[] args) throws Exception {
        boolean applicationIsRunning = true;
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (sysDate == null) {
            String date1 = "2000-02-01";
            sysDate = sdf.parse(date1);
        }
        Date sysDate = sdf.parse("2000-01-01");

        while (applicationIsRunning) {
            printMenu(sysDate);

            boolean inputIsValid = false;

            while (!inputIsValid) {
                try {

                    int userAccount = scanner.nextInt();

                    switch (userAccount) {
                        case 1:
                            SystemInter.run();
                            inputIsValid = true;
                            break;
                        case 2:
                            Customer.run();
                            inputIsValid = true;
                            break;
                        case 3:
                            Bookstore.run();
                            inputIsValid = true;
                            break;
                        case 4:
                            SystemDate.run(sysDate);
                            inputIsValid = true;
                            break;
                        case 5:
                            applicationIsRunning = false;
                            inputIsValid = true;
                            break;
                        default:
                            System.err.println("[Error] Please input [1-5].");
                    }

                } catch (Exception e) {

                    System.err.println("[Error]: " + e.getMessage());
                    scanner.next();

                }
            }
        }
        scanner.close();
    }

    public static void printMenu(Date sysDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("The system date is now: " + sdf.format(sysDate));
        System.out.println("<This is the book ordering system.>");
        System.out.println("----------------------------------");
        System.out.println("1. System interface");
        System.out.println("2. Customer interface");
        System.out.println("3. Bookstore interface");
        System.out.println("4. Show system date");
        System.out.println("5. Quit the system");
        System.out.print("\nPlease enter your choice??..");
    }
}

class SystemDate {
    public static void run(Date sysDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("The System Date is now: " + sdf.format(sysDate));
        return;
    }
}
