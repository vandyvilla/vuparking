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

// This file defines building structure.

package edu.vanderbilt.android.vuparking;

public class Building
{
	private int id;                        // Unique identifier.
	private String name;
	private String address;
	private double coordinate_x, coordinate_y; // Location information.

	public int getId() { return id; }
	public String getName() { return name; }
	public String getAddress() { return address; }
	public double getLatitude() { return coordinate_x; }
	public double getLongtitude() { return coordinate_y; }

	public void setId(int ID) { id = ID; }
	public void setName(String Name) { name = Name; }
	public void setAddress(String addr) { address = addr; }
	public void setCoordinates(double x, double y) { coordinate_x = x;  coordinate_y = y; }

	public Building(int id, String name, String address, double coordinate_x, double coordinate_y) 
	{
		setId(id);
		setName(name);
		setAddress(address);
		setCoordinates(coordinate_x, coordinate_y);
	}
}