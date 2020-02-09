package hyperloop_vacuum_train;

import java.util.Random;

public class VacuumTrainDriver {
	/**
	 * Name of  a driver
	 */
	String name;
	/**
	 * Current experience of  driver
	 */	
	int experience;
	/**
	 * Current VacTrain
	 */
	VacuumTrain currentVacTrain;
	
	public VacuumTrainDriver(String name, int experience) {
		this.name = name;
		this.experience = experience;
	}
	
	public void recountExperience(int miles) {
		Random rnd = new Random();
		this.experience +=rnd.nextInt(miles);
	}
	
	/**
	 * Travel and recounting experience
	 */
	
	public void doDrive() {
		recountExperience(70);
	}

	@Override
	public String toString() {
		return "VacuumTrainDriver [name=" + name + ", experience=" + experience + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public VacuumTrain getCurrentVacTrain() {
		return currentVacTrain;
	}

	public void setCurrentVacTrain(VacuumTrain currentVacTrain) {
		this.currentVacTrain = currentVacTrain;
		currentVacTrain.setDriver(this);
	}
	

}
