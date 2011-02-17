/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.nexmark.xml;

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
			for (int i=0;i<nl.getLength();){
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
