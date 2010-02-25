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
    /** Called when the activity is first created. */
	public static Context appContext;
	
	private static final int TOTAL_ZONE=6;
	private static int[] userChoices=new int[TOTAL_ZONE];

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (appContext == null)
            appContext = getApplicationContext();

        setContentView(R.layout.main);
        
		Button buttonMember = (Button) findViewById(R.id.buttonMember);
		buttonMember.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				showDialog(0);
			}
		});

		Button buttonVisitor = (Button) findViewById(R.id.buttonVisitor);
		buttonVisitor.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				for (int i=0; i<TOTAL_ZONE-1; i++)
					userChoices[i]=-1;
				userChoices[TOTAL_ZONE-1]=TOTAL_ZONE-1;
				toMapView();
			}
		});
    }
    
    private void toMapView(){
    	Intent toMap = new Intent(this, showMap.class);
    	Bundle bundle = new Bundle();
    	bundle.putIntArray("User", userChoices);
    	toMap.putExtras(bundle);
    	startActivity(toMap);
    }
    
    private void saveUser(boolean[] checkedItems){
    	for (int i=0; i<TOTAL_ZONE-1; i++)
    	{
    		if (checkedItems[i])
    			userChoices[i]=i;
    		else
    			userChoices[i]=-1;
    	}
    	userChoices[TOTAL_ZONE-1]=-1;
    	
    }
    
	protected Dialog onCreateDialog(int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] zones = {"zone1", "zone2", "zone3", "zone4", "Medical"};
		builder.setTitle("Please pick your zones");
		final boolean[] checkedItems=new boolean[5];
		builder.setMultiChoiceItems(zones, checkedItems, new DialogInterface.OnMultiChoiceClickListener() 
		{
			
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				checkedItems[which]=isChecked;

			}
		});
		
		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveUser(checkedItems);
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
}