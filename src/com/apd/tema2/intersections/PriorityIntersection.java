package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection {
    private Integer waitingTimeMs;
    private Integer lowPriorityCarsNo;
    private Integer highPriorityCarsNo;
    public AtomicInteger highPriorityCarsInIntersection;

    BlockingQueue<Integer> lowPriorityQueue;

    public PriorityIntersection() {}

    @Override
    public int getWaitingTime() {
        return waitingTimeMs;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {}

    public void initLowPriorityQueue(int highPriorityCarsNo, int lowPriorityCarsNo) {
        lowPriorityQueue = new ArrayBlockingQueue<Integer>(lowPriorityCarsNo);
        this.highPriorityCarsNo = highPriorityCarsNo;
        this.lowPriorityCarsNo = lowPriorityCarsNo;
        highPriorityCarsInIntersection = new AtomicInteger(0);
    }

    public void addCar(int carId) throws InterruptedException {
        lowPriorityQueue.put(carId);
    }

    public void removeCar() throws InterruptedException {
        lowPriorityQueue.take();
    }

    public int peekAtCars() {
        return lowPriorityQueue.peek();
    }
}
