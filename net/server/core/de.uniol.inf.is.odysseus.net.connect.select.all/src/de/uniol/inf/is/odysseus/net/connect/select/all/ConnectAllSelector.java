package de.uniol.inf.is.odysseus.net.connect.select.all;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelector;

public class ConnectAllSelector implements IOdysseusNodeConnectionSelector {

	@Override
	public boolean doConnect(IOdysseusNode node) {
		return true;
	}

}
