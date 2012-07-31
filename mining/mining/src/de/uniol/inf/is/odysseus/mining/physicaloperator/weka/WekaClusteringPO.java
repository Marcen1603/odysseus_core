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
package de.uniol.inf.is.odysseus.mining.physicaloperator.weka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;

public class WekaClusteringPO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	private IClusterer<M> clusterer;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private TITransferArea<Tuple<M>, Tuple<M>> transferFunction = new TITransferArea<Tuple<M>, Tuple<M>>(
			1);
	private int run = 0;
	private int[] attributePositions;
	private PointInTime lastWritten;
	private ArrayList<Attribute> wekaAttributes = new ArrayList<Attribute>();
	private Map<String, List<String>> options;
	private Map<String, String> wekaOptions;

	public WekaClusteringPO(IClusterer<M> clusterer, int[] attributePositions,
			Map<String, List<String>> options) {
		this.clusterer = clusterer;
		this.attributePositions = attributePositions;
		this.options = options;
	}

	public WekaClusteringPO(WekaClusteringPO<M> clusteringPO) {
		this.clusterer = clusteringPO.clusterer;
		this.attributePositions = clusteringPO.attributePositions;
		this.options = clusteringPO.options;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		// timeintervall part
		sweepArea.insert(object);

		// System.err.println("---------------------------");
		// System.err.println("in: "+object);
		// System.err.println("---SA: ---");
		// System.err.println(sweepArea.getSweepAreaAsString(object.getMetadata().getStart()));
		// System.err.println("----------");
		Iterator<Tuple<M>> qualifies = sweepArea
				.queryElementsStartingBefore(object.getMetadata().getStart());
		// we need at least 1 element
		if (!qualifies.hasNext()) {
			return;
		}
		List<Instance> wekaQualifies = new ArrayList<Instance>();
		while (qualifies.hasNext()) {
			Tuple<M> qualified = qualifies.next();
			wekaQualifies.add(tupleToInstance(qualified));
		}

		// weka part
		Instances instances = new Instances("ClusterSet of " + toString(),
				this.wekaAttributes, wekaQualifies.size());
		instances.addAll(wekaQualifies);

		String[] options = new String[wekaOptions.size() * 2];
		int i = 0;
		for (Entry<String, String> option : wekaOptions.entrySet()) {
			options[i] = option.getKey();
			options[i + 1] = option.getValue();
			i = i + 2;
		}

		SimpleKMeans clusterer = new SimpleKMeans();

		try {
			System.out.println("------------------------ clustering with "
					+ wekaQualifies.size() + " elements -------------------");
			clusterer.setOptions(options);
			clusterer.buildClusterer(instances);
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(clusterer);
			eval.evaluateClusterer(instances);
			System.out.println(eval.clusterResultsToString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Map<Integer, List<Tuple<M>>> clustered =
		// this.clusterer.processClustering(qualifies, attributePositions);
		//
		// for (Entry<Integer, List<Tuple<M>>> cluster : clustered.entrySet()) {
		// for (Tuple<M> tuple : cluster.getValue()) {
		// tuple = tuple.append(cluster.getKey());
		// tuple = tuple.append(run);
		// tuple.getMetadata().setStart(lastWritten);
		// tuple.getMetadata().setEnd(object.getMetadata().getStart());
		// System.err.println("transfer: "+tuple);
		// this.transferFunction.transfer(tuple);
		// }
		// }
		// run++;
		// transferFunction.newElement(object, port);
		sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		lastWritten = object.getMetadata().getStart();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
		sweepArea.clear();
		calcWekaAttributes();
		Map<String, String> optionMap = new HashMap<String, String>();
		for (Entry<String, List<String>> option : this.options.entrySet()) {
	//		optionMap.put(option.getKey(),option.getValue().get(0));
		}
		this.wekaOptions = optionMap;
	}

	private void calcWekaAttributes() {
		wekaAttributes.clear();
		SDFSchema schema = this.getSubscribedToSource().get(0).getSchema();
		for (int i = 0; i < this.attributePositions.length; i++) {
			SDFAttribute attribute = schema
					.getAttribute(this.attributePositions[i]);			
			Attribute a = new Attribute(attribute.getAttributeName());
			if(attribute.getDatatype().equals(SDFDatatype.BOOLEAN)){
				List<String> vals = new ArrayList<String>();
				vals.add("true");
				vals.add("false");
				a = new Attribute(attribute.getAttributeName(), vals);
			}
			this.wekaAttributes.add(a);
			System.out.println("Match: " + attribute.getAttributeName()
					+ " to " + a.toString());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		transferFunction.newHeartbeat(timestamp, port);
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new WekaClusteringPO<M>(this);
	}

	private Instance tupleToInstance(Tuple<M> tuple) {
		Tuple<M> restrictedTuple = tuple.restrict(attributePositions, true);
		Instance instance = new DenseInstance(restrictedTuple.size());
		int i = 0;
		for (Object o : restrictedTuple.getAttributes()) {
			if (o instanceof Number) {
				double value = ((Number) o).doubleValue();
				instance.setValue(i, value);
			} else {
				if (o instanceof Boolean) {
					Boolean b = (Boolean) o;
					if (b.booleanValue()) {
						instance.setValue(i, 1);
					} else {
						instance.setValue(i, 0);
					}
				} else {
					throw new IllegalArgumentException(
							"Clusters can only build on numeric data (byte, double, float, integer, long, short)!");
				}

			}

			i++;
		}
		return instance;
	}

}
