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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import measure.windperformancercp.event.IEventHandler;

/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

@XmlJavaTypeAdapter(AbstractSource.Adapter.class)
public interface ISource extends IDialogResult, IEventHandler {
	
	/**
	 * Sets the sources name
	 * @param newName String for identification by application user.
	 */
	public void setName(String newName);
	
	/**
	 * @return The set String for identification by application user.
	 */
	public String getName();
	
	/**
	 * Sets the name of the stream access operator.
	 * @param strId 
	 */
	public void setStreamIdentifier(String strId);
	
	/**
	 * @return The name of the stream access operator.
	 */
	public String getStreamIdentifier();
	
	
	/**
	 * Sets the host for the stream access.
	 * @param newHost host in networking context, e.g. localhost.
	 */
	public void setHost(String newHost);
	
	/**
	 * @return The host for stream access in networking context.
	 */
	public String getHost();
	
	/**
	 * Sets the hosts port for stream access.
	 * @param newPort The port.
	 */
	public void setPort(int newPort);
	
	/**
	 * @return The hosts port for stream access.
	 */
	public int getPort();
	
	
	/**
	 * An internal identification number. Not used yet. //TODO
	 * @return int Id
	 */
	public int getId();
	
	/**
	 * Sets the schema of the corresponding data stream. 
	 * @param newAttl List of Attributes.
	 */
	public void setAttributeList(ArrayList<Attribute> newAttl);
	
	/**
	 * Returns the schema of the corresponding data stream.
	 * @return List of Attributes. 
	 */
	public ArrayList<Attribute> getAttributeList();
	
	/**
	 * Returns names of schema attributes of corresponding data stream.
	 * @return List of String.
	 */
	public ArrayList<String> getAttributeNameList();
	
	/**
	 * Returns the attribut on ith position in stream schema.
	 * @param i position of the attribut.
	 * @return the attribut.
	 */
	public Attribute getIthAtt(int i);
	
	/**
	 * Sets the attribut on ith position in stream schema
	 * @param i position of the attribut.
	 * @param att the new attribut.
	 */
	public void setIthAtt(int i, Attribute att);
	
	/**
	 * Returns position of specific attribute, if it exists in the schema. 
	 * @param att the searched attribute.
	 * @return if attribute exists, position of the attribut in the List. Else -1.
	 */
	public int getAttIndex(Attribute att);
	
	/**
	 * @return Size of stream schema in attributes.
	 */
	public int getNumberOfAtts();
	
	/**
	 * Returns int type id of source.
	 * @return 0 if met mast, 1 if wind turbine, ..
	 */
	public int getType();
	
	/**
	 * Sets the int type id of source
	 * @param t 0 if met mast, 1 if wind turbine, ..
	 */
	public void setType(int t);
	
	/**
	 * @return True if source is wind turbine.
	 */
	public boolean isWindTurbine();
	
	/**
	 * @return True if source is met mast.
	 */
	public boolean isMetMast();
	
	/**
	 * @return True if source is accessed as data stream.
	 */
	public boolean getConnectState();
	
	/**
	 * Sets connect/access state of source in data stream context.
	 * @param c The state. True if connected.
	 */
	public void setConnectState(boolean c);
	
	/**
	 * The maximum frequency of data stream tuples being send. 
	 * @return frequency in Hertz. Note: it is assumed that int values are sufficient yet.
	 */
	public int getFrequency();
	
	/**
	 *  Sets the maximum frequency of data stream tuples being send.
	 * @param i frequency in Hertz. Note: it is assumed that int values are sufficient yet.
	 */
	public void setFrequency(int i);
	
}

