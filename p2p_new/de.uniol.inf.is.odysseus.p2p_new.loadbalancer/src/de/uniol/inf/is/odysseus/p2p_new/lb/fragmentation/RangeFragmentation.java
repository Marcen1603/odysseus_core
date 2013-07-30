package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * The class for a range data fragmentation strategy. <br />
 * {@link #insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)} will insert {@link RouteAO}s and
 * {@link #createOperatorForJunction() will create an {@link UnionAO}.
 * @author Michael Brand
 */
public class RangeFragmentation extends AbstractDataFragmentation {

	/**
	 * @see #getName()
	 */
	public static final String NAME = "range";

	@Override
	public Class<? extends ILogicalOperator> getOperatorForDistributionClass() {
		
		return RouteAO.class;
		
	}

	@Override
	public ILogicalOperator createOperatorForJunction() {
		
		return new UnionAO();
		
	}

	@Override
	public Class<? extends ILogicalOperator> getOperatorForJunctionClass() {
		
		return UnionAO.class;
		
	}

	@Override
	public String getName() {
		
		return RangeFragmentation.NAME;
		
	}

	@Override
	protected ILogicalOperator createOperatorForDistribution(
			int degreeOfParallelism, QueryBuildConfiguration parameters) {
		
		RouteAO routeAO = new RouteAO();
		routeAO.setPredicates(this.getPredicates(parameters, degreeOfParallelism));
		routeAO.setOverlappingPredicates(true);
		return routeAO;
		
	}
	
	/**
	 * Reads out the predicates, which define the ranges, from the {@link QueryBuildConfiguration}. <br />
	 * Example: #DATADISTRIBUTIONTYPE range bid 'price < 200' 'price >= 200' will return {price < 200,price >= 200}
	 * @param parameters  The set of all used settings.
	 * @param degreeOfParallelism The degree of parallelism is also the number of fragments.
	 * @return A list of the predicates, which define the ranges.
	 */
	private List<IPredicate<?>> getPredicates(QueryBuildConfiguration parameters, int degreeOfParallelism) {
		
		List<IPredicate<?>> predicates = Lists.newArrayList();
		
		if(!parameters.contains(ParameterFragmentationType.class)) 
			return predicates;
		
		String[] strFragStrats = parameters.get(ParameterFragmentationType.class).getValue().split(ParameterFragmentationType.OUTER_SEP);
		for(String strFragStrat : strFragStrats) {
			
			String[] strParameters = strFragStrat.split("'");
			
			// strategy name and source name
			String[] preamble = strParameters[0].trim().split(ParameterFragmentationType.INNER_SEP);			
			String strategyName = preamble[0];
			
			if(!strategyName.equals(RangeFragmentation.NAME))
				continue;
			if(strParameters.length < 2)
				throw new IllegalArgumentException("No ranges defined!");
			
			List<String> ranges = Lists.newArrayList();
			for(int index = 1; index < strParameters.length; index++) {
				
				String range = strParameters[index].trim();
				if(range.isEmpty()) {
					
					// blank between two ranges
					continue;
					
				} else ranges.add(range);
				
			}
				
			if(ranges.size() < degreeOfParallelism - 1 || ranges.size() > degreeOfParallelism) {
				
				// ranges.size() == degreeOfParallelism: alle ranges angegeben
				// ranges.size() == degreeOfParallelism - 1: eine range für den Ausgang, an den alle
				// Tupel kommen, die keines der Prädikate erfüllen, übrig
				
				throw new IllegalArgumentException("Number of ranges (" + (strParameters.length - 2) + 
						") does not fit degree of parallelism (" + degreeOfParallelism + ")!");
				
			}
			
			for(String range : ranges)
				predicates.add(new RelationalPredicate(new SDFExpression(range, MEP.getInstance())));
			
		}
		
		return predicates;
		
	}

}