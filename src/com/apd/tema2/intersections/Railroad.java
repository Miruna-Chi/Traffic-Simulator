package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;

public class Railroad implements Intersection {

    public CyclicBarrier barrier;
    private BlockingQueue<Integer> side0;
    private BlockingQueue<Integer> side1;
    public Boolean trainHasPassed = false;


    public Railroad() {
        barrier = new CyclicBarrier(Main.carsNo);
        side0 = new ArrayBlockingQueue<>(Main.carsNo);
        side1 = new ArrayBlockingQueue<>(Main.carsNo);
    }

    public void addCar(int carId, int side) throws InterruptedException {
        if (side == 0)
            side0.put(carId);
        else side1.put(carId);
    }

    public int peekAtCar(int side) {
        if (side == 0)
            return side0.peek();
        return side1.peek();
    }

    public void removeCar(int side) throws InterruptedException {
        if (side == 0)
            side0.take();
        else side1.take();
    }

    @Override
    public int getWaitingTime() {
        return 0;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {}

    public void showQueues() {
        System.out.println(side0);
        System.out.println(side1);
        System.out.println();
    }
}
