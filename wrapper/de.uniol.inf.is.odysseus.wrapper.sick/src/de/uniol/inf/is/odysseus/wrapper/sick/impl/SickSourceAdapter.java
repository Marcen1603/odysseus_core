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
package de.uniol.inf.is.odysseus.wrapper.sick.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.sick.MeasurementListener;
import de.uniol.inf.is.odysseus.wrapper.sick.SickConnection;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Sample;
import de.uniol.inf.is.odysseus.spatial.geom.*;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SickSourceAdapter extends AbstractPushingSourceAdapter implements
		MeasurementListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SickSourceAdapter.class);
	private final GeometryFactory geometryFactory = new GeometryFactory();
	private final Map<SourceSpec, SickConnection> connections = new ConcurrentHashMap<SourceSpec, SickConnection>();

	@Override
	protected void doDestroy(final SourceSpec source) {
		if (this.connections.containsKey(source)) {
			this.connections.get(source).close();
			this.connections.remove(source);
		}
	}

	@Override
	protected void doInit(final SourceSpec source) {
		if (!connections.containsKey(source)) {
			final String host = source.getConfiguration().get("host")
					.toString();
			final int port = Integer.parseInt(source.getConfiguration()
					.get("port").toString());
			final double x = Double.parseDouble(source.getConfiguration()
					.get("x").toString());
			final double y = Double.parseDouble(source.getConfiguration()
					.get("y").toString());
			final double angle = Double.parseDouble(source.getConfiguration()
					.get("angle").toString());
			final SickConnection connection;
			String record = "false";
			if (source.getConfiguration().get("record") != null) {
				record = source.getConfiguration().get("record").toString();
			}
			if (record.equalsIgnoreCase("false")) {
				connection = new SickConnectionImpl(host, port, new Coordinate(
						x, y), angle, 0l);
			} else {
				connection = new SickConnectionImpl(host, port, new Coordinate(
						x, y), angle, Long.parseLong(record));
			}

			SickSourceAdapter.LOG
					.debug(String
							.format("Open connection to SICK sensor at %s:%s BackroundRecord:%s ",
									host, port, record));

			connection.open();
			this.connections.put(source, connection);
			connection.setListener(source, this);
		}

	}

	@Override
	public String getName() {
		return "Sick";
	}

	@Override
	public void onMeasurement(final SourceSpec source, final Coordinate origin,
			final double angle, final Measurement measurement,
			final long timestamp) {

		if ((measurement != null) && (measurement.getSamples() != null)) {
			final List<PolarCoordinate> coordinates = new ArrayList<PolarCoordinate>(
					measurement.getSamples().length);

			for (int i = 0; i < measurement.getSamples().length; i++) {
				final Sample sample = measurement.getSamples()[i];
				coordinates.add(new PolarCoordinate((double) sample.getDist1(),
						sample.getAngle()));
			}
			SickSourceAdapter.this.transfer(
					source,
					timestamp,
					new Object[] { origin, angle,
							coordinates.toArray(new PolarCoordinate[] {}),
							timestamp });
			/*
			 * >>>>>>> Optimized the Sick-Background Extraction.
			 * if(this.geometryFactory.createMultiPoint(coordinates.toArray(new
			 * Point[] {})).getCoordinates().length > 0){
			 * SickSourceAdapter.this.transfer(source, timestamp,new Object[] {
			 * this.geometryFactory.createMultiPoint(coordinates.toArray(new
			 * Point[] {})) }); }
			 */

		}

	}
}
