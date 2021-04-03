import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean applicationIsRunning = true;
        Scanner scanner = new Scanner(System.in);

        while(applicationIsRunning) {
            printMenu();

            boolean inputIsValid = false;

            while (!inputIsValid) {
                try {

                    int userAccount =  scanner.nextInt();

                    switch(userAccount) {
                        case 1:
                            System.run();
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
                            SystemDate.run();
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
        System.out.println("2. A passenger");
        System.out.println("3. A driver");
        System.out.println("4. A manager");
        System.out.println("5. None of the above");
        System.out.println("Please enter [1-4]");
    }
}
