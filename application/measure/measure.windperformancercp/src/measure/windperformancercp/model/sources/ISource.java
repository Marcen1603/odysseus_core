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

import measure.windperformancercp.event.IEventHandler;



/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

public interface ISource extends IDialogResult, IEventHandler {
	
	
	public void setName(String newName);
	public String getName();
	public String getHost();
	public String getStreamIdentifier();
	public void setStreamIdentifier(String strId);
	public void setHost(String newHost);
	public int getPort();
	public void setPort(int newPort);
	public int getId();
	public ArrayList<Attribute> getAttributeList();
	public ArrayList<String> getAttributeNameList();
	public void setAttributeList(ArrayList<Attribute> newAttl);
	public Attribute getIthAtt(int i);
	public void setIthAtt(int i, Attribute att);
	public int getAttIndex(Attribute att);
	public int getNumberOfAtts();
	public int getType();
	public void setType(int t);
	public boolean isWindTurbine();
	public boolean isMetMast();
	public boolean isConnected();
	public int getConnectState();
	public void setConnectState(int i);
	public int getFrequency();
	public void setFrequency(int i);
	
}

