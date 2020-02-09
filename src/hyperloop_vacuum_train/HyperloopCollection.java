package hyperloop_vacuum_train;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class HyperloopCollection {
	/**
	 * Number of Capsules in depot
	 */
	public final static int CAPSULES_IN_DEPOT = 99;
	/**
	 * Namespace for XML reports creation
	 */
	final String NAMESPACE = "";
	/**
	 * Depot for capsules
	 */
	
	LinkedList<TravelCapsule> depot;
	
	HashSet<VacuumTrain> vacTrains;	
	/**
	 * Three separate Pipelines of Hyperloop in The  USA  between different cities
	 */
	AboveGroundPipeLine LosAngelesLine, WashingtonLine, BostonLine;
	/**
	 * Queue with Drivers to drive VacTrains
	 */
	BlockingQueue<VacuumTrainDriver> driverQueue;
	/**
	 * Map to fast search remaining Hyperloop Passenger on the Station
	 */
	Map<String, LinkedBlockingQueue<HyperloopPassenger>> passengersInStation;
	/**
	 * Auxiliary random seed
	 */
	private Random rnd;
	
	/**
	 * Comparator for priority queues
	 */
	private Comparator<VacuumTrainDriver> comparator;
	/**
	 * Complete Hyperloop in USA
	 */
	ArrayList<AboveGroundPipeLine> hyperloopUSA;
	/**
	 * Auxiliary threads collection for join() them in main thread
	 */
	private ArrayList<Thread> threads;
	
	/**
	 *XML serializer
	 */
	XmlSerializer serializer;
	/**
	 * Latch to start Passengers moving into the Capsule
	 */
	CountDownLatch startMovingToCapsuleLatch;
	/**
	 * Latch to finish Passengers moving into the Capsule
	 */
	CountDownLatch finishMovingToCapsuleLatch;
	
	/*==========================================*/
	
	/**
	 * Constructing Hyperloop
	 */
	
	public HyperloopCollection() {
		 this.rnd = new Random();
		 
		 this.hyperloopUSA = new ArrayList<>();
		 this.depot = new LinkedList<TravelCapsule>();
		 this.vacTrains = new LinkedHashSet<VacuumTrain>();
		 this.passengersInStation = new HashMap<>();
		 this.threads = new ArrayList<>();
		 
		// creating XML serializer
			XmlPullParserFactory factory;
			try {
				factory = XmlPullParserFactory.newInstance();
				this.serializer = factory.newSerializer();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Creating capsules for depot collection. Approximately 25% of leading capsules will be
	 * created
	 */
	public void createCapsules() {
		for(int i = 0; i < CAPSULES_IN_DEPOT; i++) {
			TravelCapsule cps = new TravelCapsule("<<A_Tiny_Capsula>> " + i, (rnd.nextInt(99) < 25));
			depot.add(cps);
		}
	}
	/**
	 * Creating vactrains from depot capsules usingf addCapsule method
	 * Adds ready to go vacTrain to vactrain's collection
	 */
	
	public void createVacTrain() {
		// filling vactrains with capsules
		// maximum number of  vactrains = size of depot / by the number of capsules
		// in the  vactrain
		int maxVacTrainsNumber = depot.size() / VacuumTrain.MAX_NUMBER_OF_CAPSULES;
		
		for(int j = 0; j < maxVacTrainsNumber; j++) {
			VacuumTrain vactrain = new VacuumTrain("Vactrain " + j, "00" + j);
			while (!depot.isEmpty()) {
				vactrain.addCapsule(depot.poll());
				if(vactrain.size == VacuumTrain.MAX_NUMBER_OF_CAPSULES)
					break;
			}
			if(vactrain.readyToGo)
				vacTrains.add(vactrain);
		}
		System.out.println(vacTrains);
	}
	/**
	 * Creating three pipelines in the USA. Method adds them to HyperloopCollection
	 */
	public void createsPipeLines() {
		LosAngelesLine = new AboveGroundPipeLine("Los-Angeles --> San Francisco"); 
		WashingtonLine = new AboveGroundPipeLine("Washington --> Boston");
		BostonLine = new AboveGroundPipeLine("Boston --> New-York");
		
		hyperloopUSA.add(LosAngelesLine);
		hyperloopUSA.add(WashingtonLine);
		hyperloopUSA.add(BostonLine);
	}
	
	/**
	 * adding vactrains from vactrains collection to a  pipeline vactrains
	 */
	public void createPipeLineVacTrains() {
		LosAngelesLine.setPipelineVacTrains(new LinkedList<VacuumTrain>());
		WashingtonLine.setPipelineVacTrains(new LinkedList<VacuumTrain>());
		BostonLine.setPipelineVacTrains(new LinkedList<VacuumTrain>());
		
		Iterator<VacuumTrain> iter = vacTrains.iterator();
		
		while (iter.hasNext()){
			LosAngelesLine.pipelineVacTrains.add(iter.next());
			if(iter.hasNext())
				WashingtonLine.pipelineVacTrains.add(iter.next());
			if(iter.hasNext())
				 BostonLine.pipelineVacTrains.add(iter.next());
		}
	}
	
	/**
	 * Prints a queue in right order. Using a copy of queue to poll
	 * @param queue
	 * 				- a queue (PriorityBlockingQueue or PriorityQueue) to print 
	 */
	public void  printPriorityQueue(Queue<?> queue) {
		Queue<VacuumTrainDriver> temp;
		
		if(queue instanceof PriorityBlockingQueue) {
			temp = new PriorityBlockingQueue<VacuumTrainDriver>(10, comparator);
		} else {
			temp = new PriorityQueue<VacuumTrainDriver>(10, comparator);
		}
		for (Object e :queue) {
			temp.add((VacuumTrainDriver) e);
		}
		System.out.println("======================THE RESULT OF QUEUE=====================");
		while (!temp.isEmpty())
			System.out.println(temp.poll());
	}
	
	/**
	 * Creates drivers and forms a driver queue from them. Comparator of queue
	 * is based on drivers experience. Run all vacgetrains with available drivers in
	 * different threads After all threads are finished - print a resulting
	 * queue
	 */
	public void manageDriversQueue() {
		comparator = new Comparator<VacuumTrainDriver>() {

			@Override
			public int compare(VacuumTrainDriver o1, VacuumTrainDriver o2) {
				 
				if (o1.getExperience() > o2.getExperience()) {
					return -1;
				}
				if(o1.getExperience() < o2.getExperience()) {
					return 1;
				}
				return 0;
			}
		};
		
		driverQueue = new PriorityBlockingQueue<VacuumTrainDriver>(10, comparator);
		
		try {
			driverQueue.put(new VacuumTrainDriver("Valera", 9));
			driverQueue.put(new VacuumTrainDriver("Volaev S.S", 30));
			driverQueue.put(new VacuumTrainDriver("Stepanych", 55));
			driverQueue.put(new VacuumTrainDriver("Trainee Petya", 5));
		}catch (InterruptedException exep) {
			exep.printStackTrace();
		}
		
		Iterator<VacuumTrain> iter = vacTrains.iterator();
		VacuumTrainDriver drv = null;
		
		getThreads().clear();
		
		while (iter.hasNext()) {
			VacuumTrain vactrain = iter.next();
			
			try {
				drv = driverQueue.take();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			drv.setCurrentVacTrain(vactrain);
			doThreadDrive(drv);
			iter.remove();
		}
		for(Thread curThread : getThreads()) {
			
			try {
				curThread.join();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		printPriorityQueue(driverQueue);
	}
	
	/**
	 * Run vactrain with driver in a thread. Recount experience and after run ends -
	 * return driver to the queue.
	 * 
	 * @param drv
	 */
	public void doThreadDrive(VacuumTrainDriver drv) {
		Random rnd = new Random();
		int miles = rnd.nextInt(50) + 1;
		
		Thread theThread = new Thread(new Runnable() {
			Random rnd = new Random();
			
			@Override
			public void run() {
				System.out.println("VacTrain Driver " + drv.name + "[" + drv.experience + "] runs " 
			+ miles + " miles on the " + drv.currentVacTrain.name + " vactrain");
				
				drv.recountExperience(miles);
				
				try {
					Thread.sleep(rnd.nextInt(5) * 1000);
					driverQueue.put(drv);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		getThreads().add(theThread);
		theThread.start();
	}
	
	/**
	 *  Adding stations to the pipelines(pipeline station collection)
	 */
	public void createPipeLineStations() {
		for(int i = 0; i < 10; i++) {
			LosAngelesLine.pipelineStations.add(new Station(LosAngelesLine.name + "Station " + i));
			WashingtonLine.pipelineStations.add(new Station(WashingtonLine.name + "Station " + i));
			BostonLine.pipelineStations.add(new Station(BostonLine.name + "Station " + i ));
		}
	}
	
	/**
	 * Passengers from turnstile move to station via three escalators
	 * 
	 * @throws InterruptedException
	 */
	public void passengersMoveEscalator() throws InterruptedException {
	
		//Constants for the number of passengers
		// appearance of  passengers at the turnstile
		final int turnstileNew = 13;
		//passengers that leave station
		final int stationGone = 8;
		//passengers that move via escalator
		final int escalatorMoved = 4;
		
		// constants for threads to sleep
		final int turnstileSleep = 2000;
		final int  stationSleep = 3000;
		final int escalatorSleep = 1000;
		
		// list of passengers at the station
		LinkedList<HyperloopPassenger> stationPassengers = new LinkedList<HyperloopPassenger>();
		// you can change this list to list of the first station of first line
		// for instance
		// hyperloopUSA.get(0).pipelineStations.getFirst().getAwaitingPassengers();
		
		EscalatorMovingToHyperloopStation escalator1 = new EscalatorMovingToHyperloopStation("ESCALATOR_1");
		EscalatorMovingToHyperloopStation escalator2 = new EscalatorMovingToHyperloopStation("ESCALATOR_2");
		EscalatorMovingToHyperloopStation escalator3 = new EscalatorMovingToHyperloopStation("ESCALATOR_3");
		
		Turnstile turnstile = new Turnstile();
		
		// Filling the  turnstile with passengers
		Thread turnstileAdditionThread = new Thread(new Runnable() {
			Random rnd = new Random();
			
			@Override
			public void run() {
				 while (true) {
					 turnstile.addPassengers(rnd.nextInt(turnstileNew));
					 
					 try {
						Thread.sleep(turnstileSleep);
					} catch (InterruptedException e) {
				
						e.printStackTrace();
					}
					 System.out.println("At the Turnstile " + turnstile.turnstilePassengers.size());
					 System.out.println("Turnstile :" + turnstile.printPassengers()); 
				 }
			}
		});
		turnstileAdditionThread.setName("The one who adds");
		
		// Passengers leaving station in the vactrain
		Thread stationRemoverThread = new Thread (new Runnable() {
			Random rnd = new Random();
			
			@Override
			public void run() {
				while (true) {
				try {
					 System.out.println("At the station " +  stationPassengers.size() + "; left in the  vactrain"
							 + removePassengers(rnd.nextInt(stationGone)));
					 Thread.sleep(stationSleep);
				 }catch (InterruptedException ex) {
					 ex.printStackTrace();
					 }
				 }
				
			}
			
			public  int removePassengers(int numberOfPassengers) throws InterruptedException {
				int counter = 0;
				
				synchronized (stationPassengers) {
					for(int i = 0; i < numberOfPassengers; i++) {
						if(!stationPassengers.isEmpty()) {
							stationPassengers.remove();
							counter++;
						}
					}
				}
				return counter;
			}
			 
		});
		
		stationRemoverThread.setName("The one who removes");
		
		// Escalator 1 carries passengers from the turnstile to the  station
		Thread escalator1Thread = new Thread (new Runnable() {
			Random rnd = new Random();
			
			@Override
			public void run() {
				while(true) {
					try {
					Thread.sleep(escalatorSleep);
				    System.out.println(" " + escalator1.name + " carried [" + escalator1.movePassengers(
						 rnd.nextInt(escalatorMoved) + 1, turnstile.turnstilePassengers, stationPassengers) + "]");
				}catch (InterruptedException ex) {
					 ex.printStackTrace();
					}
			   }
			
		}
			
	});
	
		escalator1Thread.setName("Escalator 1");
		
		// Escalator 2 carries passengers from the turnstile to the  station
				Thread escalator2Thread = new Thread (new Runnable() {
					Random rnd = new Random();
					
					@Override
					public void run() {
						while(true) {
							try {
							Thread.sleep(escalatorSleep);
						    System.out.println(" " + escalator2.name + " carried [" + escalator2.movePassengers(
								 rnd.nextInt(escalatorMoved) + 1, turnstile.turnstilePassengers, stationPassengers) + "]");
						}catch (InterruptedException ex) {
							 ex.printStackTrace();
							}
					   }
				}
					
			});
			
				escalator2Thread.setName("Escalator 2");
				
			// Escalator 3 carries passengers from the turnstile to the  station
				Thread escalator3Thread = new Thread (new Runnable() {
					Random rnd = new Random();
					
					@Override
					public void run() {
						while(true) {
							try {
							Thread.sleep(escalatorSleep);
						    System.out.println(" " + escalator3.name + " carried [" + escalator3.movePassengers(
								 rnd.nextInt(escalatorMoved) + 1, turnstile.turnstilePassengers, stationPassengers) + "]");
						}catch (InterruptedException ex) {
							 ex.printStackTrace();
							}
					  }
				}
					
			});
			
				escalator3Thread.setName("Escalator 3");
				
		System.out.println("====================START===================");
				
		//wainting for all threads
		turnstileAdditionThread.join();
		stationRemoverThread.join();
		escalator1Thread.join();
		escalator2Thread.join();		
		escalator3Thread.join();	
		
		
		//we start the appearance and pick-up of passengers and wait a little bit
		turnstileAdditionThread.start();
		stationRemoverThread.start();
		Thread.sleep(2000 * 2);
		
		// we start the escalators
		escalator1Thread.start();
		escalator2Thread.start();
		escalator3Thread.start();
	}
	
	
	/**
	 * Creating passengers and running all vactrains through stations
	 */
	public void passengersInOutVacTrains() {
		getThreads().clear();
		
		passengersEnterStations();
		
		for(AboveGroundPipeLine pipeline : hyperloopUSA) {
			Iterator<VacuumTrain> iterPipeline = pipeline.getPipelineVacTrains().iterator();
			while (iterPipeline.hasNext()) {
				
				VacuumTrain vactrain = iterPipeline.next();
				runVacTrainThread(pipeline, vactrain);
			}
		}
	}
	
	/**
	 * Adding passengers to stations in thread.
	 */
	private void passengersEnterStations() {
		
		Thread theThread = new Thread(new Runnable() {
			Random rnd = new Random();

			@Override
			public void run() {
				 for(AboveGroundPipeLine pipeline : hyperloopUSA) {
					 for(Station station : pipeline.pipelineStations) {
						 for(int i = 0; i < rnd.nextInt(2345); i++)
							 try {
							 station.awaitingPassengers.put(new HyperloopPassenger("Hyperloop Passenger " + rnd.nextInt(1111)));
							 Thread.sleep(1);
							 }catch (InterruptedException e) {
								 e.printStackTrace();
							 }
						 Station.LOG
						 .info(station.name + "s passengers are grown to " + station.awaitingPassengers.size());
					 }
				 }
				
			}
			
		});
		theThread.start();
		getThreads().add(theThread);
		
	}
	/**
	 * Runs vactrain through line. VacTrain stays at every station and let passengers
	 * go out and in. VacTrain goes in separate thread.
	 * 
	 * @param pipeline
	 *            -   pipeline of Hyperloop
	 * @param vactrain
	 *            - current vactrain
	 */
	private void runVacTrainThread(AboveGroundPipeLine pipeline, VacuumTrain vactrain) {
		
		Thread theThread = new Thread(new Runnable() {	
			Random rnd = new Random();
			int cntToOperate = 0, resultOper = 0;
			String reportToLog = "";
			
			@Override
			public void run() {
				
				 for(Station station : pipeline.pipelineStations) {
					 
					 cntToOperate = rnd.nextInt(45);
					 
					 VacuumTrain.LOG.info("<<--" + vactrain.name + "[" + vactrain.getPassengersCount() + "] :: "
							 + station.name + "[" + station.awaitingPassengers.size() + "]");
					 reportToLog = "";
					 for(TravelCapsule cps : vactrain.capsules) {
						 
						 System.out.print("  " + cps.name + "[" + cps.passengers.size() + "] :  ");
							reportToLog += "  " + cps.name + "[" + cps.passengers.size() + "] :  ";
							resultOper = 0;
						 
							if (!cps.passengers.isEmpty()) {
								Iterator<HyperloopPassenger> passIter = cps.passengers.iterator();
								cntToOperate = rnd.nextInt(10);
								// a few passengers go out
								while(passIter.hasNext() && cntToOperate > 0) {
									
									cps.passengers.removeFirst();
									cntToOperate--;
									resultOper++;
								}
								System.out.print("   " + resultOper + " out; ");
								reportToLog += "   " + resultOper + " out; ";
							}
						 
					// Passengers at the station board the capsules (everyone who can fit)		
							
							resultOper = 0;

							while (cps.passengers.size() < TravelCapsule.MAX_CAPACITY & station.awaitingPassengers.size() > 0) {
								try {
									cps.passengers.add(station.awaitingPassengers.take());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								resultOper++;
							}
							System.out.print("" + resultOper + " in; ");
							System.out.println("");

							reportToLog += "" + resultOper + " in; ";			 
					 }
					 
					 VacuumTrain.LOG.info("  " + vactrain.name + "[=====] " + reportToLog);

						VacuumTrain.LOG.info("->" + vactrain.name + "[" + vactrain.getPassengersCount() + "] :: " + station.name + "["
								+ station.awaitingPassengers.size() + "]");
						// sleep
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				 }
			}
			
		});
		theThread.start();
		getThreads().add(theThread);	
	}
	
	/**
	 * Moving passengers into capsules
	 */
	public void movePassengersIntoCapsules() {
		
		// get the example of vactrain
		VacuumTrain testVacTrain = hyperloopUSA.get(0).pipelineVacTrains.get(0);
		Station testStation = hyperloopUSA.get(0).pipelineStations.get(0);
		
		for(int j = 0; j < 100; j++) {
			testStation.awaitingPassengers.add(new HyperloopPassenger("Pass " + j));
		}
		
		startMovingToCapsuleLatch = new CountDownLatch(1);
		finishMovingToCapsuleLatch = new CountDownLatch(testVacTrain.capsules.size());
		
		Random rnd = new Random();
		
		for(TravelCapsule capsule : testVacTrain.capsules) {
			int passengersToMoveToCapsule = rnd.nextInt(testStation.awaitingPassengers.size() / 5);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					 try {
						
						 startMovingToCapsuleLatch.await();
						 System.out.println(capsule + " begins to fill ...");
						 for(int i = 0; i < passengersToMoveToCapsule; i++) {
						 	capsule.passengers.add(testStation.awaitingPassengers.poll());
						 	System.out.println(capsule.getName() + "+");
						 	Thread.sleep(100);	
						 }	
						 	
						 finishMovingToCapsuleLatch.countDown();
						 
					} catch (InterruptedException intExep) {
						 intExep.printStackTrace();
					}
				}
			}).start();
		}
		
		System.out.println("Attention!!!... GO!");
		startMovingToCapsuleLatch.countDown();
		
		try {
			finishMovingToCapsuleLatch.await();
		} catch (InterruptedException e) {
			 e.printStackTrace();
		}
		System.out.println(" Everyone got in the  capsule");
		System.out.println(testVacTrain);
	}
	
	
	
	
	public ArrayList<Thread> getThreads(){
		return threads;
	}
	
}
