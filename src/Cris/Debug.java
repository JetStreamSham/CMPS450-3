package Cris;
//Written by cris russ

public class Debug {
    public static boolean debugMode = false;
    public static void Println(String str){
        if(debugMode)
            System.out.println(str);
    }
}
