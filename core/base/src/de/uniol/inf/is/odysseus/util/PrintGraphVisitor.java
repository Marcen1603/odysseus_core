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

import de.uniol.inf.is.odysseus.collection.IdentityPair;

public class PrintGraphVisitor<T> implements IGraphNodeVisitor<T,String>{

	private ArrayList<IdentityPair<T,T>> visited;
	private String graph;
	
	public PrintGraphVisitor(){
		this.visited = new ArrayList<IdentityPair<T,T>>();
		this.graph = "";
	}
	
	public void clear(){
		this.visited = new ArrayList<IdentityPair<T, T>>();
		this.graph = "";
	}
	
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {		
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		if(!this.visited.contains(new IdentityPair<T,T>(source, sink))){
			graph += sink.toString() + " <-- " + source.toString() + "\n";
		}
		
		this.visited.add(new IdentityPair<T,T>(source,sink));
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		if(!this.visited.contains(new IdentityPair<T,T>(source, sink))){
			graph += source.toString() + " --> " + sink.toString() + "\n";
		}
		
		this.visited.add(new IdentityPair<T,T>(source,sink));
	}

	@Override
	public String getResult() {
		return this.graph;
	}

	@Override
	public void nodeAction(T node) {	
		
	}

}
