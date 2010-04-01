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
	private ArrayList<String> lots = new ArrayList();
	
	void calculateDistance()
	{
		LocationOverlay myLocation;
		int total_num=20;
		
		Location here = myLocation.getCurLocation();
		
		ArrayList<Double> distances = new ArrayList();
        
        ParkingDBManager parkingDb = new ParkingDBManager();
        
        Location location = new Location("");
        for (int i = 0; i < total_num; i++) 
        {
            ParkingLot p = parkingDb.queryParkingById(i);    
        	location.setLatitude(p.getLatitude() * 1.0E-6);
            location.setLongitude(p.getLongtitude() * 1.0E-6);
            distances.add(here.distanceTo(location));
        }

	}
	
	
	public void addItem(CharSequence parkingLots)
	{
		lots.add((String) parkingLots);
	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
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