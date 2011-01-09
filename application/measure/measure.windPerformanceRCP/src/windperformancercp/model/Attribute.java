package windperformancercp.model;

import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventHandler;
import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;

//public class Attribute implements IAttribute{
public class Attribute implements IEventHandler{
	//Datentyp, einer aus Double, Timestamp, Int ...
	
	public enum AttributeType{
		STARTTIMESTAMP,
		WINDSPEED,
		POWER,
		AIRTEMPERATURE,
		AIRPRESSURE,
		STATE,
		WINDDIRECTION,
		VARIOUS
	}
	
	//TODO: liste komplettieren
	///Entspricht den Datentypen in Odysseus CQL
	private enum DataType{
		STARTTIMESTAMP,
		ENDTIMESTAMP,
		INTEGER,
		DOUBLE,
		STRING
	}
	
	
	String name;
	AttributeType attType;
	DataType dataType;

	EventHandler eventHandler = new EventHandler();
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(listener);
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		eventHandler.fire(event);
	}

	
	public Attribute(String name, AttributeType type){
		this.name = name;
		this.attType = type;
		switch(type)
		{
		case STARTTIMESTAMP: this.dataType = DataType.STARTTIMESTAMP;
		case WINDSPEED: this.dataType = DataType.DOUBLE;
		case POWER: this.dataType = DataType.DOUBLE;
		case AIRTEMPERATURE: this.dataType = DataType.DOUBLE;
		case AIRPRESSURE: this.dataType = DataType.DOUBLE;
		case STATE: this.dataType= DataType.STRING; //TODO: ist das so?
		case WINDDIRECTION: this.dataType = DataType.DOUBLE;
		case VARIOUS: this.dataType = DataType.STRING; //TODO: gibt es einen beliebigen Datentyp bei Odysseus?
		default:
			this.dataType = DataType.DOUBLE; 
		}
		System.out.println("created Attribute: "+this.toString());		
	}
	
	public Attribute(String name, String type){
		this(name, Attribute.AttributeType.valueOf(type));
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
		//TODO:fire
	}
	
	public AttributeType getAttType(){
		return this.attType;
	}
	
	public void setAttType(AttributeType atype){
		this.attType = (AttributeType) atype;
		//TODO: fire
	}
	
	public String getDataType(){
		return this.dataType.toString();
	}
	
	public void setDataType(DataType dtype){
		this.dataType = (DataType) dtype;
		//TODO: fire
	}
	
	public String toString(){
		return this.getName()+" "+this.getAttType()+" "+this.getDataType();
	}
}
