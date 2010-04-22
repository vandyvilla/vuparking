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

// This file is written for Google map view activity and managing all overlays on the map.

package edu.vanderbilt.android.vuparking;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.vanderbilt.android.vuparking.network.ParkingClient;

public class ParkingMap extends MapActivity 
{
	private final static int num_zones = 6;   // Number of zones.
	private static MapView mv;
	public boolean[] zoneOnMap = {false, false, false, false, false, false}; // Record zones shown on map.
	public boolean[] settings = {false, false, false, false};    // Record user setting choices.

	public LocationOverlay myLocation;
	private int zoomLevel;

	private final static int MENU_ZONE = 0; 	//parking zone
	private final static int MENU_CURLOC = 1;	//current location
	private final static int MENU_REFRESH = 2;  //refresh parking information
	private final static int MENU_SETTINGS = 3; //settings
	private final static int MENU_DISCAL = 4;   //calculate distances
	private final static int MENU_SEARCH = 5;   //search parking lots
	private final static int MENU_RADIUS = 6;   //set search radius

	private final static int TIMEPOLICY = 1;
	private final static int ZONECOLOR = 3;

	public int hour_up = 6;      // Morning limit of time policy.
	public int hour_dn = 16;     // Afternoon limit of time policy.

	public DistanceAdapter adapter;
	public MarkerOverlay mOverlay;
	public Location curLoc;
	
	public int destID = 0;    // Default: don't specify.
	public int Range[] = {200, 300, 500, 800, 1000};
	public int radius = Range[2]; // Default search radius: 500 m.
	private final static int NORMAL_MODE = 0;
	private final static int SEARCH_MODE = 1;
	public int mode = NORMAL_MODE;

	// Called when creating the activity to show map view.
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		mv = (MapView) findViewById(R.id.map);
		mv.setBuiltInZoomControls(true);     // For zoom in/out.

		// Set default map center and zoom level.
		zoomLevel = 15;
		GeoPoint p = new GeoPoint((int)(36141710),(int)(-86803669));
		setCenter(p, zoomLevel);

		myLocation = new LocationOverlay(this, mv);
		myLocation.enableMyLocation();      // Start receiving GPS signal.
		curLoc = new Location("");

		mOverlay = new MarkerOverlay(this, this);
		adapter = new DistanceAdapter(this, R.layout.list_item, this);

		addAllOverlay();
	}

	// Add overlay object onto map view.
	private void addOverlay(Overlay object) 
	{
		mv.getOverlays().add(object);
		mv.invalidate();
	}

	// Add zone color, markers onto map view.
	private void addAllOverlay() 
	{
		for (int i = 0; i < num_zones; i++)
		{
			zoneOnMap[i] = Main.zoneChoices[i];
		}
		if (settings[TIMEPOLICY] == true)     // Applying time policy.
		{
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.getTime().getHours();
			if (Main.zoneChoices[0] || Main.zoneChoices[1] || Main.zoneChoices[2] || Main.zoneChoices[3])
			{
				if (hour >= hour_dn || hour <= hour_up)
				{
					for (int j = 0; j < num_zones-2; j++)
					{
						zoneOnMap[j] = true;    // Display all four zones if not working hours.
					}
				}
			}
		}
		if (settings[ZONECOLOR] == true)   // Add zone area colors.
		{
			ZoneOverlay zOverlay = new ZoneOverlay(this);
			addOverlay(zOverlay);
		}
		mOverlay.refreshOverlay();
		addOverlay(mOverlay);
		refreshAdapter();
	}

	// Criteria for ordering parking lots.
	private final Comparator<ParkingLot> distanceComp = new Comparator<ParkingLot>()
	{
		public int compare(ParkingLot p1, ParkingLot p2) {
			Location loc1 = new Location("");
			loc1.setLatitude(p1.getLatitude());
			loc1.setLongitude(p1.getLongtitude());
			Location loc2 = new Location("");
			loc2.setLatitude(p2.getLatitude());
			loc2.setLongitude(p2.getLongtitude());
			if (curLoc.distanceTo(loc1) < curLoc.distanceTo(loc2))
			{
				return -1;
			}
			else if (curLoc.distanceTo(loc1) == curLoc.distanceTo(loc2)) 
			{
				return 0;
			}
			else return 1;
		}
	};

	// Refresh distance calculation list
	private void refreshAdapter()
	{
		if (mOverlay.lotMarkers.size() > 0)
		{
			// Get current location.
			curLoc.setLatitude(myLocation.getCurLocation().getLatitudeE6()*1.0E-6);
			curLoc.setLongitude(myLocation.getCurLocation().getLongitudeE6()*1.0E-6);
			Collections.sort(mOverlay.lotMarkers, distanceComp);
			// Copy ordered parking lots.
			adapter.clear();
			for (int i = 0; i < mOverlay.lotMarkers.size(); i++)
			{
				adapter.add(mOverlay.lotMarkers.get(i));
			}
			//adapter.notifyDataSetChanged();
		}
	}

	// Refresh all overlays on map.
	public void refreshOverlay() 
	{		
		mv.getOverlays().clear();
		addAllOverlay();
	}

	// Set map center and zoom level.
	private void setCenter(GeoPoint p, int zoom)
	{
		mv.getController().animateTo(p);
		mv.getController().setZoom(zoom);
	}

	// Define the menu items.
	public boolean onCreateOptionsMenu (Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_ZONE, Menu.NONE, "Select Zone").setIcon(R.drawable.menu_zone);
		menu.add(Menu.NONE, MENU_CURLOC, Menu.NONE, "Show My Location").setIcon(R.drawable.menu_loc);
		menu.add(Menu.NONE, MENU_REFRESH, Menu.NONE, "Refresh").setIcon(R.drawable.menu_refresh);
		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, "Settings").setIcon(R.drawable.menu_setting);
		menu.add(Menu.NONE, MENU_DISCAL, Menu.NONE, "Distances").setIcon(R.drawable.menu_calculate);
		menu.add(Menu.NONE, MENU_SEARCH, Menu.NONE, "Select Destination").setIcon(R.drawable.menu_search);
		return true;
	}

	// Define the action when one of menu items selected.
	public boolean onOptionsItemSelected (MenuItem item)
	{
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) 
		{
		case MENU_ZONE:
			showDialog(MENU_ZONE);    // pop up zone choice dialog.
			break;
		case MENU_CURLOC:
			addOverlay(myLocation);
			setCenter(myLocation.getCurLocation(), zoomLevel);
			if (myLocation.signal == false)
				Toast.makeText(this, "Displaying your default location", Toast.LENGTH_SHORT).show();
			refreshAdapter();        // Since adapter.sort is not working...
			break;
		case MENU_REFRESH:
			Toast.makeText(this, "Updating information with server...", Toast.LENGTH_SHORT).show();
			ParkingClient client = new ParkingClient(this);
			client.getResponse();
			refreshOverlay();   // Add/remove parking lots based on available numbers.
			break;
		case MENU_SETTINGS:
			showDialog(MENU_SETTINGS);
			break;
		case MENU_DISCAL:
			showDialog(MENU_DISCAL);
			break;
		case MENU_SEARCH:
			showDialog(MENU_SEARCH);
			break;
		}
		return true;
	}

	// Define the pop up dialog for choosing zones.
	protected Dialog onCreateDialog(int id)
	{		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id)
		{
		case MENU_ZONE:
			CharSequence[] zones = {"Zone1", "Zone2", "Zone3", "Zone4", "Medical", "Visitor"};
			builder.setTitle("Please pick your zones");
			builder.setMultiChoiceItems(zones, Main.zoneChoices, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int which, boolean isChecked) 
				{
					Main.zoneChoices[which] = isChecked;
				}
			});

			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			});

			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					mode = NORMAL_MODE;
					dialog.dismiss();
					refreshOverlay();          // Refresh all the overlay items on map.
				}
			});
			break;
		case MENU_SETTINGS:
			CharSequence[] settingItem = {"Include full occupied lots", "Show after hour parking", "Show handicapped spots", "Display zone areas"};
			builder.setTitle("Settings");
			builder.setMultiChoiceItems(settingItem, settings, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int which, boolean isChecked) 
				{
					settings[which] = isChecked;
				}
			});

			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			});

			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
					if (settings[TIMEPOLICY] == true)
					{
						String TimePolicy = "Between 4pm and 7am next day, you can park at either zone from ZONE1 to ZONE4 if you have a permit.";
						Toast.makeText(getBaseContext(), TimePolicy, Toast.LENGTH_LONG).show(); 
					}
					refreshOverlay();          // Refresh based on user settings.
				}
			});
			break;
		case MENU_DISCAL:
			builder.setTitle("Distances from your current location");
			builder.setAdapter(adapter, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
					markLot(which);
				}});

			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			});
			break;
		case MENU_SEARCH:
			CharSequence[] buildings = {"I don't want to specify", "Admissions Office", "Baker Building", "Children's Hospital", "Commons Center", "Featheringill & Jacob Hall",
					"Graduate School", "Ingram Center", "Institue for Software Integrate Systems", "Law School", "Light Hall", "MC East South Tower", 
					"Medical Center North", "Medical Research Bldg", "Olin Hall", "Owen School of Management", "Peabody College", "Rand Hall", "Sarratt Commons",
					"Stevenson Center", "University Club", "Vanderbilt Eye Institute", "Vanderbilt Clinic", "Veterans Hospital", "Wyatt Center"};
			builder.setTitle("Select your destination");
			builder.setSingleChoiceItems(buildings, destID, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) {
					destID = which;
				}
			});

			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			});
			
			builder.setNeutralButton("Set Search Radius", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					setRadius();
				}
			});

			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
			        mode = destID == 0? NORMAL_MODE:SEARCH_MODE;
					refreshOverlay();          // Refresh based on user settings.
					markBuilding(destID);
				}
			});
			break;
        case MENU_RADIUS:
        	builder.setTitle("Set Search Radius");
			CharSequence[] ranges = {"Very near (200 m)", "Near (300 m)", "Medium (500 m)", "Far (800 m)", " Very Far (1 km)"}; 
			builder.setSingleChoiceItems(ranges, radius, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) {
					radius = Range[which];
				}
			});
			builder.setNegativeButton("Back", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			});

			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
					mode = destID == 0? NORMAL_MODE:SEARCH_MODE;
					refreshOverlay();
					markBuilding(destID);
				}
			});
			break;
		}
		
		return builder.create();
	}
	
	// Pop up "set radius" dialog
	private void setRadius()
	{
		showDialog(MENU_RADIUS);
	}
	
	// Highlight selected parking spot
	public void markLot(int index)
	{
		ParkingLot p = adapter.getItem(index);
		if (p != null)
		{
			GeoPoint pt = new GeoPoint((int)(p.getLatitude()*1.0E6), (int)(p.getLongtitude()*1.0E6));
			setCenter(pt, zoomLevel+2);
		}
	}
	
	// Highlight selected destination building
	public void markBuilding(int index)
	{
		if (index != 0)
		{
			ParkingDBManager ParkingDb = new ParkingDBManager();
			Building b = ParkingDb.queryBuildingById(index);
			if (b != null)
			{
				GeoPoint pt = new GeoPoint((int)(b.getLatitude()*1.0E6), (int)(b.getLongtitude()*1.0E6));
				setCenter(pt, zoomLevel+1);
			}
		}
	}

	// Needed for MapActivity.
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
