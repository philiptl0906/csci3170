
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemDate {
    public static void run(Date sysDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("The System Date is now: " + sdf.format(sysDate));

    }
}