package Cris;

public class Dispatcher {

    public static void FCFS(Core core){
<<<<<<< HEAD
        int elemCount = 0;
        core.activeTask  = CPU.readyQueue.get(elemCount);
=======
        core.activeTask  = CPU.readyQueue.get(0);
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5
        core.burstCounter = core.activeTask.burstTime;
        core.activeTask.core = core;
        CPU.readyQueue.remove(0);

        System.out.println();
        System.out.println("Dispatcher "+core.id +" running process "+core.activeTask.id);
        System.out.println("MB = "+core.activeTask.burstTime+"\t CB = "+core.activeTask.currentBurst);

    }

<<<<<<< HEAD
    public static void PSJF(Core core, boolean challengerApproaching){

        if(core.burstCounter == 0){
            core.activeTask = CPU.readyQueue.get(0);
            core.burstCounter =   core.activeTask.burstTime - core.activeTask.currentBurst;
            core.activeTask.core = core;
            CPU.readyQueue.remove(0);
        }
        else if(challengerApproaching){
            Task challenger = CPU.readyQueue.get(CPU.readyQueue.size()-1);
            System.out.println("Challenger Burst Time: "+challenger.burstTime +"\t Active Task Current Burst Time:"+core.burstCounter );

            if(challenger.burstTime < core.burstCounter){
                CPU.readyQueue.remove(CPU.readyQueue.size()-1);
                CPU.readyQueue.add(core.activeTask);
                core.activeTask =  challenger;
                core.burstCounter = challenger.burstTime-challenger.currentBurst;
                core.activeTask.core = core;
                System.out.println("Challenger succeeded");

            }
        }
=======
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


>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5

    }
}
