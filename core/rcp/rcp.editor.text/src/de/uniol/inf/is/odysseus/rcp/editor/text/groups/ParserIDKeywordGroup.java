package de.uniol.inf.is.odysseus.rcp.editor.text.groups;

import de.uniol.inf.is.odysseus.rcp.editor.text.IKeywordGroup;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;

public class ParserIDKeywordGroup implements IKeywordGroup {

	@Override
	public String[] getKeywords() {
		try {
			return ExecutorHandler.getExecutor().getSupportedQueryParsers().toArray(new String[0]);
		} catch( Exception ex ) {
			return new String[] { // Notlösung
				"CQL", "PQL", "PQLHack"	
			};
		}
	}

}
