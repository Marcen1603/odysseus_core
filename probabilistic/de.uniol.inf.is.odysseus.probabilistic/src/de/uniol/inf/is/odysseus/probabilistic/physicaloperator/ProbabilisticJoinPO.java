package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticJoinPO<K extends ITimeInterval, T extends IStreamObject<K>>
		extends JoinTIPO<K, T> {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ProbabilisticJoinPO.class);

	public ProbabilisticJoinPO() {
		super();
	}

	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
