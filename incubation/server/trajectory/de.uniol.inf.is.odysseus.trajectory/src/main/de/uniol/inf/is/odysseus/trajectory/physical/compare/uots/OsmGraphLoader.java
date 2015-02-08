package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class OsmGraphLoader implements IGraphLoader<String, Integer> {

	private final static Logger LOGGER = LoggerFactory.getLogger(OsmGraphLoader.class);
	
	private final static String AP_NODES = "//node";
	private final static String AP_WAYS = "//way";
	private final static String AP_REFS = "child::nd";
	
	private final static String NODE_ELEM_NAME = "node";
	private final static String NODE_LON_ATTR_NAME = "lon";
	private final static String NODE_LAT_ATTR_NAME = "lat";
	private final static String NODE_ID_ATTR_NAME = "id";
	
	private final static String WAY_ELEM_NAME = "way";
	
	private final static String ND_ELEM_NAME = "nd";
	private final static String ND_REF_ATTR_NAME = "ref";
	
	
	@Override
	public UndirectedSparseGraph<Point, LineSegment> load(String filepath, Integer utmZone) {
		
		final UndirectedSparseGraph<Point, LineSegment> graph = new UndirectedSparseGraph<>();
		
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(utmZone);
		
		final VTDGen vg = new VTDGen();
		vg.parseFile(filepath, false);
		
		final VTDNav vn = vg.getNav();
		final AutoPilot apNodes = new AutoPilot(vn);
		
		final Map<String, Point> pointsMap = new HashMap<>();
		
		try {
			apNodes.selectXPath(AP_NODES);
			while(apNodes.evalXPath() != -1) {
			    vn.toElement(VTDNav.FIRST_CHILD, NODE_ELEM_NAME);
			    final Point point = pointCreator.createPoint(
			    		Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LON_ATTR_NAME))), 
			    		Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LAT_ATTR_NAME))));
			    graph.addVertex(point);
			    pointsMap.put(vn.toNormalizedString(vn.getAttrVal(NODE_ID_ATTR_NAME)), point);
			}
			
			final AutoPilot apWays = new AutoPilot(vn);
			apWays.selectXPath(AP_WAYS);
			
			final AutoPilot apNds = new AutoPilot(vn);
			
			while(apWays.evalXPath() != -1) {
			    vn.toElement(VTDNav.FIRST_CHILD, WAY_ELEM_NAME);
			    
			    apNds.selectXPath(AP_REFS);
			    if(apNds.evalXPath() != -1) {
			    	vn.toElement(VTDNav.FIRST_CHILD, ND_ELEM_NAME);
			    	String firstPoint = vn.toNormalizedString(vn.getAttrVal(ND_REF_ATTR_NAME));
			    	Point point1 = pointsMap.get(firstPoint);
			    	while(apNds.evalXPath() != -1) {
			    		vn.toElement(VTDNav.FIRST_CHILD, ND_ELEM_NAME);
			    		final Point point2 = pointsMap.get(vn.toNormalizedString(vn.getAttrVal(ND_REF_ATTR_NAME)));
			    		graph.addEdge(new LineSegment(point1.getCoordinate(), point2.getCoordinate()), point1, point1 = point2);
				    }
			    }
			}			
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		return graph;
	}

}
