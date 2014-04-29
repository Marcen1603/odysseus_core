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
		sb.append("---Costmodel commands---\n");
		sb.append("    histogram <attributeName>         - Prints the sampled histogram of the given attribute.\n");
		sb.append("    listHistrogramAttributes <filter> - Prints a list of available attribute names for histograms.\n");
		return sb.toString();
	}

	public void _histogram(CommandInterpreter ci ) {
		String attributeName = ci.nextArgument();
		if( Strings.isNullOrEmpty(attributeName)) {
			System.out.println("usage: histogram <attributeName>");
			return;
		}
		
		Optional<IHistogram> optHistogram = costModelKnowledge.getHistogram(attributeName);
		if( optHistogram.isPresent() ) {
			System.out.println(optHistogram.get());
		} else {
			System.out.println("Could not find histogram for attribute '" + attributeName + "'");
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
		
		System.out.println("Following attributes are currently sampled for histograms");
		sortAndPrintList(output);
	}
	
	public void _lsHistogramAttributes( CommandInterpreter ci ) {
		_listHistogramAttributes(ci);
	}
	
	private static void sortAndPrintList(List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				System.out.println("\t" + line);
			}
		}
	}

}
