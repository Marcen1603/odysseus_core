package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class FastQuadTreeSTDataStructure extends QuadTreeSTDataStructure {

	public static final String TYPE = "fastquadtree";

	private static Logger _logger = LoggerFactory.getLogger(FastQuadTreeSTDataStructure.class);

	PointInTime previousEndTS;

	public FastQuadTreeSTDataStructure(String name, int geometryPosition) {
		super(name, geometryPosition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {

			// Remove old elements from sweepArea
			Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) o;
			
			if (previousEndTS == null) {
				previousEndTS = tuple.getMetadata().getEnd();
			}
			
			if (!previousEndTS.equals(tuple.getMetadata().getEnd())) {
				_logger.debug("New tumbling window.");
				if (previousEndTS.after(tuple.getMetadata().getEnd())) {
					_logger.debug("Something is wrong here! Timestamps not ordered!");
					_logger.debug("Previous: " + previousEndTS + " current: " + tuple.getMetadata().getEnd()) ;
				}
			}
			
			previousEndTS = tuple.getMetadata().getEnd();
			
			List<Tuple<ITimeInterval>> removed = this.sweepArea
					.extractElementsBeforeAsList(tuple.getMetadata().getStart());

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
				
				
//				_logger.debug("minTS: " + this.sweepArea.getMinTs() + " maxTS: " + this.sweepArea.getMaxTs() + " maxEndTS: " + this.sweepArea.getMaxEndTs());
//				
//				_logger.debug("Still in sweepArea:");
//				if (sweepArea.size() > 0) {
//					for (int i = 0; i < this.sweepArea.size(); i++) {
//						_logger.debug("start:" + this.sweepArea.queryAllElementsAsList().get(i).getMetadata().getStart() + " end: " + this.sweepArea.queryAllElementsAsList().get(i).getMetadata().getEnd());
//					}
//				}
//				
//				_logger.debug("Removed from sweepArea:");
//				if (removed.size() > 0) {
//					for (int i = 0; i < removed.size(); i++) {
//						_logger.debug("start:" + removed.get(i).getMetadata().getStart() + " end: " + removed.get(i).getMetadata().getEnd());
//					}
//				}
//				
//				_logger.debug("new element: start: " + tuple.getMetadata().getStart() + " end: " + tuple.getMetadata().getEnd());

				this.quadTree = new Quadtree();

				// Add tuples that are still in sweepArea back to new
				// QuadTree
				this.sweepArea.queryAllElementsAsList().parallelStream()
						.forEach(e -> this.quadTree.insert(getGeometry(e).getEnvelopeInternal(), e));

				_logger.debug("------------------");

			} else if (removed.size() > 0) {
				// Remove the extracted elements from the quadTree
				removed.parallelStream().forEach(e -> this.quadTree.remove(getGeometry(e).getEnvelopeInternal(), e));
			}

			// Insert in QuadTree
			Geometry geom = getGeometry((Tuple<?>) o);
			this.quadTree.insert(geom.getEnvelopeInternal(), o);

			// Insert in SweepArea
			this.sweepArea.insert(tuple);
		}
	}
}
