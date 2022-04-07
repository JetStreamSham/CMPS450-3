package Cris;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class CPU {
    public static CPU cpu;
    public static int type;

    public static int coreCount;
    public static Core cores[];

    public static int taskCount;
    public static ArrayList<Task> tasks;
    public static Task activeTask[];

    public static ArrayList<Task> readyQueue;
    public static  Semaphore queueLock;
    public static  Semaphore taskLock;


    public static void Setup(int coreCount, int type) {
        coreCount = coreCount;
        type = type;

        Random random = new Random();
        taskCount = random.nextInt(1, 25);
        tasks = new ArrayList<Task>();
        readyQueue = new ArrayList<Task>();
        cores = new Core[coreCount];

        taskLock  = new Semaphore(1);
        queueLock = new Semaphore(1);


        if (type == 0) {
            System.out.println("Sched Algo: FCFS");
        }
        else {
            System.out.println("Sched Algo: PSJF");
        }

        System.out.println("--------------- Ready Queue ---------------");
        for (int i = 0; i < taskCount; i++) {
            int burstTime = random.nextInt(1, 50);
            tasks.add( new Task(i, burstTime));
            readyQueue.add(tasks.get(i));
            readyQueue.get(i).start();
            System.out.println("ID:" + tasks.get(i).id + ", Max Burst:" + burstTime);
        }
        System.out.println("-------------------------------------------");


        for(int i  =0 ;i < coreCount;i++){
            cores[i] = new Core(i);
            cores[i].start();
        }

        for(int i = 0; i < taskCount ;i++){
            try{
                tasks.get(i).join();
                System.out.println("task "+i+" done");
            }catch ( Exception e){

            }
        }

        for(int i = 0; i < coreCount ;i++){
            try{
                cores[i].join();
                System.out.println("core "+i+" done");

            }catch ( Exception e){

            }
        }
    }


}
