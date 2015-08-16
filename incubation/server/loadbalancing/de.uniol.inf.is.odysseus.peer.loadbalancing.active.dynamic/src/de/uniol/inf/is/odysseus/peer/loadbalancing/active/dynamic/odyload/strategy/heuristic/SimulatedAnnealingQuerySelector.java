package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQuerySelectionStrategy;

public class SimulatedAnnealingQuerySelector implements IQuerySelectionStrategy {

	private static final Logger LOG = LoggerFactory
			.getLogger(SimulatedAnnealingQuerySelector.class);

	private static final int INITIAL_TEMPERATURE = 1000;
	private static final double INITITAL_SELECTION_PROBABILITY = 0.3;
	
	private QueryCostMap previousResult;

	public SimulatedAnnealingQuerySelector() {

	}
	

	public SimulatedAnnealingQuerySelector(QueryCostMap previousResult) {
		this.previousResult = previousResult.clone();
	}

	Random rand;

	@Override
	public QueryCostMap selectQueries(QueryCostMap allQueries,
			double minCpuLoad, double minMemLoad, double minNetLoad) {

		rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		QueryCostMap currentSolution;
		if(previousResult==null) {
			currentSolution = chooseRandom(allQueries);
		}
		else {
			currentSolution = previousResult;
		}
		QueryCostMap bestSolution = currentSolution.clone();

		int currentTemperature = INITIAL_TEMPERATURE;

		while (currentTemperature > 0) {
			currentTemperature -= 1;
			QueryCostMap neighbor = chooseRandomNeighbor(currentSolution,
					allQueries);
			if (isBetterSolution(minCpuLoad, minMemLoad, minNetLoad,
					currentSolution, neighbor)) {
				currentSolution = neighbor;
				if (isBetterSolution(minCpuLoad,minMemLoad,minNetLoad,bestSolution,neighbor)) {
					bestSolution = neighbor.clone();
				}
			} else {
				double probability = 
						Math.exp(
								-(neighbor.getCosts() - currentSolution.getCosts())
								/ currentTemperature
								);
				double random = rand.nextDouble();
				if (random <= probability) {
					currentSolution = neighbor;
				}
			}

		}

		LOG.info("Finished suimulated annealing, best found proposal is:");
		LOG.info(bestSolution.toString());
				
		return bestSolution;
	}


	private boolean isBetterSolution(double minCpuLoad, double minMemLoad,
			double minNetLoad, QueryCostMap currentSolution,
			QueryCostMap neighbor) {
		//If we already have a working solution -> test if new solution is better AND is working.
		if(isFeasible(currentSolution,minCpuLoad,minMemLoad, minNetLoad)) {
			if(neighbor.getCosts() < currentSolution.getCosts() && isFeasible(neighbor, minCpuLoad, minMemLoad, minNetLoad)) {
				return true;
			}
			else {
				return false;
			}
		}
		//Current solution is not solving the Problem at all. If new solution is dominating current Solution (=> better in solving the problem) in Load Values prefer it over the other solution.
		//There is no use in having a "cheap" solution that does not work. So we prefer an expensive Solution, that solves the Prolem better...
		else {
			
			boolean cpuNeighborDominatingCurrent = solvesProblemBetterOrEqual(neighbor.getTotalCpuLoad(), currentSolution.getTotalCpuLoad(), minCpuLoad);
			boolean memNeighborDominatingCurrent = solvesProblemBetterOrEqual(neighbor.getTotalMemLoad(), currentSolution.getTotalMemLoad(), minMemLoad);
			boolean netNeighborDominatingCurrent = solvesProblemBetterOrEqual(neighbor.getTotalNetLoad(), currentSolution.getTotalNetLoad(), minNetLoad);
			
			if(cpuNeighborDominatingCurrent && memNeighborDominatingCurrent & netNeighborDominatingCurrent) {
				return true;
			}
			else {
				return false;
			}
		}
		
		
	
	}

	private QueryCostMap chooseRandom(QueryCostMap allQueries) {
		QueryCostMap solution = new QueryCostMap();
		for (Integer query : allQueries.getQueryIds()) {
			double rnd = rand.nextDouble();
			if (rnd <= INITITAL_SELECTION_PROBABILITY) {
				solution.add(allQueries.getQueryInformation(query).clone());
			}
		}
		return solution;
	}

	private QueryCostMap chooseRandomNeighbor(QueryCostMap current,
			QueryCostMap allQueries) {
		QueryCostMap neighbor = current.clone();
		List<Integer> queryIds = allQueries.getQueryIds();
		int randomNum = rand.nextInt(queryIds.size());
		int randomlyChoosenQueryId = queryIds.get(randomNum);

		if (neighbor.containsQuery(randomlyChoosenQueryId)) {
			neighbor.remove(randomlyChoosenQueryId);
		} else {
			neighbor.add(allQueries.getQueryInformation(randomlyChoosenQueryId)
					.clone());
		}
		return neighbor;
	}

	private boolean isFeasible(QueryCostMap proposal, double minCpuSum,
			double minMemSum, double minNetSum) {
		return (proposal.getTotalCpuLoad() >= minCpuSum 
				&& proposal.getTotalMemLoad() >= minMemSum
				&& proposal.getTotalNetLoad() >= minNetSum);
	}
	

	private boolean solvesProblemBetterOrEqual(double firstLoadValue, double secondLoadValue, double loadMinimum) {
		return firstLoadValue>=Math.min(secondLoadValue, loadMinimum);
	}

}
