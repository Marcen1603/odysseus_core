package de.uniol.inf.is.odysseus.net.logging;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public final class LoggingDestinations {

	private static final Collection<IOdysseusNode> DESTINATION_NODES = Lists.newArrayList();

	private LoggingDestinations() {

	}

	public static void add(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		synchronized (DESTINATION_NODES) {
			if (!DESTINATION_NODES.contains(node)) {
				DESTINATION_NODES.add(node);

//				System.err.println("Added node as destination for logging messages: " + node);
			}
		}
	}

	public static void remove(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		synchronized (DESTINATION_NODES) {
			if (DESTINATION_NODES.contains(node)) {
				DESTINATION_NODES.remove(node);

//				System.err.println("Removed node as destination for logging messages" + node);
			}
		}
	}

	public static Collection<IOdysseusNode> getDestinations() {
		synchronized (DESTINATION_NODES) {
			return Lists.newArrayList(DESTINATION_NODES);
		}
	}
}
