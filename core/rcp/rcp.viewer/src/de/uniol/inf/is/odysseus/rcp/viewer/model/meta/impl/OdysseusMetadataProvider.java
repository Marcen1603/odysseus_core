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
package de.uniol.inf.is.odysseus.rcp.viewer.model.meta.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataProvider;

public class OdysseusMetadataProvider implements IMetadataProvider< IMonitoringData<?> > {

//	private static final Logger logger = LoggerFactory.getLogger( OdysseusMetadataProvider.class );
	
	private Collection<String> metadataTypes = new ArrayList<String>();
	
	public OdysseusMetadataProvider() {
		for (MonitoringDataTypes t: MonitoringDataTypes.values()){
			metadataTypes.add(t.name);
		}
	}

	@Override
	public Collection< String > getList() {
		return metadataTypes;
	}

	@Override
	public IMonitoringData< ? > createMetadata( String type, INodeModel<?> nodeModel ) {
//		ISource<?> source = (ISource<?>)nodeModel.getContent();
//		return MonitoringDataTypes.createMetadata(type, source);
		return null;
	}

}
