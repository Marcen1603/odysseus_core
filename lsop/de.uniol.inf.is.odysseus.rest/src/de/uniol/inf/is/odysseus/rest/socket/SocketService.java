package de.uniol.inf.is.odysseus.rest.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.NioByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;

public class SocketService {

	private Map<Integer, SocketSinkPO> socketSinkMap = new HashMap<>();
	private Map<Integer, Integer> socketPortMap = new HashMap<>();

	private InetAddress address;

	private static final int SINK_MIN_PORT = 10000;
	private static final int SINK_MAX_PORT = 20000;

	private static SocketService instance;

	private SocketService() {
	}

	public static SocketService getInstance() {
		if (SocketService.instance == null) {
			SocketService.instance = new SocketService();
		}
		return SocketService.instance;
	}

	public SocketInfo getConnectionInformation(ISession session, int queryId, int rootPort)  {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort",SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		IPhysicalOperator rootOperator = getRootOperator(queryId, rootPort);
		return getConnectionInformationWithPorts(session, queryId,minPort, maxPort, rootOperator);
	}
	
	public SocketInfo getConnectionInformation(ISession session, int queryId, IPhysicalOperator operator)  {
		int minPort = Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort",SINK_MIN_PORT));
		int maxPort = Integer.valueOf(OdysseusConfiguration.getInt("maxSinkPort", SINK_MAX_PORT));
		return getConnectionInformationWithPorts(session, queryId,minPort, maxPort, operator);
	}


	public SocketInfo getConnectionInformationWithPorts(ISession session,int queryId, int minPort, int maxPort, IPhysicalOperator operator)  {
		try {
			int port = 0;
			SocketSinkPO po = socketSinkMap.get(queryId);
			if (po == null) {
				port = getNextFreePort(minPort, maxPort);
				po = addSocketSink(queryId, port, operator);
				socketSinkMap.put(queryId,  po);
				socketPortMap.put(queryId, port);
			} else {
				port = socketPortMap.get(queryId);
				po = socketSinkMap.get(queryId);
			}
			
			po.addAllowedSessionId(session.getToken());
			if (this.address == null) {

				Enumeration<NetworkInterface> interfaces = NetworkInterface
						.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface ni = interfaces.nextElement();
					if (ni.isVirtual()) {
						continue;
					}
					if (ni.isLoopback()) {
						continue;
					}
					if (ni.getInetAddresses().hasMoreElements()) {
						this.address = ni.getInetAddresses().nextElement();
					}
				}
			}

			SocketInfo socketInfo = new SocketInfo(InetAddress.getLocalHost().getHostAddress(), port, this.getAttributeInformationList(operator));

			return socketInfo;

		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
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

	private int getNextFreePort(int min, int max) {
		int port = 0;
		do {
			port = min + (int) (Math.random() * ((max - min) + 1));
		} while (socketPortMap.containsKey(port));
		return port;
	}
	
	private IPhysicalOperator getRootOperator(int queryId, int rootPort) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		return query.getRoots().get(rootPort);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SocketSinkPO addSocketSink(int queryId, int port, IPhysicalOperator root) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		

		final ISource<?> rootAsSource = (ISource<?>) root;

		//IDataHandler<?> handler = new TupleDataHandler().getInstance(root.getOutputSchema());
		NullAwareTupleDataHandler handler = new NullAwareTupleDataHandler(root.getOutputSchema());

		ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(handler);
		

		
		SocketSinkPO sink = new SocketSinkPO(port, "",
				new NioByteBufferSinkStreamHandlerBuilder(), true, false, false,
				objectHandler, false, false);

		rootAsSource.subscribeSink((ISink) sink, 0, 0, root.getOutputSchema(),
				true, 0);
		sink.startListening();
		sink.addOwner(query);
		return sink;

	}

}
