import Cris.CPU;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String args[]){
//        Random random = new Random();
//
//        ArrayList<Integer> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            list.add(random.nextInt(0,100));
//        }
//        list.sort(((e1,e2)->{
//            if(e1.intValue() > e2.intValue()){
//                return 1;
//            } else if (e1.intValue() == e2.intValue()){
//                return  0;
//            }
//            return  -1;
//        }));
//
//        for(int i = 0; i < list.size();i++){
//            System.out.println(list.get(i) + ":"+i);
//        }
        CPU.Setup(1,1);
    }
}
