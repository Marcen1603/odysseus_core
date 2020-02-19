package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This physical operator can encapsulate another physical query and can be used
 * to define some kind of virtual operators.
 * 
 * When the operator is started, the contained query will be connected. For
 * this, the contained physical query must provide some inputs as ConnectorPOs.
 * The ports must correspond to the inputs of this operator, i.e. inputs on port
 * 0 with be delivered to the connector of the physical query with the port 0.
 * The contained query must contain at least one connector with port 0 (Remark,
 * if there is no input, use views instead) and the port numbers must be
 * continuous, e.g.: 0, 1, 2, ...
 * 
 * The operator currently send all outputs from the contained query to a single
 * output, even if there are more roots in the contained query.
 * 
 * The operator is physically connected to its inputs in the regular plan and
 * additionally to the outputs (roots) of the contained query.
 * 
 * When the operator is stopped, the contained query will also stop and the
 * connection will be removed.
 * 
 * @author Marco Grawunder
 *
 * @param <T>
 */

public class SubQueryPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private static final int MINSUBQUERYPORT = 100000;
	private static final Logger LOG = LoggerFactory.getLogger(SubQueryPO.class);
	/**
	 * The internal physical query
	 */
	private final IPhysicalQuery query;
	/**
	 * The assignment of inputs of the contained query to the ports of this operator
	 * i.e. the first element in the list is for input port 0, the second for input
	 * port 1 etc.
	 */
	private final List<ConnectorPO> leafs = new ArrayList<>();

	/**
	 * A reference to the current executor to allow starting and stopping of
	 * queries.
	 */
	private final IServerExecutor executor;
	/**
	 * The current user. This is needed for calling methods in the executor.
	 */
	private final ISession session;

	/**
	 * Create a new subquery po.
	 * 
	 * @param query
	 *            The query to execute
	 * @param executor
	 *            Reference to the executor is needed, to start and stop contained
	 *            query
	 * @param session
	 *            The user, who created this query. Is needed to call start and stop
	 *            on the executor
	 */
	public SubQueryPO(IPhysicalQuery query, IServerExecutor executor, ISession session) {
		this.executor = executor;
		this.session = session;
		this.query = query;

		determineLeafSources(query);
		validateLeafSources();
	}

	private void determineLeafSources(IPhysicalQuery query) {
		List<IPhysicalOperator> ops = query.getLeafSources();
		for (IPhysicalOperator o : ops) {
			if (o instanceof ConnectorPO) {
				leafs.add((ConnectorPO) o);
			}
		}
		Collections.sort(leafs, new Comparator<ConnectorPO>() {
			@Override
			public int compare(ConnectorPO o1, ConnectorPO o2) {
				return Integer.compare(o1.getPort(), o2.getPort());
			}
		});
	}

	private void validateLeafSources() {
		// Check if all ports are different and continuous
		Iterator<ConnectorPO> iter = leafs.iterator();

		if (!iter.hasNext()) {
			return;
//			throw new IllegalArgumentException(
//					"SubQuery must contain at least one connector. For the other case use streams or views!");
		}

		int lastPort = iter.next().getPort();
		if (lastPort != 0) {
			throw new IllegalArgumentException("Connector ports of SubQuery must start with 0!");
		}
		while (iter.hasNext()) {
			int currentPort = iter.next().getPort();
			if (lastPort + 1 != currentPort) {
				throw new IllegalArgumentException("Connector ports of SubQuery must be continious!");
			}
			lastPort = currentPort;
		}
	}

	/**
	 * Returns the physical query
	 * 
	 * @return
	 */
	public IPhysicalQuery getPhysicalQuery() {
		return query;
	}

	@Override
	public OutputMode getOutputMode() {
		// use the most restrictive mode (i.e. clone every time)
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() {
		// Attach query roots as inputs of this operator
		List<IPhysicalOperator> roots = query.getRoots();
		// To separate from real input use higher Ports
		int sinkInPort = MINSUBQUERYPORT;
		boolean connectorsUsed = false;
		for (IPhysicalOperator root : roots) {
			ISource<IStreamObject<?>> s = ((ISource<IStreamObject<?>>) root);
			final int port;
			if (s instanceof OutputConnectorPO ) {
				OutputConnectorPO<IStreamObject<?>> outConnectorPO = (OutputConnectorPO<IStreamObject<?>>)s;
				connectorsUsed = true;
				port = outConnectorPO.getPort()+MINSUBQUERYPORT;
			}else{
				if (connectorsUsed) {
					throw new RuntimeException("When using output connectors, each root needs a connector! Found "+root);
				}
				port = sinkInPort++;
			}
			s.connectSink((ISink<IStreamObject<?>>) this, port, 0, s.getOutputSchema());

			
		}
		// Start query after connection (else there could some elements get lost)
		executor.startQuery(query.getID(), session);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// Need to separate Output from connected query (port >= MINSUBQUERYPORT) and
		// the standard connected input
		if (port >= MINSUBQUERYPORT) {
			transfer(object, port - MINSUBQUERYPORT);
		} else {
			// send to the connected plan
			if (port < leafs.size()) {
				((ISource<T>) leafs.get(port)).transfer(object);
			} else {
				LOG.warn("Input for not connected operator");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Similar handling as in process_next
		if (port >= MINSUBQUERYPORT) {
			sendPunctuation(punctuation, port - MINSUBQUERYPORT);
		} else {
			if (port < leafs.size()) {
				((ISource<T>) leafs.get(port)).sendPunctuation(punctuation);
			} else {
				LOG.warn("Punctuation for not connected operator");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_done(int port) {
		// Similar handling as in process_next
		if (port >= MINSUBQUERYPORT) {
			process_done(port - MINSUBQUERYPORT);
		} else {
			if (port < leafs.size()) {
				((ISource<T>) leafs.get(port)).propagateDone();
			} else if(port == 0 && leafs.isEmpty()) {
				return;
			} else {
				LOG.warn("Input for not connected operator");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_close() {
		// First stop contained query
		executor.stopQuery(query.getID(), session);

		// Disconnect from query
		for (IPhysicalOperator root : query.getRoots()) {
			ISource<IStreamObject<?>> s = ((ISource<IStreamObject<?>>) root);
			// To avoid concurrent modification exception, determine connections to remove
			// first and then do the removal
			Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> subs = s.getConnectedSinks();
			Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> toRemove = new ArrayList<>();
			subs.forEach(sub -> {
				if (sub.getSink() == this)
					toRemove.add(sub);
			});
			toRemove.forEach(sub -> s.disconnectSink(sub));
		}
	}

}
