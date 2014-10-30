package de.uniol.inf.is.odysseus.script.parser.console;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.script.parser.IReplacementProvider;
import de.uniol.inf.is.odysseus.script.parser.ReplacementProviderManager;

public class OdysseusScriptConsole implements CommandProvider {

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---OdysseusScript commands---\n");
		sb.append("\tlsReplacements			- Lists all replacement keys currently available from providers\n");
		
		return sb.toString();
	}

	public void _lsReplacements(CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		Map<String, IReplacementProvider> keyMap = ReplacementProviderManager.generateProviderMap();
		
		List<String> output = createKeyMappingOutput(keyMap, filter);
		sortAndPrintList(ci, output);
	}

	private static List<String> createKeyMappingOutput(Map<String, IReplacementProvider> keyMap, String filter) {
		List<String> output = Lists.newArrayList();
		for( String key : keyMap.keySet() ) {
			String retValue = keyMap.get(key).getReplacementValue(key);
			
			String value = retValue != null ? retValue : "<null>";
			String textLine = key.toUpperCase() + " --> " + value;
			
			if( !isFiltered(filter, textLine)) {
				output.add( textLine);
			}
		}
		
		return output;
	}

	private static boolean isFiltered(String filter, String text) {
		return !(Strings.isNullOrEmpty(filter) || text.contains(filter));
	}
	
	private static void sortAndPrintList(CommandInterpreter ci, List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				ci.println("\t" + line);
			}
		}
	}
}
