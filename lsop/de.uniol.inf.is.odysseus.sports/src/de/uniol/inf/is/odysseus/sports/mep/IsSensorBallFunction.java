package de.uniol.inf.is.odysseus.sports.mep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class IsSensorBallFunction extends AbstractFunction<Boolean>{
	
	private static final long serialVersionUID = -1421280112062480906L;

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(IsSensorBallFunction.class);
	
	protected static IDistributedDataContainer ddc;

	private static List<Integer> ballEntityIDList = new ArrayList<Integer>();
	private static List<Integer> ballSensorIDList = new ArrayList<Integer>();
	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{ SDFDatatype.INTEGER}, { SDFDatatype.STRING } };

	public IsSensorBallFunction() {
	        super("isSensorBall", 2, accTypes, SDFDatatype.BOOLEAN);
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
		IsSensorBallFunction.ddc = ddc;
		IsSensorBallFunction.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

		setUpBallList();
	
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
		if (IsSensorBallFunction.ddc == ddc) {
			IsSensorBallFunction.ddc = null;
			IsSensorBallFunction.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	
	private static void setUpBallList(){
		try {
			String[] sensorList =AccessToDCCFunction.ddc.getValue(new DDCKey("sensoridlist")).split(",");
			
		 for (int i = 0; i < sensorList.length; i++) {
			String[] searchKey = new String[2];
			searchKey[0]="sensorid."+sensorList[i];
			searchKey[1]="team_id";
	
			Integer team_id = Integer.valueOf(IsSensorBallFunction.ddc.getValue(new DDCKey(searchKey)));
			
			if(team_id == -1){
				searchKey[1]="entity_id";
				int entity_id = Integer.valueOf(IsSensorBallFunction.ddc.getValue(new DDCKey(searchKey)));
					
				ballEntityIDList.add(entity_id);
				ballSensorIDList.add(Integer.valueOf(sensorList[i]));
			}
		
			
		 }	
				

		} catch (MissingDDCEntryException e1) {
			e1.printStackTrace();
		}		
	}

	@Override
	public Boolean getValue() {
		
		Integer id =  getInputValue(0);
		String type =  getInputValue(1).toString();
		
		if(type.equals("sid")){
			if(ballSensorIDList.contains(id)){
				return true;
			}else{
				return false;
			}
		}
		
		if(type.equals("entity_id")){
			if(ballEntityIDList.contains(id)){
				return true;
			}else{
				return false;
			}
		}
		
		return false;
	}
}
