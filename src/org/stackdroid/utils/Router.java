package org.stackdroid.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.stackdroid.parse.ParseException;

public class Router {
    private String name;
    private String ID;
    private Network gw;
    private String tenantID;
	private Vector<Network> interfaces;
	private String status;
    
    public Router( String name, String ID, String status, String tenantID, Network gwNet, Vector<Network> ifaces ) {
	  	this.name       = name;
	  	this.ID         = ID;
	  	this.gw         = gwNet;
	  	this.tenantID   = tenantID;
		this.interfaces = ifaces;
		this.status		= status;
    }

    @Override
    public String 	toString( )				{ return name; }
	public String   getStatus( )			{ return status; }
    public String  	getName( ) 				{ return name; }
    public String  	getID( ) 				{ return ID; }
    public String  	getTenantID( ) 			{ return tenantID; }
    public void 	setGateway( Network n ) { gw = n; }
	public Network 	getGateway( ) 			{ return gw; }
	public boolean 	hasGateway( ) 			{ return (gw!=null); }
	public Vector<Network> getInterfaces( ) { return interfaces; }
	public boolean hasInterfaces( )         { return interfaces.size()!=0; }
	public int getNumInterfaces( )          { return interfaces.size(); }

    public static Vector<Router> parseMultiple ( String jsonBuf, HashMap<String,Network> netMap) throws ParseException {
    	Vector<Router> VR = new Vector<Router>( );
    
    	try {
       		JSONObject jsonObject = new JSONObject( jsonBuf );
       		JSONArray routers = (JSONArray)jsonObject.getJSONArray("routers");
       		for(int i =0; i<routers.length(); ++i) {
       			JSONObject routerObj = routers.getJSONObject(i);
       			String name = routerObj.has("name") ? routerObj.getString("name") : "N/A";
       			String ID = routerObj.getString("id");
				String status = routerObj.getString("status");
       			String tenantID = routerObj.getString("tenant_id");
				Network gwNet = null;

					if (routerObj.has("external_gateway_info")) {
						if ( !routerObj.isNull("external_gateway_info") ) {
							//Log.d("ROUTER", "external_gateway_info=" + routerObj.getString("external_gateway_info"));
							JSONObject gw = routerObj.getJSONObject("external_gateway_info");
							gwNet = netMap.get(gw.getString("network_id"));
						}
					}

				//Log.d("ROUTER", "external_gateway_info=" + routerObj.getString("external_gateway_info"));
       			VR.add(new Router(name, ID, status, tenantID, gwNet, null ));
       		}
       		return VR;
        } catch(org.json.JSONException je) {
	   		throw new ParseException( je.getMessage( ) );
	   	}
    }

	public static Router parseSingle ( String jsonBuf, HashMap<String,Network> netMap) throws ParseException {
		try {
			JSONObject jsonObject = new JSONObject( jsonBuf );
			JSONObject routerObj = jsonObject.getJSONObject("router");
			String name = routerObj.has("name") ? routerObj.getString("name") : "N/A";
			String status = routerObj.has("status") ? routerObj.getString("status") : "N/A";
			String ID = routerObj.getString("id");
				String tenantID = routerObj.getString("tenant_id");
				Network gwNet = null;
				if (routerObj.has("external_gateway_info")) {
					if ( !routerObj.isNull("external_gateway_info") ) {
						JSONObject gw = routerObj.getJSONObject("external_gateway_info");
						gwNet = netMap.get(gw.getString("network_id"));
					}
				}
				return new Router(name, ID, status, tenantID, gwNet, null );
		} catch(org.json.JSONException je) {
			throw new ParseException( je.getMessage( ) );
		}
	}
}
