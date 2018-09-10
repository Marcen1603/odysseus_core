//package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.wms;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.Mercator;
//
//public class WMService {
//	private String baseUrl;
//	private String layer;
//
//	/** Creates a new instance of WMSService */
//	public WMService() {
//		// by default use a known osm server
//		setLayer("ors-osm");
//		// setBaseUrl("http://wms.jpl.nasa.gov/wms.cgi?");
//		// setBaseUrl("http://129.206.228.72/cached/osm?");
//		// setBaseUrl("http://vmap0.tiles.osgeo.org/wms/vmap0?");
//		setBaseUrl("http://wms.latlon.org");
//	}
//
//	public WMService(String baseUrl, String layer) {
//		this.baseUrl = baseUrl;
//		this.layer = layer;
//	}
//
//	public String getCapabilitiesURL() {
//		return this.baseUrl + "?SERVICE=WMS&REQUEST=GetCapabilities";
//	}
//
//	public String toWMSURL(int x, int y, int zoom, int tileSize) {
//		String format = "image/jpeg";
//		String styles = "";
//		String srs = "EPSG:4326";
//		int ts = tileSize;
//		int circumference = widthOfWorldInPixels(zoom, tileSize);
//		double radius = circumference / (2 * Math.PI);
//		double ulx = Mercator.xToLong(x * ts, radius);
//		double uly = Mercator.yToLat(y * ts, radius);
//		double lrx = Mercator.xToLong((x + 1) * ts, radius);
//		double lry = Mercator.yToLat((y + 1) * ts, radius);
//		String bbox = ulx + "," + uly + "," + lrx + "," + lry;
//		String url = getBaseUrl() + "version=1.1.1&request=" + "GetMap&Layers=" + layer + "&format=" + format + "&BBOX=" + bbox + "&width=" + ts + "&height=" + ts + "&SRS=" + srs + "&Styles=" + styles + "&transparent=TRUE" + "";
//		return url;
//	}
//
//	public Object getDOM(String content) {
//		try {
////			System.out.println(content);
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document doc = db.parse(content);
//			doc.getDocumentElement().normalize();
//			System.out.println("Root element " + doc.getDocumentElement().getNodeName());
//			
//			NodeList nodeLst = doc.getDocumentElement().getChildNodes();
//			printDOM(nodeLst, 0);
////			for(int i=0; i < nodeLst.getLength(); i++){
////				System.out.println();
////				Node node = (Node)nodeLst.item(i) ;
////				System.out.print(node.getNodeName());
////				System.out.print(node.getTextContent());
////				System.out.println();
////			}
//			
////			NodeList nodeLst = doc.getElementsByTagName("employee");
////			System.out.println("Information of all employees");
//
////			for (int s = 0; s < nodeLst.getLength(); s++) {
////
////				Node fstNode = nodeLst.item(s);
////
////				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
////
////					Element fstElmnt = (Element) fstNode;
////					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("firstname");
////					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
////					NodeList fstNm = fstNmElmnt.getChildNodes();
////					System.out.println("First Name : " + ((Node) fstNm.item(0)).getNodeValue());
////					NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
////					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
////					NodeList lstNm = lstNmElmnt.getChildNodes();
////					System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
////				}
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public void printDOM(NodeList nodeLst, int deep){
//		for(int i=0; i < nodeLst.getLength(); i++){
//			Node node = (Node)nodeLst.item(i);
//			System.out.println();
//			System.out.print(deep + ": ");
//			for(int ii = 0; ii <= deep; ii++){
//				System.out.print(' ');
//			}
//			System.out.print(node.getNodeName());
//			System.out.print(": ");
////			System.out.print(node.getTextContent());
//			System.out.print(node.getTextContent().toString().trim());
//			//System.out.print(" ");
//			//System.out.print(node.getTextContent());
//			//System.out.print(" ");
//			//System.out.print(node.getPrefix());
//			
//			if(node.getChildNodes().getLength() > 0){
//				printDOM(node.getChildNodes(),deep++);
//			}
//		}
//		
//	}
//	
//
//	public String getUrlContent(URL url) {
//		StringBuilder builder = null;
//		try {
//			// url = new URL("http://www.bancdeswiss.com");
//			builder = new StringBuilder();
//
//			// file = new File(url.getContent() + ".txt");
//			// writer = new FileWriter(file, true);
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//			String inputLine;
//			while ((inputLine = in.readLine()) != null) {
//				// writer.write(inputLine);
//				// writer.write(System.getProperty("line.separator"));
//				builder.append(inputLine);
//				builder.append(System.getProperty("line.separator"));
//
//			}
//			in.close();
//			// writer.flush();
//			// writer.close();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return builder.toString();
//	}
//
//	private int widthOfWorldInPixels(int zoom, int TILE_SIZE) {
//		// int TILE_SIZE = 256;
//		int tiles = (int) Math.pow(2, zoom);
//		int circumference = TILE_SIZE * tiles;
//		return circumference;
//	}
//
//	public String getLayer() {
//		return layer;
//	}
//
//	public void setLayer(String layer) {
//		this.layer = layer;
//	}
//
//	public String getBaseUrl() {
//		return baseUrl;
//	}
//
//	public void setBaseUrl(String baseUrl) {
//		this.baseUrl = baseUrl;
//	}
//
//	public static void main(String args[]) {
//		WMService service = new WMService();
//		try {
//			String content = service.getUrlContent(new URL(service.getCapabilitiesURL()));
////			System.out.println(content);
//			
//			service.getDOM(service.getCapabilitiesURL());
//			
//			//System.out.println(service.toWMSURL(0, 0, 0, 256));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
