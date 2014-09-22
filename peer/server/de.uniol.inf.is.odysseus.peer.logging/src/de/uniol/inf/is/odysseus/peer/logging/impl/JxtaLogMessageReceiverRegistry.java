package de.uniol.inf.is.odysseus.peer.logging.impl;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.logging.IJxtaLogMessageReceiver;

public class JxtaLogMessageReceiverRegistry {

	private static Collection<IJxtaLogMessageReceiver> logMessageReceivers = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindJxtaLogMessageReceiver(IJxtaLogMessageReceiver serv) {
		synchronized (logMessageReceivers) {
			logMessageReceivers.add(serv);
		}
	}

	// called by OSGi-DS
	public static void unbindJxtaLogMessageReceiver(IJxtaLogMessageReceiver serv) {
		synchronized (logMessageReceivers) {
			logMessageReceivers.remove(serv);
		}
	}
	
	public static ImmutableCollection<IJxtaLogMessageReceiver> getReceivers() {
		synchronized( logMessageReceivers ) {
			return ImmutableList.copyOf(logMessageReceivers);
		}
	}
}
