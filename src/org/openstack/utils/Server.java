package org.openstack.utils;

public class Server {

    public final static int STATUS_ACTIVE = 0;
    public final static int STATUS_SPAWN  = 1;
    public final static int STATUS_BUILD  = 2;
    public final static int STATUS_ERROR  = 3;
    public final static int STATUS_DELETING = 4;
    public final static int STATUS_SHUTOFF = 5;

    public final static int POWER_RUNNING = 6;
    public final static int POWER_SHUTDOWN  = 7;

    private String name;
    private String ID;
    private int status;
    private int task;
    private int powerstate;
    private String privIP;
    private String pubIP;
    private String computeNode;
    private String MAC;
    private String keyname;
    private String flavorID;
    private String secgrpID;
    
    public Server( String _name,
		   String _ID,
		   int _status,
		   int _task,
		   int _power,
		   String _privIP,
		   String _pubIP,
		   String _computeNode,
		   String _MAC,
		   String _keyname,
		   String _flavorID,
		   String _secgrpID ) {
	name        = _name;
	ID          = _ID;
	status      = _status;
	task        = _task;
	powerstate  = _power;
	privIP      = _privIP;
	computeNode = _computeNode;
	MAC         = _MAC;
	keyname     = _keyname;
	flavorID    = _flavorID;
	secgrpID    = _secgrpID;
    }

    
}