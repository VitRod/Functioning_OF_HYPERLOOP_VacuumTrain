package hyperloop_vacuum_train;

public class HyperloopPassenger {
/**
 * Name of passenger
 */
	String name; 
	/**
	 * Internal counter for unique ID is assigned
	 */
	private static  int idCounter = 0;
	/**
	 * Passenger id
	 */
	 private int id;
	 
	public HyperloopPassenger(String name) {
		this.name = name;
		this.id = idCounter++;
	}
	 
	public HyperloopPassenger() {
		 this.id =idCounter++;
	}

	@Override
	public String toString() {
		return "HyperloopPassenger " + id ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	} 
	
}
