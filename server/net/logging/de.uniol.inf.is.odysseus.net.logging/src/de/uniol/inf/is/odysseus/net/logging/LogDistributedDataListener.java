package de.uniol.inf.is.odysseus.net.logging;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class LogDistributedDataListener implements IDistributedDataListener {

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		
	}

}
