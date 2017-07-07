package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.QuerySourceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PhysicalOperatorParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.ToKeyValueAO;

/**
 * REST resource to create WebSocket-Sink queries and get the information about
 * a sink
 * 
 * @author Tobias Brandt
 *
 */
public class GetResultStreamInformationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getResultStreamInformation";

	private int port = 8001;
	
	@Post
	public Map<String, Map<Integer, String>> getResultStreamInformation(
			GenericSessionRequestDTO<Object> genericSessionRequestDTO) {
		ISession session = this.loginWithToken(genericSessionRequestDTO.getToken());
		int queryId = -1;

		/*
		 * When using the REST interface with JavaScript / JSON, integers are maybe send
		 * as string
		 */
		if (genericSessionRequestDTO.getValue() instanceof String) {
			queryId = Integer.parseInt((String) genericSessionRequestDTO.getValue());
		} else if (genericSessionRequestDTO.getValue() instanceof Integer) {
			queryId = (Integer) genericSessionRequestDTO.getValue();
		}

		Map<String, Map<Integer, String>> resultMap = new HashMap<String, Map<Integer,String>>();
		
		// Create a websocket sink query for every root operator
		
		// 1. Get the roots from the given query
		List<IPhysicalOperator> rootOperators = SocketService.getInstance().getRootOperators(queryId, session);

		for (IPhysicalOperator root : rootOperators) {
			
			Map<Integer, String> portMap = new HashMap<>();
			resultMap.put(root.getName(), portMap);
			
			// 2. Get the hashcode
			int hashCode = root.hashCode();

			// 3. Connect to the stream from the operator
			QuerySourceAO querySource = new QuerySourceAO();

			PhysicalOperatorParameter physicalParam = new PhysicalOperatorParameter();
			List<String> inputParameters = new ArrayList<>(2);
			inputParameters.add(String.valueOf(queryId));
			inputParameters.add(String.valueOf(hashCode));
			physicalParam.setInputValue(inputParameters);
			physicalParam.setServerExecutor(ExecutorServiceBinding.getExecutor());
			physicalParam.setCaller(session);

			querySource.setQueryName(physicalParam.getValue());
			// TODO All ports?!
			querySource.setPort(0);

			// 3. Convert to KeyValue
			ToKeyValueAO toKeyValueAO = new ToKeyValueAO();
			toKeyValueAO.subscribeToSource(querySource, 0, 0, querySource.getOutputSchema());

			// 4. Create sender with WebSocket
			SenderAO sender = new SenderAO();
			sender.subscribeToSource(toKeyValueAO, 0, 0, toKeyValueAO.getOutputSchema());

			sender.setProtocolHandler("JSON");
			sender.setTransportHandler("TCPServer");
			sender.setDataHandler("KeyValueObject");
			Resource resource = new Resource(session.getUser(), "webSink_" + hashCode);
			sender.setSink(resource);
			sender.setWrapper("GenericPush");
			// TODO Reuse existing websocket sinks
			Option portOption = new Option("port", port++);
			Option websocketOption = new Option("websocket", true);
			List<Option> senderOptions = new ArrayList<>(2);
			senderOptions.add(portOption);
			senderOptions.add(websocketOption);
			sender.setOptions(senderOptions);
			
			// 5. Install query
			int websocketQueryId = ExecutorServiceBinding.getExecutor().addQuery(sender, session, "Standard");
			
			// 6. Start query
			ExecutorServiceBinding.getExecutor().startQuery(websocketQueryId, session);
			
			// 7. Add websocket to the returned information
			String protocol = "ws";
			String ip = "localhost";
			String url = protocol + "://" + ip + ":" + port + "/websocket";
			
			portMap.put(queryId, url);
		}

		return resultMap;
	}

}
