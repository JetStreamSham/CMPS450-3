package Cris;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Core extends Thread {
    public int id;

    public int burstCounter;
    public Semaphore burstLock;
    public Semaphore coreLock;
    public Task activeTask;
    static int taskCreated = 0;
    public Semaphore taskCreatedLock;


    public Core(int id) {
        this.id = id;
        burstLock = new Semaphore(1);
        taskCreatedLock = new Semaphore(1);
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

            boolean challengerApproaching = false;

            try {
                taskCreatedLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //to prevent creating too many task randomly its capped at
            //creating %50 of the original task count
            float perc = (float) taskCreated / (float) CPU.taskCount;
            if (perc <= .5f) {
                // 1/3 chance for a new task to enter queue when scheduler is PSJF
                challengerApproaching = random.nextInt(3) == 0 && CPU.type == 1;
                taskCreated++;
            }
            taskCreatedLock.release();

            try {
                System.out.println("ccore " + id + " burst acq");
                burstLock.acquire();
                timeUp = burstCounter <= 0;
                burstLock.release();
                System.out.println("ccore " + id + " burst rel");

            }
            catch (Exception e) {
                e.printStackTrace();
            }


            if (timeUp || challengerApproaching || CPU.type == 1) {
                try {
                    System.out.println("ccore " + id + " queue acq");
                    CPU.queueLock.acquire();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                if (challengerApproaching) {
                    Task newTask = new Task(CPU.taskCount, random.nextInt(1, 25));
                    CPU.tasks.add(newTask);
                    CPU.readyQueue.add(newTask);
                    taskCreated++;
                }

                haveTask = !CPU.readyQueue.isEmpty();

                System.out.println(CPU.type);

                if (haveTask) {
                    if (CPU.type == 0) {

                        Dispatcher.FCFS(this);

                    } else {
                        Dispatcher.PSJF(this);
                    }
                } else {
                    System.out.println("AT empty:" + !haveTask);
                }

                CPU.queueLock.release();
                System.out.println("ccore " + id + " queue rel");


            }


            if (haveTask) {
                try {
                    activeTask.runLock.release();
                    System.out.println("ccore " + id + " actTsk rel");
                    System.out.println("ccore " + id + " core acq");
                    coreLock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } while (haveTask);
        System.out.println("Core " + id + " completed \n");
    }
}
