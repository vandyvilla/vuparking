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

// This file defines parking lot/garage structure.

package edu.vanderbilt.android.vuparking;

public class ParkingLot 
{
	private int type;                      // Indication of garage or parking lot.
	private int zone;                      // Multiple zone belonging excluded.
	private int id;                        // Unique identifier.
	private String name;
	private String address;
	private double coordinate_x, coordinate_y; // Location information.
	private int num_spot;                   // Capacity in total.
	private int num_disable;                // Number of available spots specially for disability.
	private int num_available;              // Number of available spots excluding those for disability.
	private int rate;                       // For visitor zone.

	public int getId() { return id; }
	public int getType() { return type; }
	public int getZone() { return zone; }
	public String getName() { return name; }
	public String getAddress() { return address; }
	public double getLatitude() { return coordinate_x; }
	public double getLongtitude() { return coordinate_y; }
	public int getNumSpot() { return num_spot; }
	public int getNumDisable() { return num_disable; }
	public int getNumAvailabe() { return num_available; }
	public int getRate() { return rate; }

	public void setId(int ID) { id = ID; }
	public void setType(int Type) { type = Type; }
	public void setZone(int Zone) { zone = Zone; }
	public void setName(String Name) { name = Name; }
	public void setAddress(String addr) { address = addr; }
	public void setCoordinates(double x, double y) { coordinate_x = x;  coordinate_y = y; }
	public void setNumSpot(int num) { num_spot = num; }
	public void setNumDisable(int num) { num_disable = num; }
	public void setNumAvailable(int num) { num_available = num; }
	public void setRate(int Rate) { rate = Rate; }

	public ParkingLot(int id, int type, int zone, String name, String address, double coordinate_x, double coordinate_y, 
			int num_spot, int num_disable, int num_available, int rate) 
	{
		setId(id);
		setType(type);
		setZone(zone);
		setName(name);
		setAddress(address);
		setCoordinates(coordinate_x, coordinate_y);
		setNumSpot(num_spot);
		setNumDisable(num_disable);
		setNumAvailable(num_available);
		setRate(rate);
	}

}