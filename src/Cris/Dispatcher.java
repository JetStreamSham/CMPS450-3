package Cris;

public class Dispatcher {

    //get first element put it on the core
    public static void FCFS(Core core){
        core.activeTask  = CPU.readyQueue.get(0);
        core.burstCounter = core.activeTask.burstTime;
        core.activeTask.core = core;
        CPU.readyQueue.remove(0);

        System.out.println();
        System.out.println("Dispatcher "+core.id +" running process "+core.activeTask.id);
        System.out.println("MB = "+core.activeTask.burstTime+"\t CB = "+core.activeTask.currentBurst);

    }

    //check if current task on core is done if not do preempt check
    //sort ready queue in ascending order compare top of the queue with task on core
    public static void PSJF(Core core) {

        System.out.println("outhouse");
        CPU.readyQueue.sort((t1,t2)->{
            if(t1.currentBurst > t2.currentBurst){
                return 1;
            } else if (t1.currentBurst == t2.currentBurst) {
                return  0;
            }
            return -1;
        });

        try {
            core.burstLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(core.burstCounter == 0){
            core.activeTask  = CPU.readyQueue.get(0);
            core.burstCounter = core.activeTask.burstTime;
            core.activeTask.core = core;
            CPU.readyQueue.remove(0);
        }
        core.burstLock.release();



    }
}
