package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public final class RestructParameterInfoUtil {

	public static void updatePredicateParameterInfo(ILogicalOperator op,
			Map<String, String> parameterInfos, IPredicate<?> newPred) {
		if (newPred == null)
			return;
		String newPredicateString = "'" + newPred.toString() + "'";
		if (parameterInfos.containsKey("PREDICATE")) {
			String predicateString = parameterInfos.get("PREDICATE");
			if (predicateString != null && predicateString.length() > 0) {
				// Cases where predicate type is in front
				int pos = predicateString.indexOf("(");
				if (pos > 0) {
					newPredicateString = predicateString.substring(0, pos + 1)
							+ "'" + newPred.toString() + "')";
				}
			}
		}
		op.addParameterInfo("PREDICATE", newPredicateString);
	}
	
	public static void updateAttributesParameterInfo(ILogicalOperator op,
			Map<String, String> parameterInfos, List<SDFAttribute> newAttrs) {
		if (newAttrs == null)
			return;
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(int i = 0; i < newAttrs.size(); i++) {
			buffer.append("'");
			buffer.append(newAttrs.get(i).toString());
			buffer.append("'");
			if(i < newAttrs.size() - 1) {
				buffer.append(",");
			}
		}
		buffer.append("]");
		String newAttributesString = buffer.toString();
		if (parameterInfos.containsKey("ATTRIBUTES")) {
			String attributesString = parameterInfos.get("ATTRIBUTES");
			if (attributesString != null && attributesString.length() > 0) {
				int pos = attributesString.indexOf("(");
				if (pos > 0) {
					newAttributesString = attributesString.substring(0, pos + 1)
							+ "'" + newAttrs.toString() + "')";
				}
			}
		}
		op.addParameterInfo("ATTRIBUTES", newAttributesString);
	}
	
	public static void updateAliasesParameterInfo(
			Map<String, String> parameterInfos, List<String> newAliases) {
		if (newAliases == null)
			return;
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(int i = 0; i < newAliases.size(); i++) {
			buffer.append("'");
			buffer.append(newAliases.get(i));
			buffer.append("'");
			if(i < newAliases.size() - 1) {
				buffer.append(",");
			}
		}
		buffer.append("]");
		String newAttributesString = buffer.toString();
		if (parameterInfos.containsKey("ALIASES")) {
			String attributesString = parameterInfos.get("ALIASES");
			if (attributesString != null && attributesString.length() > 0) {
				int pos = attributesString.indexOf("(");
				if (pos > 0) {
					newAttributesString = attributesString.substring(0, pos + 1)
							+ "'" + newAliases.toString() + "')";
				}
			}
		}
		parameterInfos.put("ALIASES", newAttributesString);
	}
}
