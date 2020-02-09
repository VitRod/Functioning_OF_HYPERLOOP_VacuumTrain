package hyperloop_vacuum_train;

import java.util.LinkedList;

/**
 * Class that shows  passengers at the  turnstiles of  lobby
 */

public class Turnstile {
/**
 * Passengers at the  turnstile
 */
	LinkedList<HyperloopPassenger> turnstilePassengers;
	
	
	public Turnstile() {
		 turnstilePassengers = new LinkedList<HyperloopPassenger>();
	}
	
	public synchronized void addPassengers( int numberOfPassengers) {
		for(int i = 0; i < numberOfPassengers; i++) {
			turnstilePassengers.add(new HyperloopPassenger());
		}
		notifyAll();
	}
	
	public StringBuffer printPassengers() {
		StringBuffer sb = new StringBuffer();
		synchronized (turnstilePassengers) {
			for(HyperloopPassenger pass : turnstilePassengers)
				sb.append("[" + pass + "] ");
		}
		return sb;
	}
	
}
