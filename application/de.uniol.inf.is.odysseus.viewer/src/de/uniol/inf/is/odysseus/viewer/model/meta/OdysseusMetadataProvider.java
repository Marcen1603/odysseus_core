package de.uniol.inf.is.odysseus.viewer.model.meta;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;

public class OdysseusMetadataProvider implements IMetadataProvider< IMonitoringData<?> > {

//	private static final Logger logger = LoggerFactory.getLogger( OdysseusMetadataProvider.class );
	
	private Collection<String> metadataTypes = new ArrayList<String>();
	
	public OdysseusMetadataProvider() {
		for (MonitoringDataTypes t:MonitoringDataTypes.values()){
			metadataTypes.add(t.name);
		}
	}

	@Override
	public Collection< String > getList() {
		return metadataTypes;
	}

	@Override
	public IMonitoringData< ? > createMetadata( String type, INodeModel<?> nodeModel ) {
		ISource<?> source = (ISource<?>)nodeModel.getContent();
		return MonitoringDataTypes.createMetadata(type, source);
	}

}
