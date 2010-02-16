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

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import edu.vanderbilt.android.vuparking.MapOverlay;

public class showMap extends MapActivity {
        
        MapView mv;
        MapController mc;
        GeoPoint p;
        int zoomLevel = 15;
        int userType = 0;
        
        class ZoneOverlay extends com.google.android.maps.Overlay
        {
                public boolean draw (Canvas canvas, MapView mv, boolean shadow, long when)
                {
                        super.draw(canvas, mv, shadow);
                        // Set up Paint style.
                        Paint paint1 = new Paint();
                        paint1.setColor(Color.YELLOW);
                        paint1.setAlpha(75);
                        paint1.setStyle(Paint.Style.FILL);
                        
                        Paint paint2 = new Paint();
                        paint2.setColor(Color.CYAN);
                        paint2.setAlpha(75);
                        paint2.setStyle(Paint.Style.FILL);
                        
                        Paint paint3 = new Paint();
                        paint3.setColor(Color.GREEN);
                        paint3.setAlpha(75);
                        paint3.setStyle(Paint.Style.FILL);  
                        
                        Paint paint5 = new Paint();
                        paint5.setColor(Color.GRAY);
                        paint5.setAlpha(75);
                        paint5.setStyle(Paint.Style.STROKE);
                        
                        Point pt = new Point();
                        // Add polygon overlay at specific zone. 
                        switch(userType){
                        case 1: // Zone 1
                        	 Path path1 = new Path();
                             mv.getProjection().toPixels(new GeoPoint(36143777,-86799595), pt);
                             path1.moveTo(pt.x, pt.y);
                             mv.getProjection().toPixels(new GeoPoint(36138440,-86800282), pt);
                             path1.lineTo(pt.x, pt.y);
                             mv.getProjection().toPixels(new GeoPoint(36138036,-86794420), pt);
                             path1.lineTo(pt.x, pt.y);
                             mv.getProjection().toPixels(new GeoPoint(36143188,-86793716), pt);
                             path1.lineTo(pt.x, pt.y);
                             canvas.drawPath(path1, paint1);
                             break;
                        case 2: // Zone 2
                        	 Path path2 = new Path();
                        	 mv.getProjection().toPixels(new GeoPoint(36148519,-86804420), pt);
                        	 path2.moveTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36145383,-86802253), pt);
                        	 path2.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36145470,-86799785), pt);
                        	 path2.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36148017,-86799549), pt);
                        	 path2.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36150235,-86801287), pt);
                        	 path2.lineTo(pt.x, pt.y);
                        	 canvas.drawPath(path2, paint2);
                        	 break;
                        case 3: // Zone 3
                        	 Path path3 = new Path();
                        	 mv.getProjection().toPixels(new GeoPoint(36138850,-86810750), pt);
                        	 path3.moveTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36138261,-86805257), pt);
                        	 path3.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36143182,-86804634), pt);
                        	 path3.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36143772,-86802639), pt);
                        	 path3.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36147653,-86806265), pt);
                        	 path3.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36146215,-86809591), pt);
                        	 path3.lineTo(pt.x, pt.y);
                        	 canvas.drawPath(path3, paint3);
                        	 break;
                        case 4: // Zone 4
                        	 break;
                        case 5: // Medical
                        	 Path path5 = new Path();
                        	 mv.getProjection().toPixels(new GeoPoint(36138261,-86805257), pt);
                        	 path5.moveTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36143182,-86804634), pt);
                        	 path5.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36143772,-86802639), pt);
                        	 path5.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36145470,-86799785), pt);
                        	 path5.lineTo(pt.x, pt.y);
                        	 mv.getProjection().toPixels(new GeoPoint(36137967,-86800987), pt);
                        	 path5.lineTo(pt.x, pt.y);
                        	 canvas.drawPath(path5, paint5);
                        	 break;
                        }
                        return true;
                }
        }
        
        @Override
        public void onCreate (Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.mapview);
                
                mv = (MapView) findViewById(R.id.map);
                mv.setBuiltInZoomControls(true);
                
                Bundle bundle = this.getIntent().getExtras();
                userType = bundle.getInt("User", 0);
                
                // Centered at Vanderbilt
		        p = new GeoPoint((int)(36141710),(int)(-86803669));
		        setCenter(p, zoomLevel);
                // loadMarker();
		        
		        // Show zone overlay with different color.
		        showZone();
		        
		        // Add marker overlay on top of map view.
                MapOverlay markerOverlay = new MapOverlay(this, this);
                mv.getOverlays().add(markerOverlay);

                //mapView.setStreetView(true);		        
		        mv.invalidate();
        }
        
        private void loadMarker(){
        	    GeoPoint p1, p2;
        	    ArrayList<GeoPoint> markers = new ArrayList<GeoPoint>();
                ParkingDBManager parkingDb = new ParkingDBManager();
                //parkingDb.clearDb();
                //parkingDb.static_init();
                ArrayList<ParkingLot> plots= parkingDb.queryParkingZone(userType);
                for (int i=0; i < plots.size(); i++)
                {
                	double lat = plots.get(i).getLatitude();
                	double lng = plots.get(i).getLongtitude();
                	GeoPoint p = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
    				markers.add(p);
                }
                
		        String coordinates[] = {"36.14171","-86.803669"};
		        double lat = Double.parseDouble(coordinates[0]);
		        double lng = Double.parseDouble(coordinates[1]);
		        
		        String coordinates2[] = {"36.141987","-86.805900"};
		        double lat1 = Double.parseDouble(coordinates2[0]);
		        double lng1 = Double.parseDouble(coordinates2[1]);
		        
		        p1 = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
		        p2 = new GeoPoint((int)(lat1*1E6),(int)(lng1*1E6));
		        zoomLevel = 15;
		        setCenter(p1, zoomLevel);
        }
        
        public boolean onCreateOptionsMenu (Menu menu){
        	    super.onCreateOptionsMenu(menu);
        	    menu.add(Menu.NONE, 0, Menu.NONE, "Select Zone");
        	    menu.add(Menu.NONE, 1, Menu.NONE, "");
			    return true;
        }
        
        public boolean onOptionsItemSelected (MenuItem item){
        	super.onOptionsItemSelected(item);
            switch (item.getItemId()) {
            case 0: 
            	showDialog(0);
            case 1:
            	break;
            }
        	return true;
        }
        
    	protected Dialog onCreateDialog(int id){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		CharSequence[] zones = {"zone1", "zone2", "zone3", "zone4", "Medical"};
    		boolean[] ZoneChoice = {false, false, false, false, false};
    		builder.setTitle("Select zone");
    		builder.setMultiChoiceItems(zones, ZoneChoice, new DialogInterface.OnMultiChoiceClickListener() {
				
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					
				}
    		});
    		
    		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				dialog.dismiss();
    				refreshOverlay();
    			}
    		});
    		builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {    			
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				dialog.dismiss();
    			}
    		});
    		return builder.create();
    	}
        
    	// Load multiple zones chosen by user.
    	private void refreshOverlay(){
			mv.getOverlays().clear();
			
    		
    	}
        private void setCenter(GeoPoint p, int zoom){
        	    mc = mv.getController();
    	        mc.animateTo(p);
    	        mc.setZoom(zoom);
        }
        
        private void showZone(){
	        	ZoneOverlay zOverlay = new ZoneOverlay();
		        mv.getOverlays().add(zOverlay);
        }
        
        @Override
        protected boolean isRouteDisplayed()
        {
                return false;
        }
}
