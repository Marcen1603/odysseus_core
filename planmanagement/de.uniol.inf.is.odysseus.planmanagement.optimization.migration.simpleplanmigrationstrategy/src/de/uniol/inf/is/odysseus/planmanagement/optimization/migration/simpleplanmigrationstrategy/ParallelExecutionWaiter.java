package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

/**
 * 
 * Helper class, that informs the {@link SimplePlanMigrationStrategy}, when a
 * specified period has ended.
 * 
 * @author Tobias Witt
 * 
 */
class ParallelExecutionWaiter implements Runnable {
	
	private SimplePlanMigrationStrategy sender;
	private StrategyContext context;
	
	public ParallelExecutionWaiter(SimplePlanMigrationStrategy sender, StrategyContext context) {
		this.sender = sender;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			// plus 1 second safety for scheduling
			Thread.sleep(this.context.getwMax().getWindowSize() + 1000L);
		} catch (InterruptedException e) {}
		this.sender.finishedParallelExecution(context);
	}

}
