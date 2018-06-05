package de.uniol.inf.is.odysseus.iql.basic.typing;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;

public class OperatorsObservable {

	private static Collection<Listener> listeners = new HashSet<>();
	
	public interface Listener {
		void onNewOperator(IOperatorBuilder operatorBuilder);
	}
	
	public static void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(Listener listener) {
		listeners.add(listener);
	}
	
	public static void newOperatorBuilder(IOperatorBuilder operatorBuilder) {
		for (Listener listener : listeners) {
			listener.onNewOperator(operatorBuilder);
		}
	}
	
}
