package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;


public class IQLQualifiedNameConverter implements IQualifiedNameConverter {
	public static final String DELIMITER = "::";
	
	
	@Override
	public String toString(QualifiedName name) {
		StringBuilder b = new StringBuilder();
		int i = 0;
		for (String segement : name.getSegments()) {
			if (i > 0) {
				b.append(DELIMITER);
			}
			i++;
			b.append(segement);
			
		}
		return b.toString();
	}


	@Override
	public QualifiedName toQualifiedName(String qualifiedNameAsText) {
		if (qualifiedNameAsText.contains(DELIMITER)) {
			String[] segments = qualifiedNameAsText.split(DELIMITER);
			return QualifiedName.create(segments);
		} else {
			String[] segments = qualifiedNameAsText.split("\\.");
			return QualifiedName.create(segments);
		}
	}

	public String toJavaString(String text) {
		return text.replace(DELIMITER, ".").replace(DELIMITER, "$");
	}


	public String toIQLString(String text) {
		return text.replace(".", DELIMITER).replace("$", DELIMITER);
	}

}
