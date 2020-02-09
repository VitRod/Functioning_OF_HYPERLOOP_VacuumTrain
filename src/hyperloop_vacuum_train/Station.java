package hyperloop_vacuum_train;


import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class Station {

	public static final Logger LOG = Logger.getLogger(Station.class);
	final static int MAX_NUMBER_OF_PASSENGERS = 300;
	/**
	 * Name of  Station
	 */
	String  name;
	/**
	 * List of passengers on the station
	 */
	LinkedBlockingQueue<HyperloopPassenger> awaitingPassengers;
	
	public Station(String name) {
		this.name = name;
		this.awaitingPassengers = new LinkedBlockingQueue<HyperloopPassenger>(MAX_NUMBER_OF_PASSENGERS);
	}

	@Override
	public String toString() {
		return "Station [(" + name + "); await (" + awaitingPassengers.size() + ")]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedBlockingQueue<HyperloopPassenger> getAwaitingPassengers() {
		return awaitingPassengers;
	}

	public void setAwaitingPassengers(LinkedBlockingQueue<HyperloopPassenger> awaitingPassengers) {
		this.awaitingPassengers = awaitingPassengers;
	}
	
	
	
	
	
}
