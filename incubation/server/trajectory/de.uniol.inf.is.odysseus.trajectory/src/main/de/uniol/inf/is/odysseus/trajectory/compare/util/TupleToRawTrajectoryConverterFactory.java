package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;
import de.uniol.inf.is.odysseus.trajectory.util.AbstractSimpleFactory;

public class TupleToRawTrajectoryConverterFactory extends AbstractSimpleFactory<ITupleToRawTrajectoryConverter> {

	private final static TupleToRawTrajectoryConverterFactory INSTANCE = new TupleToRawTrajectoryConverterFactory();
	
	public static TupleToRawTrajectoryConverterFactory getInstance() {
		return INSTANCE;
	}

	@Override
	protected ITupleToRawTrajectoryConverter internalCreate() {
		return new DefaultTupleToRawTrajectoryConverter();
	}
	

}
