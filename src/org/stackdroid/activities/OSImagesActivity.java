package org.stackdroid.activities;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.res.Configuration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import java.util.Iterator;
import java.util.Vector;
import org.stackdroid.comm.OSClient;
import org.stackdroid.comm.NotFoundException;
import org.stackdroid.parse.ParseUtils;
import org.stackdroid.parse.ParseException;
import org.stackdroid.utils.CustomProgressDialog;
import org.stackdroid.R;
import org.stackdroid.utils.User;
import org.stackdroid.utils.Utils;
import org.stackdroid.utils.OSImage;
import org.stackdroid.views.OSImageView;
import org.stackdroid.utils.TextViewNamed;
import org.stackdroid.utils.ImageButtonNamed;
import org.stackdroid.utils.LinearLayoutNamed;

import android.graphics.Typeface;

public class OSImagesActivity extends Activity implements OnClickListener {
    
    private Vector<OSImage> OS;
    private CustomProgressDialog progressDialogWaitStop = null;
    private String ID = null;
    private String NAME = null;
    User U = null;
    
    /**
     *
     *
     *
     */
    public boolean onCreateOptionsMenu( Menu menu ) {
        
        super.onCreateOptionsMenu( menu );
        
        int order = Menu.FIRST;
        int GROUP = 0;
                
        menu.add(GROUP, 0, order++, getString(R.string.MENUHELP)    ).setIcon(android.R.drawable.ic_menu_help);
        menu.add(GROUP, 1, order++, getString(R.string.MENUUPDATE) ).setIcon(R.drawable.ic_menu_refresh);
        return true;
    }
    
     public boolean onOptionsItemSelected( MenuItem item ) {
	 
        int id = item.getItemId();     
        
        if( id == Menu.FIRST-1 ) {
            Utils.alert( getString(R.string.NOTIMPLEMENTED) ,this );
            return true;
        }
        
        if( id == Menu.FIRST ) { 
	    //            Utils.customAlert(  );
	    if(U==null) {
		Utils.alert("An error occurred recovering User from sdcard. Try to go back and return to this activity.", this);
	    } else {
		progressDialogWaitStop.show();
		(new AsyncTaskOSListImages()).execute( );
		return true;
	    }
        }
	return super.onOptionsItemSelected( item );
    }


    //__________________________________________________________________________________
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView( R.layout.osimagelist );
	
    	String selectedUser = Utils.getStringPreference("SELECTEDUSER", "", this);
    	try {
    		U = User.fromFileID( selectedUser, Utils.getStringPreference("FILESDIR","",this), this );
    	} catch(RuntimeException re) {
    		Utils.alert("OSImagesActivity: "+re.getMessage(), this );
    		return;
    	}
	
    	if(selectedUser.length()!=0)
    		((TextView)findViewById(R.id.selected_user)).setText(getString(R.string.SELECTEDUSER)+": "+U.getUserName() + " (" + U.getTenantName() + ")"); 
    	else
    		((TextView)findViewById(R.id.selected_user)).setText(getString(R.string.SELECTEDUSER)+": "+getString(R.string.NONE)); 
	   
    	progressDialogWaitStop = new CustomProgressDialog( this, ProgressDialog.STYLE_SPINNER );
        progressDialogWaitStop.setMessage( getString(R.string.PLEASEWAITCONNECTING) );
        this.update( );
    }
    
    /**
    *
    *
    *
    *
    */
    private void update( ) {
    	progressDialogWaitStop.show();
    	(new AsyncTaskOSListImages()).execute( );
    }
    
    /**
     *
     *
     *
     *
     */
/*    @Override
    public void onResume( ) {
    	super.onResume( );
	}*/
 
    /**
     *
     *
     *
     *
     */
/*    @Override
    public void onStart( ) {
    	super.onStart( );
    }*/
  
    /**
     *
     *
     *
     *
     */
 /*   @Override
    public void onPause( ) {
    	super.onPause( );
    }*/

    /**
     *
     *
     *
     *
     */
/*    @Override
    public void onRestart( ) {
	super.onRestart( );
	//	Log.d("OSIMAGE.ONRESTART", "OSIMAGE.ONRESTART");
    }*/

 
    /**
     *
     *
     *
     *
     */
 /*   @Override
    public void onStop( ) {
	super.onStop( );
	//	Log.d("OSIMAGE.ONSTOP", "OSIMAGE.ONSTOP");
    }*/

    /**
     *
     *
     *
     *
     */
    @Override
    public void onDestroy( ) {
      super.onDestroy( );
      //	Log.d("OSIMAGE.ONDESTROY", "OSIMAGE.ONDESTROY");
      progressDialogWaitStop.dismiss();
    }
   

    /**
     *
     *
     *
     *
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	setContentView(R.layout.osimagelist);
    	this.refreshView( );
    }


    //__________________________________________________________________________________
    public void onClick( View v ) { 

	if(v instanceof ImageButtonNamed) {

	    if(((ImageButtonNamed)v).getType( ) == ImageButtonNamed.BUTTON_DELETE_IMAGE ) {
		ID = ((ImageButtonNamed)v).getOSImageView( ).getOSImage().getID();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage( "Are you sure to delete this image ?" );
		builder.setCancelable(false);
	    
		DialogInterface.OnClickListener yesHandler = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    deleteGlanceImage( ID );
			}
		    };

		DialogInterface.OnClickListener noHandler = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel( );
			}
		    };

		builder.setPositiveButton("Yes", yesHandler );
		builder.setNegativeButton("No", noHandler );
            
		AlertDialog alert = builder.create();
		alert.getWindow( ).setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,  
					    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		alert.show();
	    }
	    
	    if(((ImageButtonNamed)v).getType( ) == ImageButtonNamed.BUTTON_LAUNCH_IMAGE ) {
		ID = ((ImageButtonNamed)v).getOSImageView( ).getOSImage().getID();
		NAME = ((ImageButtonNamed)v).getOSImageView( ).getOSImage().getName();
		Class<?> c = (Class<?>)ImageLaunchActivity.class;
		Intent I = new Intent( OSImagesActivity.this, c );
		I.putExtra( "IMAGEID", ID );
	    I.putExtra("IMAGENAME", NAME);
		startActivity( I );
	    }
	}


	if(v instanceof OSImageView || v instanceof TextViewNamed || v instanceof LinearLayoutNamed ) {
	    OSImage osi = null;
	    if(v instanceof OSImageView) {
		//		Utils.alert("Toccato OSImageView", this);
		osi = ((OSImageView)v).getOSImage();
	    }
	    if(v instanceof TextViewNamed) {
		//		Utils.alert("Toccato TextViewNames: "+((TextViewNamed)v).getText().toString(), this);
		osi = ((TextViewNamed)v).getOSImageView().getOSImage();
	    }
	    if(v instanceof LinearLayoutNamed) {
		//Utils.alert("Toccato TextViewNames: "+((TextViewNamed)v).getText().toString(), this);
		osi = ((LinearLayoutNamed)v).getOSImageView().getOSImage();
	    }
	    TextView tv1 = new TextView(this);
	    tv1.setText("Image name:");
	    tv1.setTypeface( null, Typeface.BOLD );
	    TextView tv2 = new TextView(this);
	    tv2.setText(osi.getName());
	    TextView tv3 = new TextView(this);
	    tv3.setText("Status:");
	    tv3.setTypeface( null, Typeface.BOLD );
	    TextView tv4 = new TextView(this);
	    tv4.setText(osi.getStatus());
	    TextView tv5 = new TextView(this);
	    tv5.setText("Size: ");
	    tv5.setTypeface( null, Typeface.BOLD );
	    TextView tv6 = new TextView(this);
	    tv6.setText(""+osi.getSize() + " (" + osi.getSize()/1048576 + " MB)");
	    TextView tv7 = new TextView(this);
	    tv7.setText("Public:");
	    tv7.setTypeface( null, Typeface.BOLD );
	    TextView tv8 = new TextView(this);
	    tv8.setText(""+osi.isPublic());
	    TextView tv9 = new TextView(this);
	    tv9.setText("Format:");
	    tv9.setTypeface( null, Typeface.BOLD );
	    TextView tv10 = new TextView(this);
	    tv10.setText(osi.getFormat());
	    TextView tv11 = new TextView( this );
	    tv11.setText("ID:");
	    tv11.setTypeface( null, Typeface.BOLD );
	    TextView tv12 = new TextView( this );
	    tv12.setText(osi.getID());
	    TextView tv13 = new TextView( this );
	    tv13.setText("Minimum Disk:");
	    tv13.setTypeface( null, Typeface.BOLD );
	    TextView tv14 = new TextView( this );
	    tv14.setText(osi.getMinDISK( ) + " GB");
	    TextView tv15 = new TextView( this );
	    tv15.setText("Minimum RAM:");
	    tv15.setTypeface( null, Typeface.BOLD );
	    TextView tv16 = new TextView( this );
	    tv16.setText(osi.getMinRAM( ) + " MB");
	    ScrollView sv = new ScrollView(this);
	    LinearLayout.LayoutParams lp 
		= new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
	    sv.setLayoutParams( lp );
	    LinearLayout l = new LinearLayout(this);
	    l.setLayoutParams( lp );
	    l.setOrientation( LinearLayout.VERTICAL );
	    int paddingPixel = 8;
	    float density = Utils.getDisplayDensity( this );
	    int paddingDp = (int)(paddingPixel * density);
	    l.setPadding(paddingDp, 0, 0, 0);
	    l.addView( tv1 );
	    tv2.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv2 );
	    l.addView( tv3 );
	    tv4.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv4 );
	    l.addView( tv5 );
	    tv6.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv6 );
	    l.addView( tv7 );
	    tv8.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv8 );
	    l.addView( tv9 );
	    tv10.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv10 );
	    l.addView( tv11 );
	    tv12.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv12 );
	    l.addView( tv13 );
	    tv14.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv14 );
	    l.addView( tv15 );
	    tv16.setPadding(2*paddingDp, 0, 0, 0);
	    l.addView( tv16 );
	    //sv.setOrientation( LinearLayout.VERTICAL );
	    sv.addView(l);
	    String name;
	    if(osi.getName().length()>=16)
		name = osi.getName().substring(0,14) + "..";
	    else
		name = osi.getName();
	    Utils.alertInfo( sv, "Image information: " + name, this );
	    
	}

	
    }

    private  void deleteGlanceImage( String ID ) {
    	progressDialogWaitStop.show();
    	AsyncTaskOSDelete task = new AsyncTaskOSDelete();
    	task.execute( ID );
    }

    //__________________________________________________________________________________
    private void refreshView( ) {
    if(OS.size()==0) {
    	Utils.alert(getString(R.string.NOIMAGEAAVAIL), this);
    	return;
    }
	Iterator<OSImage> sit = OS.iterator();
	((LinearLayout)findViewById(R.id.osimagesLayout)).removeAllViews();
	while( sit.hasNext( )) {
	    OSImage os = sit.next();
	    ((LinearLayout)findViewById(R.id.osimagesLayout)).addView( new OSImageView(os, this) );
	    View space = new View( this );
	    space.setMinimumHeight(10);
	    ((LinearLayout)findViewById(R.id.osimagesLayout)).addView( space );
	    ((LinearLayout)findViewById( R.id.osimagesLayout) ).setGravity( Gravity.CENTER_HORIZONTAL );
	}
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    protected class AsyncTaskOSDelete extends AsyncTask<String, String, String>
    {
      private  String   errorMessage  =  null;
	  private  boolean  hasError      =  false;
	  private  String   jsonBuf       = null;
	
	  protected String doInBackground(String... u ) 
	  {
	    String imagetodel = u[0];
	    OSClient osc = OSClient.getInstance(U);
	    
	    try {
		  osc.deleteGlanceImage( imagetodel );
		  jsonBuf = osc.requestImages( );
	    } catch(RuntimeException e) {
		  errorMessage = "Runtime: " + e.getMessage();
		  hasError = true;
	    } catch(NotFoundException nfe) {
		  errorMessage = "NotFound: " + nfe.getMessage();
		  hasError = true;
	    }
	    return "";
	}
	
	@Override
	    protected void onPostExecute( String result ) {
	    super.onPostExecute(result);
	    
 	    if(hasError) {
 		Utils.alert( errorMessage, OSImagesActivity.this );
		OSImagesActivity.this.progressDialogWaitStop.dismiss( );
 		return;
 	    }
	    
	    try {
		OSImagesActivity.this.OS = ParseUtils.parseImages(jsonBuf);
		OSImagesActivity.this.refreshView( );
	    } catch(ParseException pe) {
		Utils.alert("OSImagesActivity.AsyncTaskOSListImages.onPostExecute: " + pe.getMessage( ), 
			    	OSImagesActivity.this);
	    }

	    OSImagesActivity.this.update( );
	}
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    protected class AsyncTaskOSListImages extends AsyncTask<Void, String, String>
    {
     	private  String   errorMessage  =  null;
	private  boolean  hasError      =  false;
	private  String   jsonBuf       = null;

	@Override
	protected String doInBackground(Void ... voids ) 
	{
	    OSClient osc = OSClient.getInstance(U);

	    try {
	  	  jsonBuf = osc.requestImages( );
	    } catch(Exception e) {
		  errorMessage = e.getMessage();
		  hasError = true;
		  return "";
	    }
	    
	    return jsonBuf;
	}
	
	@Override
	    protected void onPreExecute() {
	    super.onPreExecute();
	    
	    //downloading_image_list = true;
	}
	
	@Override
	protected void onPostExecute( String result ) {
	    super.onPostExecute(result);
	    
 	    if(hasError) {
 		Utils.alert( errorMessage, OSImagesActivity.this );
 		//downloading_image_list = false;
 		OSImagesActivity.this.progressDialogWaitStop.dismiss( );
 		return;
 	    }
	    
	    //downloading_image_list = false; // questo non va spostato da qui a
	    try {
		  OSImagesActivity.this.OS = ParseUtils.parseImages(jsonBuf);
		  OSImagesActivity.this.refreshView( );
	    } catch(ParseException pe) {
		  Utils.alert("OSImagesActivity.AsyncTaskOSListImages.onPostExecute: " + pe.getMessage( ), 
			          OSImagesActivity.this);
	    }
	    //Utils.putLongPreference("LASTIMAGELIST_TIMESTAMP", Utils.now( ), OSImagesActivity.this );
	    OSImagesActivity.this.progressDialogWaitStop.dismiss( );
	    //OSImagesActivity.this.refreshView( jsonBuf );
	}
    }
}
