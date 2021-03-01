package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMaintenance implements Intersection {

    public int X;
    public Semaphore direction0;
    public Semaphore direction1;
    public AtomicInteger direction0Counter;
    public AtomicInteger direction1Counter;
    public CyclicBarrier barrier0;
    public CyclicBarrier barrier1;
    public CyclicBarrier barrier2;
    public CyclicBarrier barrier3;


    public AtomicInteger turn;


    public void initX (int x) {
        this.X = x;
        direction0 = new Semaphore(X);
        direction1 = new Semaphore(X);
        direction0Counter = new AtomicInteger(0);
        direction1Counter = new AtomicInteger(0);
        barrier0 = new CyclicBarrier(X);
        barrier1 = new CyclicBarrier(X);
        barrier2 = new CyclicBarrier(X);
        barrier3 = new CyclicBarrier(X);
        turn = new AtomicInteger(0);

    }

    @Override
    public int getWaitingTime() {
        return 0;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {}
}
