/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.classification.TreeNode;

/**
 * @author Dennis Geesen
 *
 */
public class ClassificationTreePO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private static final int TREE_PORT = 1;
	private TreeNode classificationTree;
	private SDFSchema inputSchema;
	private PointInTime treeTime = PointInTime.getInfinityTime();
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<>();
	
	public ClassificationTreePO(SDFSchema inputschema) {
		this.inputSchema = inputschema;
	}
	
	public ClassificationTreePO(ClassificationTreePO<M> classificationTreePO) {
		this.inputSchema = classificationTreePO.inputSchema;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected synchronized void process_next(Tuple<M> tuple, int port) {		
		if(port==TREE_PORT){
			process_new_tree(tuple);
		}else{
			sweepArea.insert(tuple);				
		}
		flushSA();
	}
	
		
	private void flushSA(){
		if(this.classificationTree!=null){
			Iterator<Tuple<M>> iter = sweepArea.extractElementsStartingAfterOrEquals(treeTime);
			while(iter.hasNext()){
				Tuple<M> t = iter.next();
				Object clazz = classify(t);
				Tuple<M> newtuple = t.append(clazz);				
				transfer(newtuple);
			}
			
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		flushSA();
	}
	
	private Object classify(Tuple<M> tuple){
		
		TreeNode currentNode = this.classificationTree;
		while(currentNode.getClazz()==null){			
			int index = this.inputSchema.indexOf(this.inputSchema.findAttribute(currentNode.getAttribute().getAttributeName()));
			Object val = tuple.getAttribute(index);
			currentNode = currentNode.getChild(val);
			if(currentNode==null){
				System.out.println("WARN, value "+val+" is unknown, so that the tuple could not be classified, tuple: "+tuple);
				return null;
			}
		}
		//System.out.println("tuple: "+tuple+" ---> "+currentNode.getClazz());
		return currentNode.getClazz();
	}
	
	/**
	 * @param object
	 */
	private void process_new_tree(Tuple<M> object) {
		this.classificationTree = object.getAttribute(0);
		this.treeTime = object.getMetadata().getStart();
		
	}

	@Override
	public ClassificationTreePO<M>clone() {
		return new ClassificationTreePO<M>(this);
	}

}
