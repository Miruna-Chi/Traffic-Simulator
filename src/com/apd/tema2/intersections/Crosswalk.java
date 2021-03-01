package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.Collections;

public class Crosswalk implements Intersection {

    private ArrayList<Integer> previousSemaphoreColor;

    public Crosswalk() {
        previousSemaphoreColor = new ArrayList<>(Collections.nCopies(Main.carsNo, -1));
    }

    public String getSemaphoreColor(int carId) {
        switch (previousSemaphoreColor.get(carId)) {
            case 1: return "green";
            case 0: return "red";
        }
        return "uninitialized";
    }

    public void changeSemaphoreColor(String color, int carId) {
        switch (color) {
            case "green":
                previousSemaphoreColor.set(carId, 1);
                break;
            case "red":
                previousSemaphoreColor.set(carId, 0);
                break;
        }
    }

    public ArrayList<Integer> getQueue() {
        return previousSemaphoreColor;
    }

    @Override
    public int getWaitingTime() {
        return 0;
    }

    @Override
    public void initSemaphores(int permits, int lanes, int waitingTime) {}
}
