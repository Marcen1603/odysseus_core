package mg.dynaquest.xml;

import java.io.StringWriter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XPathHelper {

	

   public static NodeList processXPath(String xpathExpr, InputSource target) throws XPathExpressionException{
	   XPath xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
	   Object o = xpath.evaluate(xpathExpr,target,XPathConstants.NODESET);
	   return (NodeList) o;	   
   }
   
	public static String dumpSingleElement(InputSource input, String xpathExpr) {
		try {
			NodeList nl = XPathHelper.processXPath(xpathExpr,input);
			for (int i=0;i<nl.getLength();i++){
				Node n = nl.item(i).getFirstChild();
				if (n!= null){
					return n.getTextContent();
				}else{
					return "";
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String dumpTreeElements(InputSource input, String xpathExpr) {
		StringBuffer ret = new StringBuffer();
		try {
			NodeList nl = XPathHelper.processXPath(xpathExpr,input);
			for (int i=0;i<nl.getLength();i++){
				StringWriter w = new StringWriter();
				//System.out.println(nl.item(i));
				DOMHelp.dumpNode(nl.item(i), w, false);
				ret.append(w.toString());
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

	public static String dumpChildren(Node node){
		StringBuffer ret = new StringBuffer();
		NodeList nl = node.getChildNodes();
		for (int i=0;i<nl.getLength();i++){
			StringWriter w = new StringWriter();
			//System.out.println(nl.item(i));
			DOMHelp.dumpNode(nl.item(i), w, false);
			ret.append(w.toString());
		}
		return ret.toString();
	}
	
	public static NodeList getTreeElements(InputSource input, String xpathExpr) {
		//StringBuffer ret = new StringBuffer();
		try {
			NodeList nl = XPathHelper.processXPath(xpathExpr,input);
			return nl;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
