package windperformancercp.model;

import java.util.ArrayList;
import java.util.Arrays;

import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventHandler;
import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;

public abstract class AbstractSource implements ISource, IEventHandler {
	
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
	
	public static final int MMId = 0;
	public static final int WTId = 1;
	
	
	EventHandler eventHandler = new EventHandler();
	
	@Override
	public void subscribe(IEventListener listener, IEventType type){
		eventHandler.subscribe(listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type){
		eventHandler.unsubscribe(listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener){
		eventHandler.subscribeToAll(listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener){
		eventHandler.unSubscribeFromAll(listener);
	}

	@Override
	public void fire(IEvent<?, ?> event){
		eventHandler.fire(event);
	}


	public AbstractSource(int typeId, String name, String strIdentifier, String hostName, int portId, Attribute[] attList, int connectState){
		this.type = typeId;
		this.name = name;
		this.streamIdentifier = strIdentifier;
		this.host = hostName;
		this.port = portId;
		//TODO: ist das hier so proper? Und will ich unbedingt einen Array zur initialisierung haben?
		this.attributeList = new ArrayList<Attribute>(Arrays.asList(attList)); 
		this.connectState = connectState;
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
		this.name = newName;
		//TODO: fire
	}
	
	@Override
	public String getName(){
		return this.name;
	}

	@Override
	public void setPort(int newPort) {
		this.port = newPort;
		//TODO: fire
	}
	
	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public void setHost(String newHost){
		this.host = newHost;
		//TODO: fire
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
	public void setStreamIdentifier(String strId){
		this.streamIdentifier = strId;
		//TODO: fire
	}
	
	
	public Attribute getIthAtt(int i){
		if(attributeList.size()>i){
			return attributeList.get(i);
		} else return null;
	}
	
	@Override
	public ArrayList<Attribute> getAttList(){
		return this.attributeList;
	}
	
	@Override
	public int getNumberOfAtts(){
		return attributeList.size();
	}
	
	@Override
	public boolean isConnected(){
		if(connectState == 1) return true;
		else return false;		
	}
	
	@Override
	public int getConnectState(){
		return connectState;
	}
	
	@Override
	public void setConnectState(int i){
		this.connectState = i;
		//TODO: fire
	}
	
	@Override
	public String toString(){
		String info = "Source_ID"+this.id+"Source_Name: "+this.streamIdentifier+"Source_Type: "+this.type+" Port: "+this.port;
		return info;
	}
	
}
