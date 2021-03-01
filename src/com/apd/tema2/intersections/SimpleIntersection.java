package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.PriorityBlockingQueue;

import static java.lang.Integer.valueOf;

public class SimpleIntersection implements Intersection {

    public CyclicBarrier barrier;
    private PriorityBlockingQueue<Integer> carWaitingList;

    public SimpleIntersection() {
        carWaitingList = new PriorityBlockingQueue<Integer>(Main.carsNo);
        barrier = new CyclicBarrier(Main.carsNo);
    }

    public void addCar(int carWaitingTime) {
        carWaitingList.put(valueOf(carWaitingTime));
    }

    public void removeCar() throws InterruptedException {
        carWaitingList.take();
    }

    @Override
    public int getWaitingTime() {
        return 0;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {}

    public int peekAtCars() {
        return carWaitingList.peek();
    }

}
