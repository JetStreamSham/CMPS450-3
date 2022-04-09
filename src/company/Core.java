package company;

import java.util.concurrent.Semaphore;
// Begin code changes by Matthew Arroyo
public class Core extends Thread{

    public int id;
    public int burstCnt;
    public Task taskRunning;
    public boolean taskOn;
    public boolean taskDone;
    public Semaphore burstSem;
    public Semaphore coreSem;
    public Semaphore mutex;

    public Core(int i) {
        this.id = i;
        this.burstCnt = 0;
        this.burstSem = new Semaphore(1);
        this.coreSem = new Semaphore(1);
        this.mutex = new Semaphore(1);
        this.taskOn = true;
        this.taskDone = false;
    }

    @Override
    public void run(){
        System.out.println("Dispatcher " + id + "     | Using CPU " + id);
// End code changes by Matthew Arroyo
// Begin code changes by Matthew Boudreaux
        while(CPU.finished < CPU.taskCnt){
            try {
                mutex.acquire();
                if(CPU.queue.size() != 0) {
                    if(CPU.method == 0){
                        Dispatcher.NSJF(this);
                    }else{
                        Dispatcher.RR(this);
                    }
                }
                mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
// End code changes by Matthew Boudreaux
// Begin code changes by Matthew Arroyo
            if(taskOn){
                while (taskOn) {
                    taskDone = false;
                    try {
                        burstSem.acquire();
                        while (burstCnt > 0) {
                            taskDone = false;
                        }
                        burstSem.release();
                        taskDone = true;
                        taskOn = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // if task is done tells the queue it is open with semaphore
            if(taskDone){
                try {
                    CPU.queueSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CPU.queueSem.release();
                taskRunning.taskSem.release();
                coreSem.release();
                taskOn = false;
                taskDone = false;
            }
        }
    }
}
// End code changes by Matthew Arroyo