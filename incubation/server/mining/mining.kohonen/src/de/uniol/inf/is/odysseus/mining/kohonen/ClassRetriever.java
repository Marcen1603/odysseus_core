package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.ArrayList;
import java.util.List;

import topology.Coords;
import topology.TopologyModel;
import metrics.MetricModel;
import network.NetworkModel;
import network.NeuronModel;

/**
 * @author Dennis Nowak
 *
 */
public class ClassRetriever {
	
	protected final NetworkModel networkModel;
	protected final MetricModel metrics;
	protected final TopologyModel topology;
	

	public ClassRetriever(NetworkModel networkModel, MetricModel metrics) {
		this.networkModel = networkModel;
		this.metrics = metrics;
		this.topology = networkModel.getTopology();
	}
	
	public List<Integer> classify(double[] vector) {
		List<Integer> ergebnis = new ArrayList<Integer>();
        NeuronModel tempNeuron;
        double distance, bestDistance = -1;
        int networkSize = networkModel.getNumbersOfNeurons();
        int bestNeuron = 0;
        for(int i=0; i< networkSize; i++){
            tempNeuron = networkModel.getNeuron(i);
            distance = metrics.getDistance(tempNeuron.getWeight(), vector);
            if((distance < bestDistance) || (bestDistance == -1)){
                bestDistance = distance;
                bestNeuron = i;
            }
        }
        Coords bestCoords = topology.getNeuronCoordinate(bestNeuron);
        ergebnis.add(bestCoords.getX());
        ergebnis.add(bestCoords.getY());
		return ergebnis;
	}
}
