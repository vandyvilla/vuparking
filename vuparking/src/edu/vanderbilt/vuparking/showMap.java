package edu.vanderbilt.vuparking;

import java.util.List;
import java.util.ListIterator;

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
	List<GeoPoint> markers;
	
	class MapOverlay extends com.google.android.maps.Overlay
	{
		public boolean draw (Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			for (ListIterator<GeoPoint> it = markers.listIterator(); it.hasNext();)
			{
				Point screenPts = new Point();
				GeoPoint p = it.next();
				mapView.getProjection().toPixels(p, screenPts);
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.parking);        
	            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-20, null);				
			}
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
        
        loadMarker();
        setCenter(p1, 17);
        showMarker();
        mapView.invalidate();
	}
	
	
	
	public void setCenter(GeoPoint p, int zoomLevel){
		mc = mapView.getController();
		mc.animateTo(p);
		mc.setZoom(zoomLevel);
	}
	
	public void loadMarker(){     // For future remote server interaction. 
		
		String coordinates[] = {"36.14171","-86.803669"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        
        String coordinates2[] = {"36.141987","-86.805900"};
        double lat1 = Double.parseDouble(coordinates2[0]);
        double lng1 = Double.parseDouble(coordinates2[1]);
        
        p1 = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
        p2 = new GeoPoint((int)(lat1*1E6),(int)(lng1*1E6));
        
        markers.add(p1);
        markers.add(p2);
	}
	
	public void showMarker(){
        
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}


