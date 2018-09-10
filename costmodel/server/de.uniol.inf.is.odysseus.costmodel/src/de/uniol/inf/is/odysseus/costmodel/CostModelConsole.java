package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class CostModelConsole implements CommandProvider {

	private static ICostModelKnowledge costModelKnowledge;

	// called by OSGi-DS
	public static void bindCostModelKnowledge(ICostModelKnowledge serv) {
		costModelKnowledge = serv;
	}

	// called by OSGi-DS
	public static void unbindCostModelKnowledge(ICostModelKnowledge serv) {
		if (costModelKnowledge == serv) {
			costModelKnowledge = null;
		}
	}
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Costmodel commands ---\n");
		sb.append("    histogram <attributeName>         - Prints the sampled histogram of the given attribute.\n");
		sb.append("    listHistrogramAttributes <filter> - Prints a list of available attribute names for histograms.\n");
		sb.append("    listDatarates                     - Prints a list of all stored datarate measures.\n");
		sb.append("    listCpuTimes                      - Prints a list of all stored cpuTime measures.\n");
		return sb.toString();
	}

	public void _histogram(CommandInterpreter ci ) {
		String attributeName = ci.nextArgument();
		if( Strings.isNullOrEmpty(attributeName)) {
			ci.println("usage: histogram <attributeName>");
			return;
		}
		
		Optional<IHistogram> optHistogram = costModelKnowledge.getHistogram(attributeName);
		if( optHistogram.isPresent() ) {
			ci.println(optHistogram.get());
		} else {
			ci.println("Could not find histogram for attribute '" + attributeName + "'");
		}
	}
	
	public void _listHistogramAttributes( CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		Collection<SDFAttribute> attributes = costModelKnowledge.getHistogramAttributes();
		List<String> output = Lists.newArrayList();
		for( SDFAttribute attribute : attributes ) {
			String attributeName = attribute.getSourceName() + "." + attribute.getAttributeName();
			if( Strings.isNullOrEmpty(filter) || attributeName.contains(filter)) {
				output.add(attributeName);
			}
		}
		
		ci.println("Following attributes are currently sampled for histograms");
		sortAndPrintList(ci, output);
	}
	
	public void _lsHistogramAttributes( CommandInterpreter ci ) {
		_listHistogramAttributes(ci);
	}
	
	private static void sortAndPrintList(CommandInterpreter ci, List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				ci.println("\t" + line);
			}
		}
	}

	public void _listCpuTimes( CommandInterpreter ci ) {
		Collection<String> cpuTimeOperatorClasses = costModelKnowledge.getCpuTimeOperatorClasses();
		List<String> output = Lists.newArrayList();
		
		for( String clazz : cpuTimeOperatorClasses ) {
			output.add(clazz + ": " + format(costModelKnowledge.getCpuTime(clazz).get() / 1000000000));
		}
		
		sortAndPrintList(ci, output);
	}
	
	public void _lsCpuTimes(CommandInterpreter ci ) {
		_listCpuTimes(ci);
	}
	
	public void _listDatarates( CommandInterpreter ci ) {
		Collection<String> sourceNames = costModelKnowledge.getDatarateSourceNames();
		List<String> output = Lists.newArrayList();
		
		for( String sourceName : sourceNames ) {
			output.add(sourceName + ": " + format(costModelKnowledge.getDatarate(sourceName).get()));
		}
		
		sortAndPrintList(ci, output);
	}
	
	public void _lsDatarates( CommandInterpreter ci ) {
		_listDatarates(ci);
	}
	
	private static String format(Object text ) {
		return String.format("%-6.5f", text);
	}
}
