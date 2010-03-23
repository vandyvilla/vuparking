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

// This file is written for defining servlet service action.

package edu.vanderbilt.parkingserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VuparkingServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	
	// Servlet one-time init, prepare data store.
	public void init() throws ServletException
	{
		
	}
	
	// Same as loadData in client ParkingDBManager.
	private void loadData(ArrayList<ParkingInfo> infoCollection)
	{
		// Type field.
		final int GARAGE = 0;
		final int LOT = 1;
		// Zone field.
		final int ZONE1 = 0;
		final int ZONE2 = 1;
		final int ZONE3 = 2;
		final int ZONE4 = 3;
		final int MEDICAL = 4;
		final int VISITOR = 5;
		
		infoCollection.add(new ParkingInfo(1, LOT, ZONE1, "Magnolia lawn", "", 36.142773, -86.798635, 20, 5, 15, 0));
		infoCollection.add(new ParkingInfo(2, LOT, ZONE1, "English Language Center", "1226 18th Ave S", 36.141802, -86.795685, 50, 10, 40, 0));
		infoCollection.add(new ParkingInfo(3, LOT, ZONE1, "Wyatt Center", "", 36.139834, -86.798586, 50, 10, 40, 0));
		infoCollection.add(new ParkingInfo(4, LOT, ZONE2, "Baker building", "118 21st Ave S", 36.149334, -86.799635, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(5, LOT, ZONE2, "West End Ave", "", 36.149472, -86.802467, 20, 5, 15, 0));
		infoCollection.add(new ParkingInfo(6, LOT, ZONE2, "Law School", "Scarritt Pl", 36.148222, -86.800073, 50, 10, 40, 0));
		infoCollection.add(new ParkingInfo(7, LOT, ZONE3, "Olin hall", "2400 Highland Ave", 36.142937, -86.805072, 20, 0, 20, 0));
		infoCollection.add(new ParkingInfo(8, GARAGE, ZONE3, "25th Ave Staff garage", "2499 Highland Ave", 36.142365, -86.806124, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(9, LOT, ZONE4, "Blakemore Ave", "2140 Belcourt Ave", 36.137401, -86.804236, 20, 0, 20, 0));
		infoCollection.add(new ParkingInfo(10, LOT, MEDICAL, "Natchez field", "2964 Dudley Ave", 36.141120, -86.812059, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(11, GARAGE, MEDICAL, "Central garage", "2401 Highland Ave", 36.141924,-86.805909, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(12, GARAGE, MEDICAL, "South garage", "2376 Children's Way", 36.139454,-86.804375, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(13, GARAGE, MEDICAL, "Medical center East South Tower", "1211 Medical Center Dr", 36.141672, -86.800920, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(14, GARAGE, MEDICAL, "Vanderbilt Clinic garage", "1499 21st Ave S", 36.140381, -86.800899, 100, 20, 80, 0));
		infoCollection.add(new ParkingInfo(15, GARAGE, VISITOR, "South garage", "1598 24th Ave S", 36.139324, -86.804879, 50, 10, 40, 0.75));
		infoCollection.add(new ParkingInfo(16, GARAGE, VISITOR, "Wesley Place Garage", "1901-2035 Scarritt Pl", 36.145736,-86.79871, 20, 0, 20, 0.75));
	}
	
	// Helper function for getting Object by ID from data store.
	private ParkingInfo getParkingInfo(Long id)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ParkingInfo p, copy = null;
		try {
			p = pm.getObjectById(ParkingInfo.class, id);
			copy = pm.detachCopy(p);
		} finally
		{
			pm.close();
		}
		return copy;
	}
	
	// Handles GET request to display all parking lot information in data store (send back to client).
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest reqest, HttpServletResponse response)
	{
		
	}

	// Handle both post requests from web (modify) and android phone (query).	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		
	}

}
