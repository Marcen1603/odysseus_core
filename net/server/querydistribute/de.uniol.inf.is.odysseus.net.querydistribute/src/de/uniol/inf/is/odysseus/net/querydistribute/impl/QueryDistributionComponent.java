package de.uniol.inf.is.odysseus.net.querydistribute.impl;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class QueryDistributionComponent extends OdysseusNetComponentAdapter implements IOdysseusNetComponent {

	private static IOdysseusNode localNode;
	private static boolean isStarted = false;

	@Override
	public void init(IOdysseusNode local) throws OdysseusNetException {
		localNode = local;
	}

	@Override
	public void start() throws OdysseusNetException {
		isStarted = true;
	}

	@Override
	public void stop() {
		isStarted = false;
	}

	@Override
	public void terminate(IOdysseusNode localNode) {
		localNode = null;
	}
	
	public static boolean isStarted() {
		return isStarted;
	}
	
	public static IOdysseusNode getLocalNode() throws OdysseusNetException {
		if( !isStarted() ) {
			throw new OdysseusNetException("OdysseusNet component is not started!");
		}
		
		return localNode;
	}
}
