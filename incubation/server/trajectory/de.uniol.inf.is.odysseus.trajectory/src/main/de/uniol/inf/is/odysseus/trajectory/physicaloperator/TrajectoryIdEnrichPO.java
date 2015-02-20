package de.uniol.inf.is.odysseus.trajectory.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * A physical operator for <tt>TrajectoryIdEnrichAO</tt> which keeps <i>vehicleIds</i>
 * and their <i>trajectoryIds</i> in a <tt>Map</i>. On each time a new <tt>Tuple</tt> arrives
 * the <i>trajectoryId</i> for the <i>vehicle</i> in the Tuple will be incremented.
 * 
 * @author marcus
 *
 * @param <T> the type of the processed data
 */
public class TrajectoryIdEnrichPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {

	/** stores the current trajectoryId for each vehicleId */
	private final Map<String, int[]> vehicleTrajectories = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(final T object, final int port) {
		final String vehicleId = object.getAttribute(0);
		int[] id = this.vehicleTrajectories.get(vehicleId);
		
		if(id == null) {
			this.vehicleTrajectories.put(vehicleId, id = new int[] { -1 });
		}
		final Tuple<ITimeInterval> t = new Tuple<ITimeInterval>(new Object[] { vehicleId, ++id[0], object.getAttribute(1), null}, true);
		t.setMetadata(object.getMetadata());
		this.transfer((T)t);
	}
	
	@Override
	public void processPunctuation(final IPunctuation punctuation, final int port) {
		this.sendPunctuation(punctuation, port);
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
