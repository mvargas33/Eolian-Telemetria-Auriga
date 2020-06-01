package Sandboxes;

public class GeneralSandbox {
    public static void main(String[] args) {
        double a = 1.999;
        int d = 0;
        double one = 1;
        double tolerance = Math.pow(10, -10);
        double copy = a;

        while(true){
            double rest = copy % one;
            System.out.println(rest);
            if(rest < tolerance){
                break;
            }else{
                d++;
                copy = a * Math.pow(10, d);
            }
        }
        System.out.println(d);
    }
}
