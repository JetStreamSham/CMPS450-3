package com.company;

import java.util.concurrent.Semaphore;

public class Core extends Thread{

    public int id;
    public int burstCnt;
    public int runs;
    public int totalBursts = 0;
    public Task taskRunning;
    public boolean taskOn;
    public boolean taskDone;
    public Semaphore burstSem;
    public Semaphore coreSem;
    public Semaphore allCores;

    public Core(int i) {
        this.id = i;
        this.burstCnt = 0;
        this.burstSem = new Semaphore(1);
        this.coreSem = new Semaphore(1);
        this.allCores = new Semaphore(CPU.coreCnt);
        this.taskOn = true;
        this.taskDone = false;
    }

    @Override
    public void run(){
        // Displays The Dispatcher And the Corresponding CPU/Core
        System.out.println("Dispatcher " + id + "     | Using CPU " + id);
        /*if(CPU.method != 0){
            for(int i = 0; i < CPU.queue.size(); i++){
                int hold = CPU.queue.get(i).burstMax;
                System.out.println("Thread  " + CPU.queue.get(i).id + " BURSTS " + hold + " REQUIRED RUNS: " + hold / CPU.quantum);
                if(hold % CPU.quantum != 0){
                    System.out.println("Thread: " + CPU.queue.get(i).id + " NEEDS TO ADD A RUN");
                    totalBursts ++;
                }
                totalBursts += hold/CPU.quantum;
            }
            if(CPU.taskCnt > CPU.coreCnt){

            }else{
                runs = totalBursts;
            }
            System.out.println("Runs: " + runs );
        }else if(CPU.taskCnt > CPU.coreCnt){
            runs = CPU.taskCnt/CPU.coreCnt;
        } else{
            runs = CPU.taskCnt;
        }*/
        // Checks for how many cycles it should run before it is out of tasks
        while(CPU.finished < CPU.taskCnt){
        //for(int i = 0; i <= runs; i++){
            //System.out.println("CPU SIZE: " + CPU.queue.size());
            if(CPU.queue.size() != 0) {
                if(CPU.method == 0){
                    Dispatcher.NSJF(this);
                }else{
                    Dispatcher.RR(this);
                }
            }
            // Runs task on CPU until burst is over
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
