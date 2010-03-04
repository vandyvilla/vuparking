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

// This file is written for covering zone areas with different colors.

package edu.vanderbilt.android.vuparking;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class ZoneOverlay extends com.google.android.maps.Overlay 
{
	private int num_areas = 6;
	ArrayList<ArrayList<coordinate>> ZonePt = new ArrayList<ArrayList<coordinate>>();
	ParkingMap map;

	// Define structure for coordinate.
	class coordinate
	{
		int x, y;
		coordinate(int a, int b) { x = a; y = b;}
		int getX() { return x;}
		int getY() { return y;}
	}

	public ZoneOverlay(ParkingMap parkingmap) 
	{
		map = parkingmap;
	}

	// Add all area vertices.
	public void loadZoneTopo() 
	{
		// Zone 1;
		ArrayList<coordinate> pt1 = new ArrayList<coordinate>();
		pt1.add(new coordinate(36144044,-86799781));
		pt1.add(new coordinate(36137493,-86800640));
		pt1.add(new coordinate(36137043,-86795619));
		pt1.add(new coordinate(36143385,-86794417));
		ZonePt.add(pt1);

		// Zone 2;
		ArrayList<coordinate> pt2 = new ArrayList<coordinate>();
		pt2.add(new coordinate(36145950,-86799610));
		pt2.add(new coordinate(36148029,-86799309));
		pt2.add(new coordinate(36150524,-86800940));
		pt2.add(new coordinate(36147890,-86806347));
		pt2.add(new coordinate(36144044,-86803730));
		ZonePt.add(pt2);

		// Zone 3;
		ArrayList<coordinate> pt3 = new ArrayList<coordinate>();
		pt3.add(new coordinate(36138741,-86810896));
		pt3.add(new coordinate(36143004,-86810081));
		pt3.add(new coordinate(36146123,-86809824));
		pt3.add(new coordinate(36147890,-86806347));
		pt3.add(new coordinate(36144044,-86803730));
		pt3.add(new coordinate(36138013,-86804802));
		ZonePt.add(pt3);

		// Zone 4;
		ArrayList<coordinate> pt4 = new ArrayList<coordinate>();
		ZonePt.add(pt4);

		// Medical;
		ArrayList<coordinate> pt5 = new ArrayList<coordinate>();
		pt5.add(new coordinate(36138013,-86804802));
		pt5.add(new coordinate(36144044,-86803730));
		pt5.add(new coordinate(36145950,-86799610));
		pt5.add(new coordinate(36137597,-86800640));
		ZonePt.add(pt5);

		// Visitor;
		ArrayList<coordinate> pt6 = new ArrayList<coordinate>();
		ZonePt.add(pt6);
	}

	// Draw polygon with color on specific zones.
	public boolean draw (Canvas canvas, MapView mv, boolean shadow, long when) 
	{
		super.draw(canvas, mv, shadow);
		int opaque = 50; // 0: transparent, 255: black
		int[] color = {Color.YELLOW, Color.CYAN, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.TRANSPARENT};

		loadZoneTopo();

		for (int i=0; i < num_areas; i++) 
			if (map.zoneToDisplay[i]) 
			{
				Path path = new Path();
				if (ZonePt.get(i).size() != 0)
				{
					Point pt = new Point();
					mv.getProjection().toPixels(new GeoPoint(ZonePt.get(i).get(0).getX(),ZonePt.get(i).get(0).getY()), pt);
					path.moveTo(pt.x, pt.y);

					for (int j = 1; j < ZonePt.get(i).size(); j++)
					{
						mv.getProjection().toPixels(new GeoPoint(ZonePt.get(i).get(j).getX(),ZonePt.get(i).get(j).getY()), pt);
						path.lineTo(pt.x, pt.y);
					}
				}
				Paint paint = new Paint();
				paint.setColor(color[i]);
				paint.setAlpha(opaque);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawPath(path, paint);
			}

		return true;
	}
}