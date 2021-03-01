package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.ComplexMaintenance;
import com.apd.tema2.intersections.PriorityIntersection;
import com.apd.tema2.intersections.SimpleMaintenance;
import com.apd.tema2.intersections.SimpleNRoundabout;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simpleNRoundabout");
                    String[] line = br.readLine().split(" ");
                    Main.intersection.initSemaphores(Integer.parseInt(line[0]), 0,
                                                           Integer.parseInt(line[1]));
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simpleStrict1CarRoundabout");
                    String[] line = br.readLine().split(" ");
                    Main.intersection.initSemaphores(1, Integer.parseInt(line[0]),
                                                                     Integer.parseInt(line[1]));
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simpleStrictXCarRoundabout");
                    String[] line = br.readLine().split(" ");
                    Main.intersection.initSemaphores(Integer.parseInt(line[2]), Integer.parseInt(line[0]),
                            Integer.parseInt(line[1]));
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simpleMaxNCarRoundabout");
                    String[] line = br.readLine().split(" ");
                    Main.intersection.initSemaphores(Integer.parseInt(line[2]), Integer.parseInt(line[0]),
                            Integer.parseInt(line[1]));
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("priorityIntersection");
                    String[] line = br.readLine().split(" ");
                    ((PriorityIntersection)Main.intersection).initLowPriorityQueue(Integer.parseInt(line[0]),
                                                                                    Integer.parseInt(line[1]));
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("crosswalk");
                    String[] line = br.readLine().split(" ");
                    Main.pedestrians = new Pedestrians(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("simpleMaintenance");
                    String[] line = br.readLine().split(" ");
                    ((SimpleMaintenance) Main.intersection).initX(Integer.parseInt(line[0]));
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("complexMaintenance");
                    String[] line = br.readLine().split(" ");
                    ((ComplexMaintenance) Main.intersection).initMNX(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                            Integer.parseInt(line[2]));
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("railroad");
                }
            };
            default -> null;
        };
    }

}
