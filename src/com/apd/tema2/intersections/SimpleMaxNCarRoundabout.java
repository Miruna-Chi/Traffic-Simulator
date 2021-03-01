package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaxNCarRoundabout implements Intersection {
    private Integer waitingTimeMs;
    public List<Semaphore> semaphores;
    private int lanes;
    private int permits;
    public Integer lanesNo;

    public SimpleMaxNCarRoundabout() {}

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

    }
}
