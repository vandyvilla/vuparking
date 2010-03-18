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

// This file is written for contacting server for latest parking information.

package edu.vanderbilt.android.vuparking.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import edu.vanderbilt.android.vuparking.ParkingDBManager;

import android.content.Context;
import android.widget.Toast;

public class ParkingClient {
	
	private Context mContext;
	// IP representation of local machine
	private final String myIP = "10.0.2.2";
	private HttpClient client;
	
	public ParkingClient(Context context)
	{
		mContext = context;
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		client = new DefaultHttpClient(httpParameters);
	}
	
	// Get parking lots information from the server
	public void getResponse()
	{	
		String getUrl = "http://" + myIP + ":8888/vuparkingservice";
		HttpGet request = new HttpGet(getUrl);
		// Display error msg when error occurs
		
		updateDB();
		
		
	}
	
	// Update database
	public void updateDB()
	{
		
	}
	
	// Converting http response to string.
	public String convertToString(HttpResponse response)
	{

	}
	

}