package Cris;

import java.util.concurrent.Semaphore;

import static Cris.Debug.Println;

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
                Println("task "+id+" run acq");
                runLock.acquire();

                Println("task "+id+" burst acq");
                core.burstLock.acquire();
                core.burstCounter--;
                core.burstLock.release();
                Println("task "+id+" burst rel");


            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thrd " + id + " on Core " +core.id+ " on Burst "+currentBurst++);
            core.coreLock.release();
            Println("task "+id+" core rel");

        }
        System.out.println("Thrd " + id + " on Core " +core.id+ " finished \n");
//        core.coreLock.release();
//        Println("task "+id+" core rel final");

    }
}
