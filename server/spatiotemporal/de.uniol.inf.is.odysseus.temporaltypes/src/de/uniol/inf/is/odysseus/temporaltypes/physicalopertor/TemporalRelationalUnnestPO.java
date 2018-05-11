package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalUnNestPO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

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
					Object[] nonTempAttributeArray = (Object[]) tempType.getValue(time);
					Object nonTempAttribute = nonTempAttributeArray[0];
					Tuple<T> nonTemporalTuple = tuple.clone();
					nonTemporalTuple.setAttribute(this.nestedAttributePos, nonTempAttribute);
					unnestTuple(nonTemporalTuple);
				}
			}
		}
	}

}
