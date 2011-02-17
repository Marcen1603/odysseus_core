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
package de.uniol.inf.is.odysseus.broker.sensors.generator;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Represents an interface for a stream type. 
 * 
 * @author Dennis Geesen
 */
public interface IStreamType{
	
	/**
	 * Gets the schema of the stream.
	 *
	 * @return the schema
	 */
	public SDFAttributeList getSchema();
	
	/**
	 * Gets the next tuple from this stream.
	 *
	 * @param currentTime the current time
	 * @return the next tuple
	 */
	public RelationalTuple<ITimeInterval> getNextTuple(long currentTime);
	
	/**
	 * Gets the time to wait between to tuples in milliseconds.
	 *
	 * @return the time to wait
	 */
	public long getWaitingMillis();
	
	/**
	 * Gets the max count of items this stream will provide.
	 *
	 * @return the count of items
	 */
	public int getMaxItems();
	
	/**
	 * Checks if punctuation is enabled.
	 *
	 * @return true, if punctuation is enabled
	 */
	public boolean isPunctuationEnabled();
	
	/**
	 * Gets the name of the stream
	 *
	 * @return the name
	 */
	public String getName();	
	
	/**
	 * Gets the next punctuation.
	 *
	 * @param currentTime the current time
	 * @return the next punctuation
	 */
	public long getNextPunctuation(long currentTime);			
}
