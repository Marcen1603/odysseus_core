/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.action.services.actuator.adapter.ActuatorAdapterSchema;

/**
 * Actuator switching KNX devices of IdeAAL room
 * Birger Martens kontaktieren f�r notwendige Bundles etc.
 * @author Simon Flandergan
 *
 */
@SuppressWarnings("unused")
public class IdeaalScenarioActuator {
	private boolean personLyingBed = false;
	private boolean personStoodUp = false;
	
	/*
  	IFS20KNXService knxService;
	private ServiceTracker data;
	protected IFS20KNXDataListener knxListener;
	*/
	
	private static final String LIGHT_BELOW_BED = "10/0/9";
	private static final String LIGHT_NIGHTTABLE = "10/0/6";
	private static final String LIGHT_HALL = "7/0/3";
	private static final String LIGHT_HALL_OFF = "7/0/0";
	private static final String LIGHT_BATHROOM_DIM = "9/0/4";
	private static final String LIGHT_BATHROOM_OFF = "9/0/0";
	
	private static final String LIGHT_OFFICE_OFF = "12/0/0";//"13/0/2";
	private static final String LIGHT_BEDROOM = "10/0/3";
	
	
	private static final int DIM_VAL = 40;
	
	public IdeaalScenarioActuator(){
		/*final BundleContext bc = Activator.getContext();
		data = new ServiceTracker(bc, IFS20KNXService.class.getName(), null) {
		    public Object addingService(ServiceReference ref)   {   	
		    	if(knxService == null) 	{
		    		knxService = (IFS20KNXService) bc.getService(ref);
		    		knxService.addListener(knxListener);
		    	}
		    	return bc.getService(ref);
		    }
		    
			public void removedService(ServiceReference ref, Object service) {
				knxService.removeListener(knxListener);
				bc.ungetService(ref);
				knxService = null;
			    super.removedService(ref,service);
			}           
		};
		data.open();*/
	}
	
	@ActuatorAdapterSchema(show = true, provide = true)
	public void personWentToBed(double w1, double w2, double w3, double w4) throws InterruptedException{
		/*System.out.println(w1+" "+w2+" "+w3+" "+w4);
		if (personLyingBed){
			//person already in bed, do nothing
			return;
		}
		if (!personStoodUp) {
			//first time to go to bed
			personLyingBed = true;
			
			knxService.switchKNXDevice(LIGHT_NIGHTTABLE, true);
			Thread.sleep(3 * 1000);
			
			knxService.switchKNXDevice(LIGHT_BEDROOM, false);
			Thread.sleep(1/8 * 1000);
			
			knxService.switchKNXDevice(LIGHT_BELOW_BED, false);
			Thread.sleep(1/8 * 1000);
			
			knxService.switchKNXDevice(LIGHT_OFFICE_OFF, false);
			Thread.sleep(1/8 * 1000);
			
			knxService.switchKNXDevice(LIGHT_HALL_OFF, false);
			Thread.sleep(1/8 * 1000);
			
			knxService.switchKNXDevice(LIGHT_BATHROOM_OFF, false);
			Thread.sleep(3 * 1000);
			
			knxService.switchKNXDevice(LIGHT_NIGHTTABLE, false);
			
		}else {
			//person stood up and went to bed again
			personLyingBed = true;
			personStoodUp = false;
			
			knxService.switchKNXDevice(LIGHT_BATHROOM_OFF, false);
			Thread.sleep(1/2 * 1000);
			
			knxService.switchKNXDevice(LIGHT_HALL_OFF, false);
			Thread.sleep(1/2 * 1000);
			
			knxService.switchKNXDevice(LIGHT_OFFICE_OFF, false);
			Thread.sleep(5 * 1000);
			
			knxService.switchKNXDevice(LIGHT_BELOW_BED, false);
		}
		*/
		System.out.println("Person went to bed");
		//Thread.sleep(500);
	}
	

	@ActuatorAdapterSchema(show = true, provide = true)
	public void personStoodUp() throws InterruptedException{
	
		personLyingBed = false;
		personStoodUp = true;
		
		/*knxService.switchKNXDevice(LIGHT_BELOW_BED, true);
		Thread.sleep(2 * 1000);
		
		knxService.dimKNXDevice(LIGHT_HALL, DIM_VAL);
		Thread.sleep(2 * 1000);
		
		knxService.dimKNXDevice(LIGHT_BATHROOM_DIM, DIM_VAL);
		*/
	}

}
