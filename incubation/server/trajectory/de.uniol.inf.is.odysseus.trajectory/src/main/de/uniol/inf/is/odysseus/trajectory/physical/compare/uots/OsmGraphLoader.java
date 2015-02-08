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
	
	private final static String AP_NODE = "//node";
	
	private final static String NODE_ELEM_NAME = "node";
	private final static String NODE_LON_ATTR_NAME = "lon";
	private final static String NODE_LAT_ATTR_NAME = "lat";
	private final static String NODE_ID_ATTR_NAME = "id";
	
	
	@Override
	public UndirectedSparseGraph<Point, LineSegment> load(String filepath, Integer utmZone) {
		
		final UndirectedSparseGraph<Point, LineSegment> graph = new UndirectedSparseGraph<>();
		
		final IPointCreator pointCreator = UtmPointCreatorFactory.getInstance().create(utmZone);
		
		final VTDGen vg = new VTDGen();
		vg.parseFile(filepath, false);
		
		final VTDNav vn = vg.getNav();
		final AutoPilot apNode = new AutoPilot(vn);
		
		final Map<String, Point> pointsMap = new HashMap<>();
		
		try {
			apNode.selectXPath(AP_NODE);
			while (apNode.evalXPath() != -1) {
			    vn.toElement(VTDNav.FIRST_CHILD, NODE_ELEM_NAME);
			    final Point point = pointCreator.createPoint(
			    		Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LON_ATTR_NAME))), 
			    		Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LAT_ATTR_NAME))));
			    graph.addVertex(point);
			    pointsMap.put(vn.toNormalizedString(vn.getAttrVal(NODE_ID_ATTR_NAME)), point);
			}
		} catch(Exception e) {
			LOGGER.error("", e);
			throw new RuntimeException(e);
		}
		
		return null;
	}

}
