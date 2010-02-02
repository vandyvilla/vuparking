package edu.vanderbilt.android.vuparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.*;

public class Main extends Activity {
    /** Called when the activity is first created. */
	//public static Context appContext;
	int UserChoice = 0;    // Default: Visitor
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (appContext == null)
        //    appContext = getApplicationContext();
        if ((new File("data/data/edu.vanderbilt.android.vuparking/user.txt")).exists()) {
        	try {
        		BufferedReader in = new BufferedReader(new FileReader("data/data/edu.vanderbilt.android.vuparking/user.txt"));
        	    UserChoice = Integer.parseInt(in.readLine());
        	    in.close();
        	    toMapView();
        	}catch (Exception e){
        		System.err.println("Error: " + e.getMessage());
            }
        }
        
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
				UserChoice = 0;
				saveUser(UserChoice);
				toMapView();
			}
		});
    }
    
    private void toMapView(){
    	Intent toMap = new Intent(this, showMap.class);
    	Bundle bundle = new Bundle();
    	bundle.putInt("User", UserChoice);
    	toMap.putExtras(bundle);
    	startActivity(toMap);
    }
    
    private void saveUser(int user){
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter("data/data/edu.vanderbilt.android.vuparking/user.txt"));
    		out.write(Integer.toString(UserChoice));   // Default overwrite the file.
    		out.close();
    	}catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
	protected Dialog onCreateDialog(int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] zones = {"zone1", "zone2", "zone3", "zone4", "Medical"};
		builder.setTitle("Please pick a zone");
		builder.setSingleChoiceItems(zones, -1, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				UserChoice = which;
				saveUser(UserChoice);
				toMapView();
			}
		});
		builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		return builder.create();
	}
}