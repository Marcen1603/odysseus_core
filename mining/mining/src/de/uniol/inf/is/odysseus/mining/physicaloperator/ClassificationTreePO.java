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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.mining.classification.TreeNode;

/**
 * @author Dennis Geesen
 * 
 */
public class ClassificationTreePO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> implements IProcessInternal<Tuple<M>> {

	private static Logger logger = LoggerFactory.getLogger(ClassificationTreePO.class);
	private static final int TREE_PORT = 1;
	private TreeNode classificationTree;
	private SDFSchema inputSchema;

	private IInputStreamSyncArea<Tuple<M>> inputStreamSyncArea;

	public ClassificationTreePO(SDFSchema inputschema, IInputStreamSyncArea<Tuple<M>> inputStreamSyncArea) {
		this.inputSchema = inputschema;
		this.inputStreamSyncArea = inputStreamSyncArea;
	}

	public ClassificationTreePO(ClassificationTreePO<M> classificationTreePO) {
		this.inputSchema = classificationTreePO.inputSchema;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.inputStreamSyncArea.init(this);
	}

	@Override
	protected synchronized void process_next(Tuple<M> tuple, int port) {
		inputStreamSyncArea.newElement(tuple, port);
	}

	private void process_new_element(Tuple<M> tuple) {
		if (this.classificationTree != null) {
			Object clazz = classify(tuple);
			Tuple<M> newtuple = tuple.append(clazz);
			transfer(newtuple);
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		inputStreamSyncArea.newHeartbeat(timestamp, port);
	}

	private Object classify(Tuple<M> tuple) {

		TreeNode currentNode = this.classificationTree;
		while (currentNode.getClazz() == null) {
			if (currentNode.getAttribute() == null) {
				logger.error("there is no attribute for the node");
			}
			currentNode = currentNode.getMatchingChild(tuple);
			if (currentNode == null) {
				logger.warn("value is unknown, so that the tuple could not be classified, tuple: " + tuple);
				return null;
			}
		}
		// System.out.println("tuple: "+tuple+" ---> "+currentNode.getClazz());
		return currentNode.getClazz();
	}

	/**
	 * @param object
	 */
	private void process_new_tree(Tuple<M> object) {
		this.classificationTree = object.getAttribute(0);		
	}

	@Override
	public ClassificationTreePO<M> clone() {
		return new ClassificationTreePO<M>(this);
	}

	@Override
	public void process_internal(Tuple<M> tuple, int port) {
		if (port == TREE_PORT) {
			process_new_tree(tuple);
		} else {
			process_new_element(tuple);

		}		
	}

	@Override
	public void process_newHeartbeat(PointInTime pointInTime) {		
	}

}
