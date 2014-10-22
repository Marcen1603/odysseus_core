package de.uniol.inf.is.odysseus.sports.rest.serverresources;



import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;







import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.distributor.registry.SportsQLDistributorRegistry;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryHelper;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dto.QueryInfoRequest;
import de.uniol.inf.is.odysseus.sports.rest.resources.IDistributedQueryResource;

public class DistributedQueryServerResource extends ServerResource implements
		IDistributedQueryResource {


	@Override
	public void getDistributedQueryInfo(QueryInfoRequest request) {
		Response r = getResponse();
		try {
			String sportsQL = SportsQLDistributorRegistry.addSportsQLDistributorConfig(request.getQuery());	
			DistributedQueryInfo info = null;
			if (sportsQL != null) {
				info = DistributedQueryHelper.executeQuery(sportsQL, OdysseusRCPPlugIn.getActiveSession(), request.getQueryBuildConfigurationName());
			} 
			if (info != null) {
				info = new DistributedQueryInfo();
				info.setQueryDistributed(false);
			}			
			DataTransferObject dto = new DataTransferObject("DistributedQueryInfo",info);
			r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			r.setStatus(Status.SUCCESS_OK);			
		} catch (Exception e1) {
			e1.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}			
	}

}
