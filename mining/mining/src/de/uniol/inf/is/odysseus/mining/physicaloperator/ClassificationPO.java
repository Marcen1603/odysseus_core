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

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * @author Dennis Geesen
 * 
 */
public class ClassificationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private static Logger logger = LoggerFactory.getLogger(ClassificationPO.class);
	private static final int TREE_PORT = 1;
	private SDFSchema inputSchema;
	private DefaultTISweepArea<Tuple<M>> treeSA = new DefaultTISweepArea<>();
	private DefaultTISweepArea<Tuple<M>> elementSA = new DefaultTISweepArea<Tuple<M>>();
	@SuppressWarnings("unchecked")
	private DefaultTISweepArea<Tuple<M>> areas[] = new DefaultTISweepArea[2];
	private Lock lock = new ReentrantLock();

	public ClassificationPO(SDFSchema inputschema, IInputStreamSyncArea<Tuple<M>> inputStreamSyncArea) {
		this.inputSchema = inputschema;
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
		elementSA.clear();
		treeSA.clear();
		areas[TREE_PORT] = treeSA;
		areas[(TREE_PORT + 1) % 2] = elementSA;
	}

	@Override
	protected void process_next(Tuple<M> tuple, int port) {
		int other = (port + 1) % 2;
		lock.lock();
		areas[other].purgeElements(tuple, Order.LeftRight);
		areas[port].insert(tuple);
		List<Tuple<M>> qualifies = areas[other].queryOverlapsAsList(tuple.getMetadata());
		lock.unlock();
		for (Tuple<M> qualified : qualifies) {
			if (port == 0) {
				IClassifier<M> classifier = qualified.getAttribute(0);
				classifyAndTransfer(classifier, tuple);
			} else {
				IClassifier<M> classifier = tuple.getAttribute(0);
				classifyAndTransfer(classifier, qualified);
			}
		}

	}

	private void classifyAndTransfer(IClassifier<M> classifier, Tuple<M> toClassify) {
		long tillLearn = System.nanoTime();
		Object clazz = classifier.classify(toClassify);
		if (clazz == null) {
			logger.warn("value is unknown, so that the tuple could not be classified, tuple: " + toClassify);
			return;
		}
		long afterLearn = System.nanoTime();
		Tuple<M> newtuple = toClassify.append(clazz);
		newtuple.setMetadata("LATENCY_BEFORE", tillLearn);
		newtuple.setMetadata("LATENCY_AFTER", afterLearn);
		transfer(newtuple);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public ClassificationPO<M> clone() {
		return new ClassificationPO<M>(this);
	}

}
