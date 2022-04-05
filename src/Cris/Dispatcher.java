package Cris;

public class Dispatcher {

    //get first element put it on the core
    public static void FCFS(Core core){
        int elemCount = 0;
        core.activeTask  = CPU.readyQueue.get(elemCount);
        core.burstCounter = core.activeTask.burstTime;
        core.activeTask.core = core;
        CPU.readyQueue.remove(elemCount);

        System.out.println();
        System.out.println("Dispatcher "+core.id +" running process "+core.activeTask.id);
        System.out.println("MB = "+core.activeTask.burstTime+"\t CB = "+core.activeTask.currentBurst);

    }

    static int taskCreated = 0;
    //create a random task
    public static void PSJF(Core core, boolean challengerApproaching){
        int elemCount = 0;
        core.activeTask  = CPU.readyQueue.get(elemCount);
        core.activeTask.core = core;
        CPU.readyQueue.remove(elemCount);
    }
}
