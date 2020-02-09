package hyperloop_vacuum_train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParserException;

public class Main {
/**
 * Entering point of  the project
 */
	public static void main(String[] args) 
			throws XmlPullParserException, IllegalArgumentException, IllegalStateException, FileNotFoundException,
			UnsupportedEncodingException, IOException, InterruptedException {
		
		 
		HyperloopCollection hyperloop = new HyperloopCollection();
		
		hyperloop.createCapsules();
		System.out.println("__ __ ____ _____ ________ ____ ___ _____ ___");
		hyperloop.createVacTrain();
		System.out.println("___ _____ ______ _____ ___ ___ ______ __ ___");
		hyperloop.createsPipeLines();
		System.out.println("______ _______ ______ ________ ______ ___");
		hyperloop.createPipeLineVacTrains();
		System.out.println("_______ _______ ______ _________ ___ ____");
		System.out.println(hyperloop.hyperloopUSA);
		System.out.println("____ ________ _____ _____ ________ _____");
		 
		
		hyperloop.manageDriversQueue();
		System.out.println("____________________________________");
		
		hyperloop.createPipeLineStations();
		System.out.println(hyperloop.hyperloopUSA);
		System.out.println("____________________________________");
		
		hyperloop.passengersMoveEscalator();
		
		System.out.println("____________________________________");
		
		hyperloop.passengersInOutVacTrains();
		System.out.println("____________________________________");
		
		hyperloop.movePassengersIntoCapsules();
		
		System.out.println("____________________________________");
		for(Thread curThread : hyperloop.getThreads()) {
			
			try {
				curThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
