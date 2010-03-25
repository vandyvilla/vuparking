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

public class VuparkingServiceServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;	
	
	// Servlet one-time init, prepare data store.
	public void init() throws ServletException
	{
		super.init();
		// Load parking info data to server data store
		ArrayList<ParkingInfo> infoCollection = new ArrayList<ParkingInfo>();
		loadData(infoCollection);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			for (int i = 0; i < infoCollection.size(); i++)
			{
				//Key key = KeyFactory.createKey(ParkingInfo.class.getName(), infoCollection.get(i).getID());
				//infoCollection.get(i).setKey(key);
				pm.makePersistent(infoCollection.get(i));
			}
		}finally
		{
			pm.close();
		}
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
        infoCollection.add(new ParkingInfo(9, GARAGE, ZONE3, "Kensington Garage", "", 36.147163,-86.807613, 20, 0, 20, 0));
        infoCollection.add(new ParkingInfo(10, LOT, ZONE3, "Parmer Field House", "", 36.145984,-86.808558, 20, 0, 20, 0));        
        infoCollection.add(new ParkingInfo(11, LOT, ZONE4, "Blakemore Ave", "2140 Belcourt Ave", 36.137401, -86.804236, 20, 0, 20, 0));
        infoCollection.add(new ParkingInfo(12, LOT, ZONE4, "Real Estate", "2189-2199 Belcourt Ave", 36.136956,-86.805073, 20, 0, 20, 0));
        infoCollection.add(new ParkingInfo(13, LOT, MEDICAL, "Natchez field", "2964 Dudley Ave", 36.141120, -86.812059, 100, 20, 80, 0));
        infoCollection.add(new ParkingInfo(14, GARAGE, MEDICAL, "Central garage", "2401 Highland Ave", 36.141924,-86.805909, 100, 20, 80, 0));
        infoCollection.add(new ParkingInfo(15, GARAGE, MEDICAL, "South garage", "2376 Children's Way", 36.139454,-86.804375, 100, 20, 80, 0));
        infoCollection.add(new ParkingInfo(16, GARAGE, MEDICAL, "Medical center East South Tower", "1211 Medical Center Dr", 36.141672, -86.800920, 100, 20, 80, 0));
        infoCollection.add(new ParkingInfo(17, GARAGE, MEDICAL, "Vanderbilt Clinic garage", "1499 21st Ave S", 36.140381, -86.800899, 100, 20, 80, 0));
        infoCollection.add(new ParkingInfo(18, GARAGE, VISITOR, "South garage", "1598 24th Ave S", 36.139324, -86.804879, 50, 10, 40, 0.75));
        infoCollection.add(new ParkingInfo(19, LOT, VISITOR, "Planet beach", "2099 Scarritt Pl", 36.145770, -86.799182, 20, 0, 20, 0.75));
        infoCollection.add(new ParkingInfo(20, LOT, VISITOR, "Jess Neely Dr", "", 36.143333,-86.807935, 20, 0, 20, 0.75));
	}
	
	// Helper function for getting Object by ID from data store.
	private ParkingInfo getParkingInfo(Long id)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ParkingInfo p, copy = null;
		try {
			//Key k = KeyFactory.createKey(ParkingInfo.class.getName(), id);
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
		response.setContentType("text/plain");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		// Using JDO query
		String query = "select from " + ParkingInfo.class.getName();
	    List<ParkingInfo> info = (List<ParkingInfo>) pm.newQuery(query).execute();
	    
	    JSONArray allInfo = new JSONArray();
		for (int i = 0; i < info.size(); i++)
		{
			JSONObject parkingInfo = new JSONObject();
			try {
				parkingInfo.put("id", info.get(i).getID());
				parkingInfo.put("name", info.get(i).getName());
				parkingInfo.put("available", info.get(i).getAvailable());
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			allInfo.put(parkingInfo);
		}
		// Send response back to client.
		try {
			response.getWriter().println(allInfo.toString());
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// Handle both post requests from web (modify) and android phone (query).	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		Long id = Long.parseLong(request.getParameter("lotid"));
		String method = request.getParameter("method");
		
		// Return the available number of specific parking lot in GAE datastore.
		if (id > 0 && method.contentEquals("query"))
		{
			// Later return JSON object
			response.getWriter().print("Receive Query request for id: " + id);
		    ParkingInfo p = getParkingInfo(id);
			if (p != null)
			{
				response.getWriter().println(" Available number: " + p.getAvailable());
			}
		}
		// Modify the available number of specific parking lot.
		else if (id > 0 && method.contentEquals("modify"))
		{
			int num = Integer.parseInt(request.getParameter("num_spot"));
			ParkingInfo p = getParkingInfo(id);
			if (p != null && num >= 0 && num <= p.getCapacity())  // Validity check
			{
			    p.setAvailable(num);
			    PersistenceManager pm = PMF.get().getPersistenceManager();
			    try {
			    	pm.makePersistent(p);
			    }finally
			    {
			    	pm.close();
			    }
				response.getWriter().println("Modify available information for id: " + id + " Spot num: " + p.getAvailable());
			}
			else 
			{
				response.getWriter().println("Invalid number (may exceed capacity): " + num);
			}
		}
		else 
		{
			response.getWriter().println("Error for post request");
		}
	}
}
