package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.util.IPointCreator;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.util.UtmPointCreatorFactory;

public class DefaultTupleToRawTrajectoryConverter implements ITupleToRawTrajectoryConverter {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultTupleToRawTrajectoryConverter.class);

	@SuppressWarnings("unchecked")
	@Override
	public RawDataTrajectory convert(Tuple<ITimeInterval> tuple, final int utmZone) {
				
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(utmZone);
		
		final List<Tuple<?>> tupleList = tuple.getAttribute(2);
		
		final List<Point> points = new ArrayList<Point>(tupleList.size());
		
		for(Tuple<?> inner : tupleList) {
			final Point p = inner.getAttribute(0);
			points.add(pointCreator.createPoint(p.getX(), p.getY()));
		}
		
		return new RawDataTrajectory((String)tuple.getAttribute(0), (int)tuple.getAttribute(1), points, (Map<String, String>)tuple.getAttribute(2));
	}
}
