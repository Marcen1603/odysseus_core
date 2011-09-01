/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;

public enum MonitoringDataTypes {
	
	SELECTIVITY("selectivity"),  ESTIMATED_SELECTIVITY("estimated selectivity"),
	PRODUCTIVITY("productivity"), ESTIMATED_PRODUCTIVITY("estimated productivity"),
	PROCESSING_COST("processing cost"), ESTIMATED_PROCESSING_COST("estimated processing cost"),
	DATARATE("datarate"), ESTIMATED_DATARATE("estimated datarate"),ELEMENTS_READ("elements read"),
	AVERAGE_PROCESSING_TIME("average processing time"), MEDIAN_PROCESSING_TIME("median_processing_time");

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
			
			getLogger().debug( "Productivity-MetadataItem for "+source+" created" );
			return new Productivity(source, in);
		
		} else if( PROCESSING_COST.name.equals( type )) {
			
			getLogger().debug( "Processing cost-MetadataItem for "+source+" created" );
			return new AvgProcessingTime(source);
			
		}  else if( DATARATE.name.equals( type )) {
			
			getLogger().debug( "Datarate-MetadataItem for "+source+" created" );
			return new Datarate(source);
			
		}  else if( SELECTIVITY.name.equals( type )) {
			
			getLogger().debug( "Selectivity-MetadataItem for "+source+" created" );
			return new ClassicSelectivity(source, in);
		} else if( MEDIAN_PROCESSING_TIME.name.equals(type)) {
			getLogger().debug( "MedianProcessingTime for " + source + " created" );
			return new MedianProcessingTime(source);
			
		} else {
			getLogger().warn( "Metadatatype " + type + " is unknown!" );
			return null;
		}

	}
}
