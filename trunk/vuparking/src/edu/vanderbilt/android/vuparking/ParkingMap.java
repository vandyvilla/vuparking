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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
	private final static int num_zones = 6;
	private static MapView mv;
	public boolean[] zoneToDisplay;
	public boolean[] zoneOnMap = {false, false, false, false, false, false};
	public boolean[] settings = {false, false, false, false};

	private LocationOverlay myLocation;
	private int zoomLevel;

	private final static int MENU_ZONE = 0; 	//parking zone
	private final static int MENU_CURLOC = 1;	//current location
	private final static int MENU_REFRESH = 2;  //refresh parking information
	private final static int MENU_SETTINGS = 3; //settings
	
	private static final int HOUR_UP = 6;
	private static final int HOUR_DN = 16;

	// Called when creating the activity to show map view.
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		mv = (MapView) findViewById(R.id.map);
		mv.setBuiltInZoomControls(true);     // For zoom in/out.

		// Get user choices from main activity.
		Bundle bundle = this.getIntent().getExtras();
		zoneToDisplay = bundle.getBooleanArray("zoneChoices");

		// Set default map center and zoom level.
		zoomLevel = 15;
		GeoPoint p = new GeoPoint((int)(36141710),(int)(-86803669));
		setCenter(p, zoomLevel);

		myLocation = new LocationOverlay(this, mv);
		myLocation.enableMyLocation();      // Start receiving GPS signal.
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
			zoneOnMap[i] = zoneToDisplay[i];
		}
		if (settings[1] == true)     // Applying time policy.
		{
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.getTime().getHours();
			Toast.makeText(this, "Current hour is: " + calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
			if (zoneToDisplay[0] || zoneToDisplay[1] || zoneToDisplay[2] || zoneToDisplay[3])
			{
				if (hour >= HOUR_DN || hour <= HOUR_UP)
				{
					for (int j = 0; j < 4; j++)
					{
						zoneOnMap[j] = true;
					}
				}
			}
		}
		if (settings[3] == true)   // Add zone areas.
		{
			ZoneOverlay zOverlay = new ZoneOverlay(this);
			addOverlay(zOverlay);
		}
		MarkerOverlay mOverlay = new MarkerOverlay(this,this);
		addOverlay(mOverlay);
	}

	// Refresh all overlays on map.
	private void refreshOverlay() 
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
			break;
		case MENU_REFRESH:
			Toast.makeText(this, "Updating information with server...", Toast.LENGTH_SHORT).show();
			ParkingClient client = new ParkingClient(this);
			client.getResponse();
			break;
		case MENU_SETTINGS:
			showDialog(MENU_SETTINGS);
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
			builder.setMultiChoiceItems(zones, zoneToDisplay, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					zoneToDisplay[which] = isChecked;
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
					refreshOverlay();          // Refresh all the overlay items on map.
				}
			});
			break;
		case MENU_SETTINGS:
			CharSequence[] settingItem = {"Include full lots", "Apply time policy", "Show handicapped spots", "Display zone areas"};
			builder.setTitle("Settings");
			builder.setMultiChoiceItems(settingItem, settings, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
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
					refreshOverlay();          // Refresh all the overlay items on map.
				}
			});
			break;
		}
		return builder.create();
	}

	// Needed for MapActivity.
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
