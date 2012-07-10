/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

/**
 * Hilfsklasse, um einen physischen Operatorplan
 * systematisch von Quelle bis Senke zu durchlaufen.
 * 
 * @author Timo Michelsen
 *
 */
public class GraphWalker {

	private final List<IPhysicalOperator> operators;
	
	/**
	 * Konstruktor. Erstellt eine neue {@link GraphWalker}-Instanz. Ihr wird eine
	 * Liste aller zu besuchender Operatoren mitgegeben.
	 * @param operators
	 */
	public GraphWalker(List<IPhysicalOperator> operators) {
		this.operators = operators;
	}
	
	/**
	 * Durchläuft alle im Konstruktor gegebenen physischen Operatoren.
	 * Zu jedem Operator wird die gegebene {@link IOperatorWalker}-Instanz
	 * aufgerufen.
	 * 
	 * @param visitor Visitor, welcher bei jedem physischen Operator aufgerufen wird
	 */
	@SuppressWarnings("unchecked")
	public void walk( IOperatorWalker visitor ) {
		
		// find sources
		List<ISource<?>> sources = new ArrayList<ISource<?>>();
		for( IPhysicalOperator op : operators ) {
			if( op instanceof ISource && !(op instanceof ISink))
				sources.add((ISource<?>)op);
		}
		
		List<IPhysicalOperator> operatorsToVisit = new ArrayList<IPhysicalOperator>();
		List<IPhysicalOperator> operatorsVisited = new ArrayList<IPhysicalOperator>();
		
		operatorsToVisit.addAll(sources);
		
		while( !operatorsToVisit.isEmpty() ) {
			
			IPhysicalOperator operator = operatorsToVisit.remove( operatorsToVisit.size() - 1 );
			visitor.walk(operator);
			
			operatorsVisited.add(operator);
			
			if( operator instanceof ISource<?>) {
				ISource<?> operatorAsSource = (ISource<?>)operator;
				Collection<?> subscriptions = operatorAsSource.getSubscriptions();
				
				for( Object subscriptionAsObject : subscriptions ) {
					PhysicalSubscription<ISink<?>> subscription = (PhysicalSubscription<ISink<?>>)subscriptionAsObject;
					ISink<?> sink = subscription.getTarget();
					if( !operatorsToVisit.contains(sink) && !operatorsVisited.contains(sink) && operators.contains(sink)) {
						
						// check
						if( sink.getSubscribedToSource().size() > 1 ) {
							
							boolean ok = true;
							for( PhysicalSubscription<?> sourceSubscription : sink.getSubscribedToSource() ) {
								ISource<?> source = (ISource<?>)sourceSubscription.getTarget();
								if( !operatorsVisited.contains(source)) {
									ok = false;
									break;
								}
							}
							
							if( ok )
								operatorsToVisit.add(sink);
							
						} else {
							operatorsToVisit.add(sink);
						}
					}
				}
			}
		}
	}
	
}
