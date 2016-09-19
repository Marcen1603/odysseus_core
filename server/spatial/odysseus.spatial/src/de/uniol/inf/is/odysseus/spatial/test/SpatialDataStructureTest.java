package de.uniol.inf.is.odysseus.spatial.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import junit.framework.TestCase;

/**
 * Tests a spatial data structure.
 * 
 * Hint: Coordinates are Lat/Lng
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialDataStructureTest extends TestCase {

	private static final String DATA_STRUCTURE_NAME = "test";
	private static final int GEOMETRY_POSITION = 0;

	private List<Point> points;
	private IMovingObjectDataStructure dataStructure;
	private GeometryFactory factory;

	// For kNN test
	private Point kNNTestCenter;
	private List<Point> neighbors;

	// For range test
	private Point rangeTestCenter;
	private List<Point> rangeNeighbors;

	@Before
	public void setUp() {

		// Create the list for the kNN test
		neighbors = new ArrayList<Point>(5);

		// Create the list for the range test
		rangeNeighbors = new ArrayList<Point>();

		// Create test data
		factory = new GeometryFactory();
		fillCoordinateList();

		// Create the data structure that needs to be tested
		dataStructure = new NaiveSTDataStructure(DATA_STRUCTURE_NAME, GEOMETRY_POSITION);

		// Fill the data structure with test data
		fillDataStructure();

	}

	private void fillCoordinateList() {
		points = new ArrayList<Point>();

		for (int i = 0; i < 1000; i++) {
			// Create the test data
			// TODO Generate better data
			Coordinate coord = new Coordinate(53.0 + (i / 1000), 19.0 + (i / 1000));
			Point point = factory.createPoint(coord);
			points.add(point);
		}

		fillNeighbourTestData();
		fillRangeTestData();
	}

	private void fillDataStructure() {
		// Fill the data structure with test data
		for (Point point : points) {
			// Create the test data
			Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
			GeometryWrapper wrapper = new GeometryWrapper(point);
			tuple.setAttribute(GEOMETRY_POSITION, wrapper);

			// Add the data to the data structure
			dataStructure.add(tuple);
		}
	}

	private void fillNeighbourTestData() {
		// The point for which we search the kNNs
		kNNTestCenter = factory.createPoint(new Coordinate(10.0, 10.0));

		// Generate points which are the kNNs
		Coordinate coord1 = new Coordinate(10.0, 10.1);
		Coordinate coord2 = new Coordinate(10.1, 10.1);
		Coordinate coord3 = new Coordinate(10.2, 10.2);
		Coordinate coord4 = new Coordinate(10.3, 10.3);
		Coordinate coord5 = new Coordinate(10.4, 10.4);

		// Fill the extra list for the neighbors
		neighbors.add(factory.createPoint(coord1));
		neighbors.add(factory.createPoint(coord2));
		neighbors.add(factory.createPoint(coord3));
		neighbors.add(factory.createPoint(coord4));
		neighbors.add(factory.createPoint(coord5));

		// And add the neighbors to the normal list as well
		for (Point point : neighbors) {
			points.add(point);
		}
	}

	private void fillRangeTestData() {
		// The center to search a certain range. This is the center of Oldenburg
		// downtown.
		rangeTestCenter = factory.createPoint(new Coordinate(53.140206, 8.213074));

		// Generate points which are in the range
		// Coordinates captured with http://www.gps-coordinates.net/
		// Distances calculated with
		// http://www.sunearthtools.com/tools/distance.php
		Coordinate coord1 = new Coordinate(53.14, 8.215091); // about 136.5m
		Coordinate coord2 = new Coordinate(53.140284, 8.217452); // about 292.2
																	// m
		Coordinate coord3 = new Coordinate(53.139872, 8.220799); // about 516.8
																	// m
		Coordinate coord4 = new Coordinate(53.140052, 8.224275); // about 747.5
																	// m
		Coordinate coord5 = new Coordinate(53.14188, 8.243115); // about 2.0129
																// km
		// Huntebruecke, about 4.3577 km
		Coordinate coord6 = new Coordinate(53.153488, 8.274529);
		// Elsfleth, about 19.5791 km
		Coordinate coord7 = new Coordinate(53.238099, 8.457241);

		// Fill the extra list for the correct range result (as we do a 5 km
		// range, we do not include the Elsfleth coordinate)
		rangeNeighbors.add(factory.createPoint(coord1));
		rangeNeighbors.add(factory.createPoint(coord2));
		rangeNeighbors.add(factory.createPoint(coord3));
		rangeNeighbors.add(factory.createPoint(coord4));
		rangeNeighbors.add(factory.createPoint(coord5));
		rangeNeighbors.add(factory.createPoint(coord6));

		// And add the range neighbors to the normal list as well (and add the
		// Elsfleth coordinate as well)
		for (Point point : rangeNeighbors) {
			points.add(point);
		}
		points.add(factory.createPoint(coord7));
	}

	/**
	 * Tests whether the kNN method from the data structure works correctly. We
	 * know the kNNs for the 5 nearest points.
	 */
	@Test
	public void testkNN() {
		int k = neighbors.size();
		List<Tuple<?>> kNN = dataStructure.getKNN(kNNTestCenter, k);
		assertEqualList(kNN, neighbors);
	}

	@Test
	public void testRange() {

	}

	private void assertEqualList(List<Tuple<?>> queryResult, List<Point> correctSolution) {
		// Copy the list with the original neighbors that need to be found
		List<Point> correctSolutionCopy = new ArrayList<>(correctSolution);

		// Both lists need to have the same size
		assertEquals(correctSolutionCopy.size(), queryResult.size());

		// The lists need to be the same (but the order can be different)
		for (int i = 0; i < queryResult.size(); i++) {

			// The neighbor that was found
			Tuple<?> tuple = queryResult.get(i);
			GeometryWrapper geometry = tuple.getAttribute(GEOMETRY_POSITION);

			// Search, if there is this geometry in the list with the correct
			// neighbors
			boolean found = false;
			for (Point point : correctSolutionCopy) {
				if (point.equals(geometry.getGeometry())) {
					// We found the corresponding neighbor
					found = true;
					break;
				}
			}

			if (found) {
				// Remove the element from the list
				correctSolutionCopy.remove(geometry.getGeometry());
			}

			// Was the element found (if not, it is not a correct neighbor)
			assertTrue(found);

		}

		// Now all elements should have been found. The copy of the neighbors
		// list should be empty
		assertEquals(correctSolutionCopy.size(), 0);
	}

}
