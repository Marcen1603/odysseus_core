package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import java.util.ArrayList;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.sports.rest.dto.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dto.ElementInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.FieldInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.GoalInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.MetadataInfo;
import de.uniol.inf.is.odysseus.sports.rest.resources.IMetadataResource;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;

/**
 * Class which handles the specific Metadata given by the ddc
 * @author Thomas
 *
 */
public class MetadataServerResource extends ServerResource implements IMetadataResource {

	
	@Override
	public void getMetadata() {
		Response response = getResponse();
		
		try {
			
			MetadataInfo metadata = new MetadataInfo(getElements(), getGoals(), getField());
			DataTransferObject dto = new DataTransferObject("Metadata", metadata);
			
			response.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			response.setStatus(Status.SUCCESS_OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	/**
	 * Get the simple elements which have sensor_id's
	 * @return
	 */
	private ArrayList<ElementInfo> getElements() {
		
		ArrayList<ElementInfo> elements = new ArrayList<ElementInfo>();
		
		try {
			
			for (int sensor_id : SoccerDDCAccess.getSensorIds()) {			
					
				int entity_id = SoccerDDCAccess.getEntityId(sensor_id);
				String entity = SoccerDDCAccess.getEntity(sensor_id);
				String remark = SoccerDDCAccess.getRemark(sensor_id).orNull();
				Integer team_id = SoccerDDCAccess.getTeamId(sensor_id).orNull();
							
				elements.add(new ElementInfo(sensor_id, entity_id, entity, remark, team_id));
									
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		return elements;
	}
	
	/**
	 * Add the goals to a list and return them
	 * @return list of {@link GoalInfo}
	 */
	private ArrayList<GoalInfo> getGoals() {
		ArrayList<GoalInfo> goals = new ArrayList<GoalInfo>();
		
		try {
			int position_left = 0;
			double first_post_left = SoccerDDCAccess.getGoalareaLeftYMin();
			double second_post_left = SoccerDDCAccess.getGoalareaLeftYMax();
			double deep_left = SoccerDDCAccess.getGoalareaLeftX();
			double height_left = SoccerDDCAccess.getGoalareaLeftZMax();
			
			//add left goal to the list
			goals.add(new GoalInfo(position_left, first_post_left, second_post_left, deep_left, height_left));
			
			int position_right =1;
			double first_post_right = SoccerDDCAccess.getGoalareaRightYMin();
			double second_post_right = SoccerDDCAccess.getGoalareaRightYMax();
			double deep_right = SoccerDDCAccess.getGoalareaRightX();
			double height_right = SoccerDDCAccess.getGoalareaRightZMax();			
			//add the right goal to the list
			goals.add(new GoalInfo(position_right, first_post_right, second_post_right, deep_right, height_right));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return goals;
	}
	
	
	/**
	 * Creates the field info and returns it
	 * @return {@link FieldInfo}
	 */
	private FieldInfo getField() {
		
		FieldInfo field = null;
		
		try {
			double xmin = SoccerDDCAccess.getFieldXMin();
			double xmax = SoccerDDCAccess.getFieldXMax();
			double ymin = SoccerDDCAccess.getFieldYMin();
			double ymax = SoccerDDCAccess.getFieldYMax();
		
			field = new FieldInfo(xmin, xmax, ymin, ymax);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return field;
		
	}
	
}
