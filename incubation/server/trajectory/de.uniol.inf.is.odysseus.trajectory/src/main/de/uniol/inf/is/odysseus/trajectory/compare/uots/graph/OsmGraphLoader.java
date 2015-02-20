package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import de.uniol.inf.is.odysseus.trajectory.compare.util.IPointCreator;
import de.uniol.inf.is.odysseus.trajectory.compare.util.UtmPointCreatorFactory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

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
	
	private final static OsmGraphLoader INSTANCE = new OsmGraphLoader();
	
	public final static OsmGraphLoader getInstance() {
		return INSTANCE;
	}
	
	
	/**
	 * md5 sums and already loaded Graph
	 */
	private final Map<String, NetGraph> graphs = new HashMap<>();

	private OsmGraphLoader() { }
	
	@Override
	public NetGraph load(String filepath, Integer utmZone) {
		NetGraph graph = null;
		InputStream fis = null;
		String md5 = null;
		try {
			fis = new FileInputStream(filepath);
			md5 = DigestUtils.md5Hex(fis);

			graph = this.graphs.get(md5);
			if(graph != null) {
				LOGGER.info("Use cached graph from file \"" + filepath + "\" with MD5 \"" + md5 + "\"");
				return graph;
			}
		} catch(IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		
		final UndirectedSparseGraph<Point, LineSegment> complexGraph = new UndirectedSparseGraph<>();
		
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
			    		new Coordinate(
			    				Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LON_ATTR_NAME))), 
			    				Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LAT_ATTR_NAME)))));
			    complexGraph.addVertex(point);
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
			    		complexGraph.addEdge(new LineSegment(point1.getCoordinate(), point2.getCoordinate()), point1, point1 = point2, EdgeType.UNDIRECTED);
				    }
			    }
			}			
		} catch(NavException | XPathEvalException | XPathParseException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		this.graphs.put(md5, graph = new NetGraph(complexGraph));
		return graph;
	}

}
