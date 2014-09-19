package de.uniol.inf.is.odysseus.peer.rcp.views;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;


public class SmartDeviceView extends ViewPart implements IP2PDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartDeviceView.class);
	private TableViewer smartDevicesTable;


	@Override
	public void createPartControl(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		smartDevicesTable = new TableViewer(tableComposite, SWT.SINGLE);
		smartDevicesTable.getTable().setHeaderVisible(true);
		smartDevicesTable.getTable().setLinesVisible(true);
		smartDevicesTable.setContentProvider(ArrayContentProvider.getInstance());
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		// TODO Auto-generated method stub
		LOG.debug("sourceAdded");
		
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		// TODO Auto-generated method stub
		LOG.debug("sourceRemoved");
		
	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		LOG.debug("sourceImported");
		
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		LOG.debug("sourceImportRemoved");
		
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		LOG.debug("sourceExported");
		
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		// TODO Auto-generated method stub
		LOG.debug("sourceExportRemoved");
		
	}
}
