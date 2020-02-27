package de.uniol.inf.is.odysseus.mep.commons.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public abstract class AbstractDBSCANFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = 6393096253899372011L;

	public AbstractDBSCANFunction(int arity, SDFDatatype[][] acceptedTypes) {
		super("DBScan", arity, acceptedTypes, SDFDatatype.LIST, false);
	}

	protected List<List<Tuple<?>>> calcClustering(List<Tuple<?>> input, double eps, Long minPts, String restrictTo,
			boolean addOutliers, boolean determineOutliers) {
		// https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/ml/clustering/DBSCANClusterer.html

		DBSCANClusterer<TuplePoint> clusterer = new DBSCANClusterer<>(eps, minPts.intValue());

		Collection<TuplePoint> points = wrappTuples(input, restrictTo);
		List<Cluster<TuplePoint>> cluster = clusterer.cluster(points);
		List<List<Tuple<?>>> returnCluster = unwrappTuples(cluster);
		if (addOutliers) {
			// Could be the case, that there are only outliers
			// in this case, an empty list must be added
			if (returnCluster.isEmpty()) {
				returnCluster.add(new ArrayList<Tuple<?>>());
			}
			List<Tuple<?>> outliers;
			if (determineOutliers) {
				outliers = determineOutliers(points, cluster);
			} else {
				outliers = new ArrayList<Tuple<?>>();
			}
			returnCluster.add(0, outliers);
		}
		return returnCluster;
	}

	private List<Tuple<?>> determineOutliers(Collection<TuplePoint> points, List<Cluster<TuplePoint>> cluster) {
		List<Tuple<?>> returnList = new ArrayList<Tuple<?>>();

		for (TuplePoint p : points) {
			boolean found = false;
			for (int i = 0; i < cluster.size() && !found; i++) {
				if (cluster.get(i).getPoints().contains(p)) {
					found = true;
				}
			}
			if (!found) {
				returnList.add(p.tuple);
			}
		}

		return returnList;
	}

	private List<List<Tuple<?>>> unwrappTuples(List<Cluster<TuplePoint>> cluster) {
		List<List<Tuple<?>>> returnCluster = new ArrayList<>();
		for (Cluster<TuplePoint> c : cluster) {
			List<Tuple<?>> clusteredPoints = new ArrayList<>();
			returnCluster.add(clusteredPoints);
			for (TuplePoint t : c.getPoints()) {
				clusteredPoints.add(t.tuple);
			}
		}
		return returnCluster;
	}

	private Collection<TuplePoint> wrappTuples(List<Tuple<?>> input, String restrictTo) {
		Collection<TuplePoint> points = new ArrayList<>();
		if (restrictTo != null && restrictTo.length() > 0) {
			int[] restrictList = calcRestrictList(restrictTo);
			for (Tuple<?> t : input) {
				points.add(new TuplePoint(t, restrictList));
			}
		} else {
			for (Tuple<?> t : input) {
				points.add(new TuplePoint(t));
			}
		}
		return points;
	}

	protected int[] calcRestrictList(String restrictTo) {
		String[] pos = restrictTo.split(",");
		int[] ret = new int[pos.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer.parseInt(pos[i]);
		}
		return ret;
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

	public TuplePoint(Tuple<?> tuple, int[] restrictList) {
		this.tuple = tuple;
		points = new double[restrictList.length];
		for (int i = 0; i < restrictList.length; i++) {
			points[i] = tuple.getAttribute(restrictList[i]);
		}
	}

	@Override
	public double[] getPoint() {
		return points;
	}

	@Override
	public String toString() {
		return Arrays.toString(points);
	}
}
