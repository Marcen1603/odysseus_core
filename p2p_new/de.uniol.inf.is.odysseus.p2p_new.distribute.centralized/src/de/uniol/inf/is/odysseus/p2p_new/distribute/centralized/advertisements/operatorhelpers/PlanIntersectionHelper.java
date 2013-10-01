package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.PlanIntersection;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisement;

public class PlanIntersectionHelper {

	private static String OLD_OPERATOR_ID_TAG = "subscription_target";
	private static String SOURCEOUTPORT_TAG = "subscription_sourceoutport";
	private static String SINKINPORT_TAG = "subscription_sinkinport";
	private static String SCHEMA_TAG = "subscription_schema";
	private static String NEW_OPERATOR_ID_TAG = "subscription_origin";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generatePlanIntersectionStatement(MimeMediaType mimeType, Collection<PlanIntersection> planIntersections) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPartAdvertisement.getAdvertisementType());
		for(PlanIntersection pi : planIntersections) {
			// use the hash of the current operator and of the target as IDs. If the operators are transferred,
			// this hash will be used for its id as well and associated with the new operator during reconstruction
			result.appendChild(result.createElement(NEW_OPERATOR_ID_TAG, pi.getNewOperatorID()));
			result.appendChild(result.createElement(OLD_OPERATOR_ID_TAG, pi.getOldOperatorID()));
			result.appendChild(result.createElement(SINKINPORT_TAG, pi.getSinkInPort()));
			result.appendChild(result.createElement(SOURCEOUTPORT_TAG, pi.getSourceOutPort()));
			result.appendChild(result.createElement(SCHEMA_TAG, SchemaHelper.createOutputSchemaStatement(pi.getSchema(), mimeType)));
		}
		return result;
	}

	public static List<PlanIntersection> createPlanIntersectionsFromStatement(TextElement<?> statement) {
		List<PlanIntersection> result = new ArrayList<PlanIntersection>();
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();

		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			Enumeration<? extends TextElement<?>> subscriptionDetails = elem.getChildren();
			int newOperatorID =-1;
			int oldOperatorID =-1;
			int sinkInPort = -1;
			int sourceOutPort = -1;
			SDFSchema schema = null;

			while(subscriptionDetails.hasMoreElements()) {
				TextElement<?> subDetailElem = subscriptionDetails.nextElement();
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

