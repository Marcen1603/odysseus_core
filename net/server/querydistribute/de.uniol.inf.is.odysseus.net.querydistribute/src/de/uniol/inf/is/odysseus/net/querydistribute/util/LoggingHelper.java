package de.uniol.inf.is.odysseus.net.querydistribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.querydistribute.parameter.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public final class LoggingHelper {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingHelper.class);
	
	private LoggingHelper() {
		
	}
	
	public static void printUsedInterfaces(List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors, List<InterfaceParametersPair<IQueryPartitioner>> partitioners, List<InterfaceParametersPair<IQueryPartModificator>> modificators,
			List<InterfaceParametersPair<IQueryPartAllocator>> allocators, List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors) {
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Using preprocessors: {}", LoggingHelper.interfaceListToString(preProcessors));
			LOG.debug("Using (first) partitioner: {}", LoggingHelper.interfaceListToString(partitioners));
			LOG.debug("Using modificators: {}", LoggingHelper.interfaceListToString(modificators));
			LOG.debug("Using (first) allocators: {}", LoggingHelper.interfaceListToString(allocators));
			LOG.debug("Using postprocessors: {}", LoggingHelper.interfaceListToString(postProcessors));
		}
	}
	
	private static <T extends INamedInterface> String interfaceListToString(List<InterfaceParametersPair<T>> interfaceMap) {
		if( interfaceMap.isEmpty() ) {
			return "{}";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( InterfaceParametersPair<T> t : interfaceMap) {
			sb.append(t.getInterface().getName());
			
			List<String> params = t.getParameters();
			sb.append("[");
			for( String param : params ) {
				sb.append(param).append(", ");
			}
			sb.append("], ");
		}
		sb.append("}");
		return sb.toString();
	}

	public static void printQueryParts(Collection<ILogicalQueryPart> modifiedQueryParts) {
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Got {} query parts: ", modifiedQueryParts.size());
			for (ILogicalQueryPart part : modifiedQueryParts) {
				LOG.debug("QueryPart: {}", part);
			}
		}
	}

	public static void printAllocationMap(Map<ILogicalQueryPart, IOdysseusNode> allocationMap) {
		if( LOG.isDebugEnabled() ) {
			for (ILogicalQueryPart part : allocationMap.keySet()) {
				IOdysseusNode allocatedNode = allocationMap.get(part);
				LOG.debug("Allocated query part {} --> {}", part, allocatedNode);
			}
		}
	}
}
