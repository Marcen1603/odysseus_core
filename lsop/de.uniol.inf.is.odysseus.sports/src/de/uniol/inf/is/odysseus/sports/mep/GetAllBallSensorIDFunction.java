package de.uniol.inf.is.odysseus.sports.mep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class GetAllBallSensorIDFunction extends AbstractFunction<String>{
	
	private static final long serialVersionUID = -1421280112062480906L;

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(GetAllBallSensorIDFunction.class);
	
	protected static IDistributedDataContainer ddc;

	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{ SDFDatatype.STRING } };

	public GetAllBallSensorIDFunction() {
	        super("getBallSensorIDs", 1, accTypes, SDFDatatype.INTEGER);
	    }
	
	
	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		GetAllBallSensorIDFunction.ddc = ddc;
		GetAllBallSensorIDFunction.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (GetAllBallSensorIDFunction.ddc == ddc) {
			GetAllBallSensorIDFunction.ddc = null;
			GetAllBallSensorIDFunction.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	@Override
	public String getValue() {
		String ballSensorIDs = "";
		
		String outputType =  getInputValue(0).toString();
		
	
		try {
			String[] sensorList =AccessToDCCFunction.ddc.getValue(new DDCKey("sensoridlist")).split(",");
			
		 for (int i = 0; i < sensorList.length; i++) {
			String[] searchKey = new String[2];
			searchKey[0]="sensorid."+sensorList[i];
			searchKey[1]="team_id";
			Integer team_id = Integer.valueOf(GetAllBallSensorIDFunction.ddc.getValue(new DDCKey(searchKey)));
			
			
			if(outputType.equals("entity_id")){
				searchKey[1]="entity_id";
				Integer entity_id = Integer.valueOf(GetAllBallSensorIDFunction.ddc.getValue(new DDCKey(searchKey)));
				
				if(team_id == -1){
					if(ballSensorIDs.length() == 0){
						ballSensorIDs = "entity_id = "+entity_id;
					}else{
						ballSensorIDs += " || "+ "entity_id = "+entity_id;
					}
				}
			}else if(outputType.equals("sid")){
				if(team_id == -1){
					if(ballSensorIDs.length() == 0){
						ballSensorIDs = "sid = "+sensorList[i];
					}else{
						ballSensorIDs += " || "+ "sid = "+sensorList[i];
					}
				}
			}
			
			
		
		 }	
		} catch (MissingDDCEntryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		
		return ballSensorIDs;
	}
}
