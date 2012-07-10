/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.join.object;

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.join.object.merge.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.join.object.merge.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;

/**
 * Implements a merge function for RelationalTuples. A full outer join is
 * implemented (left or right can be null), so make sure you pass the right
 * parameters to merge, if you don't want outer joins to happen.
 * 
 * @author Andre Bolles
 */
public class NodeListMergeFunction<T extends IClone> implements IDataMergeFunction<NodeList<T>> {

	private SDFSchema leftSchema;
	private SDFSchema rightSchema;
	private SDFSchema resultSchema;
	

	public NodeListMergeFunction(SDFSchema leftSchema, 
			SDFSchema rightSchema, 
			SDFSchema resultSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
	}

	public NodeList<T> merge(NodeList<T> left, NodeList<T> right) {
		
		NodeList<T> newCargo = new NodeList<T>(this.resultSchema.size());
		// first fill the list with null, to be able to use set(index, elem)
		for(int i = 0; i<this.resultSchema.size(); i++){
			newCargo.add(null);
		}
		
		// do not add the elements, but replace them, because the list is null filled
		for(int i = 0; i<left.size(); i++){
			newCargo.set(i, left.get(i));
		}
		
		for(int i = 0; i<right.size(); i++){
			// there must always be a match, also if
			// there is not an attribute from the left
			// at the index
			for(int u = 0; u<this.resultSchema.size(); u++){
				if(SPARQL_Util.refersSameVar(this.rightSchema.get(i),
						this.resultSchema.get(u))){
					newCargo.set(u, right.get(i));
				}
			}
		}
		return newCargo;
	}
	
	public void init(){
	}
}
