package hyperloop_vacuum_train;

import java.util.LinkedList;

import org.apache.log4j.Logger;

public class VacuumTrain {

	public static final Logger LOG = Logger.getLogger(VacuumTrain.class);
	
	/**
	 * Maximum number of Capsules in a VacuumTrain
	 */
	
	public static  final byte MAX_NUMBER_OF_CAPSULES = 3;
	
	public String name;
	public  String number;
	public  int size;
	
	/**
	 *  Checking whether vacuum train can go on its route
	 */
	
	public  boolean  readyToGo;
	
	/**
	 * Hyperloop driver
	 */
	
	VacuumTrainDriver driver;
	public int passengersServed = 0;
	
	/**
	 * Capsules of  the  vacuum Train
	 */
	
	public LinkedList<TravelCapsule> capsules;

	public VacuumTrain(String name, String number) {
		this.name = name;
		this.number = number;
		this.size = 0;
		capsules = new LinkedList<TravelCapsule>();
		readyToGo = false;
	}
	
	/**
	 * Checking whether the first capsule is  a head
	 * @return true if  it  is  so
	 */
	public boolean hasLeadingCapsule() {
		return((capsules.size() > 0) && (capsules.getFirst().isLeadingCapsule));
	}
	
	/**
	 * Checking whether the last capsule is  a head
	 * @return true if  it  is  so
	 */
	public  boolean hasTrailerCapsule() {
		return ((capsules.size() > 0) && (capsules.getLast().isLeadingCapsule));
	}
	
	
	
	
	public  void  addCapsule(TravelCapsule cps) {
		if(this.size < MAX_NUMBER_OF_CAPSULES) {
			if(cps.isLeadingCapsule & !hasLeadingCapsule())
				capsules.addFirst(cps);
			else {
				if(cps.isLeadingCapsule & !hasTrailerCapsule())
					capsules.add(cps);
				else {
					if(!hasLeadingCapsule() || !hasTrailerCapsule())
						capsules.add(cps);
					else 
						capsules.add(1, cps);
				}
			}
			
			renumberCapsules();
			this.size = this.capsules.size();
			readyToGo = hasLeadingCapsule() & hasTrailerCapsule() & (size == MAX_NUMBER_OF_CAPSULES);
			
		} else 
			System.out.println(" There should NOT be added more capsules");	
	}
	
	public  void renumberCapsules() {
		for(TravelCapsule cps :capsules) {
			cps.setCapsuleNumber(this.number + "" + capsules.indexOf(cps));
		}
	}

	@Override
	public String toString() {
		return "VacuumTrain [name=" + name + ", readyToGo=" + readyToGo + ", num=" + number + ", size=" + size + 
				", capsules=" + capsules + ", driver=" + driver + "]";
	}

	public VacuumTrainDriver getDriver() {
		return driver;
	}

	public void setDriver(VacuumTrainDriver driver) {
		this.driver = driver;
	}
	
	
	public int  getPassengersCount() {
		int cnt = 0;
		for(TravelCapsule cps : this.capsules) 
			cnt += cps.passengers.size();
		return cnt;
	}

}
