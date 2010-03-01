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

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class ParkingMap extends MapActivity {
        
        private static MapView mv;
        public boolean[] zoneToDisplay;
        
        private LocationOverlay myLocation;
        private int zoomLevel;
        
        // Called when creating the activity to show map view.
        @Override
        public void onCreate (Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
            setContentView(R.layout.mapview);
            
            mv = (MapView) findViewById(R.id.map);
            mv.setBuiltInZoomControls(true);     // For zoom in/out.
            
            // Get user choices from main activity.
            Bundle bundle = this.getIntent().getExtras();
            zoneToDisplay = bundle.getBooleanArray("ZoneChoice");
            
            // Set default map center and zoom level.
            zoomLevel = 15;
            GeoPoint p = new GeoPoint((int)(36141710),(int)(-86803669));
	        setCenter(p, zoomLevel);
	        
	        myLocation = new LocationOverlay(this, mv);
            myLocation.enableMyLocation();      // Start receiving GPS signal.
            
	        addAllOverlay();
            //mapView.setStreetView(true);
        }
        
        // Add overlay object onto map view.
        private void addOverlay(Overlay object) {
        	mv.getOverlays().add(object);
        	mv.invalidate();
        }
        
        // Add zone color, markers onto map view.
        private void addAllOverlay() {
        	ZoneOverlay zOverlay = new ZoneOverlay(this);
		    addOverlay(zOverlay);
		    
            MarkerOverlay mOverlay = new MarkerOverlay(this,this);
            addOverlay(mOverlay);
        }
        
        // Refresh all overlays on map.
        private void refreshOverlay() {
        	mv.getOverlays().clear();
        	addAllOverlay();
        }
    	
    	// Set map center and zoom level.
        private void setCenter(GeoPoint p, int zoom){
            mv.getController().animateTo(p);
            mv.getController().setZoom(zoom);
        }
        
        private final static int MENU_ZONE = 0;
        private final static int MENU_CURLOC = 1;
        
        // Define the menu items.
        public boolean onCreateOptionsMenu (Menu menu){
        	super.onCreateOptionsMenu(menu);
        	menu.add(Menu.NONE, MENU_ZONE, Menu.NONE, "Select Zone");
        	menu.add(Menu.NONE, MENU_CURLOC, Menu.NONE, "Show My Position");
			return true;
        }
                
        // Define the action when one of menu items selected.
        public boolean onOptionsItemSelected (MenuItem item){
        	super.onOptionsItemSelected(item);
            switch (item.getItemId()) {
            case MENU_ZONE:
            	showDialog(MENU_ZONE);    // pop up zone choice dialog.
            	break;
            case MENU_CURLOC:
            	addOverlay(myLocation);
            	setCenter(myLocation.getCurLocation(), zoomLevel);
            	if (myLocation.signal == false)
            		Toast.makeText(this, "Displaying your default location", Toast.LENGTH_SHORT).show();
            	break;
            }
        	return true;
        }
        
        // Define the pop up dialog for choosing zones.
    	protected Dialog onCreateDialog(int id){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		CharSequence[] zones = {"Zone1", "Zone2", "Zone3", "Zone4", "Medical", "Visitor"};
    		builder.setTitle("Select parking area");
    		builder.setMultiChoiceItems(zones, zoneToDisplay, new DialogInterface.OnMultiChoiceClickListener() {				
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					zoneToDisplay[which] = isChecked;
				}
    		});
    		
    		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			    refreshOverlay();          // Refresh all the overlay items on map.
    			}
    		});
    		builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			}
    		});
    		return builder.create();
    	}
       
        // Needed for MapActivity.
        @Override
        protected boolean isRouteDisplayed()
        {
                return false;
        }
}
