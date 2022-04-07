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
        System.out.println("Dispatcher " + id + " using CPU " + id + "\n");
        boolean haveTask = false;
        do {
            boolean timeUp = false;
            Random random = new Random();

            // 1/3 chance for a new task to enter queue when scheduler is PSJF
            boolean challengerApproaching = random.nextInt(3) == 0 && CPU.type == 1;

            try {
                System.out.println("ccore "+id + " burst acq");
                burstLock.acquire();
                timeUp = burstCounter <= 0;
                burstLock.release();
                System.out.println("ccore "+id + " burst rel");

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (timeUp || challengerApproaching) {
                try {
                    System.out.println("ccore "+id + " queue acq");
                    CPU.queueLock.acquire();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (challengerApproaching) {
                    CPU.taskCount++;
                    Task newTask = new Task(CPU.taskCount, random.nextInt(1, 25));
                    CPU.tasks.add(newTask);
                    CPU.readyQueue.add(newTask);
                }

                haveTask = !CPU.readyQueue.isEmpty();


                if (haveTask) {
                    if (CPU.type == 0) {

                        Dispatcher.FCFS(this);

                    } else {
                        Dispatcher.PSJF(this, challengerApproaching);
                    }
                }
                else {
                    System.out.println("AT empty:" + !haveTask);
                }

                CPU.queueLock.release();
                System.out.println("ccore "+id + " queue rel");


            }


            if (haveTask) {
                try {
                    activeTask.runLock.release();
                    System.out.println("ccore "+id + " actTsk rel");
                    System.out.println("ccore "+id + " core acq");
                    coreLock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } while (haveTask);
        System.out.println("Core "+id+" completed \n");
    }
}
