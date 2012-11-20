package de.offis.scaiconnector.factory;

import de.offis.client.ScaiException;
import de.offis.client.Sensor;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import de.offis.xml.schema.scai20.OperatorGroupStatus;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.Reply;
import de.offis.xml.schema.scai20.SensorDescription;

/**
 * Wrapper for SCAI Get-Operations.
 *
 * @author Alexander Funk
 * 
 */
public final class GetScaiCmds {
	
	private String GET_OPERATION_ID = "GetOPid";

	private ScaiFactory factory;
	
	public GetScaiCmds(ScaiFactory factory) {
		this.factory = factory;
	}
	
	public boolean getOperatorGroupStatus(String operatorGroup){
		BuilderSCAI20 cmd = new BuilderSCAI20();		
		cmd.addGetOperatorGroupStatus(new SCAIReference(operatorGroup, false), GET_OPERATION_ID);
		
		ScaiCommand scai = factory.createScaiCommand(cmd);

        try {
        	scai.commit();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        Reply reply = scai.getReplyResponse(GET_OPERATION_ID);
        		
		if(reply != null){
			OperatorGroupStatus status = (OperatorGroupStatus) reply.getDataArray(0);
			return status.getDeployed();
		} else {
			return false;
		}
	}
	
	public Sensor getSensorById(String id) throws ScaiException {
		BuilderSCAI20 cmd = new BuilderSCAI20();		
		cmd.addGetSensor(new SCAISensorReference(id), GET_OPERATION_ID);
		
		ScaiCommand scai = factory.createScaiCommand(cmd);

        try {
        	scai.commit();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        Reply reply = scai.getReplyResponse(GET_OPERATION_ID);
					
		SensorDescription s = (SensorDescription) reply.getDataArray(0);						
		return new Sensor(s.getName(), s.getSensorDomain().getName(), s.getSensorType().getName(), s.getVirtual());	
	}
}
