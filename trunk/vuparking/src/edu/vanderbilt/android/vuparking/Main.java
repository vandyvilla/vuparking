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

// This file is written for Main activity, which is called when the application is launched. 

package edu.vanderbilt.android.vuparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	public static Context appContext;
	
	private static final int TOTAL_ZONE=6; //Zone1, Zone2, Zone3, Zone4, Medical Center, Visitor
	//Record user's zone choices. 
	private static boolean[] zoneChoices=new boolean[TOTAL_ZONE];

	//Entry point of the application
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get the application context to pass to other activities
        if (appContext == null)
            appContext = getApplicationContext();

        setContentView(R.layout.main);
        
		Button buttonMember = (Button) findViewById(R.id.buttonMember);
		buttonMember.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				showDialog(0); //pop up the dialog for choosing zones
			}
		});

		Button buttonVisitor = (Button) findViewById(R.id.buttonVisitor);
		buttonVisitor.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				for (int i=0; i<TOTAL_ZONE-1; i++)
					zoneChoices[i]=false;
				zoneChoices[TOTAL_ZONE-1]=true;
				toMapView();
			}
		});
    }
    
    //Go to Google Map view by starting a new activity
    private void toMapView(){
    	Intent toMap = new Intent(this, ParkingMap.class);
    	Bundle bundle = new Bundle();
    	bundle.putBooleanArray("ZoneChoices", zoneChoices);
    	toMap.putExtras(bundle);
    	startActivity(toMap);
    }
    
    //Save user's zone choices
    private void saveZoneChoices(boolean[] checkedItems){
    	for (int i=0; i<TOTAL_ZONE-1; i++)
    	{
    		if (checkedItems[i])
    			zoneChoices[i]=true;
    		else
    			zoneChoices[i]=false;
    	}
    	zoneChoices[TOTAL_ZONE-1]=false;
    	
    }
    
    //Create the dialog for selecting zones
	protected Dialog onCreateDialog(int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] zones = {"Zone1", "Zone2", "Zone3", "Zone4", "Medical"};
		builder.setTitle("Please pick your zones");
		final boolean[] checkedItems=new boolean[TOTAL_ZONE-1];
		builder.setMultiChoiceItems(zones, checkedItems, new DialogInterface.OnMultiChoiceClickListener() 
		{
			
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				checkedItems[which]=isChecked;

			}
		});
		
		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveZoneChoices(checkedItems);
				toMapView();
			}
		});
		builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return builder.create();
	}
	
	//Check the dialog items according to user's choices last time
	protected void onPrepareDialog(Dialog dialog){
		for (int i=0; i<TOTAL_ZONE-1; i++){
			((AlertDialog) dialog).getListView().setItemChecked(i, zoneChoices[i]);
		}
	}
}
