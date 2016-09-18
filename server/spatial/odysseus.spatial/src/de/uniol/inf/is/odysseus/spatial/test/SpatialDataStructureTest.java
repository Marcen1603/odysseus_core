package de.uniol.inf.is.odysseus.spatial.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import junit.framework.TestCase;

public class SpatialDataStructureTest extends TestCase {

	private static final String DATA_STRUCTURE_NAME = "test";

	private List<Coordinate> coords;
	private IMovingObjectDataStructure dataStructure;

	@Before
	public void setup() {

		// Create test data
		fillCoordinateList();

		// Create the data structure that needs to be tested
		dataStructure = new NaiveSTDataStructure(DATA_STRUCTURE_NAME, 0);
		
		// Fill the data structure with test data
		fillDataStructure();

	}

	private void fillCoordinateList() {
		coords = new ArrayList<Coordinate>();

		for (int i = 0; i < 1000; i++) {
			// Create the test data
			Coordinate coord = new Coordinate(53.2, 19.4);
			coords.add(coord);
		}

	}

	private void fillDataStructure() {
		// Fill the data structure with test data
		for (Coordinate coord : coords) {
			// Create the test data
			Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
			GeometryFactory factory = new GeometryFactory();
			Geometry geometry = factory.createPoint(coord);
			GeometryWrapper wrapper = new GeometryWrapper(geometry);
			tuple.setAttribute(0, wrapper);

			// Add the data to the data structure
			dataStructure.add(tuple);
		}

	}

	@Test
	public void testkNN() {

	}

}
