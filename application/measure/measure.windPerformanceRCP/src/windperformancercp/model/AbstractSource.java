package windperformancercp.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractSource implements ISource {
	
	String PROP_TYPE="type";
	String PROP_PORT="port";
	String PROP_HOST="host";
	String PROP_NAME="name";
	String PROP_STRID="streamIdentifier";
	
	private int type;
	private int port;
	private int id;
	private String host;
	private String streamIdentifier;
	private String nameId;
	private final PropertyChangeSupport sourcePCS = new PropertyChangeSupport(this);
	
	public static final int MMId = 0;
	public static final int WTId = 1;
	
	public AbstractSource(){
	}
	
	public AbstractSource(int typeId, String identifier, int portId){
		this.type = typeId;
		this.streamIdentifier = identifier;
		this.port = portId;
	}
	
	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public boolean isWindTurbine() {
		if(this.type == 1) return true;
		return false;
	}

	@Override
	public boolean isMetMast() {
		if(this.type == 0) return true;
		return false;
	}

	@Override
	public void setPort(int newPort) {
		int old = this.port;
		this.port = newPort;
		this.sourcePCS.firePropertyChange("port",old, this.port);
	}
	
	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public void setHost(String newHost){
		String old = this.host;
		this.host = newHost;
		this.sourcePCS.firePropertyChange("host", old, this.host);
	}
	
	@Override
	public String getHost(){
		return this.host;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public String getStreamIdentifier(){
		return this.streamIdentifier;
	}
	
	@Override
	public String toString(){
		String info = "Source_ID"+this.id+"Source_Name: "+this.streamIdentifier+"Source_Type: "+this.type+" Port: "+this.port;
		return info;
	}
	
	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue){
		sourcePCS.firePropertyChange(propertyName, oldValue, newValue);
	}
	 
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		   sourcePCS.addPropertyChangeListener(propertyName, listener);
	}
	 
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		   sourcePCS.removePropertyChangeListener(listener);
	}

	
	

}
