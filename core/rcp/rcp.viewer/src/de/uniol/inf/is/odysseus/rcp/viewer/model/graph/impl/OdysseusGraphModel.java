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
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;

public class OdysseusGraphModel extends DefaultGraphModel<IPhysicalOperator> implements IOdysseusGraphModel {

	private final IPhysicalQuery query;
	
	public OdysseusGraphModel() {
		this(null);
	}
	
	public OdysseusGraphModel( IPhysicalQuery query ) {
		super();
		this.query = query;
	}
	
	@Override
	public IPhysicalQuery getQuery() {
		return query;
	}
	
	@Override
	public String getName() {
		if( query != null ) 
			return "Query " + String.valueOf(query.getID());
		return super.getName();
	}

}
