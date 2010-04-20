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
import android.widget.Toast;

public class ParkingDBManager
{
	private static ParkingDBAdapter adapter;
	ArrayList<ParkingLot> lots = new ArrayList<ParkingLot>();
	ArrayList<Building> buildings = new ArrayList<Building>();

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
	
	public static final int TOTAL_LOTS_NUM = 20;
	public static final int TOTAL_BUILDING_NUM = 24;

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
			loadBuildingData();
			long id;
			for (int i = 0; i < lots.size(); i++) 
			{
				id = adapter.insertLot(lots.get(i));
				if (id == -1) 
					return false; 
			}
			for (int i = 0; i < buildings.size(); i++)
			{
				id = adapter.insertBuilding(buildings.get(i));
				if (id == -1)
					return false;
			}
		}
		return true;
	}

	// Initialization of ParkingLot Database with Static information.
	public void loadParkingData()
	{
		lots.add(new ParkingLot(1, LOT, ZONE1, "Magnolia lawn", "", 36.142773, -86.798635, 15, 15, 5, 0));
		lots.add(new ParkingLot(2, LOT, ZONE1, "English Language Center", "1226 18th Ave S", 36.141802, -86.795685, 40, 40, 10, 0));
		lots.add(new ParkingLot(3, LOT, ZONE1, "Wyatt Center", "", 36.139834, -86.798586, 40, 40, 10, 0));
		lots.add(new ParkingLot(4, LOT, ZONE2, "Baker building", "118 21st Ave S", 36.149334, -86.799635, 80, 80, 10, 0));
		lots.add(new ParkingLot(5, LOT, ZONE2, "West End Ave", "", 36.149472, -86.802467, 15, 15, 5, 0));
		lots.add(new ParkingLot(6, LOT, ZONE2, "Law School", "Scarritt Pl", 36.148222, -86.800073, 40, 40, 10, 0));
		lots.add(new ParkingLot(7, LOT, ZONE3, "Olin hall", "2400 Highland Ave", 36.142937, -86.805072, 20, 20, 0, 0));
		lots.add(new ParkingLot(8, GARAGE, ZONE3, "25th Ave Staff garage", "2499 Highland Ave", 36.142365, -86.806124, 80, 80, 20, 0));
		lots.add(new ParkingLot(9, GARAGE, ZONE3, "Kensington Garage", "", 36.147163,-86.807613, 20, 20, 5, 0));
		lots.add(new ParkingLot(10, LOT, ZONE3, "Parmer Field House", "", 36.145984,-86.808558, 20, 20, 0, 0));
		lots.add(new ParkingLot(11, LOT, ZONE4, "Blakemore Ave", "2140 Belcourt Ave", 36.137401, -86.804236, 20, 20, 5, 0));
		lots.add(new ParkingLot(12, LOT, ZONE4, "Real Estate", "2189-2199 Belcourt Ave", 36.136956,-86.805073, 20, 20, 5, 0));
		lots.add(new ParkingLot(13, LOT, MEDICAL, "Natchez field", "2964 Dudley Ave", 36.141120, -86.812059, 80, 80, 10, 0));
		lots.add(new ParkingLot(14, GARAGE, MEDICAL, "Central garage", "2401 Highland Ave", 36.141924,-86.805909, 80, 80, 10, 0));
		lots.add(new ParkingLot(15, GARAGE, MEDICAL, "South garage", "2376 Children's Way", 36.139454,-86.804375, 80, 80, 10, 0));
		lots.add(new ParkingLot(16, GARAGE, MEDICAL, "Medical center East South Tower", "1211 Medical Center Dr", 36.141672, -86.800920, 80, 80, 10, 0));
		lots.add(new ParkingLot(17, GARAGE, MEDICAL, "Vanderbilt Clinic garage", "1499 21st Ave S", 36.140381, -86.800899, 80, 80, 10, 0));
		lots.add(new ParkingLot(18, GARAGE, VISITOR, "South garage", "1598 24th Ave S", 36.139324, -86.804879, 40, 40, 5, 0.75));
		lots.add(new ParkingLot(19, LOT, VISITOR, "Planet beach", "2099 Scarritt Pl", 36.145770, -86.799182, 20, 20, 0, 0.75));
		lots.add(new ParkingLot(20, LOT, VISITOR, "Jess Neely Dr", "", 36.143333,-86.807935, 15, 15, 5, 0.75));
	}
	
	// Initialization of Building Database with Static information.
	public void loadBuildingData()
	{
		buildings.add(new Building(1, "Admissions Office", "2301-2319 West End Ave", 36.148144,-86.805094));
		buildings.add(new Building(2, "Baker Building", "106 21st Ave S", 36.149868,-86.800405));
		buildings.add(new Building(3, "Children's Hospital", "Pierce Ave", 36.13935,-86.802369));
		buildings.add(new Building(4, "Commons Center", "1231 18th Ave S", 36.141854,-86.797122));
		buildings.add(new Building(5, "Featheringill & Jacob Hall", "400 24th Ave S", 36.144748,-86.803184));
		buildings.add(new Building(6, "Graduate School", "2201 West End Ave", 36.148196,-86.802776));
		buildings.add(new Building(7, "Ingram Center", "2400 Blakemore Ave", 36.138527,-86.805555));
		buildings.add(new Building(8, "Institue for Software Integrate Systems", "2015 Terrace PI", 36.149634,-86.799687));
		buildings.add(new Building(9, "Law School", "Scarritt PI", 36.148118,-86.800523));
		buildings.add(new Building(10, "Light Hall", "2215 Garland Ave", 36.142071,-86.802648));
		buildings.add(new Building(11, "MC East South Tower", "1215 21th Ave S", 36.141638,-86.800877));
		buildings.add(new Building(12, "Medical Center North", "1161 21th Ave S", 36.143301,-86.801167));
		buildings.add(new Building(13, "Medical Research Bldg", "Edgehill Ave", 36.144609,-86.800631));
		buildings.add(new Building(14, "Olin Hall", "2400 Highland Ave", 36.142981,-86.805244));
		buildings.add(new Building(15, "Owen School of Management", "401 21st Ave S", 36.147001,-86.800041));
		buildings.add(new Building(16, "Peabody College", "Medical Center Dr.", 36.14201,-86.799322));
		buildings.add(new Building(17, "Rand Hall", "2300 Vanderbilt PI", 36.146429,-86.803227));
		buildings.add(new Building(18, "Sarratt Commons", "2300 Vanderbilt PI", 36.146351,-86.803796));
		buildings.add(new Building(19, "Stevenson Center", "2404 Garland Ave", 36.144081,-86.802412));
		buildings.add(new Building(20, "University Club", "2400 Highland Ave", 36.143561,-86.804311));
		buildings.add(new Building(21, "Vanderbilt Eye Institute", "Pierce Ave", 36.139749,-86.803678));
		buildings.add(new Building(22, "Vanderbilt Clinic", "1211 Medical Center Dr", 36.140633,-86.801983));
		buildings.add(new Building(23, "Veterans Hospital", "1310 24th Ave S", 36.141386,-86.803957));
		buildings.add(new Building(24, "Wyatt Center", "1930 South Dr", 36.139991,-86.799096));
        buildings.add(new Building(4, "Commons Center", "1231 18th Ave S", 36.141854,-86.797122));
        buildings.add(new Building(5, "Featheringill & Jacob Hall", "400 24th Ave S", 36.144748,-86.803184));
        buildings.add(new Building(6, "Graduate School", "2201 West End Ave", 36.148196,-86.802776));
        buildings.add(new Building(7, "Ingram Center", "2400 Blakemore Ave", 36.138527,-86.805555));
        buildings.add(new Building(8, "Institue for Software Integrate Systems", "2015 Terrace PI", 36.149634,-86.799687));
        buildings.add(new Building(9, "Law School", "Scarritt PI", 36.148118,-86.800523));
        buildings.add(new Building(10, "Light Hall", "2215 Garland Ave", 36.142071,-86.802648));
        buildings.add(new Building(11, "MC East South Tower", "1215 21th Ave S", 36.141638,-86.800877));
        buildings.add(new Building(12, "Medical Center North", "1161 21th Ave S", 36.143301,-86.801167));
        buildings.add(new Building(13, "Medical Research Bldg", "Edgehill Ave", 36.144609,-86.800631));
        buildings.add(new Building(14, "Olin Hall", "2400 Highland Ave", 36.142981,-86.805244));
        buildings.add(new Building(15, "Owen School of Management", "401 21st Ave S", 36.147001,-86.800041));
        buildings.add(new Building(16, "Peabody College", "Medical Center Dr.", 36.14201,-86.799322));
        buildings.add(new Building(17, "Rand Hall", "2300 Vanderbilt PI", 36.146429,-86.803227));
        buildings.add(new Building(18, "Sarratt Commons", "2300 Vanderbilt PI", 36.146351,-86.803796));
        buildings.add(new Building(19, "Stevenson Center", "2404 Garland Ave", 36.144081,-86.802412));
        buildings.add(new Building(20, "University Club", "2400 Highland Ave", 36.143561,-86.804311));
        buildings.add(new Building(21, "Vanderbilt Eye Institute", "Pierce Ave", 36.139749,-86.803678));
        buildings.add(new Building(22, "Vanderbilt Clinic", "1211 Medical Center Dr", 36.140633,-86.801983));
        buildings.add(new Building(23, "Veterans Hospital", "1310 24th Ave S", 36.141386,-86.803957));
        buildings.add(new Building(24, "Wyatt Center", "1930 South Dr", 36.139991,-86.799096));
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
	
	// Query the database for building by id.
	public Building queryBuildingById(int id) 
	{
		Cursor c = adapter.getBuildingById(id);
		Building b = null;
		if (c.moveToFirst())
			b = new Building(id, c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_BD_NAME)),
					c.getString(c.getColumnIndex(ParkingDBAdapter.KEY_BD_ADDR)),
					c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_BD_LOC_X)),
					c.getDouble(c.getColumnIndex(ParkingDBAdapter.KEY_BD_LOC_Y)));
		c.close();
		return b;
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
		Toast.makeText(Main.appContext, "Start updating.", Toast.LENGTH_SHORT).show();
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
					if (num >=0 && num < p.getNumSpot() && num != p.getNumAvailabe())
					{
						adapter.updateLot(id, num, 0);
						Log.i("i", "Updating entry :"+Integer.toString(id));
					}
					if (num > p.getNumSpot())
					{
						Log.i("i", "Error! Exceed the capacity of entry: "+Integer.toString(id));
					}						
				}
				else
				{
					Log.i("i", "Entry not found in DB");
				}
			}
		}catch (JSONException e)
		{
			Toast.makeText(Main.appContext, "JSON exception when updating DB!", Toast.LENGTH_SHORT).show();
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

	public CharSequence[] getAllLots()
	{
		CharSequence[] lots = new CharSequence[TOTAL_LOTS_NUM];		
		for (int i=0; i<TOTAL_LOTS_NUM; i++)
		{
			ParkingLot p = queryParkingById(i);
			lots[i]=p.getName();	
		}
		return lots;
	}
}
