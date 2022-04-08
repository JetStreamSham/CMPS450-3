package Cris;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Task extends Thread{
    int currentBurst;
    int burstTime;
    int id;

    public Semaphore burstLock;
    public Semaphore runLock;
    public Core core;
    public Task(int id,int burstTime){
        this.id = id;
        this.burstTime = burstTime;
        currentBurst =  0;
        runLock = new Semaphore(0);
        burstLock = new Semaphore(1);
    }



    @Override
    public void run() {
        while(burstTime>currentBurst){
            try {
                System.out.println("task "+id+" run acq");
                runLock.acquire();

                System.out.println("task "+id+" burst acq");
                core.burstLock.acquire();
                core.burstCounter--;
                core.burstLock.release();
                System.out.println("task "+id+" burst rel");


            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                burstLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thrd " + id + " on Core " +core.id+ " on Burst "+currentBurst++);
            burstLock.release();
            core.coreLock.release();
            System.out.println("task "+id+" core rel");

        }
        System.out.println("Thrd " + id + " on Core " +core.id+ " finished ");
    }
}
