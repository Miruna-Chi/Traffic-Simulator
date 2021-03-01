package com.apd.tema2.entities;

import com.apd.tema2.Main;
import com.apd.tema2.intersections.SimpleNRoundabout;

import java.util.concurrent.BrokenBarrierException;

/**
 * Clasa Thread principala.
 */
public class Car implements Runnable {
    private final int id;
    private final int startDirection;
    private final int endDirection;
    private final int priority;
    private final int waitingTime;
    private final IntersectionHandler intersectionHandler;

    public Car(final int id, final int startDirection, final IntersectionHandler intersectionHandler) {
        this(id, startDirection, -1, 0, intersectionHandler, 1);
    }

    public Car(final int id, final int startDirection, final int waitingTime, final IntersectionHandler intersectionHandler) {
        this(id, startDirection, -1, waitingTime, intersectionHandler, 1);
    }

    public Car(final int id, final int startDirection, final int endDirection, final int waitingTime, final IntersectionHandler intersectionHandler) {
        this(id, startDirection, endDirection, waitingTime, intersectionHandler, 1);
    }

    public Car(final int id, final int startDirection, final int endDirection, final int waitingTime, IntersectionHandler intersectionHandler, final int priority) {
        this.id = id;
        this.startDirection = startDirection;
        this.endDirection = endDirection;
        this.waitingTime = waitingTime;
        this.intersectionHandler = intersectionHandler;
        this.priority = priority;

        if (Main.carsOnLane.containsKey(startDirection)) {
            int carsPerLane = Main.carsOnLane.get(startDirection);
            Main.carsOnLane.put(startDirection, carsPerLane + 1);
        }
        else Main.carsOnLane.put(startDirection, 1);
    }

    @Override
    public void run() {
        try {
            intersectionHandler.handle(this);
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getStartDirection() {
        return startDirection;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void printCarIsWaiting(String handlerType) {
        switch(handlerType) {
            case "simple_semaphore":
                System.out.println("Car " + this.id + " has reached the semaphore, now waiting...");
                break;
            case "simple_n_roundabout":
            case "simple_strict_x_car_roundabout":
                System.out.println("Car " + this.id + " has reached the roundabout, now waiting...");
                break;
            case "simple_strict_1_car_roundabout":
                System.out.println("Car " + this.id + " has reached the roundabout");
                break;
            case "simple_max_x_car_roundabout":
                System.out.println("Car " + this.id + " has reached the roundabout from lane " + this.startDirection);
                break;
            case "priority_intersection":
                System.out.println("Car " + this.id + " with low priority is trying to enter the intersection...");
                break;
            case "crosswalk":
                System.out.println("Car " + this.id + " has now red light");
                break;
            case "simple_maintenance":
                System.out.println("Car " + this.id + " from side number " + this.startDirection + " has reached the bottleneck");
                break;
            case "railroad":
                System.out.println("Car " + this.id + " from side number " + this.startDirection + " has stopped by the railroad");
                break;
            case "complex_maintenance":
                System.out.println("Car " + this.id + " has come from the lane number " + this.startDirection);
                break;
        }
    }

    public void printCarSelectedToEnterIntersection(String handlerType) {
        switch (handlerType) {
            case "simple_strict_x_car_roundabout":
                System.out.println("Car " + this.id + " was selected to enter the roundabout from lane " + this.startDirection);
                break;
        }
    }

    public void printCarEnteringIntersection(String handlerType) {
        switch (handlerType) {
            case "simple_n_roundabout":
                System.out.println("Car " + this.id + " has entered the roundabout");
                break;
            case "simple_strict_1_car_roundabout":
            case "simple_strict_x_car_roundabout":
            case "simple_max_x_car_roundabout":
                System.out.println("Car " + this.id + " has entered the roundabout from lane " + this.startDirection);
                break;
            case "priority_intersection":
                switch(this.priority) {
                    case 1:
                        System.out.println("Car " + this.id + " with low priority has entered the intersection");
                        break;
                    default:
                        System.out.println("Car " + this.id + " with high priority has entered the intersection");
                        break;
                }
                break;
        }
    }

    public void printCarIsSwitchingLane(String handlerType, int newLane) {
        System.out.println("Car " + this.id + " from the lane " + this.startDirection +
                " has entered lane number " + newLane);
    }

    public void printCarIsDrivingAway(String handlerType, int waitingTimeMs) {
        switch(handlerType) {
            case "simple_semaphore":
                System.out.println("Car " + this.id + " has waited enough, now driving...");
                break;
            case "simple_n_roundabout":
            case "simple_strict_1_car_roundabout":
            case "simple_strict_x_car_roundabout":
            case "simple_max_x_car_roundabout":
                System.out.println("Car " + this.id + " has exited the roundabout after " +
                        waitingTimeMs / 1000 + " seconds");
                break;
            case "priority_intersection":
                System.out.println("Car " + this.id + " with high priority has exited the intersection");
                break;
            case "crosswalk":
                System.out.println("Car " + this.id + " has now green light");
                break;
            case "simple_maintenance":
                System.out.println("Car " + this.id + " from side number " + this.startDirection + " has passed the bottleneck");
                break;
            case "railroad":
                System.out.println("Car " + this.id + " from side number " + this.startDirection + " has started driving");
                break;
        }
    }
}
