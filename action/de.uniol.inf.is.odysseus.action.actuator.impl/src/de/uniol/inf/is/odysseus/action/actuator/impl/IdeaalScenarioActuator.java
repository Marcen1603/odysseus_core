package de.uniol.inf.is.odysseus.action.actuator.impl;

import de.uniol.inf.is.odysseus.action.services.actuator.adapter.ActuatorAdapterSchema;

/**
 * Actuator switching KNX devices of IdeAAL room
 * @author Simon Flandergan
 *
 */
public class IdeaalScenarioActuator {
	private boolean personLyingBed = false;
	private boolean personStoodUp = false;
	
/*	FIXME auskommentiert bis bundle zur verfuegung gestellt wird	
  	IFS20KNXService knxService;
	
	private static final String LIGHT_BELOW_BED = "10/0/9";
	private static final String LIGHT_NIGHTTABLE = "10/0/6";
	private static final String LIGHT_HALL = "7/0/3";
	private static final String LIGHT_HALL_OFF = "7/0/0";
	private static final String LIGHT_BATHROOM_DIM = "9/0/4";
	private static final String LIGHT_BATHROOM_OFF = "9/0/0";
	
	private static final String LIGHT_OFFICE_OFF = "12/0/0";//"13/0/2";
	private static final String LIGHT_BEDROOM = "10/0/3";
	
	private static final int DIM_VAL = 40;
	
	@ActuatorAdapterSchema(provide = false)
	public bindKNXService(IFS20KNXService service){
		//FIXME binding in osgi inf definieren, sobald bundle zur verfügung steht
		this.knxService = service;
	}
	
	@ActuatorAdapterSchema(show = true, provide = true)
	public void personWentToBed(){
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
		
	}
	

	@ActuatorAdapterSchema(show = true, provide = true)
	public void personStoodUp(){
		personLyingBed = false;
		personStoodUp = true;
		
		knxService.switchKNXDevice(LIGHT_BELOW_BED, true);
		Thread.sleep(2 * 1000);
		
		knxService.dimKNXDevice(LIGHT_HALL, DIM_VAL);
		Thread.sleep(2 * 1000);
		
		knxService.dimKNXDevice(LIGHT_BATHROOM_DIM, DIM_VAL);
	}
	*/

}
