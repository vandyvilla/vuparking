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

// This file is written to calculate the distance between current location and parking lots.

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import android.database.DataSetObserver;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class DistanceCalculator implements ListAdapter
{
	LocationOverlay myLocation;
	private final static int TOTAL_LOT_NUM = 20; 	
	
	public void calculateDistance()
	{
		ArrayList<Double> distances = new ArrayList<Double>();
		
		//Set current location
		GeoPoint p = myLocation.getCurLocation();
		Location here = new Location("");
		here.setLatitude(p.getLatitudeE6() * 1.0E-6);
		here.setLongitude(p.getLongitudeE6() * 1.0E-6);
        
        ParkingDBManager parkingDb = new ParkingDBManager();
        
        Location location = new Location("");
        for (int i = 0; i < TOTAL_LOT_NUM; i++) 
        {
            ParkingLot lot = parkingDb.queryParkingById(i);    
        	location.setLatitude(lot.getLatitude() * 1.0E-6);
            location.setLongitude(lot.getLongtitude() * 1.0E-6);
            distances.add((double)here.distanceTo(location)); //in meter
        }

	}

	public boolean areAllItemsEnabled() 
	{
		return false;
	}

	public boolean isEnabled(int position) 
	{
		return false;
	}

	public int getCount() 
	{
		return TOTAL_LOT_NUM;
	}

	public Object getItem(int position) 
	{
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}
	
}