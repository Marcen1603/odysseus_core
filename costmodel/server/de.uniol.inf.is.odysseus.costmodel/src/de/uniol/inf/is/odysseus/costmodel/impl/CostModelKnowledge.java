package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.NoSampling;
import de.uniol.inf.is.odysseus.costmodel.impl.histogram.EqualWidthHistogram;
import de.uniol.inf.is.odysseus.costmodel.impl.interval.FreedmanDiaconisRule;

public class CostModelKnowledge implements ICostModelKnowledge,
		IPlanModificationListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(CostModelKnowledge.class);

	private static IServerExecutor executor;

	private final SamplingContainer samplingContainer = new SamplingContainer();
	private final DatarateContainer datarateContainer = new DatarateContainer();
	private final CpuTimeContainer cpuTimeContainer = new CpuTimeContainer();
	private final DataSourceManager dataSourceManager = new DataSourceManager(
			samplingContainer, datarateContainer);

	// called by OSGi-DS
	public void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;

		executor.addPlanModificationListener(this);
		cpuTimeContainer.setExecutor(executor);
	}

	// called by OSGi-DS
	public void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor.removePlanModificationListener(this);
			cpuTimeContainer.unsetExecutor();

			executor = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("CostModelKnowledge activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		saveAllSamplesIfNeeded();
		datarateContainer.save();
		cpuTimeContainer.save();

		LOG.debug("CostModelKnowledge deactivated");
	}

	private void saveAllSamplesIfNeeded() {
		Collection<SDFAttribute> sampledAttributes = samplingContainer
				.getSampledAttributes();
		if (sampledAttributes.isEmpty()) {
			LOG.debug("Saving sampled data...");
			for (SDFAttribute attribute : sampledAttributes) {
				samplingContainer.removeSampler(attribute);
			}
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs
				.getEventType())) {
			processQueryAddEvent(query);
		} else if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {
			processQueryRemoveEvent(query);
		}
	}

	private void processQueryAddEvent(IPhysicalQuery query) {
		LOG.debug("Query added.");
		Collection<ISource<? extends IStreamObject<?>>> sources = determineSources(query
				.getPhysicalChilds());
		for (ISource<? extends IStreamObject<?>> source : sources) {
			LOG.debug("Adding source {} to dataSourceManager.",source.getName());
			dataSourceManager.addSource(source);
		}
	}

	private void processQueryRemoveEvent(IPhysicalQuery query) {
		LOG.debug("Query removed.");
		Collection<ISource<? extends IStreamObject<?>>> sources = determineSources(query
				.getPhysicalChilds());
		for (ISource<? extends IStreamObject<?>> source : sources) {
			LOG.debug("Removing source {} from dataSourceManager.",source.getName());
			dataSourceManager.removeSource(source);
		}

	}

	private static Collection<ISource<? extends IStreamObject<?>>> determineSources(
			List<IPhysicalOperator> operators) {
		Collection<ISource<? extends IStreamObject<?>>> sources = Lists
				.newArrayList();

		for (IPhysicalOperator operator : operators) {
			if (operator instanceof ISource
					&& !(operator instanceof ISink)
					&& !operator.getClass().isAnnotationPresent(
							NoSampling.class)) {
				@SuppressWarnings("unchecked")
				ISource<? extends IStreamObject<?>> source = (ISource<? extends IStreamObject<?>>) operator;
				sources.add(source);
			}
		}

		return sources;
	}

	@Override
	public Optional<IHistogram> getHistogram(SDFAttribute attribute) {
		Optional<ISampling> optSampler = samplingContainer
				.getSampler(attribute);
		if (!optSampler.isPresent()) {
			return Optional.absent();
		}
		ISampling sampler = optSampler.get();

		return createEqualWidthHistogram(attribute, sampler);
		// return createEqualDepthHistogram(attribute, sampler);
	}

	private static Optional<IHistogram> createEqualWidthHistogram(
			SDFAttribute attribute, ISampling sampler) {
		List<Double> values = sampler.getSampledValues();
		if (values.isEmpty()) {
			return Optional.absent();
		}

		int intervalCount = new FreedmanDiaconisRule()
				.estimateIntervalCount(values);

		double min = values.get(0);
		double max = values.get(values.size() - 1);
		double intervalSize = (max - min) / intervalCount;

		double[] counts = new double[intervalCount];
		for (Double val : values) {
			int index = (int) ((val - min) / intervalSize);

			if (index >= counts.length) {
				counts[counts.length - 1]++;
			} else {
				counts[index]++;
			}
		}
		return Optional.of((IHistogram) new EqualWidthHistogram(attribute, min,
				max, intervalSize, counts));
	}

	// private static Optional<IHistogram>
	// createEqualDepthHistogram(SDFAttribute attribute, ISampling sampler) {
	// List<Double> values = sampler.getSampledValues();
	// int valueCount = values.size();
	// int binNum = new FreedmanDiaconisRule().estimateIntervalCount(values);
	//
	// if (binNum < 1) {
	// binNum = 1;
	// }
	//
	// double depth = (double)valueCount / (double)binNum;
	// double[] borders = new double[binNum + 1];
	// double[] counts = new double[binNum];
	//
	// // assume that values are sorted
	// int actBin = 0;
	// int actBorder = 1;
	// borders[0] = values.get(0);
	// borders[borders.length - 1] = values.get(values.size() - 1 );
	//
	// double overhead = 0.0;
	// for( int i = 0; i < valueCount; i++ ) {
	// counts[actBin]++;
	// if( counts[actBin] > depth - overhead) {
	// overhead += (counts[actBin] - depth);
	//
	// borders[actBorder] = values.get(i);
	// actBorder++;
	// actBin++;
	// }
	// }
	//
	// return Optional.of((IHistogram)new EqualDepthHistogram(attribute,
	// borders, counts));
	// }

	@Override
	public Optional<IHistogram> getHistogram(String attributeName) {
		Collection<SDFAttribute> sampledAttributes = samplingContainer
				.getSampledAttributes();
		for (SDFAttribute sampledAttribute : sampledAttributes) {
			String attName = sampledAttribute.getSourceName() + "."
					+ sampledAttribute.getAttributeName();
			if (attName.equals(attributeName)) {
				return getHistogram(sampledAttribute);
			}
		}
		return Optional.absent();
	}

	@Override
	public Collection<SDFAttribute> getHistogramAttributes() {
		return samplingContainer.getSampledAttributes();
	}

	@Override
	public Optional<Double> getDatarate(String sourceName) {
		return datarateContainer.getDatarate(sourceName);
	}

	@Override
	public Collection<String> getDatarateSourceNames() {
		return datarateContainer.getDatarateSourceNames();
	}

	@Override
	public Optional<Double> getCpuTime(String operatorClass) {
		return cpuTimeContainer.getCpuTime(operatorClass);
	}

	@Override
	public Collection<String> getCpuTimeOperatorClasses() {
		return cpuTimeContainer.getCpuTimeOperatorClasses();
	}
}
