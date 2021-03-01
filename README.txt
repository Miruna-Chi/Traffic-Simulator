Chiricu Miruna
332CB

					Tema 2 - APD

1. simple_semaphore:

	- All the cars have to get to the barrier to proceed further. This allows us 
to make a Priority Queue of their waiting time, so we'll know the order in which 
they should leave the intersection.
	- Now that we have the queue, all the cars are at a point where they check if
they're at the head of the queue (matching waitingTime):
		~ they are the head: they drive away, the head of the queue is
		removed, and they notify all the other waiting cars to check if
		they're head of the queue;
		~ they are not the head: they wait until another car notifies them
		to check again

2. simple_n_roundabout:

	- Barrier: wait for all cars to reach it. 
	- Semaphore.acquire(): now N cars will be able to pass through
	- sleep: time taken to leave the intersection
	- Semaphore.release(): make room for other cars to enter the roundabout

3. simple_strict_1_car_roundabout:
	
	- Make a list of as many semaphores as there are lanes. Now as many cars from
a lane can enter the roundabout as there are permits for that lane
(And cars can enter from all lanes). Now we initialize the semaphores with one permit.


4. simple_strict_x_car_roundabout:
	- First barrier: let all cars get to the same point
	- Same list of semaphores as earlier, but with X permits for each semaphore:
		car gets permit (index of semaphore = car lane)
	- Second barrier: selects (lanes * permits) cars to enter the intersection
	- Cars traverse the intersection
	- Third barrier: wait for all the cars from earlier to traverse
	- Fourth barrier: waits for the printing to happen before another car
	might get selected to enter the intersection

5. simple_max_x_car_roundabout:
	- same as 4, but without barriers, we don't need to wait for all the cars
	to traverse the intersection anymore. Semaphores will regulate the traffic
	for each lane

6. priority_intersection:
	~ cars with high priority:
		- Counter: AtomicInteger for easy synchronization
		- Counter++: a high priority car has entered;
		- Counter--: a high priority car has left the intersection;
	~ cars with low priority:
		- they will be added to a queue, much like the one for task 1 (same
		principle with wait and notify), only, it's not a priority queue
		it's just a queue to hold the order of arrival
		- if the Counter is 0 (no high priority car in the intersection)
		and they're the head of the queue, they can pass through and notify
		the other low priority cars to check the counter and the head 
		of the queue again
		- otherwise they wait in line

7. crosswalk:
	- as long as the execution time hasn't ended, check if pedestrians are crossing:
		~ have a list for all the cars to hold the last message they printed
		(was their semaphore green or red or uninitialized?) to check if 
		they should print something (different from what they printed last)
		~ pedestrians are crossing: all the cars have a red light (print
 		accordingly)
		~ pedestrians are not crossing: all the cars have a green light
		(print accordingly)
		~ cycle repeats

8. simple_maintenance:
	- we have two directions that do just about the same thing:
		~ they acquire a semaphore (one semaphore for each direction), so we can
		control the number of cars that get through
		~ if it's this direction's turn (0 is always first), all the cars waiting
		at the semaphore pass through the intersection (otherwise wait)
		~ after they all passed it's time to change turns, but before notifying
		the others, we need to make sure all of the cars reached this point
		(barrier2)
		~ after this notification, everyone will check their status again and
		the cars from the other direction repeat the process we've just explained

9. complex_maintenance:
	- Map in ComplexMaintenance: Ln (newLane) - [(An, X)]
		~ All the keys will be the new M lanes (Ln)
		~ The values will be queues of pairs with the old lanes and the max number
		of cars to be allowed (X), (elements in queues according to start/end
indexes)
		~ decomment line 292 in IntersectionHandlerFactory for visual guidance

	- Map in Main: A map of queues with all the old lanes - for each lane, all the
	cars in the order of their arrival (Car ID has come from the lane number
OLD_LANE_ID)
	
	- Barrier: wait for all the cars to reach this point: both maps are complete after
this barrier
	- For each car, we determine what its new lane is going to be (in which
start-end interval it resides)
	- Now we check for each car if:
		~ it's at the head of its old lane queue (order of arrival = order of
		leaving)
		~ its old lane is the head of its assigned new lane's queue 
		ex: L1: {A1 - someNo<X, A2 - X, A3 - X}
		OldLane = A1, L1 = newLane, A1 is the head of L1

	- If those conditions are true:
		~ we remove the car from its old lane queue

		~ we decrement someNo<X (second item in the pair), if it's 0, the pair goes
		is removed from the head of the queue and added to the end(with X as the
 		second element). If this was the last car on the whole previous lane, then we
 		don't add the pair to the end anymore

		~ we always have to compare the max number of cars that can pass through with
		the ones that are left. X (or a no. less than it) might be higher
		than the number of cars that are still on that lane. We also have to decrement
		accordingly when a car leaves its oldLane.

		~ notify all the other cars that a change has been made

10. railroad
	- Barrier: we wait for every car to get here, two queues will be completed
	when the barrier is lifted: two queues with the order of arrival of cars from
	each direction
	- One car will see the train passing, prints it
	- Now, on the same logic as for task1, we take out cars of the two queues
	one by one. They will be syncronized on different objects, because
	we only care about their order in their own direction (not related to the other
	lane)
	
	