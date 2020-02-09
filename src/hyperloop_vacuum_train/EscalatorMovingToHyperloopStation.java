package hyperloop_vacuum_train;

import java.util.LinkedList;

public class EscalatorMovingToHyperloopStation {
	/**
	 * Escalator name
	 */
	String name;

	public EscalatorMovingToHyperloopStation(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EscalatorMovingUpToHyperloopStation [name=" + name + "]";
	}
	/**
	 * Move number of passengers from one LinkedList to another
	 * 
	 * @param numberOfPassengers
	 *            desired number of passengers to move
	 * @param listFrom
	 *            source LinkedList&lt;Passenger&gt;
	 * @param listTo
	 *            target LinkedList&lt;Passenger&gt;
	 * @return recount number of passenger moved
	 * @throws InterruptedException
	 */
	
	public int movePassengers(int numberOfPassengers, LinkedList<HyperloopPassenger> listFrom, LinkedList<HyperloopPassenger> listTo)
			throws InterruptedException {
		int counter = 0;
		
		for(int i = 0; i < numberOfPassengers; i++) {
			synchronized (listFrom) {
				
				if(!listFrom.isEmpty()) {
					HyperloopPassenger pass = listFrom.remove();
					System.out.println(this.name + "===>>>[" + pass + "] ");
					counter++;
					listTo.add(pass);
				}
			}
		}
		if(counter > 0)
			System.out.println("");
		return counter;
	}
	
}
