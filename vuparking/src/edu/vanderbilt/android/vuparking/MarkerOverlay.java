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

// This file is written for querying parking lot information from DB and displaying marker on map.

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem> 
{

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ParkingDBManager parkingDb = new ParkingDBManager();    // Access local database.
	private int num_areas = 6;
	private Context mContext;
	ParkingMap map;

	public MarkerOverlay(ParkingMap parkingmap, Context context) 
	{
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.parking)));
		mContext = context;
		map = parkingmap;
		// Get corresponding parking lots information for each selected zone from Database.
		addAllOverlayItems();
	}

	// Add overlay item.
	public void addOverlay(OverlayItem overlay) 
	{
		mOverlays.add(overlay);
		populate();
	}

	// Called by populate().
	@Override
	protected OverlayItem createItem(int i) 
	{
		return mOverlays.get(i);
	}

	// Query database to add all parking markers on map.
	public void addAllOverlayItems() 
	{
		if (parkingDb.openDB()) {
			for (int i = 0; i < num_areas; i++)
				if (map.zoneToDisplay[i]) 
				{
					ArrayList<ParkingLot> lots = parkingDb.queryParkingZone(i);
					for (int j = 0; j < lots.size(); j++)
					{
						double lat = lots.get(j).getLatitude();
						double lng = lots.get(j).getLongtitude();
						GeoPoint p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
						OverlayItem overlay = new OverlayItem(p, lots.get(j).getName(), Integer.toString(lots.get(j).getId()));
						overlay.setMarker(boundCenterBottom(mContext.getResources().getDrawable(R.drawable.garage_marker)));
						addOverlay(overlay);
					}
				}	
		}
		else Toast.makeText(mContext, "Database open failed!", Toast.LENGTH_LONG).show();
	}

	// Define the action when click on the marker to show detailed information.
	@Override
	protected boolean onTap(int index) 
	{
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setIcon(R.drawable.garages);
		dialog.setTitle(item.getTitle());
		int lotId = Integer.parseInt(item.getSnippet());

		if (parkingDb.openDB()) 
		{
			ParkingLot p = parkingDb.queryParkingById(lotId);
			dialog.setMessage("Address: " + p.getAddress() + 
					"\nCapacity: " + Integer.toString(p.getNumSpot()) +
					"\nAvailable No.: " + Integer.toString(p.getNumAvailabe()));
		}
		else 
			Toast.makeText(mContext, "Database open failed!", Toast.LENGTH_LONG).show();

		dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
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
