package com.company;

import java.util.concurrent.Semaphore;

public class Task extends Thread {

    public int id;
    public int burstCnt;
    public int burstMax;
    public int currentBurstCnt;
    public int quantumBursts;
    public Semaphore taskSem;
    public Semaphore mutex;
    public Semaphore done;
    public Semaphore runAgain;
    public Core core;

    public Task(int i, int burst) {
        this.id = i;
        this.burstMax = burst;
        this.burstCnt = 0;
        this.currentBurstCnt = 0;
        this.quantumBursts = CPU.quantum;
        this.taskSem = new Semaphore(0);
        this.mutex = new Semaphore(1);
        this.done = new Semaphore(1);
        this.runAgain = new Semaphore(1);
    }

    @Override
    public void run(){
        // decreases the burst count in cores "the max bursts being decreased" and increases the burst count for the thread.
        // Only does both to ensure they are both keeping track of how many bursts are being completed.
        try {
            taskSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (burstCnt < burstMax){
            if (CPU.method == 0) {
                try {
                    mutex.acquire();
                    core.burstCnt--;
                    burstCnt++;
                    mutex.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + id + "        | Using CPU " + core.id + "; On Burst " + burstCnt + ".");
            }else{
                if(burstCnt + CPU.quantum > burstMax){
                    quantumBursts = burstMax - burstCnt;
                }
                while(currentBurstCnt < quantumBursts){
                    if(burstCnt < burstMax){
                        try {
                            mutex.acquire();
                            core.burstCnt--;
                            burstCnt++;
                            currentBurstCnt++;
                            mutex.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Thread " + id + "        | Using CPU " + core.id + "; On Burst " + burstCnt + ".");
                    }else{
                        currentBurstCnt = quantumBursts;
                    }
                    if(currentBurstCnt == quantumBursts && burstCnt < burstMax){
                        if(CPU.finished != CPU.taskCnt){
                            try {
                                runAgain.acquire();
                                CPU.queue.add(CPU.queue.size(),this);
                                System.out.println("I have been added THREAD: " + id);
                                runAgain.release();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            taskSem.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        try {
            done.acquire();
            CPU.finished++;
            done.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("I AM DONE THREAD " + id + " TOTAL THREADS FINISHED: " + CPU.finished);
    }
}
