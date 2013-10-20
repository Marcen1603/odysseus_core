package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.PlanIntersection;

public class PlanIntersectionHelper {

	private static String OLD_OPERATOR_ID_TAG = "subscription_target";
	private static String SOURCEOUTPORT_TAG = "subscription_sourceoutport";
	private static String SINKINPORT_TAG = "subscription_sinkinport";
	private static String SCHEMA_TAG = "subscription_schema";
	private static String NEW_OPERATOR_ID_TAG = "subscription_origin";
	private static String PLANINTERSECTION_ELEMENT_TAG = "planIntersection";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generatePlanIntersectionStatement(MimeMediaType mimeType, Collection<PlanIntersection> planIntersections, StructuredDocument rootDoc) {
		if(planIntersections.isEmpty()) {
			return rootDoc;
		}
		for(PlanIntersection pi : planIntersections) {
			Element planIntersectionElement = rootDoc.createElement(PLANINTERSECTION_ELEMENT_TAG);
			rootDoc.appendChild(planIntersectionElement);
			// use the hash of the current operator and of the target as IDs. If the operators are transferred,
			// this hash will be used for its id as well and associated with the new operator during reconstruction
			planIntersectionElement.appendChild(rootDoc.createElement(NEW_OPERATOR_ID_TAG, Integer.toString(pi.getNewOperatorID())));
			planIntersectionElement.appendChild(rootDoc.createElement(OLD_OPERATOR_ID_TAG, Integer.toString(pi.getOldOperatorID())));
			planIntersectionElement.appendChild(rootDoc.createElement(SINKINPORT_TAG, Integer.toString(pi.getSinkInPort())));
			planIntersectionElement.appendChild(rootDoc.createElement(SOURCEOUTPORT_TAG, Integer.toString(pi.getSourceOutPort())));
			Element schemaElement = rootDoc.createElement(SCHEMA_TAG);
			planIntersectionElement.appendChild(schemaElement);
			SchemaHelper.createOutputSchemaStatement(pi.getSchema(), mimeType,rootDoc,schemaElement);
		}
		return rootDoc;
	}

	public static List<PlanIntersection> createPlanIntersectionsFromStatement(StructuredDocument<? extends TextElement<?>> statement) {
		List<PlanIntersection> result = new ArrayList<PlanIntersection>();
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();

		// iterate over all the planIntersection-Elements
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			
			Enumeration<? extends TextElement<?>> planIntersectionDetails = elem.getChildren();
			int newOperatorID =-1;
			int oldOperatorID =-1;
			int sinkInPort = -1;
			int sourceOutPort = -1;
			SDFSchema schema = null;
			// collect necessary information for this element
			while(planIntersectionDetails.hasMoreElements()) {
				TextElement<?> subDetailElem = planIntersectionDetails.nextElement();
				if(subDetailElem.getName().equals(NEW_OPERATOR_ID_TAG)) {
					newOperatorID = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(OLD_OPERATOR_ID_TAG)) {
					oldOperatorID = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SINKINPORT_TAG)) {
					sinkInPort = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SOURCEOUTPORT_TAG)) {
					sourceOutPort = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SCHEMA_TAG)) {
					schema = SchemaHelper.createSchemaFromStatement(subDetailElem);
				}
			}
			PlanIntersection pi = new PlanIntersection(oldOperatorID, newOperatorID, sinkInPort, sourceOutPort, schema);
			result.add(pi);
		}
		return result;	
	}		
}

