package windperformancercp.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractSource implements ISource {
	
	public static final String ID = "measure.windPerformanceRCP.ASource";
	//String PROP_TYPE="type"; //this should not change
	private String PROP_NAME="name";
	private String PROP_PORT="port";
	private String PROP_HOST="host";
	//String PROP_STRID="streamIdentifier"; //this should not change
	
	private static int sourceCounter = 0;
	
	private int type;
	private int port;
	private int id;
	private String host;
	private String streamIdentifier;
	private String name;
	private  ArrayList<Attribute> attributeList;
	private int connectState; //0=disconnected, 1=proceeding, 2= connected
	private final PropertyChangeSupport sourcePCS = new PropertyChangeSupport(this);
	
	public static final int MMId = 0;
	public static final int WTId = 1;
	
	//public AbstractSource(){
	//}
	
	public AbstractSource(int typeId, String name, String strIdentifier, String hostName, int portId, Attribute[] attList){
		this.type = typeId;
		this.name = name;
		this.streamIdentifier = strIdentifier;
		this.host = hostName;
		this.port = portId;
		//TODO: ist das hier so proper? Und will ich unbedingt einen Array zur initialisierung haben?
		this.attributeList = new ArrayList<Attribute>(Arrays.asList(attList));
		sourceCounter++;
		this.id = sourceCounter;
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
	public void setName(String newName) {
		String old = this.name;
		this.name = newName;
		this.sourcePCS.firePropertyChange(PROP_NAME,old, this.name);
	}
	
	@Override
	public String getName(){
		return this.name;
	}

	@Override
	public void setPort(int newPort) {
		int old = this.port;
		this.port = newPort;
		this.sourcePCS.firePropertyChange(PROP_PORT,old, this.port);
	}
	
	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public void setHost(String newHost){
		String old = this.host;
		this.host = newHost;
		this.sourcePCS.firePropertyChange(PROP_HOST, old, this.host);
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
	
	public Attribute getIthAtt(int i){
		if(attributeList.size()>i){
			return attributeList.get(i);
		} else return null;
	}
	
	public ArrayList<Attribute> getAttList(){
		return this.attributeList;
	}
	
	public int getNumberOfAtts(){
		return attributeList.size();
	}
	
	public boolean isConnected(){
		if(connectState == 1) return true;
		else return false;		
	}
	
	public int getConnectState(){
		return connectState;
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
	
	//TODO: das lassen wir besser
	/*protected void finalize(){
		sourceCounter--;
	}*/

}
