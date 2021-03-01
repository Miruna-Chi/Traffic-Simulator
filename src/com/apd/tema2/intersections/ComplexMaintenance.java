package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Math.min;

public class ComplexMaintenance implements Intersection {

    public CyclicBarrier barrier;

    public int N; // old lanes
    public int M; // new lanes
    public int X; // max no. of cars

    public Map<Integer, ArrayBlockingQueue<Pair<Integer, Integer>>> newLaneQueues;

    public Map<Integer, ArrayBlockingQueue<Integer>> carOrderOnLane;

    public void initMNX (int M, int N, int X) {

        this.M = M;
        this.N = N;
        this.X = X;

        newLaneQueues = new HashMap<>(M);
        carOrderOnLane = new HashMap<>(M);

        int start, end;
        for (int i = 0; i < M; i++) {
            start = (int) (i * (double) N / M);
            end = (int) min ((i + 1) * (double) N / M, N);

            ArrayBlockingQueue<Pair<Integer, Integer>> laneQueue =
                    new ArrayBlockingQueue<Pair<Integer, Integer>>(end - start + 1);
            for (int j = start; j < end; j++) {
                Pair pair = new Pair(j, X);
                laneQueue.add(pair);
            }

            newLaneQueues.put(i, laneQueue);
        }

        barrier = new CyclicBarrier(Main.carsNo);
    }

    public void printLanes () {
        for (int i = 0; i < this.M; i++) {
            System.out.print("L" + i + " - ");
            System.out.print(this.newLaneQueues.get(i));
            System.out.println();
        }
    }

    public void addCarToLane(int carId, int startDirection) throws InterruptedException {

        if (carOrderOnLane.containsKey(startDirection)) {
            carOrderOnLane.get(startDirection).put(carId);
        }
        else {
            ArrayBlockingQueue<Integer> cars;
            cars = new ArrayBlockingQueue<Integer>(Main.carsOnLane.get(startDirection));
            cars.put(carId);
            carOrderOnLane.put(startDirection, cars);
        }
    }

    public int getFirstCarFromLane(int startDirection) {
        if (carOrderOnLane.isEmpty())
            return -1;
        return carOrderOnLane.get(startDirection).peek();
    }

    public void removeCarFromOldLane(int startDirection) throws InterruptedException {
        carOrderOnLane.get(startDirection).take();
    }

    public int getOldLane (int newLane) {
        if (newLaneQueues.get(newLane).peek() != null)
            return newLaneQueues.get(newLane).peek().getL();
        return -1;
    }

    public void removeCarFromLane(int newLane) throws InterruptedException {
        ArrayBlockingQueue<Pair<Integer, Integer>> currentLane;
        currentLane = this.newLaneQueues.get(newLane);

        int oldLaneCarsNo = currentLane.peek().getR(); // number of cars to pass from this lane ( <= X)
        int oldLane = currentLane.peek().getL();

        int carsLeftOnLane = Main.carsOnLane.get(oldLane); // number of cars left on the lane (can be anything)

        if ((carsLeftOnLane - 1 == 0)) { // this is the last car to leave the lane and less than X cars have passed
            currentLane.take();
            this.newLaneQueues.put(newLane, currentLane);
            Main.carsOnLane.put(oldLane, 0);
            System.out.println("The initial lane " + oldLane + " has been emptied and removed from the new lane queue");
        }
        else if (oldLaneCarsNo - 1 == 0) {
            currentLane.take(); // remove first old lane, add it to the end of the queue with an updated max no. of cars

            Pair<Integer, Integer> pair = new Pair<>(oldLane, this.X);
            currentLane.put(pair);

            Main.carsOnLane.put(oldLane, carsLeftOnLane - 1);
            this.newLaneQueues.put(newLane, currentLane);

            System.out.println("The initial lane " + oldLane + " has no permits and is moved to the back of the new lane queue");
        }
        else {
            ArrayBlockingQueue<Pair<Integer, Integer>> newCurrentLane;
            Pair<Integer, Integer> pair = new Pair<>(oldLane, oldLaneCarsNo - 1);
            currentLane.take();

            Main.carsOnLane.put(oldLane, carsLeftOnLane - 1);

            newCurrentLane = new ArrayBlockingQueue<Pair<Integer, Integer>>(N / M + 1);
            newCurrentLane.put(pair);

            int size = currentLane.size();
            for (int i = 0; i < size; i++) {
                newCurrentLane.put(currentLane.take());
            }
            this.newLaneQueues.put(newLane, newCurrentLane);
        }

    }

    @Override
    public int getWaitingTime() {
        return 0;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {

    }
}

class Pair<L,R> {
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }

    @Override
    public String toString() {
        return "Pair{" +
                "A" + l +
                " - " + r +
                '}';
    }
}