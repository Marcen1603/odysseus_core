package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;

public enum MonitoringDataTypes {
	
	SELECTIVITY("selectivity"),  ESTIMATED_SELECTIVITY("estimated selectivity"),
	PRODUCTIVITY("productivity"), ESTIMATED_PRODUCTIVITY("estimated productivity"),
	PROCESSING_COST("processing cost"), ESTIMATED_PROCESSING_COST("estimated processing cost"),
	DATARATE("datarate"), ESTIMATED_DATARATE("estimated datarate"),ELEMENTS_READ("elements read");

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}
	
	public final String name;

	private MonitoringDataTypes(String name) {
		this.name = name;
	}
	
	static public IMonitoringData< ? > createMetadata( String type, IPhysicalOperator source ){
		int in = 0;
		if (source instanceof ISink<?>){
			in = ((ISink<?>)source).getSubscribedToSource().size();
		}
		
		if( PRODUCTIVITY.name.equals( type )) {
			
			getLogger().debug( "Productivity-MetadataItem created" );
			return new Productivity(source, in);
		
		} else if( PROCESSING_COST.name.equals( type )) {
			
			getLogger().debug( "Processing cost-MetadataItem created" );
			return new AvgProcessingTime(source);
			
		}  else if( DATARATE.name.equals( type )) {
			
			getLogger().debug( "Datarate-MetadataItem created" );
			return new Datarate(source);
			
		}  else if( SELECTIVITY.name.equals( type )) {
			
			getLogger().debug( "Selectivity-MetadataItem created" );
			return new ClassicSelectivity(source, in);
			
		} else if( ELEMENTS_READ.name.equals( type )) {
			
			getLogger().debug( "Elementsread-MetadataItem created" );
			return new ElementsRead(source);
			
		} else {
			getLogger().warn( "Metadatatype " + type + " is unknown!" );
			return null;
		}

	}
}
