package Cris;

public class Dispatcher {

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

    }
}
