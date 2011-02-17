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
package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Class representing a message for a webservice call.
 * Holds a number of {@link MessagePart} for in- and output
 * @author Simon Flandergan
 *
 */
public class Message {
	private QName messageName;
	private List<MessagePart> inputParts;
	private List<MessagePart> outputParts;
	
	/**
	 * Creates new Message with qualified name
	 * @param name
	 */
	public Message(QName name){
		this.inputParts = new ArrayList<MessagePart>();
		this.outputParts = new ArrayList<MessagePart>();
		this.messageName = name;
	}
	
	/**
	 * Adds a {@link MessagePart} used at invokes
	 * @param part
	 */
	public void addInputMessagePart(MessagePart part){
		this.inputParts.add(part);
	}
	
	/**
	 * Adds a {@link MessagePart} expected as result from invoking
	 * @param part
	 */
	public void addOutputMessagePart(MessagePart part){
		this.outputParts.add(part);
	}
	
	public List<MessagePart> getInputParts() {
		return inputParts;
	}
	
	public QName getMessageName() {
		return messageName;
	}
	
	public List<MessagePart> getOutputParts() {
		return outputParts;
	}
	
	

}
