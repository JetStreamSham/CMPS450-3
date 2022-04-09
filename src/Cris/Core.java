package Cris;
//Written by cris russ
import java.util.Random;
import java.util.concurrent.Semaphore;

import static Cris.Debug.Println;

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
        Random random = new Random();
        System.out.println("Dispatcher " + id + " using CPU " + id + "\n");
        boolean haveTask = false;
        do {
            boolean timeUp = false;
            boolean challengerApproaching = false;

            Println("core "+id + " tcl acq");
            try {
                CPU.taskCreatedLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float perc = (float)CPU.taskCreated/(float)CPU.taskCount;
            Println("Created Task Count "+CPU.taskCreated+"\tPerc "+perc);

            //limits the amount of created task to 50% the original task count
            if(perc <= .5){
                // 1/3 chance for a new task to enter queue when scheduler is PSJF
                challengerApproaching = random.nextInt(3) == 0 && CPU.type == 1;
                if(challengerApproaching)
                    Println("Can create more task");

                CPU.taskCreated++;
            }
            CPU.taskCreatedLock.release();
            Println("core "+id + " tcl rel");



            try {
                Println("core "+id + " burst acq");
                burstLock.acquire();
                timeUp = burstCounter <= 0;
                burstLock.release();
                Println("core "+id + " burst rel");

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (timeUp || challengerApproaching) {
                try {
                    Println("core "+id + " queue acq");
                    CPU.queueLock.acquire();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (challengerApproaching) {

                    try {
                        CPU.taskCreatedLock.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    Task newTask = new Task((CPU.taskCount-1)+CPU.taskCreated, random.nextInt(1, 25));
                    CPU.tasks.add(newTask);
                    CPU.readyQueue.add(newTask);
                    newTask.start();

                    CPU.taskCreatedLock.release();
                }

                haveTask = !CPU.readyQueue.isEmpty();


                if (haveTask) {
                    if (CPU.type == 0) {

                        Dispatcher.FCFS(this);

                    } else {
                        Dispatcher.PSJF(this, challengerApproaching);
                    }
                }

                CPU.queueLock.release();
                Println("core "+id + " queue rel");

            }


            if (haveTask) {
                try {
                    activeTask.runLock.release();
                    Println("core "+id + " actTsk rel");

                    Println("core "+id + " core acq");
                    coreLock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } while (haveTask);
        Println("Core "+id+" completed \n");
    }
}
