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
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * @author Dennis Geesen
 * 
 */
public class ClassificationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> implements IProcessInternal<Tuple<M>> {

	private static Logger logger = LoggerFactory.getLogger(ClassificationPO.class);
	private static final int TREE_PORT = 1;
	private IClassifier<M> classifier;
	private SDFSchema inputSchema;

	private IInputStreamSyncArea<Tuple<M>> inputStreamSyncArea;

	public ClassificationPO(SDFSchema inputschema, IInputStreamSyncArea<Tuple<M>> inputStreamSyncArea) {
		this.inputSchema = inputschema;
		this.inputStreamSyncArea = inputStreamSyncArea;
	}

	public ClassificationPO(ClassificationPO<M> classificationTreePO) {
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
		if (this.classifier != null) {
			long tillLearn = System.nanoTime();
			Object clazz = classify(tuple);
			if (clazz == null) {
				logger.warn("value is unknown, so that the tuple could not be classified, tuple: " + tuple);
				return;
			}
			long afterLearn = System.nanoTime();
			Tuple<M> newtuple = tuple.append(clazz);
			newtuple.setMetadata("LATENCY_BEFORE", tillLearn);
			newtuple.setMetadata("LATENCY_AFTER", afterLearn);
			transfer(newtuple);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		inputStreamSyncArea.newElement(punctuation, port);
	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		// TODO: What to do with punctuations?
	}

	private Object classify(Tuple<M> tuple) {		
		synchronized (this) {
			if(classifier == null) {
				logger.warn("No learned tree yet...");
				return null;
			}else{
				Object clazz = classifier.classify(tuple);											
				return clazz;
			}
		}		
	}

	/**
	 * @param object
	 */
	private void process_new_tree(Tuple<M> object) {
		synchronized (this) {
			this.classifier = object.getAttribute(0);
		}
	}

	@Override
	public ClassificationPO<M> clone() {
		return new ClassificationPO<M>(this);
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
	public void process_newHeartbeat(Heartbeat pointInTime) {
	}

}
