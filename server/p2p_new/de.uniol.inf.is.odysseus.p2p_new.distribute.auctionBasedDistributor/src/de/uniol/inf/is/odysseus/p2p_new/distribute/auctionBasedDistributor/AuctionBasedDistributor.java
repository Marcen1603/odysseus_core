package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.allocator.Allocator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.CostCalculator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.communicator.Communicator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.DataRateRecorder;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.StopWatch;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartController;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class AuctionBasedDistributor implements ILogicalQueryDistributor {

	private static final String DISTRIBUTION_TYPE = "auto";
	private static final Logger log = LoggerFactory
			.getLogger(AuctionBasedDistributor.class);
	private static final boolean FORCE_DISTRIBUTION = true;
	
	private ExecutorService executorService;
	private Partitioner partitioner;
	private Allocator allocator;
	private CostCalculator costCalculator;
	private Communicator communicator;
	private IPQLGenerator generator;
	private DataRateRecorder recorder;

	// called by OSGi-DS
	public final void activate() {
		executorService = Executors.newSingleThreadExecutor();

		log.debug("{} activated (all dependencies statisfied)",
				AuctionBasedDistributor.class.getName());
	}

	// called by OSGi-DS
	public final void deactivate() {
		executorService = null;
		partitioner = null;
		log.debug("{} deactivated", AuctionBasedDistributor.class.getName());
		if(recorder!=null)
			recorder.shutdown();
	}

	public void bindAllocator(Allocator allocator) {
		this.allocator = allocator;
	}

	public void unbindAllocator(Allocator allocator) {
		if (this.allocator == allocator) {
			allocator = null;
		}
	}
	
	public void bindPartitioner(Partitioner partitioner) {
		this.partitioner = partitioner;
	}

	public void unbindPartitioner(Partitioner partitioner) {
		if (this.partitioner == partitioner) {
			allocator = null;
		}
	}	
	
	public void bindCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public void unbindCommunicator(Communicator communicator) {
		if (this.communicator == communicator) {
			communicator = null;
		}
	}	
	
	
	public void bindPQLGenerator(IPQLGenerator generator) {
		this.generator = generator;
	}
	
	public void unbindPQLGenerator(IPQLGenerator generator) {
		if( this.generator == generator ) {
			generator = null;
		}
	}	

	public void bindCostCalculator(CostCalculator costCalculator) {
		this.costCalculator = costCalculator;
	}

	public void unbindCostCalculator(CostCalculator costCalculator) {
		if (this.costCalculator == costCalculator) {
			costCalculator = null;
		}
	}	

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(final IExecutor sender,
			final List<ILogicalQuery> queriesToDistribute,
			final QueryBuildConfiguration transCfg) {
		
		if(recorder==null) {
			//10minutes
			recorder = new DataRateRecorder((IServerExecutor)sender, 600*1000);
		}
		
		if (queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			return queriesToDistribute;
		}

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				for (ILogicalQuery query : queriesToDistribute) {
					try {
						preparePlan(query);
						
						CostSummary relativeCosts = calcRelativeCosts(transCfg,
								query);
						
						if(communicator.getRemotePeerIds().size()>0 &&
								(relativeCosts.getCpuCost() < 1 || relativeCosts.getMemCost() <1 || FORCE_DISTRIBUTION)) {
							log.info("Decided to distribute the query (relative costs: {})", relativeCosts);	
							try {						
								final ID sharedQueryID = communicator.generateSharedQueryId();
								
								// Partitionierung mit DummyOperatoren
								StopWatch sw = StopWatch.start();								
								log.info("Partitioning logical plan...");
								
								List<SubPlan> result = partitioner
										.partitionWithDummyOperators(query.getLogicalPlan(), sharedQueryID, transCfg);
								
								log.info("Logical plan partitioned to {} sub plans in {} ms",
										result.size(), sw.stop().getTimeInMillis());
																
								// Allokation								
								sw = StopWatch.start();
								log.info("Allocating remote subplans...");
								
								ILogicalQuery q = allocator.allocate(query, sharedQueryID, result, transCfg);
								
								log.info("Subplans allocated in {} ms", sw.stop().getTimeInMillis());
								
								// Lokalen plan als Master-Query registrieren
								int id = sender.addQuery(q.getLogicalPlan(),
										UserManagementProvider.getSessionmanagement().loginSuperUser(null, ""),
										transCfg.getName());
		
								if(Helper.containsJxtaOperators(q.getLogicalPlan())) {
									QueryPartController.getInstance().registerAsMaster(q, sharedQueryID);
								}							
								
								List<IPhysicalOperator> roots = sender.getPhysicalRoots(id);
								recorder.recordDataRate(roots.iterator().next());								
							}
							catch (CouldNotPartitionException e) {
								e.printStackTrace();					
							}			
						}
						else {
							log.info("Query can be processed locally. No need to distribute it");	
							Collection<Integer> ids = addQuery(sender, query.getQueryText(), transCfg);		
							
							List<IPhysicalOperator> roots = sender.getPhysicalRoots(ids.iterator().next());
							recorder.recordDataRate(roots.iterator().next());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}	
		});		
		
		return Lists.newLinkedList();
	}
	
	private Collection<Integer> addQuery(IExecutor sender, String qryText,
			QueryBuildConfiguration transCfg) {
		return sender.addQuery(qryText, "PQL",
				UserManagementProvider.getSessionmanagement().loginSuperUser(null, ""),
				transCfg.getName(), null);	
	}
	
	private CostSummary calcRelativeCosts(
			final QueryBuildConfiguration transCfg, ILogicalQuery query) {
		CostSummary costs = costCalculator.calcCostsForPlan(query, transCfg.getName());

		CostSummary relativeCosts = costCalculator.calcBearableCostsInPercentage(costs);
		return relativeCosts;
	}

	private void preparePlan(ILogicalQuery query) {
		Helper.addOperatorIds(query.getLogicalPlan());
		DistributionHelper.replaceStreamAOs(query.getLogicalPlan());
	}
	
	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}
}
