package de.uniol.inf.is.odysseus.net.rcp.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.CellLabelProviderAndSorter;

public class DistributedDataTableViewer extends AbstractTableViewer<IDistributedData> {

	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private final Map<OdysseusNodeID, String> nodeNameMap = Maps.newHashMap();

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
				return determineNodeName(data.getCreator());
			}
		});
		
		createColumn("Timestamp", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return convertTimestampToString(data.getTimestamp());
			}
		});
		
		createColumn("Persist", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				return String.valueOf(data.isPersistent());
			}
		});

		createColumn("Lifetime", 10, new CellLabelProviderAndSorter<IDistributedData, String>() {
			@Override
			protected String getValue(IDistributedData data) {
				if( data.isPersistent() ) {
					long lifetime = data.getLifetimeMillis();
					if( lifetime < 0 ) {
						return "infinite";
					}
					return String.valueOf(lifetime);
				} 
				return null;
			}
		});
	}

	private static String convertTimestampToString(long ts) {
		return TIME_FORMAT.format(new Date(ts));
	}

	private String determineNodeName(OdysseusNodeID odysseusNodeID) {
		if( OdysseusNetRCPPlugIn.getOdysseusNodeManager().isLocalNode(odysseusNodeID)) {
			return "<local>";
		}
		
		if( nodeNameMap.containsKey(odysseusNodeID)) {
			return nodeNameMap.get(odysseusNodeID);
		}
		
		Optional<IOdysseusNode> optNode = OdysseusNetRCPPlugIn.getOdysseusNodeManager().getNode(odysseusNodeID);
		if( optNode.isPresent() ) {
			nodeNameMap.put(odysseusNodeID, optNode.get().getName());
			
			return optNode.get().getName();
		} 
		return "<unknown>";
	}
}
