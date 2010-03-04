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

// This file is written for managing SQLite DB based on DBAdapter, including initialization, sync (update) with server
// and refreshing. 

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;
import android.database.Cursor;

public class ParkingDBManager
{
	private static ParkingDBAdapter adapter;
	ArrayList<ParkingLot> lots = new ArrayList<ParkingLot>();

	// Type field.
	public static final int GARAGE = 0;
	public static final int LOT = 1;

	// Zone field.
	public static final int ZONE1 = 0;
	public static final int ZONE2 = 1;
	public static final int ZONE3 = 2;
	public static final int ZONE4 = 3;
	public static final int MEDICAL = 4;
	public static final int VISITOR = 5;


	// Open or Create the database.
	public boolean openDB()
	{
		if (adapter == null)
			adapter = new ParkingDBAdapter(Main.appContext);
		adapter.open();
		if (adapter == null) 
			return false;

		// Make sure only initialize the database exactly once.
		if (adapter.checkDb() == false) 
		{
			loadParkingData();
			long id;
			for (int i = 0; i < lots.size(); i++) 
			{
				id = adapter.insertLot(lots.get(i));
				if (id == -1) 
					return false; 
			}
		}
		return true;
	}

	// Initialization of ParkingLot Database with Static information.
	public void loadParkingData()
	{
		lots.add(new ParkingLot(1, LOT, ZONE1, "Magnolia lawn", "", 36.142773, -86.798635, 20, 5, 15, 0));
		lots.add(new ParkingLot(2, LOT, ZONE1, "English Language Center", "1226 18th Ave S", 36.141802, -86.795685, 50, 10, 40, 0));
		lots.add(new ParkingLot(3, LOT, ZONE1, "Wyatt Center", "", 36.139834, -86.798586, 50, 10, 40, 0));
		lots.add(new ParkingLot(4, LOT, ZONE2, "Baker building", "118 21st Ave S", 36.149334, -86.799635, 100, 20, 80, 0));
		lots.add(new ParkingLot(5, LOT, ZONE2, "West End Ave", "", 36.149472, -86.802467, 20, 5, 15, 0));
		lots.add(new ParkingLot(6, LOT, ZONE2, "Law School", "Scarritt Pl", 36.148222, -86.800073, 50, 10, 40, 0));
		lots.add(new ParkingLot(7, LOT, ZONE3, "Olin hall", "2400 Highland Ave", 36.142937, -86.805072, 20, 0, 20, 0));
		lots.add(new ParkingLot(8, GARAGE, ZONE3, "25th Ave Staff garage", "2499 Highland Ave", 36.142365, -86.806124, 100, 20, 80, 0));
		lots.add(new ParkingLot(9, LOT, ZONE4, "Blakemore Ave", "2140 Belcourt Ave", 36.137401, -86.804236, 20, 0, 20, 0));
		lots.add(new ParkingLot(10, LOT, MEDICAL, "Natchez field", "2964 Dudley Ave", 36.141120, -86.812059, 100, 20, 80, 0));
		lots.add(new ParkingLot(11, GARAGE, MEDICAL, "Central garage", "2401 Highland Ave", 36.141924,-86.805909, 100, 20, 80, 0));
		lots.add(new ParkingLot(12, GARAGE, MEDICAL, "South garage", "2376 Children's Way", 36.139454,-86.804375, 100, 20, 80, 0));
		lots.add(new ParkingLot(13, GARAGE, MEDICAL, "Medical center East South Tower", "1211 Medical Center Dr", 36.141672, -86.800920, 100, 20, 80, 0));
		lots.add(new ParkingLot(14, GARAGE, MEDICAL, "Vanderbilt Clinic garage", "1499 21st Ave S", 36.140381, -86.800899, 100, 20, 80, 0));
		lots.add(new ParkingLot(15, GARAGE, VISITOR, "South garage", "1598 24th Ave S", 36.139324, -86.804879, 50, 10, 40, 0));
		lots.add(new ParkingLot(16, LOT, VISITOR, "Planet beach", "2099 Scarritt Pl", 36.145770, -86.799182, 20, 0, 20, 0));	
	}

	// Query the database for parking lot by id.
	public ParkingLot queryParkingById(int id) 
	{
		Cursor c = adapter.getLotById(id);
		ParkingLot p = null;

		if (c.moveToFirst())
			p = new ParkingLot(id, c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_TYPE)),
					c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_ZONE)),
					c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_NAME)),
					c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_ADDRESS)),
					c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_COORDINATE_X)),
					c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_COORDINATE_Y)),
					c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_CAPACITY)),
					c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_AVAILABLE)),
					c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_DISABLE)),
					c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_RATE)));
		c.close();
		return p;
	}

	// Query the database for parking lot data of specific zone.
	public ArrayList<ParkingLot> queryParkingZone(int zone)
	{
		ArrayList<ParkingLot> lotInZone = new ArrayList<ParkingLot>();
		Cursor c = adapter.getLotsByZone(zone);

		if (c.moveToFirst())
			do 
			{
				ParkingLot p = new ParkingLot(c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_ROWID)), 
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_TYPE)),
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_ZONE)),
						c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_NAME)),
						c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_ADDRESS)),
						c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_COORDINATE_X)),
						c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_COORDINATE_Y)),
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_CAPACITY)),
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_AVAILABLE)),
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_DISABLE)),
						c.getInt(c.getColumnIndex(ParkingDBAdapter.KEY_RATE)));
				lotInZone.add(p);
			} while (c.moveToNext());

		c.close();
		return lotInZone;
	}

	// Delete all parking lots information in DB.
	public boolean cleanUp() 
	{
		adapter.deleteAllLots();
		adapter.close();

		if (adapter == null) 
			return true;
		else 
			return false;
	}
}

