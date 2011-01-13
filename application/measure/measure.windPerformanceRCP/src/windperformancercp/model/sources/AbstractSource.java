package windperformancercp.model.sources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;


//@XmlRootElement(name="source")
public abstract class AbstractSource implements ISource {
	
	public static final String ID = "measure.windPerformanceRCP.ASource";
	
	private static int sourceCounter = 0;
	private int type;
	private int port;
	private int id;
	private String host;
	private String streamIdentifier;
	private String name;
	private ArrayList<Attribute> attributeList;
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


	public AbstractSource(int typeId, String name, String strIdentifier, String hostName, int portId, ArrayList<Attribute> attList, int connectState){
		this.type = typeId;
		this.name = name;
		this.streamIdentifier = strIdentifier;
		this.host = hostName;
		this.port = portId;
		this.attributeList = new ArrayList<Attribute>(attList); 
		this.connectState = connectState;
		sourceCounter++;
		this.id = sourceCounter;
	}
	
	public AbstractSource(){
		this.type = -1;
		this.name = "";
		this.streamIdentifier = "";
		this.host = "";
		this.port = 0;
		this.attributeList = new ArrayList<Attribute>();
		this.connectState = -1;
	}
	
	public AbstractSource(AbstractSource copy){
		this(copy.getType(),copy.getName(),copy.getStreamIdentifier(),copy.getHost(),copy.getPort(),copy.getAttributeList(),copy.getConnectState());
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
	public String getName(){
		return this.name;
	}
	
	@Override
	public void setName(String newName) {
			this.name = newName;
	}
		
	@Override
	public String getStreamIdentifier(){
		return this.streamIdentifier;
	}
	
	@Override
	public void setStreamIdentifier(String strId){
			this.streamIdentifier = strId;
	}

	
	@Override
	public String getHost(){
		return this.host;
	}
	
	@Override
	public void setHost(String newHost){
			this.host = newHost;
	}
		
	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public void setPort(int newPort) {
			this.port = newPort;
	}
		
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@XmlElement(name="attribute")
	@Override
	public ArrayList<Attribute> getAttributeList(){
		if(attributeList == null)
			attributeList = new ArrayList<Attribute>();
		return this.attributeList;
	}
	
	@Override
	public void setAttributeList(ArrayList<Attribute> newAttl){
			this.attributeList = newAttl;
	}
		
	@Override
	public Attribute getIthAtt(int i){
		if(attributeList.size()>i){
			return attributeList.get(i);
		} else return null;
	}
	
	@Override
	public void setIthAtt(int i, Attribute att){
		if(attributeList.size()>i){
				attributeList.set(i, att);
		} 
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
	}
	
	@Override
	public String toString(){
		String info = "Source_ID: "+this.id+", Source_Name: "+this.streamIdentifier+", Source_Type: "+this.type+", Port: "+this.port+", Host: "+this.host+", Attributes: "+this.attributeList.toString();
		return info;
	}
	
}
