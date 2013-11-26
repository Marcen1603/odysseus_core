package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class PredicateHelper {
	private final static String EXPRESSIONSTRING_TAG = "expressionstring";
	private final static String PREDICATE_TYPE_TAG = "predicate_type";
	private final static String RELATIONALPREDICATE_TYPE_TAG = "RelationalPredicate";
	private final static String RELATIONALPREDICATE_LEFT_SCHEMA_TAG = "left_schema";
	private final static String RELATIONALPREDICATE_RIGHT_SCHEMA_TAG = "right_schema";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generatePredicateStatement(IPredicate p, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo, ISink operatorUsingThisPredicate) {
		if(p instanceof RelationalPredicate) {
			toAppendTo.appendChild(rootDoc.createElement(PREDICATE_TYPE_TAG, RELATIONALPREDICATE_TYPE_TAG));
			RelationalPredicate pred = (RelationalPredicate)p;
			String predicateExpressionString = pred.getExpression().getExpressionString();
			toAppendTo.appendChild(rootDoc.createElement(EXPRESSIONSTRING_TAG, predicateExpressionString));
			
			Element leftSchemaElement = rootDoc.createElement(RELATIONALPREDICATE_LEFT_SCHEMA_TAG);
			toAppendTo.appendChild(leftSchemaElement);
			SchemaHelper.createOutputSchemaStatement(operatorUsingThisPredicate.getSubscribedToSource(0).getSchema(), mimeType, rootDoc, leftSchemaElement);
			int schemataSize = operatorUsingThisPredicate.getSubscribedToSource().size();
			if(schemataSize > 1) {
				Element rightSchemaElement = rootDoc.createElement(RELATIONALPREDICATE_RIGHT_SCHEMA_TAG);
				toAppendTo.appendChild(rightSchemaElement);
				SchemaHelper.createOutputSchemaStatement(operatorUsingThisPredicate.getSubscribedToSource(1).getSchema(), mimeType, rootDoc, rightSchemaElement);
			}
		}
		return rootDoc;
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
			SDFSchema leftSchema = null;
			SDFSchema rightSchema = null;
			for(TextElement e : details) {
				if(e.getName().equals(EXPRESSIONSTRING_TAG)) {
					SDFExpression exp = new SDFExpression(e.getTextValue(), MEP.getInstance());
					pred = new RelationalPredicate(exp);
				} else if(e.getName().equals(RELATIONALPREDICATE_LEFT_SCHEMA_TAG)) {
					leftSchema = SchemaHelper.createSchemaFromStatement(e);
				} else if(e.getName().equals(RELATIONALPREDICATE_RIGHT_SCHEMA_TAG)) {
					rightSchema = SchemaHelper.createSchemaFromStatement(e);
				}
			}
			if(rightSchema != null) {
				((RelationalPredicate)pred).init(leftSchema, rightSchema, true);
			} else {
				((RelationalPredicate)pred).init(leftSchema, rightSchema, false);
			}
			
		}

		return pred;
	}
}
