//package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.wms;
//
//import java.awt.BorderLayout;
//import java.awt.Image;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//
//import org.geotools.data.ows.CRSEnvelope;
//import org.geotools.data.ows.Layer;
//import org.geotools.data.ows.StyleImpl;
//import org.geotools.data.ows.WMSCapabilities;
//import org.geotools.data.wms.WMSUtils;
//import org.geotools.data.wms.WebMapServer;
//import org.geotools.data.wms.request.GetMapRequest;
//import org.geotools.data.wms.response.GetMapResponse;
//import org.geotools.ows.ServiceException;
//
//public class WMServiceTest {
//
//	private static WebMapServer wms;
//	
//	public WMServiceTest() {
//		 try {
//	        wms = new WebMapServer(new URL("http://www.osmgb.org.uk/ogc/wms"));
//        } catch (ServiceException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//        } catch (MalformedURLException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//        } catch (IOException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//        }
//	}
//	
//	
//	
//	public String getInfo(){
//		StringBuilder builder = new StringBuilder();
//		WMSCapabilities capabilities = wms.getCapabilities();
//
//		String serverName = capabilities.getService().getName();
//		String serverTitle = capabilities.getService().getTitle();
//		builder.append("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");
//		builder.append('\n');
//		Layer rootLayer = capabilities.getLayer();
//		List layers = capabilities.getLayerList();
//		Layer[] layersArray = WMSUtils.getNamedLayers(capabilities);
//
//		if (capabilities.getRequest().getGetFeatureInfo() != null) {
//			// This server supports GetFeatureInfo requests!
//			// We could make one if we wanted to.
//			builder.append(("GetFeatures Formats: " + capabilities.getRequest().getGetMap().getFormats()));
//			
//			builder.append('\n');
//		}
//		
//		for (int i = 0; i < layersArray.length; i++) {
//			// Print layer info
//			builder.append("Layer: (" + i + ")" + layersArray[i].getName()); builder.append('\n');
//			builder.append("       " + layersArray[i].getTitle()); builder.append('\n');
//			builder.append("       " + layersArray[i].getChildren().length); builder.append('\n');
//			builder.append("       " + layersArray[i].getBoundingBoxes()); builder.append('\n');
//			CRSEnvelope env = layersArray[i].getLatLonBoundingBox();
//			builder.append("       " + env.getLowerCorner() + " x " + env.getUpperCorner()); builder.append('\n');
//
//			// Get layer styles
//			List styles = layersArray[i].getStyles();
//			for (Iterator it = styles.iterator(); it.hasNext();) {
//				StyleImpl elem = (StyleImpl) it.next();
//
//				// Print style info
//				builder.append("Style:");
//				builder.append('\n');
//				builder.append("  Name:" + elem.getName());
//				builder.append('\n');
//				builder.append("  Title:" + elem.getTitle());
//				builder.append('\n');
//			}
//			builder.append('\n');
//			builder.append('\n');
//		}
//		
//		return builder.toString();
//	}
//	
//	
//
//	public static void main(String args[]) {
//		try {
//			
//			WMSCapabilities capabilities = wms.getCapabilities();
//
//			String serverName = capabilities.getService().getName();
//			String serverTitle = capabilities.getService().getTitle();
//			System.out.println("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");
//
//			Layer rootLayer = capabilities.getLayer();
//			List layers = capabilities.getLayerList();
//			Layer[] layersArray = WMSUtils.getNamedLayers(capabilities);
//
//			if (capabilities.getRequest().getGetFeatureInfo() != null) {
//				// This server supports GetFeatureInfo requests!
//				// We could make one if we wanted to.
//				System.out.println("GetFeatures: " + capabilities.getRequest().getGetMap().getFormats());
//				
//				
//			}
//
//			for (int i = 0; i < layersArray.length; i++) {
//				// Print layer info
//				System.out.println("Layer: (" + i + ")" + layersArray[i].getName());
//				System.out.println("       " + layersArray[i].getTitle());
//				System.out.println("       " + layersArray[i].getChildren().length);
//				System.out.println("       " + layersArray[i].getBoundingBoxes());
//				CRSEnvelope env = layersArray[i].getLatLonBoundingBox();
//				System.out.println("       " + env.getLowerCorner() + " x " + env.getUpperCorner());
//
//				// Get layer styles
//				List styles = layersArray[i].getStyles();
//				for (Iterator it = styles.iterator(); it.hasNext();) {
//					StyleImpl elem = (StyleImpl) it.next();
//
//					// Print style info
//					System.out.println("Style:");
//					System.out.println("  Name:" + elem.getName());
//					System.out.println("  Title:" + elem.getTitle());
//				}
//			}
//
//			GetMapRequest request = wms.createGetMapRequest();
//			request.setFormat("image/jpeg");
//			request.setDimensions(256, 256);
//			request.setTransparent(true);
//			request.setSRS("EPSG:4326");
//			request.setBBox("-131.13151509433965,46.60532747661736,-117.61620566037737,56.34191403281659");
//			
//
//			for (Layer layer : WMSUtils.getNamedLayers(capabilities)) {
//				
//				
//				request.addLayer(layer);
//				GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
//
//				System.out.println("Load Test Image");
//				System.out.println();
//								
////				ImageIO.read();
//				
////				InputStream stream = response.getInputStream();
////				BufferedInputStream in = new BufferedInputStream(stream);
////
////				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("/Users/cai/Desktop/tmp/image_" + System.currentTimeMillis() + ".png"));
////
////				int i;
////				while ((i = in.read()) != -1) {
////					out.write(i);
////					//System.out.print(i);
////				}
////				out.flush();
////
////				out.close();
////				in.close();
//				System.out.println();
//				System.out.println("End");
//			}
//
//			
//			
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
