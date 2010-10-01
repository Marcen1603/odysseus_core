package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class IntegrationPipe extends AbstractPipe {

	private int curPort = 0;

	@Override
	public IntegrationPipe clone() {
		return new IntegrationPipe();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Object object, int port) {
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public void subscribeToSource(ISource source, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		PhysicalSubscription<ISource> sub = new PhysicalSubscription<ISource>(
				source, curPort - 1, sourceOutPort, schema);
		if (!getSubscribedToSource().contains(sub)) {
			super.subscribeToSource(source, curPort++, sourceOutPort, schema);
		}
	}
	
	@Override
	protected void process_done(int port) {
		super.process_done(port);
	}
	
}
