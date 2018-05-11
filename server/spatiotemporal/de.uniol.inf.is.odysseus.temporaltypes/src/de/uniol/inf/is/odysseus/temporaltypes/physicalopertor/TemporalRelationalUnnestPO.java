package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalUnNestPO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * This operator unnests every tuple for every valid point in time.
 * 
 * @author Tobias Brandt
 */
public class TemporalRelationalUnnestPO<T extends IValidTimes> extends RelationalUnNestPO<T> {

	public TemporalRelationalUnnestPO(RelationalUnNestPO<T> po) {
		super(po);
	}

	@Override
	protected void process_next(final Tuple<T> tuple, final int port) {
		Object attribute = tuple.getAttribute(this.nestedAttributePos);
		if (attribute instanceof TemporalType) {
			TemporalType<?> tempType = (TemporalType<?>) attribute;
			for (IValidTime validTime : tuple.getMetadata().getValidTimes()) {
				for (PointInTime time = validTime.getValidStart(); time
						.before(validTime.getValidEnd()); time = time.plus(1)) {
					Tuple<T> nonTemporalTuple = getNonTemporalTuple(tuple, time, tempType);
					unnestTuple(nonTemporalTuple);
				}
			}
		}
	}

	private Tuple<T> getNonTemporalTuple(Tuple<T> tuple, PointInTime time, TemporalType<?> temporalAttribute) {
		Object[] nonTempAttributeArray = (Object[]) temporalAttribute.getValue(time);
		Object nonTempAttribute = nonTempAttributeArray[0];
		Tuple<T> nonTemporalTuple = tuple.clone();
		nonTemporalTuple.setAttribute(this.nestedAttributePos, nonTempAttribute);
		return nonTemporalTuple;
	}

}
