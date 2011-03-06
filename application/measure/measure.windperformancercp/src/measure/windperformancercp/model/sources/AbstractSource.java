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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.IEventType;

/**
 * Abstract implementation of ISource
 * @author Diana von Gallera
 *
 */
@XmlType(name = "abstractSource", propOrder = {
	    "name",
	    "type",
	    "streamIdentifier",
	    "host",
	    "port",
	    "frequency",
	    "attributeList",
	    "connectState"
	})
public abstract class AbstractSource implements ISource {
	
	public static class Adapter extends XmlAdapter<AbstractSource,ISource> {
	    public ISource unmarshal(AbstractSource src) { return src; }
	    public AbstractSource marshal(ISource src) { return (AbstractSource) src; }
	}
	
	public static final String ID = "measure.windPerformanceRCP.ASource";
	
	private static int sourceCounter = 0;
	private int type;
	protected int port;
	protected @XmlAttribute int id;
	protected String host;
	protected String streamIdentifier;
	protected String name;
	protected ArrayList<Attribute> attributeList;
	protected int frequency;
	protected boolean connectState; //0=disconnected, 1=proceeding, 2= connected
	
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


	public AbstractSource(int typeId, String name, String strIdentifier, String hostName, int portId, ArrayList<Attribute> attList, int freq){
		this.type = typeId;
		this.name = name;
		this.streamIdentifier = strIdentifier;
		this.host = hostName;
		this.port = portId;
		this.attributeList = new ArrayList<Attribute>(attList); 
		this.connectState = false;
		this.frequency = freq;
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
		this.connectState = false;
		this.frequency = 1;
	}
	
	public AbstractSource(AbstractSource copy){
		this(copy.getType(),copy.getName(),copy.getStreamIdentifier(),copy.getHost(),copy.getPort(),copy.getAttributeList(), copy.getFrequency());
	}
	
	@Override
	public int getType() {
		return this.type;
	}
	
	@Override
	public void setType(int t) {
		this.type = t;
	}

	@Override
	public boolean isWindTurbine() {
		if(this.type == WTId) return true;
		return false;
	}

	@Override
	public boolean isMetMast() {
		if(this.type == MMId) return true;
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
	
	@Override
	@XmlElementWrapper(name = "attributeList") 
	@XmlElement(name = "attribute")
	public ArrayList<Attribute> getAttributeList(){
		if(attributeList == null)
			attributeList = new ArrayList<Attribute>();
		return this.attributeList;
	}
	
	@Override
	public ArrayList<String> getAttributeNameList(){
		ArrayList<String> nameList = new ArrayList<String>();
		for(Attribute a: attributeList){
			nameList.add(a.getName());
		}
		return nameList;
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
	public int getAttIndex(Attribute att){
		return attributeList.indexOf(att);
	}
		
	@Override
	public int getNumberOfAtts(){
		return attributeList.size();
	}
	
	@Override
	public boolean getConnectState(){
		return connectState;
	}
	
	@Override
	public void setConnectState(boolean c){
			this.connectState = c;
	}
	
	@Override
	public int getFrequency(){
		return frequency;
	}
	
	@Override
	public void setFrequency(int i){
			this.frequency = i;
	}
	
	@Override
	public String toString(){
		String info = "Source_ID: "+this.id+", Source_Name: "+this.name+" Stream Id: "+this.streamIdentifier+", Source_Type: "+this.type+", Port: "+this.port+", Host: "+this.host+", Attributes: "+this.attributeList.toString()+" connectState: "+this.connectState;
		return info;
	}
	
	/**
	 * Computes equality between two sources.
	 * Note that name and connect stat are not relevant,  
	 * if the two are distinct in only (one or both of) this two points, they are equal.  
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null)
			return false;
		if(!(obj instanceof AbstractSource)){
			return false;
		}
		//now they are both abstract sources
		AbstractSource other = (AbstractSource) obj;
		if(this.type == other.getType()){	//wind turbine can't be met mast
			if(this.streamIdentifier.equals(other.getStreamIdentifier())){	//same stream id
				if(this.host.equals(other.getHost())){	//same host
					if(this.port == other.getPort()){	//same port
						if(this.attributeList.equals(other.getAttributeList())){	//same schema //TODO: when are two lists equal?
							if(this.frequency == other.getFrequency()){ //same frequency
								return true;
							}
						}
					}
				}
			}
		}
		
		//Note: name and connect state are not relevant
		return false;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + port;
		result = prime * result + frequency;
		result = prime * result + type;
		result = prime * result + ((streamIdentifier == null) ? 0 : streamIdentifier.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((attributeList == null) ? 0 : attributeList.hashCode());
		return result;
		
	}
	
}
