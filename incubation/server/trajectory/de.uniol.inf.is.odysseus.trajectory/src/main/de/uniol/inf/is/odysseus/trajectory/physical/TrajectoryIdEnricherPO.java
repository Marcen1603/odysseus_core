package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TrajectoryIdEnricherPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	
	private final Map<String, int[]> vehicleTrajectories = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		final String vehicleId = object.getAttribute(0);
		int[] id = this.vehicleTrajectories.get(vehicleId);
		
		if(id == null) {
			this.vehicleTrajectories.put(vehicleId, id = new int[] { -1 });
		}
		Tuple<ITimeInterval> t = new Tuple<ITimeInterval>(new Object[] { vehicleId, ++id[0], object.getAttribute(1), null}, true);
		t.setMetadata(object.getMetadata());
		this.transfer((T)t);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		
		// TODO: Implement me!
		
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	protected void process_close() {
		this.vehicleTrajectories.clear();
	}
}
