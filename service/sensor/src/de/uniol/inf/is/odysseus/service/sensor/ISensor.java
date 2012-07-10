/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.service.sensor;

import de.uniol.inf.is.odysseus.service.sensor.data.DataTuple;

/**
 * The Interface ISensor represents a sensor.
 */
public interface ISensor {
	
	/**
	 * Sends a tuple to all connected odysseus instances 
	 *
	 * @param tuple the tuple that has to be sent
	 */
	public void sendTuple(DataTuple tuple);
	
	/**
	 * Gets the port on which the sensor is listening for new clients.
	 *
	 * @return the current port for this sensor
	 */
	public int getPort();
	
	/**
	 * Gets the own host. The returned value should be either "localhost" or the IP-Address
	 *
	 * @return the own host
	 */
	public String getOwnHost();
}
