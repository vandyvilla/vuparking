package edu.vanderbilt.android.vuparking;


public class ParkingLot {
	
	private int zone;
	private int id;
	private String name;
	private String coordinates[];
	private String address;
	private int num_spot;
	private int num_disable;
	
	public int getId() { return id; }
	public String getName() { return name; }
	public int getZone() { return zone; }
	public String[] getCoordinates() { return coordinates; }
	public String getAddress() { return address; }
	public int getNumSpot() { return num_spot; }
	public int getNumDisalbe() { return num_disable; }
	
	public void setId(int ID) { id = ID; }
	public void setName(String Name) { name = Name; }
	public void setCoordinates(String[] Coordinate) { coordinates[0] = Coordinate[0];  coordinates[1] = Coordinate[1]; }
	public void setAddress(String addr) { address = addr; }
	public void setNumSpot(int num) { num_spot = num; }
	public void setNumDisable(int num) { num_disable = num; }
	public void setZone(int type) { zone = type; }
	
	
	
	
}