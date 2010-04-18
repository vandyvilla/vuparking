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
import android.graphics.drawable.Drawable;
import android.location.Location;
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
	public ArrayList<ParkingLot> lotMarkers;
	
	private final static int FULLSPOT = 0;
	private final static int DISABLE = 2;
	
	private final static int SEARCH_MODE = 1;
	GeoPoint p;
	Location dest = new Location("");
	
	private Integer[] markers = { R.drawable.yellow, R.drawable.lightblue, R.drawable.green, R.drawable.red, R.drawable.pink, R.drawable.orange};
	
	public MarkerOverlay(ParkingMap parkingmap, Context context) 
	{
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.parkinglot)));
		mContext = context;
		map = parkingmap;
		lotMarkers = new ArrayList<ParkingLot>();
	}
	
	// Get corresponding parking lots information for each selected zone from Database.
	public void refreshOverlay()
	{
		mOverlays.clear();
		lotMarkers.clear();
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
	
	public void drawMarker(int zone)
	{
		if (parkingDb.openDB())
		{
			ArrayList<ParkingLot> lots = parkingDb.queryParkingZone(zone);
			for (int j = 0; j < lots.size(); j++)
			{
				if (map.settings[FULLSPOT] == false && lots.get(j).getNumAvailabe() == 0)   // Filter out non-available spots
					continue;
				double lat = lots.get(j).getLatitude();
				double lng = lots.get(j).getLongtitude();
				GeoPoint p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
				// Filter the faraway parking lots in search mode.
				if (map.mode == SEARCH_MODE)
				{
					Location location = new Location("");
				    location.setLatitude(lat);
				    location.setLongitude(lng);
				    if (location.distanceTo(dest) > (double)map.radius)   continue;   // Jump to next marker due to distance.
				} 
				OverlayItem overlay = new OverlayItem(p, lots.get(j).getName(), Integer.toString(lots.get(j).getId()));
				// Display handicapped marker
				if (map.settings[DISABLE] == true)
				{
					if (lots.get(j).getNumDisable() != 0)
					{
						Drawable handiMarker = boundCenterBottom(mContext.getResources().getDrawable(R.drawable.wheel_chair));
						overlay.setMarker(handiMarker);
						addOverlay(overlay);
						lotMarkers.add(lots.get(j));
					}
				}
				else
				{
					int zoneNum = lots.get(j).getZone();
					Drawable marker = boundCenterBottom(mContext.getResources().getDrawable(markers[zoneNum]));
					//Toast.makeText(mContext, marker.getBounds().width() + marker.getBounds().height(), Toast.LENGTH_SHORT);
					overlay.setMarker(marker);
					addOverlay(overlay);
					lotMarkers.add(lots.get(j));
				}
			}
		}
		else Toast.makeText(mContext, "Database open failed!", Toast.LENGTH_LONG).show();
	}

	// Query database to add all parking markers on map.
	public void addAllOverlayItems() 
	{
		// If in search mode, add destination marker on map. 
		if (map.mode == SEARCH_MODE && parkingDb.openDB())
		{
			Building b = parkingDb.queryBuildingById(map.destID);
			if (b != null)
			{
				double lat = b.getLatitude();
				double lng = b.getLongtitude();
				p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
			    dest.setLatitude(b.getLatitude());
			    dest.setLongitude(b.getLongtitude());
				OverlayItem overlay = new OverlayItem(p, b.getName(), Integer.toString(b.getId()));
				Drawable destMarker = boundCenterBottom(mContext.getResources().getDrawable(R.drawable.dest));
				overlay.setMarker(destMarker);
				addOverlay(overlay);     // Don't add into lotMarkers to differentiate from ParkingLots.
			}		
		}
		for (int i = 0; i < num_areas; i++)
		{
			if (map.zoneOnMap[i])
			{
				drawMarker(i);
			}
		}
	}
	
	// Define the action when click on the marker to show detailed information.
	@Override
	protected boolean onTap(int index) 
	{
		OverlayItem item = mOverlays.get(index);
		String[] ZONES = {"Zone1", "Zone2", "Zone3", "Zone4", "Medical", "Visitor"};
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setIcon(R.drawable.garages);
		int Id = Integer.parseInt(item.getSnippet());
		
		if (parkingDb.openDB()) 
		{
			// When destination marker tapped
			if (map.mode == SEARCH_MODE && index == 0)  // first added.
			{
				Building b = parkingDb.queryBuildingById(Id);
				if (b != null)
				{
					dialog.setIcon(R.drawable.building);
					dialog.setMessage("Address: " + b.getAddress());
				}
			}
			else
			{
				ParkingLot p = parkingDb.queryParkingById(Id);
				if (map.settings[2] == true)
				{
					dialog.setMessage("Lot ID: " + Integer.toString(Id) +
					          "\nZone: " + ZONES[p.getZone()] +
					          "\nAddress: " + p.getAddress() +
					          "\nCapacity: " + Integer.toString(p.getNumSpot()) +
					          "\nHandicapped Spots: " + Integer.toString(p.getNumDisable()));
				}
				else
				{
					dialog.setMessage("Lot ID: " + Integer.toString(Id) +
					          "\nZone: " + ZONES[p.getZone()] +
					          "\nAddress: " + p.getAddress() +
					          "\nCapacity: " + Integer.toString(p.getNumSpot()) +
					          "\nAvailable Spots: " + Integer.toString(p.getNumAvailabe()));
				}
			}
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
	public int size() 
	{
		return mOverlays.size();
	}
}
