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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.ServiceLevel;
import de.uniol.inf.is.odysseus.latency.physicaloperator.CalcLatencyPO;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationEvent;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.test.GenQueries;

/**
 * abstract sla conformance super class. extends {@link AbstractSink} so the sla
 * conformance object could be added to a plan as a physical operator to measure
 * data inside the physical operator plan.
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public abstract class AbstractSLaConformance<T extends IStreamObject<?>>
		extends AbstractSink<T> implements ISLAConformance {
	/**
	 * factor between nanoseconds and milliseconds
	 */
	private static final double NANO_TO_MILLI = 1000000.0;
	/**
	 * reference to the object, that distributes generated
	 * {@link SLAViolationEvent} to event listeners
	 */
	private ISLAViolationEventDistributor distributor;
	/**
	 * reference to the related sla
	 */
	private SLA sla;
	/**
	 * reference to the related query, required for violationEvent generation
	 */
	private IPhysicalQuery query;
	/**
	 * timestamp marking the end of the evaluation window of the sla conformance
	 * in milliseconds
	 */
	private long windowEnd;
	/**
	 * true iff the window has run in the current evaluation window
	 */
	private boolean hasRunInWindow;
	/**
	 * value of the longest latency defined in the sla
	 */
	private double maxLatency;

	private List<IBuffer<?>> buffers;

	private int numberOfPredictedElements;
	/**
	 * map containing all operator paths starting from a buffer
	 */
	private Map<IBuffer<?>, List<List<IPhysicalOperator>>> pathMap;
	/**
	 * mapping buffers to the maximum operating time for outgoing operator paths
	 */
	private Map<IBuffer<?>, Double> maxPathTimeMap;
	/**
	 * update interval for path time map in millis
	 */
	private int pathTimeUpdateInterval;
	private long lastUpdate;

	/**
	 * default constructor
	 * 
	 * @param dist
	 *            event distributor, that sends generated
	 *            {@link SLAViolationEvent} to the listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public AbstractSLaConformance(ISLAViolationEventDistributor dist, SLA sla,
			IPhysicalQuery query) {
		this.distributor = dist;
		this.sla = sla;
		this.query = query;
		this.windowEnd = System.nanoTime()
				+ this.getSLA().getWindow().lengthToNanoseconds();
		this.hasRunInWindow = false;
		this.maxLatency = sla.getMetric().getValue();
		this.buffers = new ArrayList<IBuffer<?>>();
		this.maxPathTimeMap = new HashMap<IBuffer<?>, Double>();
		this.pathMap = new HashMap<>();
		this.pathTimeUpdateInterval = Integer.parseInt(OdysseusConfiguration.instance
				.get("sla_pathTimeUpdateInterval"));
	}

	/**
	 * copy constructor, required for clone method
	 * 
	 * @param conformance
	 */
	protected AbstractSLaConformance(AbstractSLaConformance<T> conformance) {
		this.distributor = conformance.distributor;
		this.sla = conformance.sla;
		this.query = conformance.query;
		this.windowEnd = conformance.windowEnd;
		this.buffers = conformance.buffers;
	}

	/**
	 * @return the event distributor
	 */
	protected ISLAViolationEventDistributor getDistributor() {
		return this.distributor;
	}

	/**
	 * @return the related sla
	 */
	protected SLA getSLA() {
		return this.sla;
	}

	protected IPhysicalQuery getQuery() {
		return query;
	}

	/**
	 * @return the end timestamp of the evaluation window
	 */
	protected long getWindowEnd() {
		return this.windowEnd;
	}

	/**
	 * creates a SLAViolationEvent and puts it to the event queue of the
	 * scheduler
	 * 
	 * @param cost
	 *            the cost caused by violating certain service levels
	 */
	private void violation(double cost, int serviceLevel, double conformance) {
		SLAViolationEvent event = new SLAViolationEvent(this.query, cost,
				serviceLevel, conformance, this.getSLA().getMetric());
		this.distributor.queueSLAViolationEvent(event);
	}

	/**
	 * checks if a window has reached its end. If a violation is detected an
	 * event will be fired and queued in the event queue of the scheduler. if a
	 * window's end has been reached a new window will be set and all results of
	 * conformance are reset.
	 */
	@Override
	public void checkViolation() {
		/*
		 * most valuable service level is first list entry! so iterate reverse
		 * over list to find the less valuable violated service level first
		 */
		if (System.nanoTime() >= this.windowEnd) {
			boolean violated = false;
			double conformance = this.getConformance();
			System.err.println(conformance);
			List<ServiceLevel> serviceLevels = this.getSLA().getServiceLevel();
			for (int i = serviceLevels.size() - 1; i >= 0 && !violated; i--) {
				if (this.getSLA().getScope().thresholdIsMin()) {
					if (!this.hasRunInWindow
							&& (this.windowEnd - this
									.getTimestampOfOldestBufferedElement()) > this.maxLatency) {
						conformance = 0.0;
						System.err.println("manual change of conformance: "
								+ conformance);
						System.err.println("ts element: "
								+ this.getTimestampOfOldestBufferedElement()
								+ " / window end: " + this.windowEnd);
						System.err
								.println("diff: "
										+ (this.windowEnd - this
												.getTimestampOfOldestBufferedElement()));
						System.err.println("has run in window"
								+ this.hasRunInWindow);
					} else {
						// System.err.println("no manual change");
						// System.err.println(this.hasRunInWindow);
						// System.err.println(this.windowEnd -
						// this.getTimestampOfOldestBufferedElement());
					}
					if (serviceLevels.get(i).getThreshold() > conformance) {
						this.violation(serviceLevels.get(i).getPenalty()
								.getCost(), i + 1, conformance);
						violated = true;
					}
				} else {
					if (!this.hasRunInWindow
							&& (this.windowEnd - this
									.getTimestampOfOldestBufferedElement()) > this.maxLatency) {
						conformance = Double.MAX_VALUE;
						System.err.println("manual change of conformance: "
								+ conformance);
						System.err.println("ts element: "
								+ this.getTimestampOfOldestBufferedElement()
								+ " / window end: " + this.windowEnd);
						System.err
								.println("diff: "
										+ (this.windowEnd - this
												.getTimestampOfOldestBufferedElement()));
						System.err.println("has run in window"
								+ this.hasRunInWindow);
					}
					if (serviceLevels.get(i).getThreshold() < conformance) {
						this.violation(serviceLevels.get(i).getPenalty()
								.getCost(), i + 1, conformance);
						violated = true;
					}
				}
			}
			if (!violated)
				this.violation(0.0, 0, conformance);
			this.windowEnd += this.getSLA().getWindow().lengthToNanoseconds();
			this.reset();
		}
	}

	protected double nanoToMilli(double value) {
		return value / NANO_TO_MILLI;
	}

	@Override
	public void reset() {
		this.hasRunInWindow = false;
	}

	/**
	 * @return time stamp of oldest buffered element in nanoseconds
	 */
	private long getTimestampOfOldestBufferedElement() {
		long timestamp = System.nanoTime();
		for (IPhysicalOperator po : query.getAllOperators()) {
			if (po instanceof IBuffer<?>) {
				IBuffer<?> buffer = (IBuffer<?>) po;
				IMetaAttribute metadata = buffer.peekMetadata();
				if (metadata != null) {
					if (metadata instanceof ILatency) {
						ILatency latency = (ILatency) metadata;
						long ts = latency.getLatencyStart();
						if (ts < timestamp) {
							timestamp = ts;
						}
					} else {
						throw new RuntimeException("Latency missing");
					}
				}
			}
		}
		return timestamp;
	}

	@Override
	protected void process_next(T object, int port) {
		this.hasRunInWindow = true;
	}

	@Override
	public void setBuffers(List<IBuffer<?>> buffers) {
		this.buffers = buffers;
	}

	protected List<IBuffer<?>> getBuffers() {
		return this.buffers;
	}

	/**
	 * in nanos
	 */
	@Override
	public double getMaxPredictedLatency() {
		this.numberOfPredictedElements = 0;
		double max = 0.0;
		long timestamp = System.nanoTime();
		for (IBuffer<?> buffer : this.buffers) {
			synchronized (buffer) {
				if (buffer.size() > 0) {
					IMetaAttribute metadata = buffer.peekMetadata();
					if (metadata != null) {
						if (metadata instanceof ILatency) {
							ILatency latency = (ILatency) metadata;
							long waitingTime = timestamp
									- latency.getLatencyStart();
							double predictedLatency = calcLatency(waitingTime,
									buffer);
							if (predictedLatency > max) {
								max = predictedLatency;
							}
							this.numberOfPredictedElements += buffer.size();
						} else {
							throw new RuntimeException("Latency missing");
						}
					}
				}
			}
		}
		return max;
	}

	/**
	 * in nanos
	 */
	@Override
	public double getSumPredictedLatency() {
		this.numberOfPredictedElements = 0;
		double sum = 0.0;
		long timestamp = System.nanoTime();
		for (IBuffer<?> buffer : this.buffers) {
			if (buffer.size() > 0) {
				IMetaAttribute metadata = buffer.peekMetadata();
				if (metadata != null) {
					if (metadata instanceof ILatency) {
						ILatency latency = (ILatency) metadata;
						long waitingTime = timestamp
								- latency.getLatencyStart();
						sum += calcLatency(waitingTime, buffer) * buffer.size();
						this.numberOfPredictedElements += buffer.size();
					} else {
						throw new RuntimeException("Latency missing");
					}
				}
			}
		}
		return sum;
	}

	/**
	 * nanos
	 */
	@Deprecated
	protected double getOpTime() {
		return GenQueries.OP_PROCESSING_TIME;
	}

	protected double getMaxPathTime(IBuffer<?> buffer) {
		Double cachedTime = this.maxPathTimeMap.get(buffer);
		if (cachedTime != null
				&& (System.currentTimeMillis() < lastUpdate
						+ pathTimeUpdateInterval)) {
			return cachedTime.doubleValue();
		} else {
			double maxPathTime = calcMaxPathTime(buffer);
			this.maxPathTimeMap.put(buffer, maxPathTime);
			this.lastUpdate = System.currentTimeMillis();
			return maxPathTime;
		}
	}

	protected double calcMaxPathTime(IBuffer<?> buffer) {
		double maxPathTime = 0.0;

		List<List<IPhysicalOperator>> paths = this.pathMap.get(buffer);
		if (paths != null) {
			for (List<IPhysicalOperator> path : paths) {
				double pathTime = this.getPathTime(path);
				if (pathTime > maxPathTime) {
					maxPathTime = pathTime;
				}
			}
		} else {
			throw new RuntimeException("buffer " + buffer
					+ " not in buffer map");
		}

		return maxPathTime;
	}

	private double getPathTime(List<IPhysicalOperator> path) {
		double time = 0.0;

		for (IPhysicalOperator op : path) {
			if (!(op instanceof ISLAConformance || op instanceof CalcLatencyPO)) {
				time += getMeanCPUTimeMetadataMilli(op);
			}
		}

		return time;
	}

	/**
	 * nanos
	 * 
	 * @param waitingTime
	 * @return
	 */
	protected double calcLatency(double waitingTime, IBuffer<?> buffer) {
		return waitingTime + getMaxPathTime(buffer);
	}

	@Override
	public int getNumberOfPredictedLatency() {
		return numberOfPredictedElements;
	}

	@Override
	public int getNumberOfViolationsPredictedLatency() {
		this.numberOfPredictedElements = 0;
		int numViolations = 0;
		long timestamp = System.nanoTime();
		for (IBuffer<?> buffer : this.buffers) {
			if (buffer.size() > 0) {
				IMetaAttribute metadata = buffer.peekMetadata();
				if (metadata != null) {
					if (metadata instanceof ILatency) {
						ILatency latency = (ILatency) metadata;
						long waitingTime = timestamp
								- latency.getLatencyStart();
						double predictedLatency = calcLatency(waitingTime,
								buffer);
						if (predictedLatency > this.maxLatency) {
							numViolations += buffer.size();
						}
						this.numberOfPredictedElements += buffer.size();
					} else {
						throw new RuntimeException("Latency missing");
					}
				}
			}
		}
		return numViolations;
	}

	@Override
	public void setPathMap(
			Map<IBuffer<?>, List<List<IPhysicalOperator>>> pathMap) {
		this.pathMap = pathMap;
	}

	/**
	 * Liefert das Metadatum "median_processing_time" zum gegebenen physischen
	 * Operator zur��ck (in Sekunden). Falls das Metadatum nichr vorhanden ist,
	 * oder dessen R��ckgabewert NaN oder <code>null</code> ist, wird -1
	 * zur��ckgegeben.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Prozessorzeit zur��ckgegeben
	 *            werden soll.
	 * @return Median der Prozessorzeiten des physischen Operators, oder -1,
	 *         falls das Metadatum nicht existiert oder (noch) ung��ltig ist
	 */

	public static double getMeanCPUTimeMetadataSeconds(
			IPhysicalOperator operator) {
		return getMeanCPUTimeMetadataNano(operator) / 1000000000.0;
	}

	public static double getMeanCPUTimeMetadataNano(IPhysicalOperator operator) {
		double time = -1.0;
		try {
			// measure directly
			IMonitoringData<Double> cpuTime = operator
					.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
			if (cpuTime != null && cpuTime.getValue() != null
					&& !Double.isNaN(cpuTime.getValue())) {
				time = cpuTime.getValue();
			} else {
				System.out.println("no cpuTime found");
			}

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

		return time;
	}

	public static double getMeanCPUTimeMetadataMilli(IPhysicalOperator operator) {
		return getMeanCPUTimeMetadataNano(operator) / 1000000.0;
	}
}
