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

// This file is written for testing building table in Sqlite and distance computing with Parking Lots.

package edu.vanderbilt.android.vuparking.test;

import java.util.Random;

import edu.vanderbilt.android.vuparking.Main;
import edu.vanderbilt.android.vuparking.ParkingDBManager;
import edu.vanderbilt.android.vuparking.Building;
import edu.vanderbilt.android.vuparking.ParkingLot;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

public class BuildingTest extends ActivityInstrumentationTestCase2<Main> 
{

	Main app;
	
	public BuildingTest() 
	{
		super("edu.vanderbilt.android.vuparking", Main.class);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		// Start app so that the private database could be accessed. 
		app = (Main) getActivity();
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	// Test Query the Building table in Database.
	public void testBuildingDBQuery()
	{
		ParkingDBManager parkingDb = new ParkingDBManager();
		// Open the database successfully
		assertEquals(parkingDb.openDB(), true);
		// Test queryBuildingById method which is based on adapter.insertBuilding and getBuildingById
		Random generator = new Random();
		int index = generator.nextInt(24) + 1;
		Building b = parkingDb.queryBuildingById(index);
		assertNotNull(b);
		assertEquals(b.getId(), index);
	}
	
	// Test Distance Computation within range.
	public void testDistanceComputation()
	{
		ParkingDBManager parkingDb = new ParkingDBManager();
		// Open the database successfully
		assertEquals(parkingDb.openDB(), true);
		Random generator = new Random();
		int index = generator.nextInt(24) + 1;
		Building b = parkingDb.queryBuildingById(index);
		Location b_loc = new Location("");
		b_loc.setLatitude(b.getLatitude());
		b_loc.setLongitude(b.getLongtitude());
		int Ranges[] = {0, 200, 300, 500, 800, 1000, 2000, 5000};
		int Numbers[] = {0, 0, 0, 0, 0, 0, 0};
		for (int j = 0; j < 7; j++)
		{
			for (int i = 1; i < 21; i++)
			{
				ParkingLot p = parkingDb.queryParkingById(i);
				Location p_loc = new Location("");
				p_loc.setLatitude(p.getLatitude());
				p_loc.setLongitude(p.getLongtitude());
				double distance = p_loc.distanceTo(b_loc);
				if (distance > Ranges[j] && distance <= Ranges[j+1])  Numbers[j]++;
			}
		}
		for (int i = 0; i < 7; i++)
		{
			System.out.println(Numbers[i]);
		}
		int sum = Numbers[0]+Numbers[1]+Numbers[2]+Numbers[3]+Numbers[4]+Numbers[5]+Numbers[6];
		assertEquals(sum, 20);
	}
}
