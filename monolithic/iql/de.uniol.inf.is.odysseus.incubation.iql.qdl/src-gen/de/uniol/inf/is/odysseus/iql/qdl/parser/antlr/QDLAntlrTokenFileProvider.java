/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class QDLAntlrTokenFileProvider implements IAntlrTokenFileProvider {

	@Override
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream("de/uniol/inf/is/odysseus/iql/qdl/parser/antlr/internal/InternalQDL.tokens");
	}
}
