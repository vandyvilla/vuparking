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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

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
		//Zone 1, total num=15
		lots.add(new ParkingLot(1, LOT, ZONE1, "Peabody Administration", "", 36.14200546268723, -86.79931730031967, 20, 5, 15, 0));
		lots.add(new ParkingLot(2, LOT, ZONE1, "Magnolia Circle", "", 36.142773, -86.798635, 20, 5, 15, 0));
		lots.add(new ParkingLot(3, LOT, ZONE1, "Appleton Ace", "", 36.14312962864645, -86.79657876491547, 20, 5, 15, 0));
		lots.add(new ParkingLot(4, LOT, ZONE1, "Office of Initiatives in Education", "", 36.14337005511312, -86.79533958435059, 20, 5, 15, 0));
		lots.add(new ParkingLot(5, LOT, ZONE1, "English Language Center", "", 36.143166450765705, -86.79521083831787, 50, 10, 40, 0));
		lots.add(new ParkingLot(6, GARAGE, ZONE1, "Lot 76 Garage", "", 36.142819888959124, -86.79562121629715, 50, 10, 40, 0));
		lots.add(new ParkingLot(7, LOT, ZONE1, "", "Appleton Ace", 36.141756, -86.795683, 20, 5, 15, 0));
		lots.add(new ParkingLot(8, LOT, ZONE1, "Generator Control Bldg", "", 36.142255, -86.796708, 20, 5, 15, 0));
		lots.add(new ParkingLot(9, LOT, ZONE1, "", "1808 Edgehill", 36.14381,-86.795973, 20, 5, 15, 0));
		lots.add(new ParkingLot(10, LOT, ZONE1, "Hill Center", "", 36.142008,-86.796498, 20, 5, 15, 0));
		lots.add(new ParkingLot(11, LOT, ZONE1, "", "1112 18th Ave S", 36.143792,-86.795404, 20, 5, 15, 0));
		lots.add(new ParkingLot(12, LOT, ZONE1, "", "18th Ave S, South Dr", 36.139426,-86.796831, 20, 5, 15, 0));
		lots.add(new ParkingLot(13, LOT, ZONE1, "Wyatt Center", "", 36.139834, -86.798586, 50, 10, 40, 0));
		lots.add(new ParkingLot(14, LOT, ZONE1, "", "1114 19th Ave", 36.144477, -86.796911, 20, 5, 15, 0));
		lots.add(new ParkingLot(15, GARAGE, ZONE1, "Village at Vanderbilt", "", 36.139742,-86.800066, 20, 5, 15, 0));

		//Zone 2, total num=16
		lots.add(new ParkingLot(16, LOT, ZONE2, "Vanerbilt & Barnard", "", 36.148833,-86.803933, 100, 20, 80, 0));
		lots.add(new ParkingLot(17, LOT, ZONE2, "Mims & Reinke", "", 36.149692,-86.802093, 100, 20, 80, 0));
		lots.add(new ParkingLot(18, LOT, ZONE2, "Heimingway", "", 36.149747,-86.801082, 100, 20, 80, 0));
		lots.add(new ParkingLot(19, LOT, ZONE2, "Wilson", "", 36.149175,-86.800624, 100, 20, 80, 0));
		lots.add(new ParkingLot(20, LOT, ZONE2, "", "112 21st Ave S", 36.149337,-86.80005, 100, 20, 80, 0));
		lots.add(new ParkingLot(21, LOT, ZONE2, "", "114 21st Ave S", 36.148713,-86.799567, 100, 20, 80, 0));
		lots.add(new ParkingLot(22, LOT, ZONE2, "Law School", "Scarritt Pl", 36.148222, -86.800073, 50, 10, 40, 0));
		lots.add(new ParkingLot(23, LOT, ZONE2, "", "969-999 21st Ave S", 36.146285,-86.799626, 100, 20, 80, 0));
		lots.add(new ParkingLot(24, LOT, ZONE2, "Wesley Place Parking", "", 36.145724,-86.798446, 100, 20, 80, 0));
		lots.add(new ParkingLot(25, GARAGE, ZONE2, "Terrace Place Garage", "", 36.150234,-86.799744, 100, 20, 80, 0));
		lots.add(new ParkingLot(26, LOT, ZONE2, "Institute for Software Integrated Systems", "", 36.149712,-86.799403, 100, 20, 80, 0));
		lots.add(new ParkingLot(27, LOT, ZONE2, "Community, Neighborhood, & Government Relations", "", 36.149712,-86.799403, 100, 20, 80, 0));
		lots.add(new ParkingLot(28, LOT, ZONE2, "Baker Bldg", "118 21st Ave S", 36.149334, -86.799635, 100, 20, 80, 0));
		lots.add(new ParkingLot(29, LOT, ZONE2, "Barbizon Apartment", "", 36.149417,-86.798821, 100, 20, 80, 0));
		lots.add(new ParkingLot(30, LOT, ZONE2, "Barbizon Apartment", "", 36.149417,-86.798821, 100, 20, 80, 0));
		lots.add(new ParkingLot(31, GARAGE, ZONE2, "Center Garage", "", 36.149099,-86.799315, 20, 5, 15, 0));

		//Zone 3, total num=10
		lots.add(new ParkingLot(32, LOT, ZONE3, "Olin Hall", "2400 Highland Ave", 36.142937, -86.805072, 20, 0, 20, 0));
		lots.add(new ParkingLot(33, GARAGE, ZONE3, "25th Ave Staff Garage", "2499 Highland Ave", 36.142365, -86.806124, 100, 20, 80, 0));
		lots.add(new ParkingLot(34, GARAGE, ZONE3, "Kensington Garage", "", 36.147163,-86.807613, 20, 0, 20, 0));
		lots.add(new ParkingLot(35, LOT, ZONE3, "Parmer Field House", "", 36.145984,-86.808558, 20, 0, 20, 0));
		lots.add(new ParkingLot(36, LOT, ZONE3, "Tarpley Bldg", "", 36.148549,-86.807699, 20, 0, 20, 0));
		lots.add(new ParkingLot(37, LOT, ZONE3, "Delta Kappa Epsilon", "", 36.148618,-86.80624, 20, 0, 20, 0));
		lots.add(new ParkingLot(38, LOT, ZONE3, "PI Kappa Alpha", "", 36.147786,-86.806927, 20, 0, 20, 0));
		lots.add(new ParkingLot(39, LOT, ZONE3, "Lambda Chi Alpha", "", 36.147925,-86.805897, 20, 0, 20, 0));
		lots.add(new ParkingLot(40, LOT, ZONE3, "Kensington Pl", "", 36.147578,-86.805296, 20, 0, 20, 0));
		lots.add(new ParkingLot(41, LOT, ZONE3, "VANDERBILT PLACE", "", 36.144356,-86.809974, 20, 0, 20, 0));

		//Zone 4, total num=4
		lots.add(new ParkingLot(34, LOT, ZONE4, "", "2189-2199 Belcourt Ave", 36.136956,-86.805073, 20, 0, 20, 0)); 
		lots.add(new ParkingLot(35, LOT, ZONE4, "", "1203 17th Ave S", 36.142995,-86.794862, 20, 0, 20, 0));
		lots.add(new ParkingLot(36, GARAGE, ZONE4, "1207 17th Ave Garage", "1207 17th Ave", 36.142428,-86.795034, 20, 0, 20, 0));
		lots.add(new ParkingLot(37, GARAGE, ZONE4, "Real Estate", "", 36.144538,-86.795592, 20, 0, 20, 0));

		//Medical, total num=12
		lots.add(new ParkingLot(38, LOT, MEDICAL, "Natchez Field", "2964 Dudley Ave", 36.141120, -86.812059, 100, 20, 80, 0));
		lots.add(new ParkingLot(39, GARAGE, MEDICAL, "Central Garage", "2401 Highland Ave", 36.141924,-86.805909, 100, 20, 80, 0));
		lots.add(new ParkingLot(40, GARAGE, MEDICAL, "South Garage", "2376 Children's Way", 36.139454,-86.804375, 100, 20, 80, 0));
		lots.add(new ParkingLot(41, GARAGE, MEDICAL, "Medical Center East South Tower", "1211 Medical Center Dr", 36.141672, -86.800920, 100, 20, 80, 0));
		lots.add(new ParkingLot(42, GARAGE, MEDICAL, "Vanderbilt Clinic Garage", "1499 21st Ave S", 36.140381, -86.800899, 100, 20, 80, 0));
		lots.add(new ParkingLot(43, GARAGE, MEDICAL, "Medical Center East North Tower", "", 36.143628,-86.800489, 100, 20, 80, 0));
		lots.add(new ParkingLot(44, LOT, MEDICAL, "Eskind Biomedical Library", "", 36.144182,-86.802292, 100, 20, 80, 0));
		lots.add(new ParkingLot(45, LOT, MEDICAL, "Preston Research", "", 36.140751,-86.802818, 100, 20, 80, 0));
		lots.add(new ParkingLot(46, LOT, MEDICAL, "Belcourt Child Care", "", 36.137086,-86.80389, 100, 20, 80, 0));
		lots.add(new ParkingLot(47, LOT, MEDICAL, "", "2147 Blakemore Ave", 36.137615,-86.804395, 100, 20, 80, 0));
		lots.add(new ParkingLot(48, LOT, MEDICAL, "", "2120 Belcourt Ave", 36.136991,-86.802378, 100, 20, 80, 0));
		lots.add(new ParkingLot(49, LOT, MEDICAL, "Dayani Center", "", 36.140717,-86.800919, 100, 20, 80, 0));

		//Visitor, total num=6
		lots.add(new ParkingLot(50, GARAGE, VISITOR, "South Garage", "1598 24th Ave S", 36.139324, -86.804879, 50, 10, 40, 0.75));
		lots.add(new ParkingLot(51, GARAGE, VISITOR, "Wesley Place Garage", "1901-2035 Scarritt Pl", 36.145736,-86.79871, 20, 0, 20, 0.75));
		lots.add(new ParkingLot(52, LOT, VISITOR, "Schulman Center", "", 36.14481,-86.806256, 20, 0, 20, 0.75));
		lots.add(new ParkingLot(53, LOT, VISITOR, "Jess Neely Dr", "", 36.143333,-86.807935, 20, 0, 20, 0.75));
		lots.add(new ParkingLot(54, LOT, VISITOR, "Garland Ave", "", 36.143316,-86.805511, 20, 0, 20, 0.75));
		lots.add(new ParkingLot(55, LOT, VISITOR, "24th Ave S", "", 36.142787,-86.804749, 20, 0, 20, 0.75));
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

	// Update database information based on parking lot unique ID number.
	// !! Right now doesn't support inserting or deleting entry in DB.
	public boolean updateDB(JSONArray servResp)
	{
		try {
			for (int i = 0; i < servResp.length(); i++)
			{
				JSONObject lot = (JSONObject) servResp.get(i);
				int id = lot.getInt("id");
				ParkingLot p = queryParkingById(id);
				if (p != null)
				{
					int num = lot.getInt("available");
					// Database consistency check.
					if (num >=0 && num < p.getNumSpot())
					{
						adapter.updateLot(id, num, 0);
						Log.i("i", "Updating entry :"+Integer.toString(id));
					}
					else
						Log.i("i", "Error! Exceed the capacity of entry: "+Integer.toString(id));
				}
				else
				{
					Log.i("i", "Entry not found in DB");
				}
			}
		}catch (JSONException e)
		{
			e.printStackTrace();
		}
		return true;
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
