package de.uniol.inf.is.odysseus.trajectory.compare.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

public class CsvQueryTrajectoryLoader implements IQueryTrajectoryLoader {

	private final static Logger LOGGER = LoggerFactory.getLogger(CsvQueryTrajectoryLoader.class);
	
	@Override
	public RawQueryTrajectory load(String param, Integer additional) {
		
		Reader reader = null;
		CSVParser parser = null;
		List<CSVRecord> records = null;
		try {
			reader = new FileReader(param);
			parser = CSVFormat.DEFAULT.parse(reader);
			records = parser.getRecords();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
			if(parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
		}
		
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(additional);
		final List<Point> points = new LinkedList<Point>();
		for(final CSVRecord record : records) {
			points.add(pointCreator.createPoint(
					Double.parseDouble(record.get(0)), 
					Double.parseDouble(record.get(1))));
		}
		
		return new RawQueryTrajectory(points);
	}
}
