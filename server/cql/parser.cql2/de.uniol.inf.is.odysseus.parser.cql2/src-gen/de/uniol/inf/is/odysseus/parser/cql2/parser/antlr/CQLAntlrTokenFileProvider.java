/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class CQLAntlrTokenFileProvider implements IAntlrTokenFileProvider {

	@Override
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream("de/uniol/inf/is/odysseus/parser/cql2/parser/antlr/internal/InternalCQLParser.tokens");
	}
}
