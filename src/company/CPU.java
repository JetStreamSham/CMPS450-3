package company;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
// Begin code changes by Matthew Arroyo
public class CPU {
    public static int coreCnt;
    public static Core[] cores;
    public static int taskCnt;
    public static int finished;
    public static int method;
    public static int quantum;
    public static ArrayList<Task> tasks;
    public static ArrayList<Task> queue;
    public static Semaphore queueSem;
    public static Semaphore taskSem;

    public static void Start(int coreCnt, int method, int quantum){
        CPU.coreCnt = coreCnt;
        CPU.method = method;
        CPU.quantum = quantum;
        Random rnd = new Random();
        finished = 0;
        taskCnt = rnd.nextInt(25)+1;
        //taskCnt = 5;
        tasks = new ArrayList<>();
        queue = new ArrayList<>();
        cores = new Core[coreCnt];

        queueSem = new Semaphore(1);
        taskSem = new Semaphore(0);

        if(method == 0){
            System.out.println("\nScheduling Algorithm: NSJF\n");
            System.out.println("Number Of Threads: " + taskCnt + "\n");
            CPU.quantum = 0;
        }else{
            System.out.println("\nScheduling Algorithm: RR\n");
            System.out.println("Number Of Threads: " + taskCnt + "     | Time Quantum: " + CPU.quantum + "\n");
        }

        System.out.println("---------------Ready Queue---------------");
        /*tasks.add(new Task(0,18));
        tasks.add(new Task(1,7));
        tasks.add(new Task(2,25));
        tasks.add(new Task(3,41));
        tasks.add(new Task(4,21));*/
        for(int i = 0; i < taskCnt; i++){
            int burst = rnd.nextInt(50)+1;
            tasks.add(new Task(i, burst));
            queue.add(tasks.get(i));
            queue.get(i).start();
            System.out.println("ID: " + tasks.get(i).id + "  Max Burst: " + tasks.get(i).burstMax + "  Current Burst: 0");
        }
        System.out.println("-----------------------------------------");
        if(coreCnt > taskCnt){
            coreCnt = taskCnt;
        }
        // Creates the number of cores/cpus
        for(int i = 0; i < coreCnt; i++){
            cores[i] = new Core(i);
            cores[i].start();
        }
        // Starting up the task threads
        for(int i = 0; i < taskCnt; i++){
            try {
                tasks.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Starting up the core/cpu threads
        for(int i = 0; i < coreCnt; i++){
            try{
                cores[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
// End code changes by Matthew Arroyo