package de.uniol.inf.is.odysseus.costmodel.physical.util;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;

public class CostModelUtil {

	private CostModelUtil() {
		
	}
	
	
	public static Collection<IPhysicalOperator> getAllPhysicalOperators( IPhysicalOperator startOperator ) {
		Preconditions.checkNotNull(startOperator, "StartOperator must not be null!");
		
		Collection<IPhysicalOperator> collectedOperators = Lists.newArrayList();
		
		collectPhysicalOperators( startOperator, collectedOperators );
		
		return collectedOperators;
	}


	private static void collectPhysicalOperators(IPhysicalOperator currentOperator, Collection<IPhysicalOperator> collectedOperators) {
		if( !collectedOperators.contains(currentOperator)) {
			collectedOperators.add(currentOperator);
			
			if( currentOperator instanceof ISink ) {
				ISink<?> opAsSink = (ISink<?>)currentOperator;
				for( AbstractPhysicalSubscription<?,?> physSub : opAsSink.getSubscribedToSource() ) {
					collectPhysicalOperators((IPhysicalOperator)physSub.getSource(), collectedOperators);
				}
			}
			
			if( currentOperator instanceof ISource ) {
				ISource<?> opAsSource = (ISource<?>)currentOperator;
				for( AbstractPhysicalSubscription<?,?> physSub : opAsSource.getSubscriptions() ) {
					collectPhysicalOperators((IPhysicalOperator)physSub.getSink(), collectedOperators);
				}
			}
		}
	}
	
	public static Collection<IPhysicalOperator> filterForPhysicalSources(Collection<IPhysicalOperator> operators) {
		return Collections2.filter(operators, new Predicate<IPhysicalOperator>() {
			@Override
			public boolean apply(IPhysicalOperator input) {
				if( input.isSource() ) {
					if( input.isSink() ) {
						ISink<?> asSink = (ISink<?>)input;
						return asSink.getSubscribedToSource().isEmpty();
					}
					return true;
				}
				
				return false;
			}
		});
	}
}
