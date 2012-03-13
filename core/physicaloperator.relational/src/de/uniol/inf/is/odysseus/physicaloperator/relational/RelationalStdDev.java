package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalStdDev
		extends
		AbstractListAggregation<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8289285906323304991L;
	final int attribPos;

	public RelationalStdDev(int[] pos) {
		super("STDDEV");
		this.attribPos = pos[0];
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> evaluate(
			IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		ListPartialAggregate<RelationalTuple<? extends IMetaAttribute>> list = (ListPartialAggregate<RelationalTuple<? extends IMetaAttribute>>) p;
		int n = list.size();
		if (n > 0) {
			// Calc Average
			double sum = 0;
			for (RelationalTuple<? extends IMetaAttribute> tuple : list) {
				sum = sum
						+ ((Number) (tuple.getAttribute(attribPos)))
								.doubleValue();
			}
			double avg = sum / n;
			// Calc Sum
			double stddev = 0.0;
			for (RelationalTuple<? extends IMetaAttribute> tuple : list) {
				stddev += Math.pow((((Number) (tuple.getAttribute(attribPos)))
						.doubleValue() - avg), 2);
			}
			stddev = (1.0 / (n-1.0)) * stddev;
			stddev = Math.sqrt(stddev);
			RelationalTuple<IMetaAttribute> returnVal = new RelationalTuple<IMetaAttribute>(
					1);
			returnVal.setAttribute(0, stddev);
			return returnVal;
		}
        return null;
	}

}
