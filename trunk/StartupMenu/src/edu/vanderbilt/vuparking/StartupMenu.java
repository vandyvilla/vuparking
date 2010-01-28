package edu.vanderbilt.vuparking;

import android.app.Activity;
import android.os.Bundle;
/**
import android.view.View;
import android.widget.Button;
import android.app.Dialog;
*/
public class StartupMenu extends Activity {
    /** Called when the activity is first created. */
/**  
	private Button mVistor=(Button)findViewById(R.id.btnVisitor);
    private Button mPermit=(Button)findViewById(R.id.btnPermit);
	
    static final int DIALOG_PAUSED_ID = 0;
    static final int DIALOG_GAMEOVER_ID = 1;
    
   protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
        case DIALOG_PAUSED_ID:
            // do the work to define the pause Dialog
            break;
        case DIALOG_GAMEOVER_ID:
            // do the work to define the game over Dialog
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
*/   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    
  /**          
    mVistor.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

        }
    });
    
    mPermit.setOnClickListener(new View.OnClickListener() {
    	public void onClick(View v){
    		
    	}
    });
  */
    }
	
	
    
}

