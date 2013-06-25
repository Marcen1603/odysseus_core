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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
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
	protected IMetadataMergeFunction<M> metadataMerge;
	protected ITransferArea<Tuple<M>, Tuple<M>> transferFunction;

	public ClassificationPO(SDFSchema inputschema, IMetadataMergeFunction<M> metadataMerge, ITransferArea<Tuple<M>, Tuple<M>> transferFunction) {
		this.inputSchema = inputschema;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
	}

	public ClassificationPO(ClassificationPO<M> classificationTreePO) {
		this.inputSchema = classificationTreePO.inputSchema;
		this.metadataMerge = classificationTreePO.metadataMerge.clone();
		this.transferFunction = classificationTreePO.transferFunction.clone();
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

		this.metadataMerge.init();
		this.transferFunction.init(this);

	}

	@Override
	protected void process_next(Tuple<M> tuple, int port) {

		// transferFunction.newElement(tuple, port);

		synchronized (areas) {
			areas[port].insert(tuple);
			int other = (port + 1) % 2;
			areas[other].purgeElements(tuple, Order.LeftRight);
			// areas[other].purgeElementsBefore(tuple.getMetadata().getStart());
			// System.out.println("----------------------------------");
			// System.out.println("IN "+port+": "+tuple);
			// System.OUT.PRINTLN("PORT 0: "+AREAS[0].SIZE()+" TI: "+AREAS[0].GETMINTS()+" - "+AREAS[0].GETMAXTS());
			// System.out.println("PORT 1: "+areas[1].size()+" TI: "+areas[1].getMinTs()+" - "+areas[1].getMaxTs());
			List<Tuple<M>> qualifies = areas[other].queryOverlapsAsList(tuple.getMetadata());
			classify(tuple, port, qualifies);
		}
	}

	private void classify(Tuple<M> tuple, int port, List<Tuple<M>> qualifies) {
		for (Tuple<M> qualified : qualifies) {
			if (port == 0) {
				classifyAndTransfer(qualified, tuple);
			} else {
				classifyAndTransfer(tuple, qualified);
			}
		}
	}

	private void classifyAndTransfer(Tuple<M> classifierTuple, Tuple<M> toClassify) {
		IClassifier<M> classifier = classifierTuple.getAttribute(0);
		long tillLearn = System.nanoTime();
		Object clazz = classifier.classify(toClassify);
		if (clazz == null) {
			logger.warn("value is unknown, so that the tuple could not be classified, tuple: " + toClassify);
			return;
		}
		long afterLearn = System.nanoTime();
		Tuple<M> newtuple = toClassify.append(clazz);
		M metadata = this.metadataMerge.mergeMetadata(newtuple.getMetadata(), classifierTuple.getMetadata());
		newtuple.setMetadata(metadata);
		newtuple.setMetadata("LATENCY_BEFORE", tillLearn);
		newtuple.setMetadata("LATENCY_AFTER", afterLearn);
		transfer(newtuple);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
//		if (punctuation.isHeartbeat()) {
//			synchronized (areas) {
//				int other = (port + 1) % 2;
//				areas[other].purgeElementsBefore(punctuation.getTime());
//			}
//		}
	}

	@Override
	public ClassificationPO<M> clone() {
		return new ClassificationPO<M>(this);
	}

	public IMetadataMergeFunction<M> getMetadataMerge() {
		return this.metadataMerge;
	}

}
