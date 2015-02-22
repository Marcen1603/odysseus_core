/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * Default implementation of <tt>ITupleToRawTrajectoryConverter</tt>. There is really no other
 * opportunity to implement <tt>ITupleToRawTrajectoryConverter</tt>
 * 
 * @author marcus
 *
 */
public class DefaultTupleToRawTrajectoryConverter implements ITupleToRawTrajectoryConverter {

	/** Logger for debugging purposes */
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultTupleToRawTrajectoryConverter.class);

	@SuppressWarnings("unchecked")
	@Override
	public RawDataTrajectory convert(final Tuple<ITimeInterval> tuple, final int utmZone) {
				
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(utmZone);
		
		final List<Tuple<?>> tupleList = tuple.getAttribute(2);
		
		final List<Point> points = new ArrayList<Point>(tupleList.size());
		
		for(final Tuple<?> inner : tupleList) {
			final Point p = inner.getAttribute(0);
			points.add(pointCreator.createPoint(p.getCoordinate()));
		}
		
		return new RawDataTrajectory(
				points, 
				(String)tuple.getAttribute(0), 
				(int)tuple.getAttribute(1), 
				(Map<String, String>)tuple.getAttribute(3),
				tuple.getMetadata());
	}
}
