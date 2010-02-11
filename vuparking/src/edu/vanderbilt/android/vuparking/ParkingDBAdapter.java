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

// This file is written for accessing SQLite Database, Adapter Pattern. 

package edu.vanderbilt.android.vuparking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.vanderbilt.android.vuparking.ParkingLot;

public class ParkingDBAdapter {
	
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COORDINATE_X = "coordinate_x";
    public static final String KEY_COORDINATE_Y = "coordinate_y";
    public static final String KEY_CAPACITY = "capacity";
    public static final String KEY_ZONE = "zone";
    public static final String KEY_AVAILABLE = "available";
    public static final String KEY_DISABLE = "disable";
    
    private static final String TAG = "ParkingDBAdapter";
    
    private static final String DATABASE_NAME = "vuparking.db";
    private static final String DATABASE_TABLE = "parkinglots";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table parkinglots (_id integer primary key autoincrement, "
        + "name text not null, address text, " 
        + "zone integer not null, capacity integer not null, "
        + "available integer not null, disable integer not null, "
        + "coordinate_x text not null, coordinate_y text not null);";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    
    // Handle of database instance.
    private SQLiteDatabase db;

    public ParkingDBAdapter(Context ctx) 
    {
        this.context = ctx;
        //db = context.openOrCreateDatabase(DATABASE_NAME, DATABASE_VERSION, null)
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
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public ParkingDBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    // -1 return to indicate failure.
    public long insertLot(ParkingLot p) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, p.getName());
        initialValues.put(KEY_ADDRESS, p.getAddress());
        initialValues.put(KEY_ZONE, p.getZone());
        initialValues.put(KEY_CAPACITY, p.getNumSpot());    
        initialValues.put(KEY_AVAILABLE, p.getNumAvailabe());
        initialValues.put(KEY_DISABLE, p.getNumDisable());
        initialValues.put(KEY_COORDINATE_X, Double.toString(p.getLatitude()));
        initialValues.put(KEY_COORDINATE_Y, Double.toString(p.getLongtitude()));  
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular row---
    public boolean deleteLot(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }
    
    public boolean deleteAllLots(){
    	return db.delete(DATABASE_TABLE, null, null)>0;
    } 

    //---retrieves all the rows---
    public Cursor getAllLots() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_NAME,
        		KEY_ADDRESS,
                KEY_COORDINATE_X,
                KEY_COORDINATE_Y,
                KEY_CAPACITY,
                KEY_ZONE,
                KEY_AVAILABLE,
                KEY_DISABLE,
                }, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular row---
    public Cursor getLot(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_NAME,
                		KEY_ADDRESS,
                        KEY_COORDINATE_X,
                        KEY_COORDINATE_Y,
                        KEY_CAPACITY,
                        KEY_ZONE,
                        KEY_AVAILABLE,
                        KEY_DISABLE,
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a row---
    public boolean updateLot(long rowId, ParkingLot p) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, p.getName());
        args.put(KEY_ADDRESS, p.getAddress());
        args.put(KEY_COORDINATE_X, p.getLatitude());
        args.put(KEY_COORDINATE_Y, p.getLongtitude());
        args.put(KEY_CAPACITY, p.getNumSpot());
        args.put(KEY_ZONE, p.getZone());
        args.put(KEY_AVAILABLE, p.getNumAvailabe());
        args.put(KEY_DISABLE, p.getNumDisable());
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
	
    public boolean updateColumn(long rowId, String column, int value)
    {
    	 ContentValues args = new ContentValues();
         args.put(column, value);
         return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;

    }
	
}