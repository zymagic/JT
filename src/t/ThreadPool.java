package t;

import java.util.ArrayList;
import java.util.LinkedList;

public class ThreadPool {

    private static final Object sLock = new Object();
    
    private LinkedList<Runnable> mTasks = new LinkedList<Runnable>();
    
    private ArrayList<WorkThread> mThreads = new ArrayList<WorkThread>();
    
    private int mCount = 5;
    
    public ThreadPool() {
        
    }
    
    public ThreadPool(int count) {
        this.mCount = count;
    }
    
    public void setCount(int count) {
        synchronized (sLock) {
            mCount = count;
        }
    }
    
    public void start() {
        synchronized (sLock) {
            for (int i = 0; i < mCount; i++) {
                WorkThread t = new WorkThread(this);
                mThreads.add(t);
                t.start();
            }
        }
    }
    
    public void remove(Runnable r) {
        synchronized (sLock) {
            mTasks.remove(r);
        }
    }
    
    public void stopAll() {
        synchronized (sLock) {
            mTasks.clear();
            for (WorkThread t : mThreads) {
                t.cancel();
            }
            sLock.notifyAll();
        }
    }
    
    public void executeTask(Runnable task) {
        synchronized (sLock) {
            mTasks.add(task);
            sLock.notifyAll();
        }
    }
    
    private Runnable getTask() {
        synchronized (sLock) {
            return mTasks.poll();
        }
    }
    
    private static final class WorkThread extends Thread {
        
        private ThreadPool mPool;
        private boolean running = true;
        
        public WorkThread(ThreadPool pool) {
            mPool = pool;
        }
        
        public void run() {
            while (running) {
                Runnable task = mPool.getTask();
                synchronized (sLock) {
                    if (task == null) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            // nothing
                        }
                    }
                }
                
                task.run();
            }
        }
        
        public void cancel() {
            running = false;
        }
    }
}
