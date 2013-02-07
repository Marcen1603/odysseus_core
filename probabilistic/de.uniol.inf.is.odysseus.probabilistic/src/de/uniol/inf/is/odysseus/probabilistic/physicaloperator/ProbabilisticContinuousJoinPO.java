package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticContinuousJoinPO<K extends ITimeInterval, T extends ProbabilisticTuple<K>>
		extends JoinTIPO<K, T> {
	private final int[] leftViewAttributePos;
	private final int[] leftJoinAttributePos;
	private final int[] rightViewAttributePos;
	private final int[] rightJoinAttributePos;
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ProbabilisticContinuousJoinPO.class);

	public ProbabilisticContinuousJoinPO(int[] leftViewAttributePos,
			int[] leftJoinAttributePos, int[] rightViewAttributePos,
			int[] rightJoinAttributePos) {
		super();
		this.leftViewAttributePos = leftViewAttributePos;
		this.leftJoinAttributePos = leftJoinAttributePos;
		this.rightViewAttributePos = rightViewAttributePos;
		this.rightJoinAttributePos = rightJoinAttributePos;
	}

	public int[] getLeftViewAttributePos() {
		return leftViewAttributePos;
	}

	public int[] getLeftJoinAttributePos() {
		return leftJoinAttributePos;
	}

	public int[] getRightViewAttributePos() {
		return rightViewAttributePos;
	}

	public int[] getRightJoinAttributePos() {
		return rightJoinAttributePos;
	}

	@Override
	protected void process_next(T object, int port) {
		transferFunction.newElement(object, port);
		if (isDone()) {
			return;
		}

		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);

		if (inOrder) {
			synchronized (this.areas[otherport]) {
				areas[otherport].purgeElements(object, order);
			}
		}

		synchronized (this) {
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, order);
			}
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			T newElement = dataMerge.merge(object, next, metadataMerge, order);

			if (order == Order.LeftRight) {
				for (int i = 0; i < this.leftViewAttributePos.length; i++) {
					newElement.setAttribute(this.leftViewAttributePos[i],
							this.areas[port]);
				}
				for (int i = 0; i < this.rightViewAttributePos.length; i++) {
					newElement.setAttribute(object.getAttributes().length
							+ this.rightViewAttributePos[i],
							this.areas[otherport]);
				}
			} else {
				for (int i = 0; i < this.leftViewAttributePos.length; i++) {
					newElement.setAttribute(this.leftViewAttributePos[i],
							this.areas[otherport]);
				}
				for (int i = 0; i < this.rightViewAttributePos.length; i++) {
					newElement.setAttribute(newElement.getAttributes().length
							- object.getAttributes().length
							+ this.rightViewAttributePos[i], this.areas[port]);
				}
			}

			transferFunction.transfer(newElement);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
