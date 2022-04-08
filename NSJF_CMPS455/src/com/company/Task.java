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
    public Semaphore hold;
    public Core core;

    public Task(int i, int burst) {
        this.id = i;
        this.burstMax = burst;
        this.burstCnt = 0;
        this.currentBurstCnt = 0;
        this.quantumBursts = CPU.quantum;
        this.taskSem = new Semaphore(0);
        this.mutex = new Semaphore(1);
        this.done = new Semaphore(CPU.taskCnt);
        this.hold = new Semaphore(1);
    }

    @Override
    public void run(){
        while (burstCnt < burstMax){
            try {
                taskSem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (CPU.method == 0) {
                while(burstCnt < burstMax){
                    try {
                        mutex.acquire();
                        core.burstCnt--;
                        burstCnt++;
                        mutex.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread " + id + "        | Using CPU " + core.id + "; On Burst " + burstCnt + ".");
                }
            }else{
                if(burstCnt + CPU.quantum > burstMax){
                    quantumBursts = burstMax - burstCnt;
                }
                while(currentBurstCnt < quantumBursts){
                    if(burstCnt < burstMax){
                        try {
                            //System.out.println("Thread: " + id + " I am trying to acquire mutex semaphore");
                            mutex.acquire();
                            core.burstCnt--;
                            burstCnt++;
                            currentBurstCnt++;
                            mutex.release();
                            //System.out.println("Thread: " + id + " I have released mutex semaphore");
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
                                //runAgain.acquire();
                                //System.out.println("Thread: " + id + " I am trying to acquire dispatcher mutex semaphore");
                                Dispatcher.mutex.acquire();
                                CPU.queue.add(CPU.queue.size(),this);
                                //System.out.println("I have been added THREAD: " + id);
                                Dispatcher.mutex.release();
                                //System.out.println("Thread: " + id + " I have released dispatcher mutex semaphore");
                                //taskSem.acquire();
                                //runAgain.release();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        try {
            done.acquire();
            hold.acquire();
            CPU.finished++;
            hold.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(CPU.finished == CPU.taskCnt){
            for(int i = 0; i < CPU.finished; i++){
                done.release();
            }
        }
        //System.out.println("I AM DONE THREAD " + id + " TOTAL THREADS FINISHED: " + CPU.finished);
    }
}
