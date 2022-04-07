package com.company;

import java.util.concurrent.Semaphore;

public class Task extends Thread {

    public int id;
    public int burstCnt;
    public int burstMax;
    public int currentBurstCnt;
    public Semaphore taskSem;
    public Semaphore mutex;
    public Core core;

    public Task(int i, int burst) {
        this.id = i;
        this.burstMax = burst;
        this.burstCnt = 0;
        this.currentBurstCnt = 0;
        this.taskSem = new Semaphore(0);
        this.mutex = new Semaphore(1);
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
                while(currentBurstCnt < CPU.quantum && burstCnt < burstMax){
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
                }
                if(burstCnt < burstMax){
                    CPU.queue.add(this);
                    System.out.println("CURRENT BURST = " + burstCnt + " CPU QUEUE SIZE " + CPU.queue.size());
                }
                try {
                    taskSem.acquire();
                    currentBurstCnt = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
