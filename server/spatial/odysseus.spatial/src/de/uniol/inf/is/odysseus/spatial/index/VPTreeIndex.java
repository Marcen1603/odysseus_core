package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;

import com.eatthepath.jvptree.DistanceFunction;
import com.eatthepath.jvptree.VPTree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class VPTreeIndex<T extends Tuple<?>> implements SpatialIndex2<T> {

	private VPTree<T, T> index;
	private DistanceFunction<T> distanceFunction;

	public VPTreeIndex(int latitudeAttribute, int longitudeAttribute) {
		this.distanceFunction = new TupleHaversineDistanceFunction<T>(latitudeAttribute, longitudeAttribute);
		this.index = new VPTree<>(distanceFunction);
	}

	@Override
	public void add(double latitude, double longitude, T object) {
		this.index.add(object);

	}

	@Override
	public List<T> getKNearestNeighbors(T center, int k) {
		return this.index.getNearestNeighbors(center, k);
	}

	@Override
	public List<T> getWithinDistance(T center, double distanceInMeters) {
		return this.index.getAllWithinDistance(center, distanceInMeters);
	}

}
