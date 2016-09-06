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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

/**
 * An implementation of <tt>IQueryTrajectoryLoader</tt> which loads 
 * <tt>RawQueryTrajectory</tt> from <i>CSV files</i>. The CSV file 
 * must be comma separated and each line must contain the <i>latitude</i>
 * and <i>longitude</i>.
 * 
 * @author marcus
 *
 */
public class CsvQueryTrajectoryLoader implements IQueryTrajectoryLoader {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(CsvQueryTrajectoryLoader.class);
	
	/**
	 * Loads and returns a <tt>RawQueryTrajectory</tt> from a CSV file
	 * identified by the passed <i>filepath</i> in the passed <i>utmZone</i>.
	 *  
	 * @param filepath the path to the CSV file where trajectory data is stored
	 * @param utmZone
	 */
	@Override
	public RawQueryTrajectory load(final String filepath, final Integer utmZone) {
		
		Reader reader = null;
		CSVParser parser = null;
		List<CSVRecord> records = null;
		try {
			reader = new FileReader(filepath);
			parser = CSVFormat.DEFAULT.parse(reader);
			records = parser.getRecords();
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					LOGGER.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
			if(parser != null) {
				try {
					parser.close();
				} catch (final IOException e) {
					LOGGER.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
		}
		
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(utmZone);
		final List<Point> points = new LinkedList<Point>();
		for(final CSVRecord record : records) {
			points.add(pointCreator.createPoint(
					new Coordinate(
							Double.parseDouble(record.get(0)), 
							Double.parseDouble(record.get(1)))));
		}
		
		return new RawQueryTrajectory(points);
	}
}
