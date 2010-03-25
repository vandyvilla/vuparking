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

// This file is written for testing Parking Client to access the web server and update the number. 

package edu.vanderbilt.android.vuparking.test;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import android.test.ActivityInstrumentationTestCase2;
import edu.vanderbilt.android.vuparking.Main;
import edu.vanderbilt.android.vuparking.ParkingDBManager;
import edu.vanderbilt.android.vuparking.ParkingLot;
import edu.vanderbilt.android.vuparking.network.ParkingClient;

public class ClientTest extends ActivityInstrumentationTestCase2<Main> 
{

	Main app;
	
	public ClientTest() 
	{
		super("edu.vanderbilt.android.vuparking", Main.class);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		// Start app so that the private database could be accessed. 
		app = (Main) getActivity();
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	//Test server responds the request with not null response. 
	public void testResponse() throws ClientProtocolException, IOException 
	{
		HttpClient client;
		HttpParams httpParameters = new BasicHttpParams();
		client = new DefaultHttpClient(httpParameters);
		String getUrl = "http://vuparking.appspot.com/vuparkingservice";
		HttpGet request = new HttpGet(getUrl);
		HttpResponse response;
		response = client.execute(request);
		assertNotNull(response);
	}
	
	//Test server response status indicating if successful. 
	public void testStatus() throws ClientProtocolException, IOException
	{
		HttpClient client;
		HttpParams httpParameters = new BasicHttpParams();
		client = new DefaultHttpClient(httpParameters);
		String getUrl = "http://vuparking.appspot.com/vuparkingservice";
		HttpGet request = new HttpGet(getUrl);
		HttpResponse response = client.execute(request);
		int status = response.getStatusLine().getStatusCode();
		assertEquals(status, 200);
	}
	
	//Test number of Parking Lots (number of JSON objects) returned by server. 
	public void testRepSize() throws ClientProtocolException, IOException, JSONException
	{
		ParkingClient parkingClient= new ParkingClient(app.getApplicationContext());
		HttpClient client;
		HttpParams httpParameters = new BasicHttpParams();
		client = new DefaultHttpClient(httpParameters);
		String getUrl = "http://vuparking.appspot.com/vuparkingservice";
		HttpGet request = new HttpGet(getUrl);
		HttpResponse response = client.execute(request);		
		
		String result = parkingClient.convertToString(response);
		JSONArray servResp = new JSONArray(result);
		assertEquals(servResp.length(), 20);
	}
	
	//Test available parking spot number before and after updating. 
	public void testAvailableNum()
	{
		ParkingDBManager manager = new ParkingDBManager();		
		int id=1;
		if (manager.openDB())
		{	
			ParkingLot p = manager.queryParkingById(id);
			assertEquals(p.getNumAvailabe(), 5);
		}
		
		// Get updated data from server. 
		ParkingClient parkingClient= new ParkingClient(app.getApplicationContext());
		parkingClient.getResponse();
		if (manager.openDB())
		{	
			ParkingLot p = manager.queryParkingById(id);
			assertEquals(p.getNumAvailabe(), 15);
		}		
		
	}
}
	