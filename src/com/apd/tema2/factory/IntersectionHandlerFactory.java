package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;

import java.util.concurrent.BrokenBarrierException;

import static java.lang.Math.min;
import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws BrokenBarrierException, InterruptedException {
                    SimpleIntersection simple_semaphore = (SimpleIntersection) Main.intersection;

                    // wait for all the cars to get to the semaphore
                    simple_semaphore.addCar(car.getWaitingTime());
                    car.printCarIsWaiting(handlerType);
                    simple_semaphore.barrier.await();

                    String priority = "priority";

                    /* "priority" - synchronize on it so we can call wait and notify from all cars to take their turn
                                    to possibly alter the queue
                    */

                    synchronized (priority) {
                        while(simple_semaphore.peekAtCars() != car.getWaitingTime()) {
                            priority.wait();
                        }
                    }

                    car.printCarIsDrivingAway(handlerType, 0);
                    simple_semaphore.removeCar();

                    synchronized (priority) {
                        priority.notifyAll();
                    }
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    SimpleNRoundabout simple_n_roundabout = (SimpleNRoundabout) Main.intersection;

                    car.printCarIsWaiting(handlerType);
                    simple_n_roundabout.barrier.await();

                    simple_n_roundabout.semaphore.acquire();

                    car.printCarEnteringIntersection(handlerType);
                    sleep(simple_n_roundabout.getWaitingTime());
                    car.printCarIsDrivingAway(handlerType, simple_n_roundabout.getWaitingTime());

                    simple_n_roundabout.semaphore.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    SimpleStrictNCarRoundabout simple_strict_1_car_roundabout = (SimpleStrictNCarRoundabout) Main.intersection;

                    car.printCarIsWaiting(handlerType);

                    simple_strict_1_car_roundabout.semaphores.get(car.getStartDirection()).acquire();
                    car.printCarEnteringIntersection(handlerType);

                    sleep(simple_strict_1_car_roundabout.getWaitingTime());

                    car.printCarIsDrivingAway(handlerType, simple_strict_1_car_roundabout.getWaitingTime());
                    simple_strict_1_car_roundabout.semaphores.get(car.getStartDirection()).release();
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    SimpleStrictNCarRoundabout simple_strict_x_car_roundabout = (SimpleStrictNCarRoundabout) Main.intersection;

                    car.printCarIsWaiting(handlerType);
                    simple_strict_x_car_roundabout.allBarrier.await();

                    simple_strict_x_car_roundabout.semaphores.get(car.getStartDirection()).acquire();
                    car.printCarSelectedToEnterIntersection(handlerType);

                    simple_strict_x_car_roundabout.barrier1.await();
                    car.printCarEnteringIntersection(handlerType);
                    sleep(simple_strict_x_car_roundabout.getWaitingTime());

                    simple_strict_x_car_roundabout.barrier2.await();

                    car.printCarIsDrivingAway(handlerType, simple_strict_x_car_roundabout.getWaitingTime());
                    simple_strict_x_car_roundabout.barrier3.await();
                    simple_strict_x_car_roundabout.semaphores.get(car.getStartDirection()).release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    // Get your Intersection instance
                    SimpleMaxNCarRoundabout simple_max_x_car_roundabout = (SimpleMaxNCarRoundabout) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    car.printCarIsWaiting(handlerType);

                    simple_max_x_car_roundabout.semaphores.get(car.getStartDirection()).acquire();
                    car.printCarSelectedToEnterIntersection(handlerType);

                    car.printCarEnteringIntersection(handlerType);
                    sleep(simple_max_x_car_roundabout.getWaitingTime());

                    car.printCarIsDrivingAway(handlerType, simple_max_x_car_roundabout.getWaitingTime());
                    simple_max_x_car_roundabout.semaphores.get(car.getStartDirection()).release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    // Get your Intersection instance
                    PriorityIntersection priority_intersection = (PriorityIntersection) Main.intersection;
                    String priority = "priority";

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (car.getPriority() != 1) { // cars with high priority
                        car.printCarEnteringIntersection(handlerType);
                        priority_intersection.highPriorityCarsInIntersection.getAndIncrement();

                        sleep(2000);
                        car.printCarIsDrivingAway(handlerType, 0);
                        priority_intersection.highPriorityCarsInIntersection.getAndDecrement();

                        synchronized (priority){
                            if(priority_intersection.highPriorityCarsInIntersection.get() == 0)
                                priority.notifyAll();
                        }
                    }
                    else {
                        priority_intersection.addCar(car.getId());
                        car.printCarIsWaiting(handlerType);

                        //System.out.println(priority_intersection.prio());
                        synchronized (priority) {
                            while(priority_intersection.peekAtCars() != car.getId() ||
                                    priority_intersection.highPriorityCarsInIntersection.get() != 0) {
                                priority.wait();
                                //System.out.println(priority_intersection.prio() + " waiting or over");
                            }
                        }

                        synchronized (priority) {
                            priority_intersection.removeCar();
                            car.printCarEnteringIntersection(handlerType);
                            priority.notifyAll();
                        }
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Crosswalk crosswalk = (Crosswalk) Main.intersection;

                    while(!Main.pedestrians.isFinished()) {

                        System.out.print("");
                        if(!Main.pedestrians.isPass()) {
                            switch (crosswalk.getSemaphoreColor(car.getId())) {
                                case "uninitialized", "red":
                                    car.printCarIsDrivingAway(handlerType, 0);
                                    crosswalk.changeSemaphoreColor("green", car.getId());
                                    break;
                            }
                        }
                        else {
                            switch (crosswalk.getSemaphoreColor(car.getId())) {
                                case "uninitialized", "green":
                                    car.printCarIsWaiting(handlerType);
                                    crosswalk.changeSemaphoreColor("red", car.getId());
                                    break;
                            }
                        }
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    SimpleMaintenance simple_maintenance = (SimpleMaintenance) Main.intersection;
                    String priority = "priority";

                    if(car.getStartDirection() == 0) {
                        simple_maintenance.direction0.acquire();
                        car.printCarIsWaiting(handlerType);
                        synchronized (priority) {
                            while (simple_maintenance.turn.get() != 0) {
                                priority.wait();
                            }
                        }

                        car.printCarIsDrivingAway(handlerType, 0);
                        simple_maintenance.barrier0.await();
                        simple_maintenance.turn.getAndSet(1);
                        simple_maintenance.barrier2.await();
                        synchronized (priority) {
                            priority.notifyAll();
                        }
                        simple_maintenance.direction0.release();
                    }
                    else if(car.getStartDirection() == 1) {
                        simple_maintenance.direction1.acquire();
                        car.printCarIsWaiting(handlerType);

                        synchronized (priority) {
                            while (simple_maintenance.turn.get() != 1) {
                                priority.wait();
                            }
                        }
                        car.printCarIsDrivingAway(handlerType, 0);
                        simple_maintenance.barrier1.await();
                        simple_maintenance.turn.getAndSet(0);
                        simple_maintenance.barrier3.await();
                        synchronized (priority) {
                            priority.notifyAll();
                        }
                        simple_maintenance.direction1.release();
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws BrokenBarrierException, InterruptedException {
                    ComplexMaintenance complex_maintenance = (ComplexMaintenance) Main.intersection;

                    car.printCarIsWaiting(handlerType);
                    complex_maintenance.addCarToLane(car.getId(), car.getStartDirection());
                    complex_maintenance.barrier.await();

                    int newLane = 0;
                    int start, end;

                    for (int i = 0; i < complex_maintenance.M; i++) {
                        start = (int) (i * (double) complex_maintenance.N / complex_maintenance.M);
                        end = (int) min ((i + 1) * (double) complex_maintenance.N / complex_maintenance.M,
                                complex_maintenance.N);

                        if (car.getStartDirection() >= start && car.getStartDirection() < end) {
                            newLane = i;
                            break;
                        }
                    }

                    String priority = "priority";
                    synchronized (priority) {

                        while(complex_maintenance.getOldLane(newLane) != car.getStartDirection()
                                && complex_maintenance.getOldLane(newLane) != -1
                                || complex_maintenance.getFirstCarFromLane(car.getStartDirection()) != car.getId()) {
                            priority.wait();
                        }

                        //complex_maintenance.printLanes();
                        if (complex_maintenance.getOldLane(newLane) != -1) {
                            car.printCarIsSwitchingLane(handlerType, newLane);
                            complex_maintenance.removeCarFromLane(newLane);
                            complex_maintenance.removeCarFromOldLane(car.getStartDirection());
                            priority.notifyAll();
                        }
                    }
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws BrokenBarrierException, InterruptedException {
                    Railroad railroad = (Railroad) Main.intersection;

                    car.printCarIsWaiting(handlerType);
                    railroad.addCar(car.getId(), car.getStartDirection());

                    railroad.barrier.await();

                    synchronized (railroad.trainHasPassed) {
                        if (!railroad.trainHasPassed) {
                            System.out.println("The train has passed, cars can now proceed");
                            railroad.trainHasPassed = true;
                        }
                    }

                    railroad.barrier.await();

                    String priority0 = "priority0";
                    String priority1 = "priority1";

                    /* "priority" - synchronize on it so we can call wait and notify from all cars to take their turn
                                    to possibly alter the queue
                    */

                    if (car.getStartDirection() == 0) {
                        synchronized (priority0) {
                            while (railroad.peekAtCar(0) != car.getId()) {
                                priority0.wait();
                            }
                        }
                        car.printCarIsDrivingAway(handlerType, 0);
                        railroad.removeCar(0);

                        synchronized (priority0) {
                            priority0.notifyAll();
                        }
                    } else {
                        {
                            synchronized (priority1) {
                                while (railroad.peekAtCar(1) != car.getId()) {
                                    priority1.wait();
                                }
                            }
                            car.printCarIsDrivingAway(handlerType, 0);
                            railroad.removeCar(1);

                            synchronized (priority1) {
                                priority1.notifyAll();
                            }
                        }
                    }
                }
            };
            default -> null;
        };
    }
}
