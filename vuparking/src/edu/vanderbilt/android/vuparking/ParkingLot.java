/*****************************************************************************
 *   Copyright VUparking for CS279 project                                   *
 *   Developed by Xiaowei Li, Yuan Zhuang                                    *
 *   Licensed under the Apache License, Version 2.0 (the "License");         *
 *   you may not use this file except in compliance with the License.        *
 *   You may obtain a copy of the License at                                 *
 *                                                                           *
 *   http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                           *
 *   Unless required by applicable law or agreed to in writing, software     *
 *   distributed under the License is distributed on an "AS IS" BASIS,       *
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 *   See the License for the specific language governing permissions and     *
 *   limitations under the License.                                          *
 ****************************************************************************/
package edu.vanderbilt.android.vuparking;

// Definition of Parking Lot Structure.
public class ParkingLot {
	
	private int zone;
	private int id;
	private String name;
	//private double coordinates[];
	private double coordinate_x, coordinate_y;
	private String address;
	private int num_spot;
	private int num_disable;
	private int num_available;
	
	public int getId() { return id; }
	public String getName() { return name; }
	public int getZone() { return zone; }
	public double getLatitude() { return coordinate_x; }
	public double getLongtitude() { return coordinate_y; }
	public String getAddress() { return address; }
	public int getNumSpot() { return num_spot; }
	public int getNumDisable() { return num_disable; }
	public int getNumAvailabe() { return num_available; }
	
	public void setId(int ID) { id = ID; }
	public void setName(String Name) { name = Name; }
	public void setCoordinates(double x, double y) { coordinate_x = x;  coordinate_y = y; }
	public void setAddress(String addr) { address = addr; }
	public void setNumSpot(int num) { num_spot = num; }
	public void setNumDisable(int num) { num_disable = num; }
	public void setZone(int type) { zone = type; }
	public void setNumAvailable(int num) { num_available = num; }
	
	ParkingLot() {
		// TODO Auto-generated constructor stub
	}
	
	public ParkingLot(int id, String name, int zone, double coordinate_x, double coordinate_y, 
			           String address, int num_spot, int num_disable) {
		setId(id);
		setName(name);
		setCoordinates(coordinate_x, coordinate_y);
		setAddress(address);
		setNumSpot(num_spot);
		setNumDisable(num_disable);
		setNumAvailable(num_spot);
		setZone(zone);
	}

	
}