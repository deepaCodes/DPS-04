	
import java.util.*;
public class Power {

    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        int num = 0;
        int pow = 0;
        int power = 0;

        System.out.print("Enter number: ");
        num = sc.nextInt();

        System.out.print("Enter power: ");
        pow = sc.nextInt();
        if(num < 0 || pow < 0)
        {
            System.out.print("One of the inputs was negative . Cannot compute");
            System.exit(0);
        }

        System.out.print(power(num,pow));
    }
    public static int power(int a, int b)
    {
            int power = 1;
            for(int c=0;c<b;c++)
            power*=a;
            return power;
        }

}