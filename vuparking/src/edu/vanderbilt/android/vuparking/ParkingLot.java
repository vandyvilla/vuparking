package edu.vanderbilt.android.vuparking;


public class ParkingLot {
	
	private int zone;
	private int id;
	private String name;
	private double coordinates[];
	private String address;
	private int num_spot;
	private int num_disable;
	private int num_available;
	
	public int getId() { return id; }
	public String getName() { return name; }
	public int getZone() { return zone; }
	public double[] getCoordinates() { return coordinates; }
	public String getAddress() { return address; }
	public int getNumSpot() { return num_spot; }
	public int getNumDisable() { return num_disable; }
	public int getNumAvailabe() { return num_available; }
	
	public void setId(int ID) { id = ID; }
	public void setName(String Name) { name = Name; }
	public void setCoordinates(double x, double y) { coordinates[0] = x;  coordinates[1] = y; }
	public void setAddress(String addr) { address = addr; }
	public void setNumSpot(int num) { num_spot = num; }
	public void setNumDisable(int num) { num_disable = num; }
	public void setZone(int type) { zone = type; }
	public void setNumAvailable(int num) { num_available = num; }
	
	ParkingLot(int id, String name, int zone, double coordinate_x, double coordinate_y, 
			           String address, int num_spot, int num_disable) {
		setId(id);
		setName(name);
		setCoordinates(coordinate_x, coordinate_y);
		setAddress(address);
		setNumSpot(num_spot);
		setNumDisable(num_disable);
	}
	
}