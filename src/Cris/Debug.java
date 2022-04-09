package Cris;

public class Debug {
    public static boolean debugMode = false;
    public static void Println(String str){
        if(debugMode)
            System.out.println(str);
    }
}
