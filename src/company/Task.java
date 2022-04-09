package company;

import java.util.concurrent.Semaphore;
// Begin code changes by Matthew Arroyo
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
// End code changes by Matthew Arroyo
// Begin code changes by Matthew Boudreaux
            }else{
                if(burstCnt + CPU.quantum > burstMax){
                    quantumBursts = burstMax - burstCnt;
                }
                while(currentBurstCnt < quantumBursts){
                    if(burstCnt < burstMax){
                        try {
                            mutex.acquire();
                            core.burstCnt--;
                            burstCnt++;
                            currentBurstCnt++;
                            mutex.release();
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
                                Dispatcher.mutex.acquire();
                                CPU.queue.add(CPU.queue.size(),this);
                                Dispatcher.mutex.release();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
// End code changes by Matthew Boudreaux
// Begin code changes by Matthew Arroyo
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
    }
}
// End code changes by Matthew Arroyo