package edu.vanderbilt.vuparking;

import java.util.List;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class showMap extends MapActivity {
	
	MapView mapView;
	MapController mc;
	GeoPoint p1, p2;
	
	class MapOverlay extends com.google.android.maps.Overlay
	{
		public boolean draw (Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			Point screenPts1 = new Point();
			Point screenPts2 = new Point();
			mapView.getProjection().toPixels(p1, screenPts1);
			mapView.getProjection().toPixels(p2, screenPts2);
			
			Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
	            canvas.drawBitmap(bmp1, screenPts1.x, screenPts1.y-20, null);
	            
	        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
	            canvas.drawBitmap(bmp2, screenPts2.x, screenPts2.y-20, null);
	        return true;
	     }
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mapView = (MapView) findViewById(R.id.mapView);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoomview);  
        View zoomView = mapView.getZoomControls();

        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mapView.displayZoomControls(true);
        //mapView.setStreetView(true);
        
        mc = mapView.getController();
        String coordinates[] = {"36.14171","-86.803669"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        
        String coordinates2[] = {"36.141987","-86.805900"};
        double lat1 = Double.parseDouble(coordinates2[0]);
        double lng1 = Double.parseDouble(coordinates2[1]);
        
        p1 = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
        mc.animateTo(p1);
        mc.setZoom(17);
         
        p2 = new GeoPoint((int)(lat1*1E6),(int)(lng1*1E6));
        
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        
        mapView.invalidate();
	}
	
	
	
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}


