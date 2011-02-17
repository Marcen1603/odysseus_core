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
package de.uniol.inf.is.odysseus.event;


/**
 * This object is a base class for creating events.
 * 
 * @author Wolf Bauer
 * 
 * @param <SenderType>
 *            Type of the sender which could send this event.
 * @param <ValueType>
 *            Type of the value which this event could have.
 */
public abstract class AbstractEvent<SenderType, ValueType> implements
		IEvent<SenderType, ValueType> {

	/**
	 * The value of this event.
	 */
	private ValueType value;

	/**
	 * The Type of this Event
	 */
	private IEventType eventType;
	
	/**
	 * The sender of this event.
	 */
	private SenderType sender;

	/**
	 * Creates a new event.
	 * 
	 * @param sender The sender of this event.
	 * @param id ID that identifies this event. This ID should be unique.
	 * @param value The value of this event.
	 */
	protected AbstractEvent(SenderType sender, IEventType eventType, ValueType value) {
		this.eventType = eventType;
		this.sender = sender;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.event.IEvent#getSender()
	 */
	@Override
	public SenderType getSender() {
		return this.sender;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.event.IEvent#getValue()
	 */
	@Override
	public ValueType getValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.event.IEvent#getID()
	 */
	@Override
	public IEventType getEventType() {
		return this.eventType;
	}
	
	@Override
	public String toString() {
		return this.value+" from "+sender;
	}
}
