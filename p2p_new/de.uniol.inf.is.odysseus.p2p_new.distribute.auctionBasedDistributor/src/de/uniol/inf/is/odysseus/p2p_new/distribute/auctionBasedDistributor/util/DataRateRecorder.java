package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelPlugin;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;

public class DataRateRecorder implements IPlanModificationListener {
	private static final Logger log = LoggerFactory
			.getLogger(DataRateRecorder.class);
	private final List<RecordEntry> entries;
	private final ExecutorService executorService;
	private final IServerExecutor executor;
	private final Map<IPhysicalQuery, RecordEntry> entryMap;
	private boolean shutdown;
	
	private long startTime;
	private long duration;

	public DataRateRecorder(IServerExecutor executor, long duration) {
		entryMap = Maps.newHashMap();
		this.executor = executor;
		this.executor.addPlanModificationListener(this);
		entries = Lists.newArrayList();
		executorService = Executors.newSingleThreadExecutor();
		this.duration=duration;
		init();
	}

	private void init() {
		shutdown = false;
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				startTime = System.currentTimeMillis();
				while (!shutdown) {					
					List<RecordEntry> copiedEntries = Lists.newArrayList();
					synchronized (entries) {
						copiedEntries.addAll(Lists.newArrayList(entries));
					}

					final List<RecordEntry> entriesToRemove = Lists
							.newArrayList();
					for (RecordEntry entry : copiedEntries) {
						try {
							if (entry.isQueryStopped()) {
								entry.getWriter().flush();
								entry.getWriter().close();
								if (entry.isQueryRemoved())
									entriesToRemove.add(entry);
								continue;
							}

							double dataRate = EstimatorHelper
									.getDatarateMetadata(entry.getOperator());
							if (dataRate >= 0) {
								log.debug("Current data rate: {}", dataRate);
								entry.getWriter()
										.write((entry.getCountRecords() * OperatorCostModelPlugin.MONITORING_PERIOD_SECONDS)
												+ ";" + dataRate + "\n");
							}
							entry.setCountRecords(entry.getCountRecords()+1);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					synchronized (entries) {
						entries.removeAll(entriesToRemove);
						entriesToRemove.clear();
					}
					
					try {
						Thread.sleep(OperatorCostModelPlugin.MONITORING_PERIOD_MILLIS);
					} catch (InterruptedException e) {
					}
					
					if(duration>0 &&
							System.currentTimeMillis()>=(startTime+duration)) {
						log.info("Time limit reached");						
						shutdown();
					}					
				}

				// shutdown
				synchronized (entries) {
					for (RecordEntry entry : entries) {
						try {
							entry.getWriter().flush();
							entry.getWriter().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				entries.clear();
			}
		});
	}

	public void recordDataRate(IPhysicalOperator physicalPlan) {
		if (shutdown) {
			init();
		}

		IPhysicalQuery physicalQuery = getPhysicalQuery(physicalPlan);
		if (physicalQuery == null) {
			String msg = "Could not record data rate for operators in physical plan "
					+ physicalPlan
					+ " because the corresponding physical query could not be found";
			log.error(msg);
			throw new RuntimeException(msg);
		}

		for (IPhysicalOperator op : physicalQuery.getAllOperators()) {
			String pathToRecordDataRate = op.getParameterInfos().get(
					"recordDataRate".toUpperCase());
			if (pathToRecordDataRate != null) {
				log.debug("Recording data rate for operator {} in file {}", op,
						pathToRecordDataRate);

				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(
							pathToRecordDataRate));
					RecordEntry entry = new RecordEntry(out, op);
					entryMap.put(physicalQuery, entry);
					synchronized (entries) {
						entries.add(entry);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private IPhysicalQuery getPhysicalQuery(IPhysicalOperator physicalPlan) {
		List<IOperatorOwner> owners = physicalPlan.getOwner();
		for (IOperatorOwner o : owners) {
			if (o instanceof IPhysicalQuery) {
				return (IPhysicalQuery) o;
			}
		}
		return null;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void shutdown() {
		log.debug("Shutting down DataRecorder");
		this.shutdown = true;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {
			final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
			if (entryMap.get(query) != null) {
				entryMap.get(query).setQueryStopped(true);
				entryMap.get(query).setQueryRemoved(true);
			}
		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs
				.getEventType())) {
			final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
			if (entryMap.get(query) != null)
				entryMap.get(query).setQueryStopped(true);
		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs
				.getEventType())) {
			final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
			if (entryMap.get(query) != null)
				entryMap.get(query).setQueryStopped(false);
		}
	}

	private static class RecordEntry {
		private final BufferedWriter writer;
		private final IPhysicalOperator operator;
		private boolean queryRemoved = false;
		private boolean queryStopped = false;
		private int countRecords;
		
		public RecordEntry(BufferedWriter writer, IPhysicalOperator operator) {
			super();
			this.writer = writer;
			this.operator = operator;
		}

		public BufferedWriter getWriter() {
			return writer;
		}

		public IPhysicalOperator getOperator() {
			return operator;
		}

		public boolean isQueryRemoved() {
			return queryRemoved;
		}

		public void setQueryRemoved(boolean queryRemoved) {
			this.queryRemoved = queryRemoved;
		}

		public boolean isQueryStopped() {
			return queryStopped;
		}

		public void setQueryStopped(boolean queryStopped) {
			this.queryStopped = queryStopped;
		}

		public int getCountRecords() {
			return countRecords;
		}

		public void setCountRecords(int countRecords) {
			this.countRecords = countRecords;
		}		
	}
}
