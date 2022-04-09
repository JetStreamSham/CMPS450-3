package Cris;

import java.util.Random;
import java.util.concurrent.Semaphore;

import static Cris.Debug.Println;

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


<<<<<<< HEAD

            try {
                Println("core "+id + " burst acq");
                burstLock.acquire();
                timeUp = burstCounter <= 0;
                burstLock.release();
                Println("core "+id + " burst rel");
=======
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
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5

            }
            catch (Exception e) {
                e.printStackTrace();
            }


            if (timeUp || challengerApproaching || CPU.type == 1) {
                try {
<<<<<<< HEAD
                    Println("core "+id + " queue acq");
=======
                    System.out.println("ccore " + id + " queue acq");
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5
                    CPU.queueLock.acquire();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                if (challengerApproaching) {
<<<<<<< HEAD

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
=======
                    Task newTask = new Task(CPU.taskCount, random.nextInt(1, 25));
                    CPU.tasks.add(newTask);
                    CPU.readyQueue.add(newTask);
                    taskCreated++;
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5
                }

                haveTask = !CPU.readyQueue.isEmpty();

                System.out.println(CPU.type);

                if (haveTask) {
                    if (CPU.type == 0) {

                        Dispatcher.FCFS(this);

                    } else {
                        Dispatcher.PSJF(this);
                    }
<<<<<<< HEAD
                }

                CPU.queueLock.release();
                Println("core "+id + " queue rel");
=======
                } else {
                    System.out.println("AT empty:" + !haveTask);
                }

                CPU.queueLock.release();
                System.out.println("ccore " + id + " queue rel");

>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5

            }


            if (haveTask) {
                try {
                    activeTask.runLock.release();
<<<<<<< HEAD
                    Println("core "+id + " actTsk rel");

                    Println("core "+id + " core acq");
=======
                    System.out.println("ccore " + id + " actTsk rel");
                    System.out.println("ccore " + id + " core acq");
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5
                    coreLock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } while (haveTask);
<<<<<<< HEAD
        Println("Core "+id+" completed \n");
=======
        System.out.println("Core " + id + " completed \n");
>>>>>>> bbbcc852c79a67fcce88194059617eaf1d06a6f5
    }
}
