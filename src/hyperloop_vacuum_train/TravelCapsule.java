package hyperloop_vacuum_train;

import java.util.LinkedList;

public class TravelCapsule {
  /**
   *Maximum capsule capacity
   */
	public static final int MAX_CAPACITY = 30;
	
	/**
	 * Checking whether the capsule is "leading"
	 */
	
	public boolean isLeadingCapsule;
	public String name;
	public String capsuleNumber;
	int capacity;
	
	/**
	 * A list of  passengers
	 */
	
	LinkedList<HyperloopPassenger> passengers;

	public TravelCapsule(String name, boolean isLeadingCapsule) {
		this.name = name;
		this.isLeadingCapsule = isLeadingCapsule;
		capacity = 0;
		passengers = new LinkedList<>();
	}

	public boolean isLeadingCapsule() {
		return isLeadingCapsule;
	}

	public void setLeadingCapsule(boolean isLeadingCapsule) {
		this.isLeadingCapsule = isLeadingCapsule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapsuleNumber() {
		return capsuleNumber;
	}

	public void setCapsuleNumber(String capsuleNumber) {
		this.capsuleNumber = capsuleNumber;
	}

	@Override
	public String toString() {
		return "TravelCapsule [head=" + isLeadingCapsule + ", name=" + name + ", Num="
				+ capsuleNumber + "]";
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	
}
