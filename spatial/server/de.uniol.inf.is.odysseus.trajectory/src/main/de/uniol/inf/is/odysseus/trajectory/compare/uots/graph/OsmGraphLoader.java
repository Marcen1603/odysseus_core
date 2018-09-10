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

/**
 * An implementation of <tt>IGraphLoader</tt> which loads a <tt>NetGraph</tt>
 * from an <i>OSM file</i>. Due to performance gains <i>vtd-xml</i> is used to 
 * process the <i>OSM data</i>. Additionally <tt>OsmGraphLoader</tt> stores 
 * <i>MD5 sums</i> of the files that has been loaded and the corresponding 
 * <tt>NetGraphs</tt>. In this way the same data does not need to be processed
 * twice even then if its source is from different files.
 * 
 * @author marcus
 *
 */
public class OsmGraphLoader implements IGraphLoader<String, Integer> {

	/** Logger for debugging purposes */
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
	
	/** the singleton instance */
	private final static OsmGraphLoader INSTANCE = new OsmGraphLoader();
	
	/**
	 * Returns the <tt>OsmGraphLoader</tt> as an eager singleton.
	 * 
	 * @return the <tt>OsmGraphLoader</tt> as an eager singleton
	 */
	public final static OsmGraphLoader getInstance() {
		return INSTANCE;
	}
	
	/**
	 * md5 sums and already loaded Graph
	 */
	private final Map<String, NetGraph> graphs = new HashMap<>();

	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private OsmGraphLoader() { }
	
	/**
	 * Loads an <tt>NetGraph</tt> from an <i>OSM file</i>.
	 * 
	 * @param filepath the path to the OSM file
	 * @param utmZone the UTM zone of the OSM data
	 * @throws IllegalArgumentException if <tt>filepath == null</tt>
	 * 
	 */
	@Override
	public NetGraph load(final String filepath, final Integer utmZone) throws IllegalArgumentException {
		if(filepath == null) {
			throw new IllegalArgumentException("filepath is null");
		}
		NetGraph graph = null;
		InputStream fis = null;
		String md5 = null;
		try {
			fis = new FileInputStream(filepath);
			md5 = DigestUtils.md5Hex(fis);

			graph = this.graphs.get(md5);
			if(graph != null) {
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("Use cached graph from file \"" + filepath + "\" with MD5 \"" + md5 + "\"");
				}
				return graph;
			}
		} catch(final IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (final IOException e) {
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
			    				Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LAT_ATTR_NAME))), 
			    				Double.parseDouble(vn.toNormalizedString(vn.getAttrVal(NODE_LON_ATTR_NAME)))));
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
			    	final String firstPoint = vn.toNormalizedString(vn.getAttrVal(ND_REF_ATTR_NAME));
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
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("New graph created from file \"" + filepath + "\" with MD5 \"" + md5 + "\"");
		}
		
		this.graphs.put(md5, graph = new NetGraph(complexGraph));
		return graph;
	}

}
