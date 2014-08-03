package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.rest.dao.AttributeInformation;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.QueryInfo;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;

public class QueryServerResource extends ServerResource implements
		IQueryResource {

	@Override
	public void receiveQuery(String sportsQL) {

		Response r = getResponse();
		try {
			//sportsQL = "SELECT sid, ts FROM soccergame";
			//Collection<Integer> queryIDs = StandardExecutor.getInstance().addQuery(sportsQL, "CQL",
			//		OdysseusRCPPlugIn.getActiveSession(), "Standard", Context.empty());
			
			Collection<Integer> queryIDs = StandardExecutor.getInstance().addQuery(sportsQL, "SportsQL",
					OdysseusRCPPlugIn.getActiveSession(), "Standard", Context.empty());
			
			int queryId = queryIDs.iterator().next();	
			StandardExecutor.getInstance().startQuery(queryId, OdysseusRCPPlugIn.getActiveSession());
			ILogicalQuery query = StandardExecutor.getInstance().getLogicalQueryById(queryId, OdysseusRCPPlugIn.getActiveSession());
			String name = query.getName();
			DataTransferObject dto = new DataTransferObject("QueryInfo", new QueryInfo(queryId, name, getAttributeInformationList(query)));
			r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			r.setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	private ArrayList<AttributeInformation> getAttributeInformationList(ILogicalQuery query){
		SDFSchema outputSchema = query.getLogicalPlan().getOutputSchema();
		
		List<SDFAttribute> attibuteList = outputSchema.getAttributes();
		
		ArrayList<AttributeInformation> attributeInformationList = new ArrayList<AttributeInformation>();
		for (SDFAttribute sdfAttribute : attibuteList) {
			attributeInformationList.add(new AttributeInformation(sdfAttribute.getAttributeName(),sdfAttribute.getDatatype().getQualName()));
		}
		
		return attributeInformationList;
	}
}
