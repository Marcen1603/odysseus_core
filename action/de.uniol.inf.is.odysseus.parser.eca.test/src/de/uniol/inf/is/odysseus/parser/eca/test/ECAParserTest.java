package de.uniol.inf.is.odysseus.parser.eca.test;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.parser.eca.ECAParser;

public class ECAParserTest implements CommandProvider {
	private IQueryParser parser;

	public void bindParser (IQueryParser parser){
		if (parser.getClass() == ECAParser.class){
			this.parser = parser;
		}
		
	}
	
	public void _testECA(CommandInterpreter ci){
		ci.println(this.parser);
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test \ntestECA - runs eca test cases\n";
	}


}
