package windperformancercp.model.sources;

import windperformancercp.event.EventHandler;

//public class Attribute implements IAttribute{
//public class Attribute implements IEventHandler, IDialogResult{
public class Attribute implements IDialogResult{
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
	
	public Attribute(Attribute copy){
		this(copy.name,copy.attType);
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public AttributeType getAttType(){
		return this.attType;
	}
	
	public void setAttType(AttributeType atype){
		this.attType = (AttributeType) atype;
	}
	
	public String getDataType(){
		return this.dataType.toString();
	}
	
	public void setDataType(DataType dtype){
		this.dataType = (DataType) dtype;
	}
	
	public String toString(){
		return this.getName()+" "+this.getAttType()+" "+this.getDataType();
	}
}
