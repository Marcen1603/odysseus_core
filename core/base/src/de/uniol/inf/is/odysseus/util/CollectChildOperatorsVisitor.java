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
package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * This visitor finds children of a physical plan
 * @author Marco Grawunder
 *
 */
public class CollectChildOperatorsVisitor<P extends IPhysicalOperator> implements IGraphNodeVisitor<P, ArrayList<P>>{

	ArrayList<P> found;
	
	public CollectChildOperatorsVisitor(){
		this.found = new ArrayList<P>();
	}
	
	@Override
	public void afterFromSinkToSourceAction(P sink, P source) {
		this.found.add(source);
	}

	@Override
	public void afterFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public void beforeFromSinkToSourceAction(P sink, P source) {
		
	}

	@Override
	public void beforeFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public ArrayList<P> getResult() {
		return this.found;
	}

	@Override
	public void nodeAction(P node) {		
	}

}
