package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatedAnnealingQuerySelector implements IQuerySelectionStrategy {

	private static final Logger LOG = LoggerFactory
			.getLogger(SimulatedAnnealingQuerySelector.class);

	private static final int INITIAL_TEMPERATURE = 100;
	private static final double INITITAL_SELECTION_PROBABILITY = 0.3;

	public SimulatedAnnealingQuerySelector() {

	}

	Random rand;

	@Override
	public QueryCostMap selectQueries(QueryCostMap allQueries,
			double minCpuLoad, double minMemLoad, double minNetLoad) {

		rand = new Random();
		rand.setSeed(System.currentTimeMillis());

		QueryCostMap currentSolution = chooseRandom(allQueries);
		QueryCostMap bestSolution = currentSolution.clone();

		int currentTemperature = INITIAL_TEMPERATURE;

		while (currentTemperature > 0) {
			currentTemperature -= 1;
			QueryCostMap neighbor = chooseRandomNeighbor(currentSolution,
					allQueries);
			if (neighbor.getCosts() < currentSolution.getCosts()
					&& isFeasible(neighbor, minCpuLoad, minMemLoad, minNetLoad)) {
				currentSolution = neighbor;
				if (neighbor.getCosts() < bestSolution.getCosts()) {
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
		
		if(!isFeasible(bestSolution,minCpuLoad,minMemLoad,minNetLoad)) {
			LOG.warn("Solution is not feasible. Returning null instead.");
			return null;
		}
		
		return bestSolution;
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

}
