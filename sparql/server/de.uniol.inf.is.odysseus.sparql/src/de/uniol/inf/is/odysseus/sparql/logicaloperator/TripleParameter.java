package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.rdf.datamodel.INode;
import de.uniol.inf.is.odysseus.rdf.datamodel.INodeFactory;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;

public class TripleParameter extends AbstractParameter<Triple<IMetaAttribute>> {

	private static final long serialVersionUID = -6698511127175246173L;

	@Override
	protected void internalAssignment() {
		if (inputValue instanceof String) {
			String[] parts = ((String) inputValue).split(" ");
			if (parts.length != 3) {
				throwException();
			}
			setValue(parts[0], parts[1], parts[2]);
		} else {
			if (!(inputValue instanceof List)) {
				throwException();
			}
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) inputValue;
			if (list.size() != 3) {
				throwException();
			}
			setValue(String.valueOf(list.get(0)), String.valueOf(list.get(1)), String.valueOf(list.get(2)));
		}

	}

	private void setValue(String strSubject, String strPredicate, String strObject) {
		INode subject = INodeFactory.createNode(strSubject);
		INode predicate = INodeFactory.createNode(strPredicate);
		INode obj = INodeFactory.createNode(strObject);

		Triple<IMetaAttribute> triple = new Triple<>(subject, predicate, obj);
		setValue(triple);
	}

	private void throwException() {
		throw new IllegalArgumentException(
				"Triple parameter format must be \'subject predicate object\' or [\"subject\",\"predicate\",\"object\"]");
	}

	@Override
	protected String getPQLStringInternal() {
		Triple<IMetaAttribute> v = getValue();
		String subject = v.getSubject().toString();
		String predicate = v.getPredicate().toString();
		String obj = v.getObject().toString();
		return "[\"" + subject + "\",\"" + predicate + "\",\"" + obj + "\"]";
	}

}
