package de.uniol.inf.is.odysseus.sports.rest.serverresources;


import java.util.ArrayList;
import java.util.List;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryHelper;
import de.uniol.inf.is.odysseus.sports.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sports.rest.dto.AttributeInformation;
import de.uniol.inf.is.odysseus.sports.rest.dto.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dto.DistributedQuerySocketInfo;
import de.uniol.inf.is.odysseus.sports.rest.dto.SocketInfo;
import de.uniol.inf.is.odysseus.sports.rest.resources.IDistributedQuerySocketInfoResource;
import de.uniol.inf.is.odysseus.sports.rest.socket.SocketService;

public class DistributedQuerySocketInfoServerResource extends ServerResource implements IDistributedQuerySocketInfoResource {

	@Override
	public void getSocketInfoOfQuery(String sharedQueryId) {
		sharedQueryId = sharedQueryId.replace("\"", "");
		Response r = getResponse();
		try {
			
			//login with default user: System
			ISession session = SocketService.getInstance().login();
			
			//get SocketInformation
			Integer queryId = DistributedQueryHelper.getQueryIdWithTopOperator(sharedQueryId, session);
			ExecutorServiceBinding.getExecutor().startQuery(queryId, OdysseusRCPPlugIn.getActiveSession());
			
			IPhysicalOperator operator = DistributedQueryHelper.getTopOperatorOfQuery(queryId, session);
			
			
			SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session.getToken(),queryId, operator);
						
			if(peerSocket != null){
				DistributedQuerySocketInfo info = new DistributedQuerySocketInfo();
				info.setIp(peerSocket.getIp());
				info.setPort(peerSocket.getPort());
				info.setAttributeList(this.getAttributeInformationList(operator));
				DataTransferObject dto = new DataTransferObject("DistributedQuerySocketInfo", info);
				
				r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
				r.setStatus(Status.SUCCESS_OK);
			}else{
				r.setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		
		
	}
	
	private List<AttributeInformation> getAttributeInformationList(IPhysicalOperator operator){
		SDFSchema outputSchema = operator.getOutputSchema();
		
		List<SDFAttribute> attibuteList = outputSchema.getAttributes();
		
		ArrayList<AttributeInformation> attributeInformationList = new ArrayList<AttributeInformation>();
		for (SDFAttribute sdfAttribute : attibuteList) {
			attributeInformationList.add(new AttributeInformation(sdfAttribute.getAttributeName(),sdfAttribute.getDatatype().getQualName()));
		}
		
		return attributeInformationList;
	}
	
	
	
	
	
	




}
