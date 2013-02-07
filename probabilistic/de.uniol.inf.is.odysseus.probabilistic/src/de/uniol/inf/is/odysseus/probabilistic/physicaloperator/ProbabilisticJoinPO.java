package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticJoinPO
		extends
		JoinTIPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> {
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ProbabilisticJoinPO.class);
	private final int[] leftViewAttributePos;
	private final int[] rightViewAttributePos;

	public ProbabilisticJoinPO(int[] leftViewAttributePos,
			int[] rightViewAttributePos) {
		super();
		this.leftViewAttributePos = leftViewAttributePos;
		this.rightViewAttributePos = rightViewAttributePos;
	}

	@Override
	protected void process_next(
			ProbabilisticTuple<ITimeIntervalProbabilistic> object, int port) {
		Order order = Order.fromOrdinal(port);
		if (order == Order.LeftRight) {
			for (int i = 0; i < leftViewAttributePos.length; i++) {
				AbstractProbabilisticValue<?> viewAttribute = (AbstractProbabilisticValue<?>) object
						.getAttribute(leftViewAttributePos[i]);
				for (Entry<?, Double> value : viewAttribute.getValues()
						.entrySet()) {
					ProbabilisticTuple<ITimeIntervalProbabilistic> copy = object
							.clone();
					copy.setAttribute(leftViewAttributePos[i], value.getKey());
					ITimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
					metadata.setExistence(copy.getMetadata().getExistence()
							* value.getValue());
					copy.setMetadata(metadata);
					super.process_next(copy, port);
				}
			}
		} else {
			for (int i = 0; i < rightViewAttributePos.length; i++) {
				AbstractProbabilisticValue<?> viewAttribute = (AbstractProbabilisticValue<?>) object
						.getAttribute(rightViewAttributePos[i]);
				for (Entry<?, Double> value : viewAttribute.getValues()
						.entrySet()) {
					ProbabilisticTuple<ITimeIntervalProbabilistic> copy = object
							.clone();
					copy.setAttribute(rightViewAttributePos[i], value.getKey());
					ITimeIntervalProbabilistic metadata = new TimeIntervalProbabilistic();
					metadata.setExistence(copy.getMetadata().getExistence()
							* value.getValue());
					copy.setMetadata(metadata);
					super.process_next(copy, port);
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
