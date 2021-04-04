import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean applicationIsRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (applicationIsRunning) {
            printMenu();

            boolean inputIsValid = false;

            while (!inputIsValid) {
                try {

                    int userAccount = scanner.nextInt();

                    switch (userAccount) {
                    case 1:
                        System.run(scanner);
                        inputIsValid = true;
                        break;
                    case 2:
                        Customer.run(scanner);
                        inputIsValid = true;
                        break;
                    case 3:
                        Bookstore.run(scanner);
                        inputIsValid = true;
                        break;
                    case 4:
                        SystemDate.run(scanner);
                        inputIsValid = true;
                        break;
                    case 5:
                        applicationIsRunning = false;
                        inputIsValid = true;
                        break;
                    default:
                        System.err.println(Error.INVALID_INPUT);
                    }

                } catch (Exception e) {

                    System.err.println(Error.INVALID_INPUT);
                    scanner.next();

                }
            }
        }
        scanner.close();
    }

    public static void printMenu() {
        System.out.println("The system date is now: 0000-00-00");
        System.out.println("<This is the book ordering system.>");
        System.out.println("----------------------------------");
        System.out.println("1. System interface");
        System.out.println("2. Customer interface");
        System.out.println("3. Bookstore interface");
        System.out.println("4. Show system date");
        System.out.println("5. Quit the system");
    }
}
