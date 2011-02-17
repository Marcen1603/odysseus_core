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
package measure.windperformancercp.event;

/**
 * Defines an event which is send by an object and has a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <SenderType>
 *            Type of the sender which could send this event.
 * @param <ValueType>
 *            Type of the value which this event could have.
 */
public interface IEvent<SenderType, ValueType> {
	
	public IEventType getEventType();

	/**
	 * Returns the value of this event.
	 * 
	 * @return The value of this event.
	 */
	public ValueType getValue();

	/**
	 * Returns the sender of this event.
	 * 
	 * @return The sender of this event.
	 */
	public SenderType getSender();
}
