package org.stackdroid.utils;

import android.widget.Button;
import android.content.Context;

import org.stackdroid.views.FloatingIPView;
import org.stackdroid.views.ListSecGroupView;
import org.stackdroid.views.NetworkListView;
import org.stackdroid.views.RouterPortView;
import org.stackdroid.views.RouterView;
import org.stackdroid.views.RuleView;
import org.stackdroid.views.SecGroupView;
import org.stackdroid.views.UserView;
import org.stackdroid.views.ServerView;
import org.stackdroid.views.OSImageView;
import org.stackdroid.views.NetworkView;
import org.stackdroid.views.VolumeView;

public class ButtonWithView extends Button implements GetView {

	private RuleView relatedRuleView = null;
    private UserView relatedUserView = null;
    private ServerView relatedServerView = null;
    private VolumeView relatedVolumeView = null;
    private OSImageView relatedOSImageView = null;
    private NetworkView relatedNetworkView = null;
    private SecGroupView relatedSecGroupView = null;
    private FloatingIPView relatedFloatingIPView = null;
    private NetworkListView relatedNetworkListView = null;
    private ListSecGroupView relatedListSecGroupView = null;
    private RouterView relatedRouterView = null;
    private RouterPortView relatedRouterPortView   = null;

    public ButtonWithView( Context ctx, RouterView rv ) {
        super( ctx );
        relatedRouterView = rv;
    }

    public ButtonWithView( Context ctx, UserView uv ) {
    	super( ctx );
    	relatedUserView = uv;
    }

    public ButtonWithView( Context ctx, ServerView sve ) {
    	super( ctx );
    	relatedServerView = sve;
    }

    public ButtonWithView( Context ctx, OSImageView iv ) {
	super( ctx );
	relatedOSImageView = iv;
    }

    public ButtonWithView( Context ctx, NetworkView iv ) {
	super( ctx );
	relatedNetworkView = iv;
    }

    public ButtonWithView( Context ctx, FloatingIPView iv ) {
	super( ctx );
	relatedFloatingIPView = iv;
    }

    public ButtonWithView( Context ctx, ListSecGroupView sv ) {
    	super( ctx );
    	relatedListSecGroupView = sv;
     }

    public ButtonWithView( Context ctx, NetworkListView nv ) {
    	super( ctx );
    	relatedNetworkListView = nv;
     }
    public ButtonWithView( Context ctx, RouterPortView rv ) {
        super( ctx );
        relatedRouterPortView = rv;
    }
    
    @Override
	public UserView getUserView( ) { return relatedUserView; }
    @Override
	public RuleView getRuleView( ) { return relatedRuleView; }
    @Override
	public ServerView getServerView( ) { return relatedServerView; }
    @Override
	public OSImageView getOSImageView( ) { return relatedOSImageView; }
    @Override
	public NetworkView getNetworkView( ) { return relatedNetworkView; }
    @Override
	public SecGroupView getSecGroupView( ) { return relatedSecGroupView; }
    @Override
	public ListSecGroupView getListSecGroupView( ) { return relatedListSecGroupView; }
    @Override
	public FloatingIPView getFloatingIPView( ) { return relatedFloatingIPView; }
    @Override
	public VolumeView getVolumeView( ) { return relatedVolumeView; }
	@Override
	public NetworkListView getNetworkListView() { return relatedNetworkListView; }
    @Override
    public RouterView getRouterView() {return relatedRouterView;}
    @Override
    public RouterPortView getRouterPortView( ) { return relatedRouterPortView; }
};
