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
	
	public void _testeca(CommandInterpreter ci){
		try {
			System.out.println("Testcase 1: Checking if compiler has been bound.");
			if (((ECAParser)this.parser).getCompiler() != null) {
				System.out.println("---success---");
			}
			
			System.out.println("Adding sources for next testcases");
			
		}catch (Exception e){
			System.err.print("Test failed: ");
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test--- \n" +
				"	testeca - runs eca test cases\n";
	}


}
