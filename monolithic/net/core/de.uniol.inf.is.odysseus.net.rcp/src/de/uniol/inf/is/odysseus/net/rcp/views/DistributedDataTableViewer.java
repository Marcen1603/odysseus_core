package de.uniol.inf.is.odysseus.net.rcp.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;

public class DistributedDataTableViewer extends AbstractTableViewer<IDistributedData> {

	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	public DistributedDataTableViewer(Composite parent, Collection<IDistributedData> dataContainer) {
		super(parent, dataContainer);
	}

	@Override
	protected void createColumns(TableViewer tableViewer, TableColumnLayout tableColumnLayout) {
		createColumn("Name", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return data.getName();
			}
		});
		
		createColumn("UUID", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return data.getUUID().toString();
			}
		});
		
		createColumn("JSON-Data", 30, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return data.getData().toString();
			}
		});
		
		createColumn("Creator", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				Optional<IOdysseusNode> optNode = OdysseusNetRCPPlugIn.getOdysseusNodeManager().getNode(data.getCreator());
				if( optNode.isPresent() ) {
					return optNode.get().getName();
				} 
				return "<unknown>";
			}
		});
		
		createColumn("Timestamp", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return convertTimestampToString(data.getTimestamp());
			}
		});
	}

	private static String convertTimestampToString(long ts) {
		return TIME_FORMAT.format(new Date(ts));
	}

}
