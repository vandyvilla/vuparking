package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.os.Bundle;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class showMap extends MapActivity {
        
        MapView mv;
        MapController mc;
        GeoPoint p1, p2;
        int zoomLevel = 17;
        ArrayList<GeoPoint> markers = new ArrayList<GeoPoint>();
        int userType = 0;
        
        class MapOverlay extends com.google.android.maps.Overlay
        {
                public boolean draw (Canvas canvas, MapView mv, boolean shadow, long when)
                {
                        super.draw(canvas, mv, shadow);
                        for (ListIterator<GeoPoint> it = markers.listIterator(); it.hasNext();)
                        {
		                        Point screenPts = new Point();
		                        //Point screenPts2 = new Point();
		                        GeoPoint p = it.next();
		                        mv.getProjection().toPixels(p, screenPts);
		                        //mv.getProjection().toPixels(p2, screenPts2);
		                        
		                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.parking_purple);       
		                        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-20, null);
		                    
		                        //Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
		                        //canvas.drawBitmap(bmp2, screenPts2.x, screenPts2.y-20, null);
                        }
                        /*Point screenPts1 = new Point();
                        Point screenPts2 = new Point();
                        mv.getProjection().toPixels(p1, screenPts1);
                        mv.getProjection().toPixels(p2, screenPts2);
                        
                        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
                        canvas.drawBitmap(bmp1, screenPts1.x, screenPts1.y-20, null);
                    
                        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
                        canvas.drawBitmap(bmp2, screenPts2.x, screenPts2.y-20, null);*/
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
                /*LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);
                View zoomView = mv.getZoomControls();

                zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
                                   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                mv.displayZoomControls(true);*/
                //mapView.setStreetView(true);
                Bundle bundle = this.getIntent().getExtras();
                userType = bundle.getInt("User", 0);
                   
		        loadMarker();
		        showMarker();
		        mv.invalidate();
        }
        
        private void loadMarker(){
		        String coordinates[] = {"36.14171","-86.803669"};
		        double lat = Double.parseDouble(coordinates[0]);
		        double lng = Double.parseDouble(coordinates[1]);
		        
		        String coordinates2[] = {"36.141987","-86.805900"};
		        double lat1 = Double.parseDouble(coordinates2[0]);
		        double lng1 = Double.parseDouble(coordinates2[1]);
		        
		        p1 = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
		        p2 = new GeoPoint((int)(lat1*1E6),(int)(lng1*1E6));
		        if (userType == 0) 
		        {
		        	markers.add(p1);
		        }
		        else
		        {
		        	markers.add(p2);
		        }
		        setCenter(p1, zoomLevel);
        }
        
        private void setCenter(GeoPoint p, int zoom){
        	    mc = mv.getController();
    	        mc.animateTo(p);
    	        mc.setZoom(zoom);
        }
        
        private void showMarker(){
	        	MapOverlay mapOverlay = new MapOverlay();
		        List<Overlay> listOfOverlays = mv.getOverlays();
		        listOfOverlays.clear();
		        listOfOverlays.add(mapOverlay);
        }
        
        
        @Override
        protected boolean isRouteDisplayed()
        {
                return false;
        }
}
