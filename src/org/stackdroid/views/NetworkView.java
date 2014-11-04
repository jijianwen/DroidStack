package org.stackdroid.views;


import org.stackdroid.R;
import org.stackdroid.utils.CheckBoxWithView;
import org.stackdroid.utils.LinearLayoutWithView;
import org.stackdroid.utils.Network;
import org.stackdroid.utils.SubNetwork;
import org.stackdroid.utils.Utils;

import android.content.Context;
import android.text.method.NumberKeyListener;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
/*
public class NetworkView extends CheckBox {
    
    Network    net 	  = null;
    SubNetwork subnet = null;
    
    public NetworkView( Network net, SubNetwork subnet, OnClickListener listener, Context ctx ) {
	  super(ctx);
	  setOnClickListener(listener);
	  if(net.getSubNetworks().size()>0)
		  setText( net.getName( )+" ("+subnet.getAddress()+")" );
	  this.net = net;
	  this.subnet = subnet;
    }
    
    public Network getNetwork( ) { return net; }
    public SubNetwork getSubNetwork( ) { return subnet; }
}
*/
import android.widget.LinearLayout.LayoutParams;

public class NetworkView extends LinearLayout {
	private Network    				net    = null;
    private SubNetwork 				subnet = null;
    private LinearLayoutWithView	row    = null;
    private CheckBoxWithView		select = null;
    private EditText				netIP  = null;
    
    public NetworkView( Network net, 
					    SubNetwork subnet,
					    OnClickListener listener,
					    NumberKeyListener kl,
					    Context ctx ) {
    	super(ctx);
    	
    	this.net = net;
    	this.subnet = subnet;
    	setOrientation( LinearLayout.HORIZONTAL );
    	LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams( params1 );
		int padding = Utils.getDisplayPixel( ctx, 2 );
		setPadding( padding, padding, padding, padding );

		row = new LinearLayoutWithView( ctx, (NetworkView)this );
		row.setOrientation( LinearLayout.VERTICAL );
		LinearLayout.LayoutParams _params1
		    = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		row.setLayoutParams( _params1 );
		row.setBackgroundResource(R.drawable.rounded_corner_thin);
		//row.setOnClickListener(listener);
		select = new CheckBoxWithView(ctx, this);
		select.setOnClickListener(listener);
		select.setChecked(false);
		row.addView(select);
		if(subnet.getIPVersion().compareTo("4")==0) {
			netIP = new EditText(ctx);
			netIP.setEnabled(false);
			netIP.setKeyListener(kl);
			row.addView(netIP);
		}
		
		addView( row );
    }
    
    public EditText getNetworkIP( ) { return netIP; }
    public Network getNetwork( ) { return net; }
    public SubNetwork getSubNetwork( ) { return subnet; }
}