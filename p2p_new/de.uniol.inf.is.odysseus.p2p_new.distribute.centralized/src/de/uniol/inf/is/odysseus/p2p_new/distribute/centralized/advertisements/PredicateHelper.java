package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class PredicateHelper {
	private final static String EXPRESSIONSTRING_TAG = "expressionstring";
	private final static String PREDICATE_TYPE_TAG = "predicate_type";
	private final static String RELATIONALPREDICATE_TYPE_TAG = "RelationalPredicate";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generatePredicateStatement(IPredicate p, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		if(p instanceof RelationalPredicate) {
			result.appendChild(result.createElement(PREDICATE_TYPE_TAG, RELATIONALPREDICATE_TYPE_TAG));
			RelationalPredicate pred = (RelationalPredicate)p;
			String predicateExpressionString = pred.getExpression().getExpressionString();
			result.appendChild(result.createElement(EXPRESSIONSTRING_TAG, predicateExpressionString));
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static IPredicate createPredicateFromStatement(TextElement<?> statement) {
		IPredicate pred = null;
		String type = "";
		Enumeration<? extends TextElement<?>> elements = statement.getChildren();
		List<TextElement<?>> details = new ArrayList<TextElement<?>>();
		
		while(elements.hasMoreElements()) {
			TextElement elem = elements.nextElement();
			if(elem.getName().equals(PREDICATE_TYPE_TAG)) {
				type = elem.getTextValue();
			} else {
				details.add(elem);
			}
		}
		if(type.equals(RELATIONALPREDICATE_TYPE_TAG)) {
			for(TextElement e : details) {
				if(e.getName().equals(EXPRESSIONSTRING_TAG)) {
					SDFExpression exp = new SDFExpression(e.getTextValue(), MEP.getInstance());
					pred = new RelationalPredicate(exp);
				}
			}
		}

		return pred;
	}
}
