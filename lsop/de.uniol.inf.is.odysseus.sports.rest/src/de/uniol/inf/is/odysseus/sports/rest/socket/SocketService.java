package de.uniol.inf.is.odysseus.sports.rest.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ByteBufferSinkStreamHandlerBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SocketSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.sports.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sports.rest.dao.SocketInfo;
import de.uniol.inf.is.odysseus.sports.rest.exception.InvalidUserDataException;

public class SocketService {
	
	private Map<Integer, Map<Integer, Integer>> socketPortMap = new HashMap<>();
	private Map<Integer, Map<Integer, SocketSinkPO>> socketSinkMap = new HashMap<>();
	private InetAddress address;
	
	private static final int SINK_MIN_PORT = 10000;
	private static final int SINK_MAX_PORT = 20000;
	
	private static SocketService instance;
	
	private SocketService () {}
	
	  public static SocketService getInstance () {
	    if (SocketService.instance == null) {
	    	SocketService.instance = new SocketService ();
	    }
	    return SocketService.instance;
	  }
	
	
	public SocketInfo getConnectionInformation(String securityToken, int queryId)
			throws InvalidUserDataException {

		return getConnectionInformationWithPorts(securityToken, queryId,0,
				Integer.valueOf(OdysseusConfiguration.getInt("minSinkPort",
						SINK_MIN_PORT)), Integer.valueOf(OdysseusConfiguration
						.getInt("maxSinkPort", SINK_MAX_PORT)));
	}
	
	public SocketInfo getConnectionInformationWithPorts(
			String securityToken,
			 int queryId,
			 int rootPort,
			 int minPort,
			 int maxPort)
			throws InvalidUserDataException {
		try {
			loginWithSecurityToken(securityToken);
			int port = 0;
			SocketSinkPO po;
			Map<Integer, Integer> socketPortMapEntry = socketPortMap.get(queryId);
			Map<Integer, SocketSinkPO> socketSinkMapEntry = socketSinkMap.get(queryId);
			
			if (socketSinkMapEntry != null) {
				if (socketSinkMapEntry.get(rootPort) == null) {
					port = getNextFreePort(minPort, maxPort);
					po = addSocketSink(queryId,rootPort, port);
					socketSinkMapEntry.put(rootPort, po);
					socketPortMapEntry.put(rootPort, port);					
				} else {
					port = socketPortMapEntry.get(rootPort);
					po = socketSinkMapEntry.get(rootPort);
				}
			} else {
				socketSinkMapEntry = new HashMap<>();
				socketPortMapEntry = new  HashMap<>();
				socketPortMap.put(queryId, socketPortMapEntry);
				socketSinkMap.put(queryId, socketSinkMapEntry);			
				port = getNextFreePort(minPort, maxPort);
				po = addSocketSink(queryId,rootPort, port);
				socketSinkMapEntry.put(rootPort, po);
				socketPortMapEntry.put(rootPort, port);
			}
			po.addAllowedSessionId(securityToken);
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
			
			SocketInfo socketInfo = new SocketInfo(InetAddress.getLocalHost().getHostAddress(), port);
			
			return socketInfo;
			
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	private int getNextFreePort(int min, int max) {
		int port = 0;
		do {
			port = min + (int) (Math.random() * ((max - min) + 1));
		} while (socketPortMap.containsKey(port));
		return port;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SocketSinkPO addSocketSink(int queryId, int rootPort, int port) {
		IExecutionPlan plan = ExecutorServiceBinding.getExecutor().getExecutionPlan();
		IPhysicalQuery query = plan.getQueryById(queryId);
		List<IPhysicalOperator> roots = query.getRoots();
		IPhysicalOperator root = null;
		
		/*
		 * Workaround 
		 * query.getRoots gibt mehr roots zurück auch vom anderen Query. vllt. ein Bug im Odysseus
		 */
		for (IPhysicalOperator iPhysicalOperator : roots) {
			if (!(iPhysicalOperator instanceof SocketSinkPO)) {
				List<IOperatorOwner> ownerList = iPhysicalOperator.getOwner();
				for (IOperatorOwner iOperatorOwner : ownerList) {
					if (iOperatorOwner.getID() == queryId) {
						root = iPhysicalOperator;
					}
				}
			}
		}
	
		if(root != null){
			final ISource<?> rootAsSource = (ISource<?>) root;

			IDataHandler<?> handler = new TupleDataHandler().getInstance(root
					.getOutputSchema());
			ByteBufferHandler<Tuple<ITimeInterval>> objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(
					handler);
			SocketSinkPO sink = new SocketSinkPO(port, "",
					new ByteBufferSinkStreamHandlerBuilder(), true, false,
					false, objectHandler, false);
			
			rootAsSource.subscribeSink((ISink) sink, 0, 0,
					root.getOutputSchema(), true, 0);
			sink.startListening();
			return sink;
		}
		return null;
		
	}
	
	protected ISession loginWithSecurityToken(String securityToken)
			throws InvalidUserDataException {
		ISession session = UserManagementProvider.getSessionmanagement().login(
				securityToken);
		if (session != null) {
			return session;
		}
		throw new InvalidUserDataException(
				"Security token unknown! You have to login first to obtain a security token!");
	}
	
	public ISession login(){
		String passwordText = "manager";
		byte[] password = passwordText.getBytes();
		ITenant tenant = UserManagementProvider.getTenant("");
		ISession session = UserManagementProvider.getSessionmanagement().login("System", password, tenant);
		
		return session;
	}

}
