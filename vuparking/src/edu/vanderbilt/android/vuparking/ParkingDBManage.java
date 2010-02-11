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

// This file is written for Android SQLite Database initialization, sync with remote server,
// commit and management. 

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;
import java.util.ListIterator;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import edu.vanderbilt.android.vuparking.ParkingDBAdapter;
import edu.vanderbilt.android.vuparking.ParkingLot;


public class ParkingDBManage
{
	private static ParkingDBAdapter adapter;
	
	public static final int VISITOR = 0;
	public static final int ZONE1 = 1;
	public static final int ZONE2 = 2;
	public static final int ZONE3 = 3;
	public static final int ZONE4 = 4;
	public static final int MEDICAL = 5;
	
	
	// Static initialization of ParkingLot Database
	public void static_init(){
		
		ArrayList<ParkingLot> lots = new ArrayList<ParkingLot>();
		lots.add(new ParkingLot(1, "Magnolia lawn", ZONE1, 36.142773, -86.798635, "", 20, 5));
		lots.add(new ParkingLot(2, "English Language Center", ZONE1, 36.141802, -86.795685, "1226 18th Ave S", 50, 10));
		lots.add(new ParkingLot(3, "Wyatt Center", ZONE1, 36.139834, -86.798586, "", 50, 10));
		lots.add(new ParkingLot(4, "Baker building", ZONE2, 36.149334, -86.799635, "118 21st Ave S", 100, 20));
		lots.add(new ParkingLot(5, "West End Ave", ZONE2, 36.149472, -86.802467, "", 20, 5));
		lots.add(new ParkingLot(6, "Law School", ZONE2, 36.148222, -86.800073, "Scarritt Pl", 50, 10));
		lots.add(new ParkingLot(7, "Olin hall", ZONE3, 36.142937, -86.805072, "2400 Highland Ave", 20, 0));
		lots.add(new ParkingLot(8, "25th Ave Staff garage", ZONE3, 36.142365, -86.806124, "2499 Highland Ave", 100, 20));
		lots.add(new ParkingLot(9, "Blakemore Ave", ZONE4, 36.137401, -86.804236, "2140 Belcourt Ave", 20, 0));
		lots.add(new ParkingLot(10, "Natchez field", MEDICAL, 36.141120, -86.812059, "2964 Dudley Ave", 100, 20));
		lots.add(new ParkingLot(11, "Central garage", MEDICAL, 36.141924,-86.805909, "2401 Highland Ave", 100, 20));
		lots.add(new ParkingLot(12, "South garage", MEDICAL, 36.139454,-86.804375, "2376 Children's Way", 100, 20));
		lots.add(new ParkingLot(13, "Medical center East South Tower", MEDICAL, 36.141672, -86.800920, "1211 Medical Center Dr", 100, 20));
		lots.add(new ParkingLot(14, "Vanderbilt Clinic garage", MEDICAL, 36.140381, -86.800899, "1499 21st Ave S", 100, 20));
		lots.add(new ParkingLot(15, "South garage", VISITOR, 36.139324, -86.804879, "1598 24th Ave S", 50, 10));
		lots.add(new ParkingLot(16, "Planet beach", VISITOR, 36.145770, -86.799182, "2099 Scarritt Pl", 20, 0));
		
		adapter.open();
		long id;
		for (ListIterator<ParkingLot> it = lots.listIterator(); it.hasNext();)
		{
			adapter.insertLot(it.next());
		}
		adapter.close();
	}
	
	// 
	public void sync_server(){
		
		
	}
	
	
	
	
	
	
	
	
	
}

