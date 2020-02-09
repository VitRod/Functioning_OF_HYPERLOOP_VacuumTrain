package hyperloop_vacuum_train;

import java.util.LinkedList;

public class AboveGroundPipeLine {

	/**
	 * class for pipeline of  hyperloop
	 */
	
	String  name;
	/**
	 * list of vacuum trains in the  pipeline
	 */
	
	LinkedList<VacuumTrain> pipelineVacTrains;
	/**
	 * list of  stations in this  pipeline 
	 */
	LinkedList<Station> pipelineStations;
	
	public AboveGroundPipeLine(String name) {
		this.name = name;
		this.pipelineStations = new LinkedList<Station>();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LinkedList<VacuumTrain> getPipelineVacTrains() {
		return pipelineVacTrains;
	}
	public void setPipelineVacTrains(LinkedList<VacuumTrain> pipelineVacTrains) {
		this.pipelineVacTrains = pipelineVacTrains;
	}
	public LinkedList<Station> getPipelineStations() {
		return pipelineStations;
	}
	public void setPipelineStations(LinkedList<Station> pipelineStations) {
		this.pipelineStations = pipelineStations;
	}


	@Override
	public String toString() {
		return "AboveGroundPipeLine [name=" + name + ", pipelineVacTrains=" + pipelineVacTrains + ", pipelineStations="
				+ pipelineStations + "]";
	}
	

	
	
	
	
	
	
	
	
}
