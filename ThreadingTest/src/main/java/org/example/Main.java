package org.example;

public class Main {
    public static void main(String[] args) {
        Object sharedLock = new Object();
        Thread t1 = new Thread(new NumberPrinter(1, sharedLock));
        Thread t2 = new Thread(new NumberPrinter(2, sharedLock));
        Thread t3 = new Thread(new NumberPrinter(0, sharedLock));

        t1.start();
        t2.start();
        t3.start();
    }
}

class NumberPrinter implements Runnable {

    static boolean DEBUG = false;

    static int MAX = 10;

    static int start = 1;

    int index = 0;

    Object sharedLock = null;

    public NumberPrinter(int index, Object sharedLock) {
        System.out.println("Thread with index : " + index + " started...");
        this.index = index;
        this.sharedLock = sharedLock;
    }

    @Override
    public void run() {

        while (true) {
            if (start > MAX) {
                if (DEBUG) {
                    System.out.println("Thread : " + index + " exiting...");
                }
                return;
            }

            synchronized (sharedLock) {
                int mod = start % 3;

                try {
                    if (mod == index) {
                        System.out.println("Thread idx : " + Thread.currentThread().getName() + (DEBUG ? "(start : " + start + ", mod : " + mod + ") : " : "") + " : " +  start);

                        start++;
                        sharedLock.notifyAll();
                    } else {
                        if (DEBUG) {
                            System.out.println("Thread : " + index + " is in waiting state...");
                        }
                        sharedLock.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
