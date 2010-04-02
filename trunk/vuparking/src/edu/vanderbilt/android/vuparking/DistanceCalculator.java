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

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DistanceCalculator extends ArrayAdapter<CharSequence>
{
	static ParkingMap app;
	static Context context = app.getApplicationContext();
	LocationOverlay myLocation;
	private final static int TOTAL_LOT_NUM = 20;

	DistanceCalculator(Context c)
	{
		super(c, R.layout.distancemenu, 0);
	}
	
	public CharSequence[] getLotName()
	{
		ParkingDBManager lots = new ParkingDBManager();
		CharSequence[] parkingLots=lots.getAllLots(); //name of parking lot
	
		return parkingLots;
		
	}

	
	public CharSequence[] calculateDistance()
	{
		ArrayList<Double> d = new ArrayList<Double>();
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
            d.add((double)here.distanceTo(location)); //in meter
        }
        
    	CharSequence[] distances = new CharSequence[TOTAL_LOT_NUM];
    	d.toArray(distances);
        
        return distances;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) 
	{
			LayoutInflater inflater=LayoutInflater.from(context);
			View distancemenu=inflater.inflate(R.layout.distancemenu, parent, false);

			TextView name=(TextView)distancemenu.findViewById(R.id.name);
	    	CharSequence[] name_text = getLotName();
			name.setText(name_text[position]);
			
			TextView distance=(TextView)distancemenu.findViewById(R.id.distance);
	    	CharSequence[] distance_text = calculateDistance();
	    	distance.setText(distance_text[position]);

			return(distancemenu);
	}

}