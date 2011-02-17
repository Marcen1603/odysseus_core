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
package de.uniol.inf.is.odysseus.action.actuator.impl;

import java.text.DateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor;
import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor.AuctionStatus;
import de.uniol.inf.is.odysseus.action.services.actuator.adapter.ActuatorAdapterSchema;

/**
 * Actuator notifying DemoApp of new auction data
 * @author Simon Flandergan
 *
 */
public class DemoAppActuator{
	
	@ActuatorAdapterSchema(
			show = true,
			paramNames = {"id", "itemName", "timestamp"}
			)
	public void newAuction(int id, String itemName, long timeStamp) {
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, "-", "-", DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.RED);
	}
	
	@ActuatorAdapterSchema(show = true)
	public void updateGreen(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.GREEN);
	}
	
	@ActuatorAdapterSchema(show = true)
	public void updateOrange(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.ORANGE);
	}

}
