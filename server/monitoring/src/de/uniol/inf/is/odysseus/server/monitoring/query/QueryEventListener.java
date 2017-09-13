package de.uniol.inf.is.odysseus.server.monitoring.query;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryEventListener implements IPOEventListener {
	private Measurement measurement;

	public QueryEventListener(IPhysicalQuery query, Measurement m) {
		this.measurement = m;
		for (IPhysicalOperator o : query.getPhysicalChilds()) {
			if (!query.getLeafSources().contains(o) && !query.getIterableSources().contains(o)
					&& !query.getIteratableLeafSources().contains(o)) {
				o.subscribe(this, POEventType.PushInit);
				o.subscribe(this, POEventType.ProcessInit);
			}
		}
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		new ThreadCalculateLatency(this.measurement, event, nanoTimestamp);
	}
}
