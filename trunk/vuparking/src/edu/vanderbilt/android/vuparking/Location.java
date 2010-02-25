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

// This file is written for determining users' current location and other location-based service.

package edu.vanderbilt.android.vuparking;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

class Location extends MyLocationOverlay {
	
	private Context mContext;
	
	public GeoPoint p = new GeoPoint(36141987, -86805900);   // Default location at vanderbilt.
	
	public boolean signal = false;    // if received new GPS signal.
	
	public Location (Context context, MapView mapView)
    {
	        super(context, mapView);
	        mContext = context;
	}
	
	public GeoPoint getCurLocation(){
		    // Update current location.
		    if ((p = getMyLocation()) != null) {
		    	signal = true;
		    }
		    else {
		    	p = new GeoPoint(36141987, -86805900);
		    	signal = false;
		    }
		    return p;
	}
	
	// Draw current location marker.
    @Override
	public boolean draw (Canvas canvas, MapView mv, boolean shadow, long when)
    {
            super.draw(canvas, mv, shadow);
            Point screenPts = new Point();
            mv.getProjection().toPixels(getCurLocation(), screenPts);
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.marker);
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-20, null);
            return true;
    }
}