package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;

public class DistributedDataView extends ViewPart implements IDistributedDataListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataView.class);

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.DistributedData";

	private final Collection<IDistributedData> dataContainer = Lists.newArrayList();

	private DistributedDataTableViewer ddViewer;

	@Override
	public void createPartControl(Composite parent) {
		OdysseusNetRCPPlugIn.getDistributedDataManager().addListener(this);
		ddViewer = new DistributedDataTableViewer(parent, dataContainer);

		if (OdysseusNetRCPPlugIn.getDistributedDataManager().isStarted()) {
			fillTable();
		}
	}

	private void fillTable() {
		try {
			Collection<UUID> uuids = OdysseusNetRCPPlugIn.getDistributedDataManager().getUUIDs();
			for (UUID uuid : uuids) {
				Optional<IDistributedData> optData = OdysseusNetRCPPlugIn.getDistributedDataManager().get(uuid);
				if (optData.isPresent()) {
					IDistributedData dd = optData.get();
					if( !dataContainer.contains(dd)) {
						dataContainer.add(dd);
					}
				}
			}
		} catch (DistributedDataException e) {
			LOG.error("Could not get distributed data for view", e);
		}

		if (!dataContainer.isEmpty()) {
			ddViewer.refreshTableAsync();
		}
	}

	@Override
	public void setFocus() {
		ddViewer.getTableViewer().getTable().setFocus();
	}

	@Override
	public void dispose() {
		OdysseusNetRCPPlugIn.getDistributedDataManager().removeListener(this);

		super.dispose();
	}

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		synchronized (dataContainer) {
			dataContainer.add(addedData);
		}

		ddViewer.refreshTableAsync();
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		synchronized (dataContainer) {
			dataContainer.remove(oldData);
			dataContainer.add(newData);
		}

		ddViewer.refreshTableAsync();
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		synchronized (dataContainer) {
			dataContainer.remove(removedData);
		}

		ddViewer.refreshTableAsync();
	}

	@Override
	public void distributedDataManagerStarted(IDistributedDataManager sender) {
		fillTable();
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		synchronized( dataContainer ) {
			dataContainer.clear();
		}
		
		ddViewer.refreshTableAsync();
	}
}
