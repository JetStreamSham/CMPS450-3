package Cris;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Task extends Thread{
    int currentBurst;
    int burstTime;
    int id;

    public Semaphore runLock;
    public Core core;
    public Task(int id,int burstTime){
        this.id = id;
        this.burstTime = burstTime;
        currentBurst =  0;
        runLock = new Semaphore(0);
    }



    @Override
    public void run() {
        while(burstTime>currentBurst){
            try {
                runLock.acquire();

                core.burstLock.acquire();
                core.burstCounter--;
                core.burstLock.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thrd " + id + " on Core " +core.id+ " on Burst "+currentBurst++);
            core.coreLock.release();
        }
    }
}
