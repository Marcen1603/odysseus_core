package de.uniol.inf.is.odysseus.trajectory.physical.compare;

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

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.util.IPointCreator;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.util.UtmPointCreatorFactory;

public class CsvQueryTrajectoryLoader implements IQueryTrajectoryLoader {

	private final static Logger LOGGER = LoggerFactory.getLogger(CsvQueryTrajectoryLoader.class);
	
	@Override
	public RawTrajectory load(String param, Integer additional) {
		
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
		
		return new RawTrajectory(null, points);
	}
}
