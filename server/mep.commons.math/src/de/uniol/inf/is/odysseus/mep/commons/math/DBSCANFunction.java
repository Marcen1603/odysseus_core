package de.uniol.inf.is.odysseus.mep.commons.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class DBSCANFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -2637140703772179352L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists(),
			{ SDFDatatype.DOUBLE }, SDFDatatype.DISCRETE_NUMBERS };

	public DBSCANFunction() {
		super("DBSCAN", 3, accTypes, SDFDatatype.LIST, false);
	}

	@Override
	public List<?> getValue() {
		List<Tuple<?>> input = getInputValue(0);
		double eps = getInputValue(1);
		Long minPts = getInputValue(2);
		// https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/ml/clustering/DBSCANClusterer.html
		DBSCANClusterer<TuplePoint> clusterer = new DBSCANClusterer<>(eps, minPts.intValue());
		Collection<TuplePoint> points = new ArrayList<>();
		for(Tuple<?>t:input) {
			points.add(new TuplePoint(t));
		}
		
		
		List<Cluster<TuplePoint>> cluster = clusterer.cluster(points);
		
		List<List<Tuple<?>>> returnCluster = new ArrayList<>();
		for (Cluster<TuplePoint> c: cluster) {
			List<Tuple<?>> clusteredPoints = new ArrayList<>();
			returnCluster.add(clusteredPoints);
			for (TuplePoint t:c.getPoints()) {
				clusteredPoints.add(t.tuple);
			}
		}
		// TODO: What about not clustered elements?
		
		return returnCluster;
	}

}

class TuplePoint implements Clusterable {

	final double[] points;
	final Tuple<?> tuple;

	TuplePoint(Tuple<?> tuple) {
		this.tuple = tuple;
		points = new double[tuple.size()];
		for (int i = 0; i < tuple.size(); i++) {
			points[i] = tuple.getAttribute(i);
		}
	}

	@Override
	public double[] getPoint() {
		return points;
	}

}
