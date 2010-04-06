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

// This file is written to handle the calculation, sort and display of distances between current location and parking lots shown on map.

package edu.vanderbilt.android.vuparking;

import java.text.DecimalFormat;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DistanceAdapter extends ArrayAdapter<ParkingLot>
{
        public ParkingMap map;
        Context mContext;
        
        public DistanceAdapter(Context context, int textViewResourceId, ParkingMap parkingmap)
        {
        	super(context, textViewResourceId);
            mContext = context;
            map = parkingmap;
        }
        
        // Construct list item view.
        public View getView(int position, View convertView, ViewGroup parent)
        {
        	View v = convertView;
            if (v == null) 
            {
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            
            ParkingLot p = getItem(position);
            if (p != null) 
            {
            	TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null) 
                {
                	tt.setText("Name: "+p.getName());
                }
                // Get parking lot location.
                Location location = new Location("");
                location.setLatitude(p.getLatitude());
                location.setLongitude(p.getLongtitude());
                if(bt != null)
                {
                	DecimalFormat df = new DecimalFormat("##.###");
                	bt.setText("Distance (mile): "+ df.format(map.curLoc.distanceTo(location)* 0.0006));
                }
            }
            return v;
        }
}

