import java.util.*;
import java.io.*;

    // test
public class Bookstore{
    public static void run(Scanner in){
        
        run: while(true){
            int input= 0;
            System.out.println("----------------------------------");
            System.out.println("1. Order Update.");
            System.out.println("2. Order Query.");
            System.out.println("3. N most Popular Book Query.");
            System.out.println("4. Back to main menu");
            System.out.println(" ");
            System.out.print("What is your choice??..");
            
            try {
                input = in.nextInt();
            }

            catch(Exception e){
                input = 0 ;
            }

            finally{
                switch(input){
                    case 1:
                        OrUpdate();
                        break;
                    case 2:
                        OrQuery();
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

    private static void OrUpdate(){
        int input = 0
        System.out.print("Please enter the OrderID that you want to change: ");
        try{
            
        }
    }

    private static void OrQuery(){

    }

    private static void Npop(){

    }

    
}
