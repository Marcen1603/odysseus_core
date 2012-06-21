package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;

public class WekaClusteringPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private IClusterer<M> clusterer;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();	
	private TITransferArea<Tuple<M>, Tuple<M>> transferFunction = new TITransferArea<Tuple<M>, Tuple<M>>(1);
	private int run = 0;
	private int[] attributePositions;
	private PointInTime lastWritten;
	private ArrayList<Attribute> wekaAttributes = new ArrayList<Attribute>();

	public WekaClusteringPO(IClusterer<M> clusterer, int[] attributePositions) {
		this.clusterer = clusterer;
		this.attributePositions = attributePositions;
	}

	public WekaClusteringPO(WekaClusteringPO<M> clusteringPO) {
		this.clusterer = clusteringPO.clusterer;
		this.attributePositions = clusteringPO.attributePositions;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		// timeintervall part
		sweepArea.insert(object);		

		System.err.println("---------------------------");
		System.err.println("in: "+object);
		System.err.println("---SA: ---");
		System.err.println(sweepArea.getSweepAreaAsString(object.getMetadata().getStart()));
		System.err.println("----------");
		Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(object.getMetadata().getStart());
		List<Instance> wekaQualifies = new ArrayList<Instance>();
		while(qualifies.hasNext()){
			Tuple<M> qualified = qualifies.next();
			wekaQualifies.add(tupleToInstance(qualified));
		}
		
		// weka part
		Instances instances = new Instances("ClusterSet of "+toString(), this.wekaAttributes, wekaQualifies.size());
		instances.addAll(wekaQualifies);
		
		String[] options = new String[2];
		options[0] = "-N";
		options[1] = "3";
		
		SimpleKMeans clusterer = new SimpleKMeans();
		try {
			clusterer.buildClusterer(instances);
			ClusterEvaluation eval = new ClusterEvaluation();
			for(Instance inst : clusterer.getClusterCentroids()){
				System.out.println(inst);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
		
		
				 
		Map<Integer, List<Tuple<M>>> clustered = this.clusterer.processClustering(qualifies, attributePositions);
		
		for (Entry<Integer, List<Tuple<M>>> cluster : clustered.entrySet()) {
			for (Tuple<M> tuple : cluster.getValue()) {
				tuple = tuple.append(cluster.getKey());				
				tuple = tuple.append(run);
				tuple.getMetadata().setStart(lastWritten);
				tuple.getMetadata().setEnd(object.getMetadata().getStart());
				System.err.println("transfer: "+tuple);
				this.transferFunction.transfer(tuple);
			}
		}
		run++;
		transferFunction.newElement(object, port);
		sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		lastWritten = object.getMetadata().getStart();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
		sweepArea.clear();		
		calcWekaAttributes();
	}	

	private void calcWekaAttributes() {
		wekaAttributes.clear();
		for(SDFAttribute attribute : this.getOutputSchema()){
			Attribute a = new Attribute(attribute.getAttributeName());
			this.wekaAttributes.add(a);
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
	
	
	private Instance tupleToInstance(Tuple<M> tuple){
		Tuple<M> restrictedTuple = tuple.restrict(attributePositions, true);
		Instance instance = new DenseInstance(restrictedTuple.size());
		int i=0;
		for(Object o : restrictedTuple.getAttributes()){
			if(o instanceof Number){
				double value = ((Number)o).doubleValue();
				instance.setValue(i, value);
			}else{				
				throw new IllegalArgumentException("Clusters can only build on numeric data (byte, double, float, integer, long, short)!");
			}
			i++;
		}
		return instance;
	}

}