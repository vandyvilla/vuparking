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

// This file is written for testing multiple zone choices and database access. 

package edu.vanderbilt.android.vuparking.test;

import edu.vanderbilt.android.vuparking.Main;
import edu.vanderbilt.android.vuparking.ParkingDBManager;
import edu.vanderbilt.android.vuparking.ParkingLot;
import android.test.ActivityInstrumentationTestCase2;

public class MultiZoneTest extends ActivityInstrumentationTestCase2<Main> 
{

	Main app;
	
	public MultiZoneTest() 
	{
		super("edu.vanderbilt.android.vuparking", Main.class);
	}

	public static final int ZONE1 = 0;
	public static final int ZONE2 = 1;
	public static final int ZONE3 = 2;
	public static final int ZONE4 = 3;
	public static final int MEDICAL = 4;
	public static final int VISITOR = 5;

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
	
	// Test zone1 zone choice.
	public void testZone1()
	{
		ParkingDBManager parkingDb = new ParkingDBManager();
		assertEquals(parkingDb.openDB(), true);
		int num = parkingDb.queryParkingZone(ZONE1).size();
		assert(num == 3);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE1).get(i);
			assert(p.getZone() == ZONE1);
		}
	}
	
	// Test zone2 zone choice.
	public void testZone2(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE2).size();
		assert(num == 3);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE2).get(i);
			assert(p.getZone() == ZONE2);
		}
	}
	
	// Test zone3 zone choice.
	public void testZone3(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE3).size();
		assert(num == 2);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE3).get(i);
			assert(p.getZone() == ZONE3);
		}
	}
	
	// Test zone4 zone choice.
	public void testZone4(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE4).size();
		assert(num == 1);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE4).get(i);
			assert(p.getZone() == ZONE4);
		}
	}
	
	// Test Medical zone choice.
	public void testZoneMedical() {
	    ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(MEDICAL).size();
		assert(num == 5);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(MEDICAL).get(i);
			assert(p.getZone() == MEDICAL);
		}
	}
	
	// Test Visitor zone choice
	public void testZoneVisitor() {
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(VISITOR).size();
		assert(num == 2);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(VISITOR).get(i);
			assert(p.getZone() == VISITOR);
		}
	}
}
