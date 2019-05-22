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
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQueryStarter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SubQueryPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IQueryStarter {

	private static final int MINSUBQUERYPORT = 100000;
	private static final Logger LOG = LoggerFactory.getLogger(SubQueryPO.class);
	private final IPhysicalQuery query;
	private final List<ConnectorPO> leafs = new ArrayList<>();
	private final IServerExecutor executor;
	private final ISession session;

	public SubQueryPO(IPhysicalQuery query, IServerExecutor executor, ISession session) {
		this.executor = executor;
		this.session = session;
		this.query = query;
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

		// Check if all ports are different and continous
		Iterator<ConnectorPO> iter = leafs.iterator();
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

	public IPhysicalQuery getPhysicalQuery() {
		return query;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		LOG.warn("Punctuations are currently not handled by subquerypo");
	}

	@Override
	public OutputMode getOutputMode() {
		// use most restrictive mode
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() {
		// Attach query roots as inputs of this operator
		List<IPhysicalOperator> roots = query.getRoots();
		// To separate from real input use higher Ports
		int sinkInPort = MINSUBQUERYPORT;
		for (IPhysicalOperator root : roots) {
			ISource<IStreamObject<?>> s = ((ISource<IStreamObject<?>>) root);
			s.connectSink((ISink<IStreamObject<?>>) this, sinkInPort++, 0, s.getOutputSchema());
		}
		executor.startQuery(query.getID(), session);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// Output from connected query
		if (port >= MINSUBQUERYPORT) {
			transfer(object, port - MINSUBQUERYPORT);
		} else {
			if (port < leafs.size()) {
				((ISource<T>) leafs.get(port)).transfer(object);
			} else {
				throw new RuntimeException("Input for not connected operator");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_close() {
		executor.stopQuery(query.getID(), session);

		for (IPhysicalOperator root : query.getRoots()) {
			ISource<IStreamObject<?>> s = ((ISource<IStreamObject<?>>) root);
			// To avoid concurrent modification exception, determine connections to remove
			// first
			Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> subs = s.getConnectedSinks();
			Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> toRemove = new ArrayList<>();
			subs.forEach(sub -> {
				if (sub.getSink() == this)
					toRemove.add(sub);
			});
			toRemove.forEach(sub -> s.disconnectSink(sub));

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_done(int port) {

		// Output from connected query
		if (port >= MINSUBQUERYPORT) {
			process_done(port - MINSUBQUERYPORT);
		} else {
			if (port < leafs.size()) {
				((ISource<T>) leafs.get(port)).propagateDone();
			} else {
				throw new RuntimeException("Input for not connected operator");
			}
		}
	}

	@Override
	public void done(PhysicalQuery physicalQuery) {
		propagateDone();
	}

}
