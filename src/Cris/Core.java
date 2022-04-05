package Cris;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Core extends Thread {
    public int id;

    public int burstCounter;
    public Semaphore burstLock;
    public Semaphore coreLock;
    public Task activeTask;


    public Core(int id) {
        this.id = id;
        burstLock = new Semaphore(1);
        coreLock = new Semaphore(0);
        burstCounter = 0;
    }

    @Override
    public void run() {
        System.out.println("Dispatcher "+id+" using CPU "+id+"\n");
        boolean haveTask = false;
        do {
            boolean timeUp = false;
            Random random = new Random();

            //1/10 chance for a new task to enter queue when scheduler is PSJF
            boolean challengerApproaching = random.nextInt(10) == 0 && CPU.type == 1;

            try {
                burstLock.acquire();
                timeUp = burstCounter <= 0;
                burstLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (timeUp || challengerApproaching) {
                try {
                    CPU.queueLock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                haveTask = !CPU.readyQueue.isEmpty();

                if(challengerApproaching){
                    CPU.taskCount++;
                    Task newTask = new Task(CPU.taskCount, random.nextInt(1,25));
                    CPU.tasks.add(newTask);
                    CPU.readyQueue.add(newTask);
                }



                if(haveTask){
                    if (CPU.type == 0) {

                        Dispatcher.FCFS(this);

                    } else {
                        Dispatcher.PSJF(this,challengerApproaching);
                    }
                }


                CPU.queueLock.release();

            }
            activeTask.runLock.release();

            try {
                coreLock.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (haveTask);
    }
}
