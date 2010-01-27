package edu.vanderbilt.vuparking;


public class ParkingSpot {
	
	private int id;
	private String name;
	private String coordinate[];  // Location information
	
	private static final int VISITOR = 0;
	private static final int ZONE1 = 1;
	private static final int ZONE2 = 2;
	private static final int ZONE3 = 3;
	private static final int ZONE4 = 4;
	private static final int MEDICAL = 5;
	
	private int type;             // Parking type
	private boolean isForDisable;
	private String neignbors[];   // Its nearby building
	

	
	
}