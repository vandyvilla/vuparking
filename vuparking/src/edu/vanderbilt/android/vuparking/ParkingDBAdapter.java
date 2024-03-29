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

// This file is written for accessing android SQLite3 Database, Adapter Pattern.

package edu.vanderbilt.android.vuparking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ParkingDBAdapter 
{
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_ZONE = "zone";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_COORDINATE_X = "coordinate_x";
	public static final String KEY_COORDINATE_Y = "coordinate_y";
	public static final String KEY_CAPACITY = "capacity";
	public static final String KEY_AVAILABLE = "available";
	public static final String KEY_DISABLE = "disable";
	public static final String KEY_RATE = "rate";
	
	public static final String KEY_BD_ID = "bd_id";
	public static final String KEY_BD_NAME = "bd_name";
	public static final String KEY_BD_ADDR = "bd_addr";
	public static final String KEY_BD_LOC_X = "bd_loc_x";
	public static final String KEY_BD_LOC_Y = "bd_loc_y";

	private static final String TAG = "ParkingDBAdapter";

	private static final String DATABASE_NAME = "vuparking.db";
	private static final String DATABASE_TABLE_PARKINGLOT = "ParkingLot";	
	private static final String DATABASE_TABLE_BUILDING = "Building";
	
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE_PARKINGLOT =
		"create table if not exists ParkingLot (_id integer primary key, "
		+ "type integer not null, zone integer not null, "
		+ "name text not null, address text, "
		+ "coordinate_x text not null, coordinate_y text not null, "
		+ "capacity integer not null, available integer not null,"
		+ "disable integer not null, rate integer);";
	
	private static final String DATABASE_CREATE_BUILDING = 
		"create table if not exists Building (bd_id integer primary key, "
		+ "bd_name text not null, bd_addr text, "
		+ "bd_loc_x text not null, bd_loc_y text not null);";

	private final Context context;    
	private DatabaseHelper DBHelper;

	// Handle of database instance.
	private SQLiteDatabase db;

	public ParkingDBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper 
	{
		DatabaseHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.i("i", "Creating Database");
			db.execSQL(DATABASE_CREATE_PARKINGLOT);
			db.execSQL(DATABASE_CREATE_BUILDING);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion 
					+ " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS ParkingLot");
			db.execSQL("DROP TABLE IF EXISTS Building");
			onCreate(db);
		}
	}    

	// opens the database.
	public ParkingDBAdapter open() throws SQLException 
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// closes the database.  
	public void close() 
	{
		Log.i("i", "Closing Database");
		DBHelper.close();
	}

	// Check if there exist parking data.
	public boolean checkDb()
	{
		Cursor c1 = db.rawQuery("select * from ParkingLot", null);
		Cursor c2 = db.rawQuery("select * from Building", null);
		int count1 = c1.getCount();
		int count2 = c2.getCount();
		c1.close();
		c2.close();
		if (count1 != 0 && count2 != 0) return true;
		else return false;
	}    

	// Insertion: insert a new parking lot into database, return -1 to indicate failure.
	public long insertLot(ParkingLot p)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID, p.getId());
		initialValues.put(KEY_TYPE, p.getType());
		initialValues.put(KEY_ZONE, p.getZone());
		initialValues.put(KEY_NAME, p.getName());
		initialValues.put(KEY_ADDRESS, p.getAddress());
		initialValues.put(KEY_COORDINATE_X, Double.toString(p.getLatitude()));
		initialValues.put(KEY_COORDINATE_Y, Double.toString(p.getLongtitude()));
		initialValues.put(KEY_CAPACITY, p.getNumSpot());
		initialValues.put(KEY_AVAILABLE, p.getNumAvailabe());
		initialValues.put(KEY_DISABLE, p.getNumDisable());
		initialValues.put(KEY_RATE, p.getRate());
		return db.insert(DATABASE_TABLE_PARKINGLOT, null, initialValues);
	}
	
	// Insertion: insert a new building into database, return -1 to indicate failure.
	public long insertBuilding(Building b)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BD_ID, b.getId());
		initialValues.put(KEY_BD_NAME, b.getName());
		initialValues.put(KEY_BD_ADDR, b.getAddress());
		initialValues.put(KEY_BD_LOC_X, Double.toString(b.getLatitude()));
		initialValues.put(KEY_BD_LOC_Y, Double.toString(b.getLongtitude()));
		return db.insert(DATABASE_TABLE_BUILDING, null, initialValues);
	}

	// Update: update number of available and disable spots got from server.
	// If we want to update all parking lots, we need a cycle to do this. 
	public boolean updateLot(long rowId, int num_available, int num_disable)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_AVAILABLE, num_available);
		args.put(KEY_DISABLE, num_disable);
		return db.update(DATABASE_TABLE_PARKINGLOT, args,
				KEY_ROWID + "=" + rowId, null) > 0;
	}

	// Deletion: deletes a particular parking lot from database.
	public boolean deleteLot(long rowId)
	{
		return db.delete(DATABASE_TABLE_PARKINGLOT, KEY_ROWID + 
				"=" + rowId, null) > 0;
	}
	
	// Deletion: deletes a particular building from database.
	public boolean deleteBuilding(long rowId)
	{
		return db.delete(DATABASE_TABLE_BUILDING, KEY_BD_ID + 
				"=" + rowId, null) > 0;
	}

	// deletes all parking lots from database.
	public boolean deleteAllLots()
	{
		return db.delete(DATABASE_TABLE_PARKINGLOT, null, null)>0;
	}

	// Query: retrieve parking lot information by zone.
	public Cursor getLotsByZone(int zone)
	{
		Cursor mCursor = 
			db.query(false, DATABASE_TABLE_PARKINGLOT, new String[]
			{
				KEY_ROWID,
				KEY_TYPE,
				KEY_ZONE,
				KEY_NAME,
				KEY_ADDRESS,
				KEY_COORDINATE_X,
				KEY_COORDINATE_Y,
				KEY_CAPACITY,
				KEY_AVAILABLE,
				KEY_DISABLE,
				KEY_RATE,
			},
			KEY_ZONE + "=" + zone, null, null, null, null, null);
		
		if (mCursor != null) 
			mCursor.moveToFirst();

		return mCursor;    	
	}

	// retrieve parking lot information by id.
	public Cursor getLotById(long rowId) throws SQLException 
	{
		Cursor mCursor =
			db.query(true, DATABASE_TABLE_PARKINGLOT, new String[] 
			{
				KEY_ROWID,
				KEY_TYPE,
				KEY_ZONE,
				KEY_NAME,
				KEY_ADDRESS,
				KEY_COORDINATE_X,
				KEY_COORDINATE_Y,
				KEY_CAPACITY,
				KEY_AVAILABLE,
				KEY_DISABLE,
				KEY_RATE,
			}, 
			KEY_ROWID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null)
			mCursor.moveToFirst();

		return mCursor;
	}
	
	// retrieve building coordinates by id.
	public Cursor getBuildingById(long rowId) throws SQLException 
	{
		Cursor mCursor =
			db.query(true, DATABASE_TABLE_BUILDING, new String[] 
			{
				KEY_BD_ID,
				KEY_BD_NAME,
				KEY_BD_ADDR,
				KEY_BD_LOC_X,
				KEY_BD_LOC_Y,
			}, 
			KEY_BD_ID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null)
			mCursor.moveToFirst();

		return mCursor;
	}	
}