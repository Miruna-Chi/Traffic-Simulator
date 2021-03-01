package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static java.lang.Integer.valueOf;

public class SimpleNRoundabout implements Intersection {

    public Semaphore semaphore;
    private Integer waitingTimeMs;
    public CyclicBarrier barrier;


    public SimpleNRoundabout() {}

    @Override
    public int getWaitingTime() {
        return waitingTimeMs;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {
        semaphore = new Semaphore(permits);
        this.waitingTimeMs = waitingTime;
        barrier = new CyclicBarrier(Main.carsNo);
    }


}