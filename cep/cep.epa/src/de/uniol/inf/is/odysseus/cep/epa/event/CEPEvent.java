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
package de.uniol.inf.is.odysseus.cep.epa.event;

import java.util.EventObject;

/**
 * This class defines a CEPEvent.
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class CEPEvent extends EventObject {

	// type constants
	public static final int ADD_MASCHINE = 0;
	public static final int CHANGE_STATE = 1;
	public static final int MACHINE_ABORTED = 2;

	// the type of this event
	private int type;
	// the content of this event
	private Object content;

	/**
	 * This is the constructor of this class.
	 * 
	 * @param source
	 *            is the listener to be notified by this event
	 * @param type
	 *            is the type of this event
	 * @param content
	 *            is the content to be saved
	 */
	public CEPEvent(ICEPEventListener source, int type, Object content) {
		super(source);
		this.type = type;
		this.content = content;
	}

	/**
	 * This is the getter for the type of this event
	 * 
	 * @return the event type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * This is the getter for the content of this event
	 * 
	 * @return the content of this event
	 */
	public Object getContent() {
		return this.content;
	}

}
