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

// This file is written for creating and managing overlay items shown on map, including retrieving data from DB, adding
// overlay onto mapView. 

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.android.vuparking.ParkingLot;

public class MapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ParkingDBManager parkingDb = new ParkingDBManager();
	private ArrayList<ParkingLot> plots;
	
	private Context mContext;
	private MapView mv;
	
	public MapOverlay(showMap map, Context context) {
		super(boundCenterBottom(map.getResources().getDrawable(R.drawable.parking)));
		mContext = context;
		
		mv = (MapView) map.findViewById(R.id.map);
		
		// Get corresponding parking information from DB.
		plots = parkingDb.queryParkingZone(map.userType);
        for (int i=0; i < plots.size(); i++)
        {
        	double lat = plots.get(i).getLatitude();
        	double lng = plots.get(i).getLongtitude();
        	GeoPoint p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
        	OverlayItem overlay = new OverlayItem(p, plots.get(i).getName(), plots.get(i).getAddress());
        	overlay.setMarker(boundCenterBottom(map.getResources().getDrawable(R.drawable.parking)));
        	addOverlay(overlay);
        }
		
		// TODO Auto-generated constructor stub
	}
	
	// Add overlay item.
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  
	  //Dialog dialog = new Dialog();
	  //dialog.setContentView(R.layout.parkinglot);
	  
	  // Display detailed parking lot information.	 
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage("Address:   " + item.getSnippet() + "\nCapacity:  "
			            + Integer.toString(plots.get(index).getNumSpot()) + "\nAvailable: "
			            + Integer.toString(plots.get(index).getNumAvailabe()) + "\nDisable:    "
			            + Integer.toString(plots.get(index).getNumDisable()));
      dialog.setIcon(R.drawable.garages);
      
	  dialog.setPositiveButton("Direction", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// ToDirection();
				Toast.makeText(mContext, "Service currently unavailable!", Toast.LENGTH_LONG).show();
			}
		});
	  dialog.setNeutralButton("Refresh", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method
				// Contact remote server to get the latest information.
				dialog.dismiss();
				parkingDb.sync_server();
			}
		});
	  dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	  dialog.show();
	  return true;
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}

}
