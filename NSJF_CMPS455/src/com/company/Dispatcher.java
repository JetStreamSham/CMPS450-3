package com.company;

import java.util.concurrent.Semaphore;

public class Dispatcher {

    public static Semaphore mutex = new Semaphore(1);
    public static int count = 0;
    public static int RRCount = 0;

    public static void NSJF(Core core){
        // Selecting the task to run on CPU/Core. Mutex used to stop two CPU/Cores from trying to run same task
        try {
            mutex.acquire();
            int elemCnt;
            elemCnt = 0;
            int burst = 100;
            for (int i = 0; i < CPU.queue.size(); i++) {
                if(CPU.queue.get(i).burstMax < burst){
                    burst = CPU.queue.get(i).burstMax;
                    elemCnt = i;
                }
            }
            count++;
            core.taskRunning = CPU.queue.get(elemCnt);
            CPU.queue.remove(elemCnt);
            core.burstCnt = core.taskRunning.burstMax;
            core.taskRunning.core = core;
            core.taskOn = true;
            core.taskDone = false;
            core.coreSem.acquire();
            System.out.println("\nDispatcher " + core.id + "    | Running Process " + core.taskRunning.id +
                    "\nThread " + core.taskRunning.id + "        | MB = " + core.taskRunning.burstMax + " CB = "
                    + core.taskRunning.burstCnt + " BT = " + core.taskRunning.burstMax + " BG = " + core.taskRunning.burstMax);
            core.taskRunning.taskSem.release();
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void RR(Core core){
        try {
            mutex.acquire();
            System.out.println("We MAKE IS TO DISPATCH   CPU QUEUE " + CPU.queue.size() + " CONTAINS " + CPU.queue.get(RRCount));
            if(CPU.queue.size() != 0){
                core.taskRunning = CPU.queue.get(RRCount);
                CPU.queue.remove(RRCount);
                if(core.taskRunning.burstCnt + CPU.quantum > core.taskRunning.burstMax){
                    core.burstCnt = core.taskRunning.burstMax - core.taskRunning.burstCnt;
                }else{
                    core.burstCnt = CPU.quantum;
                }
                System.out.println("\nDispatcher " + core.id + "    | Running Process " + core.taskRunning.id +
                        "\nThread " + core.taskRunning.id + "        | MB = " + core.taskRunning.burstMax + " CB = "
                        + core.taskRunning.burstCnt + " BT = " + CPU.quantum + " BG = " + CPU.quantum);
                core.taskRunning.core = core;
                core.taskRunning.currentBurstCnt = 0;
                core.taskOn = true;
                core.taskDone = false;
                core.coreSem.acquire();
                core.taskRunning.taskSem.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
