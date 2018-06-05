package de.uniol.inf.is.odysseus.net.data;

public class DistributedDataAdapter implements IDistributedDataListener {

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		// do nothing
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		// do nothing
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		// do nothing
	}

	@Override
	public void distributedDataManagerStarted(IDistributedDataManager sender) {
		// do nothing
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		// do nothing
	}

}
