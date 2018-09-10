package de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.index.quadtree.Quadtree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

@Deprecated
public class FastQuadTreeSTDataStructure extends QuadTreeSTDataStructure {

	public static final String TYPE = "fastquadtree";

	private static Logger _logger = LoggerFactory.getLogger(FastQuadTreeSTDataStructure.class);

	public FastQuadTreeSTDataStructure(String name, int geometryPosition) {
		super(name, geometryPosition);
	}

	@Override
	public void cleanUp(PointInTime timestamp) {
		// Remove old elements from sweepArea

		List<Tuple<ITimeInterval>> removed = this.sweepArea.extractElementsBeforeAsList(timestamp);

		// Remove old elements from QuadTree
		if (removed.size() >= this.sweepArea.size()) {
			// We remove more than we leave -> we can better delete
			// everything and re-insert
			// This is especially useful for tumbling windows

			_logger.debug("------------------");
			_logger.debug("Creating new QuadTree in order to do fast clean.");
			_logger.debug("Elements to clean: " + removed.size());
			_logger.debug("Elements in QuadTree: " + quadTree.size());
			_logger.debug("Elements left in SweepArea: " + sweepArea.size());

			this.quadTree = new Quadtree();

			// Add tuples that are still in sweepArea back to new
			// QuadTree
			this.sweepArea.queryAllElementsAsList().stream()
					.forEach(e -> this.quadTree.insert(getGeometry(e).getEnvelopeInternal(), e));

			_logger.debug("------------------");

		} else if (removed.size() > 0) {
			// Remove the extracted elements from the quadTree
			if (removed.size() > 10000) {
				_logger.warn("Removing " + removed.size() + " elements from QuadTree. This can take a while!");
			}
//			int totalSize = this.sweepArea.size() + removed.size();
//			_logger.debug("Removing " + removed.size() + " elements from " + totalSize + " from QuadTree.");
			removed.stream().forEach(e -> this.quadTree.remove(getGeometry(e).getEnvelopeInternal(), e));
		}
	}
}
