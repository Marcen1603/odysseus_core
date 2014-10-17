package de.uniol.inf.is.odysseus.sports.rest.serverresources;



import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;



import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributedQueryHelper;
import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributedQueryInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dto.QueryInfoRequest;
import de.uniol.inf.is.odysseus.sports.rest.resources.IDistributedQueryResource;

public class DistributedQueryServerResource extends ServerResource implements
		IDistributedQueryResource {


	@Override
	public void getDistributedQueryInfo(QueryInfoRequest request) {
		Response r = getResponse();
		try {			
			
		/* TODO activate distribution for querys
			String sportsQL = SportsQLDistributorRegistry.addSportsQLDistributorConfig(request.getQuery());	
			Collection<Integer> queryIDs = ExecutorServiceBinding.getExecutor().addQuery(sportsQL, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), request.getQueryBuildConfigurationName(), Context.empty());		
		*/
			
			String sportsQL = request.getQuery();
			
			DistributedQueryInfo info = DistributedQueryHelper.executeQuery(sportsQL, OdysseusRCPPlugIn.getActiveSession(), request.getQueryBuildConfigurationName());
			
			DataTransferObject dto = new DataTransferObject("DistributedQueryInfo",info);
			r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			r.setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}			
	}

}
