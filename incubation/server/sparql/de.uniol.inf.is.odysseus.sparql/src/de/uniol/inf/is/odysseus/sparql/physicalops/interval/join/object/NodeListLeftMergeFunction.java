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

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.join.object.merge.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.join.object.merge.LeftMergeFunction;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.predicate.NodeListCompatiblePredicate;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.predicate.SPARQLFilterPredicate;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;

public class NodeListLeftMergeFunction<M extends IClone> extends LeftMergeFunction<NodeList<M>> {
	
	public NodeListLeftMergeFunction(
			SDFSchema leftSchema, 
			SDFSchema rightSchema, 
			SDFSchema resultSchema){
		
		super(leftSchema, rightSchema, resultSchema);
	}
	
	/**
	 * Falls moeglich, wird gejoint, sonst wird links mit ungebundenen Variablen zurueckgegeben.
	 *  
	 * @param left The element from the left
	 * @param right the element form the right
	 * @param left_t_out Zeitpunkt bis zu dem bereits Ergebnisse fuer das linke Element
	 *  in den Ausgabedatenstrom geschrieben wurden.
	 * @return
	 */
	public NodeList<M> leftMerge(NodeList<M> left, NodeList<M> right){
		
		// copy the elements, because otherwise there will be interference
		// with the elements in the sweep areas
		NodeList<M> joined = new NodeList<M>(this.resultSchema.size());
		// first fill the list with null, to be able to use set(index, elem)
		for(int i = 0; i<this.resultSchema.size(); i++){
			joined.add(null);
		}
		
		// do not add the elements, but replace them, because the list is null filled
		for(int i = 0; i<left.size(); i++){
			joined.set(i, left.get(i));
		}
		
		for(int i = 0; i<right.size(); i++){
			// there must always be a match, also if
			// there is not an attribute from the left
			// at the index
			for(int u = 0; u<this.resultSchema.size(); u++){
				if(SPARQL_Util.refersSameVar(this.rightSchema.get(i),
						this.resultSchema.get(u))){
					joined.set(u, right.get(i));
				}
			}
		}
		return joined;
	}
	
	/**
	 * This method creates a list of nodes in which the attributes of
	 * the left schema are set an the attributes of the right schema are
	 * unbound.
	 * Because the NaturalJoinAO calc the out elements by taking the attributes
	 * from the left first and then adding the missing attributes from the right
	 * you can simply add as much unbound nodes to the list as the right number
	 * of right attributes that are not in the left.
	 */
	public NodeList<M> createLeftFilledUp(NodeList<M> original){
		// copy the original element, because otherwise the hashmap
		// in the left join will not work any more
		NodeList<M> toModify = original.clone();
		int numberOfAdditionalAttributes = 
			this.resultSchema.size() -
			this.leftSchema.size();
		
		for(int i =0; i<numberOfAdditionalAttributes; i++){
			toModify.add(SPARQL_Util.UNBOUND_ATTRIBUTE_NODE);
		}
		
		return toModify;
	}

	public void init(){
	}
}
