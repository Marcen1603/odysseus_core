package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

@Deprecated
public class IncludePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "INCLUDE";
	
	private static List<String> includingFiles = Lists.newArrayList();

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		File includingFile = new File(parameter);
		if( !includingFile.exists() ) {
			throw new OdysseusScriptException("File for including '" + parameter + "' does not exist!");
		}
		
		if( includingFiles.contains(parameter)) {
			throw new OdysseusScriptException("Infinite including detected with file '" + parameter + "'");
		}
		
		try {
			includingFiles.add(parameter);
			
			String[] lines = readTextLinesFromFile(includingFile);
			getParser().validate(lines, caller, context, executor);
			
		} catch( Exception ex ) {
			throw new OdysseusScriptException("Could not read including file '" + parameter + "' for validating", ex );
		} finally {
			includingFiles.remove(parameter);
		}
	}

	private static String[] readTextLinesFromFile(File includingFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(includingFile));
		List<String> lines = Lists.newArrayList();
		
	    try {
	        String line = br.readLine();

	        while (line != null) {
	        	lines.add(line);
	            line = br.readLine();
	        }
	        
	        return lines.toArray(new String[lines.size()]);
	    } finally {
	        br.close();
	    }
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		try {
			String[] lines = readTextLinesFromFile(new File(parameter));
			
			ISink<?> defaultSink = variables.containsKey("_defaultSink") ? (ISink<?>) variables.get("_defaultSink") : null;
			
			List<?> ret = getParser().parseAndExecute(lines, caller, defaultSink, context, executor);
			List<IExecutorCommand> commands = new ArrayList<IExecutorCommand>();
			for (Object o:ret){
				if (o instanceof IExecutorCommand){
					commands.add((IExecutorCommand)o);
				}
			}
			return commands;
		} catch (IOException ex) {
			throw new OdysseusScriptException("Could not read including file '" + parameter + "' for execution", ex );
		}		
	}
}
