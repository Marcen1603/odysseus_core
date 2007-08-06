package mg.dynaquest.queryexecution.factory;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.4 $
 Log: $Log: PlanFactory.java,v $
 Log: Revision 1.4  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2004/07/28 11:29:47  grawund
 Log: POManager erste Version integriert
 Log:
 Log: Revision 1.2  2002/01/31 16:13:56  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.xml.DOMHelp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PlanFactory {

	public static PlanOperator[] createPlan(String xmlFilename) {
		HashMap<String,PlanOperator> idToPOMap = new HashMap<String,PlanOperator>();
		try {
			Document planDoc = DOMHelp.parse(xmlFilename, false);
			//NodeList childNodes = planDoc.getChildNodes();

			Element docNode = planDoc.getDocumentElement();
			if (docNode.getLocalName().equals("plan")) {
				NodeList planNodes = docNode.getChildNodes();

				for (int j = 0; j < planNodes.getLength(); j++) {
					Node pNode = planNodes.item(j);
					if (pNode.getNodeType() == Node.ELEMENT_NODE) {
//						//System.out.println(j+"-->"+pNode.getLocalName());
//						tpo = POFactory.createPO(pNode.getLocalName());
//						//System.out.println(tpo.getClass().getName());
//						idToPOMap.put(tpo.initBaseValues(pNode), tpo);
						
						NamedNodeMap attribs = pNode.getAttributes();
						String class_name = attribs.getNamedItem("class").getNodeValue();
						//System.out.println(j+"-->"+pNode.getLocalName());
						PlanOperator tpo = POFactory.createPO(class_name);
						//System.out.println(tpo.getClass().getName());
						idToPOMap.put(tpo.initBaseValues(pNode), tpo);						
					}
				} // for

			} else {
				System.err.println("ToDo");
			}

			// if (Plan-Knoten)

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Nun haben wir den "unverbundenen" Plan in idToPOMap
		for (PlanOperator tpo: idToPOMap.values()){
			tpo.initInputs(idToPOMap);
		}

		// Jetzt ist der Plan verbunden, was man jetzt noch machen muss, ist die
		// Wurzelelemente finden
		// Wurzelelemente sind die Elemente, dich nicht in einem anderen PO
		// als Input auftreten

		// Vorgehen. Dupliziere die Menge aller POS und interiere durch eine
		// der Mengen durch. Streiche in der zweiten Menge alle die Knoten,
		// die als Kinder auftreten. Was uebrig bleibt sind die Menge der
		// Wurzel-
		//  Knoten		
		Set<PlanOperator> topPOs = new HashSet<PlanOperator>(idToPOMap.values());
        for (PlanOperator tpo: idToPOMap.values()){			
			for (int i = 0; i < tpo.getNumberOfInputs(); i++) {
				PlanOperator in = tpo.getInputPO(i);
				topPOs.remove(in);
			}
		}
	
        PlanOperator[] tops = new PlanOperator[topPOs.size()];
//		int i = 0;
//		for (PlanOperator top: tops) {
//			tops[i] = (NAryPlanOperator) iter.next();
//			System.out.println("Wurzel gefunden: " + tops[i].getPOName());
//			StringBuffer xmlBuff = new StringBuffer();
//			System.out.println(tops[i]
//					.getXMLRepresentation("  ", "  ", xmlBuff));
//			System.out.println(xmlBuff);
//			i++;
//		}
                
		return topPOs.toArray(tops);
	}

	public static void main(String[] args) {
		PlanFactory
				.createPlan("e:\\dissertation\\Buchsuche\\Init\\outputFULL.xml");
	}

}