package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.valueOf;

public class SimpleStrictNCarRoundabout implements Intersection {

    private Integer waitingTimeMs;
    public List<Semaphore>  semaphores;
    public CyclicBarrier allBarrier;
    public CyclicBarrier barrier1;
    public CyclicBarrier barrier2;
    public CyclicBarrier barrier3;
    private int lanes;
    private int permits;
    public Integer lanesNo;

    public SimpleStrictNCarRoundabout() {}

    @Override
    public int getWaitingTime() {
        return waitingTimeMs;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {
        semaphores = new ArrayList<>();

        for (int i = 0; i < lanes; i++) {
            semaphores.add(new Semaphore(permits));
        }

        this.waitingTimeMs = waitingTime;

        allBarrier = new CyclicBarrier(Main.carsNo);
        barrier1 = new CyclicBarrier(lanes * permits);
        barrier2 = new CyclicBarrier(lanes * permits);
        barrier3 = new CyclicBarrier(lanes * permits);

    }

}
