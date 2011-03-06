/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.model.sources;

import javax.xml.bind.annotation.XmlEnum;

public class Attribute implements IDialogResult{
	//Datentyp, einer aus Double, Timestamp, Int ...
	@XmlEnum
	public enum AttributeType{
		STARTTIMESTAMP,
		ENDTIMESTAMP,
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
	@XmlEnum
	public enum DataType{
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
		/*	// this doesn't work!
		switch(type)
		{
		case STARTTIMESTAMP: this.dataType = DataType.STARTTIMESTAMP;
		case ENDTIMESTAMP: this.dataType = DataType.ENDTIMESTAMP;
		case WINDSPEED: this.dataType = DataType.DOUBLE;
		case POWER: this.dataType = DataType.DOUBLE;
		case AIRTEMPERATURE: this.dataType = DataType.DOUBLE;
		case AIRPRESSURE: this.dataType = DataType.DOUBLE;
		case STATE: this.dataType= DataType.STRING; //TODO: ist das so?
		case WINDDIRECTION: this.dataType = DataType.DOUBLE;
		case VARIOUS: this.dataType = DataType.STRING; //TODO: gibt es einen beliebigen Datentyp bei Odysseus?
		default:
			this.dataType = DataType.DOUBLE; 
		}*/
		
		if(type.equals(AttributeType.STARTTIMESTAMP)){
			this.dataType = DataType.STARTTIMESTAMP;
		} else  
		if(type.equals(AttributeType.ENDTIMESTAMP)){
			this.dataType = DataType.ENDTIMESTAMP;
		} else
		if(type.equals(AttributeType.STATE)){
			this.dataType = DataType.STRING;
		} else
		if(type.equals(AttributeType.VARIOUS)){
			this.dataType = DataType.STRING;
		} else this.dataType = DataType.DOUBLE;
		
		
		
		
		//System.out.println("created Attribute: "+this.toString());		
	}
	
	public Attribute(String name, String type){
		this(name, Attribute.AttributeType.valueOf(type));
	}
	
	public Attribute(Attribute copy){		
		this(copy.name,copy.attType);
	}
	
	public Attribute(){
		this("",Attribute.AttributeType.VARIOUS);
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
	
	public DataType getDataType(){
		return this.dataType;
	}
	
	public void setDataType(DataType dtype){
		this.dataType = (DataType) dtype;
	}
	
	public String toString(){
		return this.getName()+" "+this.getAttType()+" "+this.getDataType();
	}
}
