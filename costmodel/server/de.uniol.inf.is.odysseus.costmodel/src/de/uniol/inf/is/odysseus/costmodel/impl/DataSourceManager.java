package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.costmodel.impl.sampling.CombinedSampling;

public class DataSourceManager {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceManager.class);

	private static final int SAMPLING_SIZE_TUPLES = 1500;

	private final Map<ISource<? extends IStreamObject<?>>, DefaultStreamConnection<IStreamObject<?>>> connectionMap = Maps.newHashMap();
	private final Map<ISource<? extends IStreamObject<?>>, Integer> sourceUsageMap = Maps.newHashMap();
	
	//To avoid Problems with JxtaSources that have identical Attributes.
	private final Map<SDFAttribute,Integer> attributeUsageMap = Maps.newHashMap();

	private final SamplingContainer samplingContainer;
	private final DatarateContainer datarateContainer;

	public DataSourceManager(SamplingContainer samplingContainer, DatarateContainer datarateContainer) {
		Preconditions.checkNotNull(samplingContainer, "samplingContainer must not be null!");
		Preconditions.checkNotNull(datarateContainer, "datarateContainer must not be null!");

		this.samplingContainer = samplingContainer;
		this.datarateContainer = datarateContainer;
	}

	public void addSource(ISource<? extends IStreamObject<?>> source) {
		Preconditions.checkNotNull(source, "Source to register must not be null!");

		LOG.debug("Registering source {}", source.getName());
		if (!connectionMap.containsKey(source)) {
			LOG.debug("Source {} is new", source);
			
			SDFSchema outputSchema = source.getOutputSchema();
			if( outputSchema == null ) {
				LOG.warn("Operator {} has no output schema.", source);
				return;
			}
			
			ISampling[] samplers = new ISampling[outputSchema.size()];
			List<Integer> validIndicesList = Lists.newArrayList();
			for (int index = 0; index < source.getOutputSchema().size(); index++) {
				SDFAttribute attribute = source.getOutputSchema().get(index);

				if (isNumeric(attribute)) {
					if(attributeUsageMap.containsKey(attribute)) {
						attributeUsageMap.put(attribute, attributeUsageMap.get(attribute)+1);
						LOG.debug("Attribute {} is already known not sampling again. Usage is now: {}",attribute,attributeUsageMap.get(attribute));
						samplers[index] = samplingContainer.getSampler(attribute).get();
						validIndicesList.add(index);
					}
					else {
						samplers[index] = new CombinedSampling(SAMPLING_SIZE_TUPLES);
						samplingContainer.addSampler(attribute, samplers[index]);
						attributeUsageMap.put(attribute, 1);
						LOG.debug("Attribute {} is new.",attribute);
						validIndicesList.add(index);
					}
					
				}
			}

			if (!validIndicesList.isEmpty()) {
				Integer[] validIndices = validIndicesList.toArray(new Integer[0]);

				DefaultStreamConnection<IStreamObject<?>> connection = addStreamConnectionForSampling(source, samplers, validIndices);
				connectionMap.put(source, connection);
				sourceUsageMap.put(source, 1);
			}
		} else {
			sourceUsageMap.put(source, sourceUsageMap.get(source) + 1);
			LOG.debug("Source {} is already registered. Usages is {} now", source, sourceUsageMap.get(source));
		}
	}

	private DefaultStreamConnection<IStreamObject<?>> addStreamConnectionForSampling(final ISource<? extends IStreamObject<?>> source, final ISampling[] samplers, final Integer[] validIndices) {
		DefaultStreamConnection<IStreamObject<?>> connection = new DefaultStreamConnection<IStreamObject<?>>(source) {

			private IMonitoringData<Double> datarateData;
			private Double lastValue;

			@Override
			public void process(IStreamObject<?> element, int port) {
				Tuple<?> tuple = (Tuple<?>) element;
				for (Integer validIndex : validIndices) {
					Double number = toNumeric(tuple.getAttribute(validIndex));
					if (number != null) {
						samplers[validIndex].addValue(number);
					}
				}
				
				if( source instanceof ReceiverPO ) {
					if( datarateData == null ) {
						datarateData = source.getMonitoringData(MonitoringDataTypes.DATARATE.name);
					}
					
					Double currentValue = datarateData.getValue();
					if( currentValue != lastValue ) {
						datarateContainer.putDatarate(source.getName(), currentValue * 1000);
						lastValue = currentValue;
					}
				}
			}
		};
		connection.connect();
		LOG.debug("Created default stream connection to source {}", source);
		return connection;
	}

	private static boolean isNumeric(SDFAttribute attribute) {
		return attribute.getDatatype().equals(SDFDatatype.LONG) || attribute.getDatatype().equals(SDFDatatype.DOUBLE) || attribute.getDatatype().equals(SDFDatatype.FLOAT) || attribute.getDatatype().equals(SDFDatatype.BYTE)
				|| attribute.getDatatype().equals(SDFDatatype.INTEGER);
	}

	private static Double toNumeric(Object value) {
		if( value == null ) {
			return null;
		}
		
		if (value instanceof Double) {
			return ((Double) value);
		}
		if (value instanceof Float) {
			return ((Float) value).doubleValue();
		}
		if (value instanceof Long) {
			return ((Long) value).doubleValue();
		}
		if (value instanceof Byte) {
			return ((Byte) value).doubleValue();
		}
		if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		}

		LOG.error("Could not sample value '{}'. Is it not numeric?", value);
		return null;
	}

	public void removeSource(ISource<? extends IStreamObject<?>> source) {
		if (connectionMap.containsKey(source)) {
			Integer usages = sourceUsageMap.get(source);
			if (usages == 1) {
				DefaultStreamConnection<IStreamObject<?>> connection = connectionMap.get(source);
				connection.disconnect();

				for (SDFAttribute attribute : source.getOutputSchema()) {
					if(attributeUsageMap.containsKey(attribute)) {
						int attributeUsage = attributeUsageMap.get(attribute);
						if(attributeUsage<=1) {
							LOG.debug("Attribute {} is not used anymore. Removing...",attribute);
							samplingContainer.removeSampler(attribute);
							attributeUsageMap.remove(attribute);
						}
						else {
							attributeUsageMap.put(attribute,attributeUsageMap.get(attribute)-1);
							LOG.debug("Can not remove Attribute {}, as it is used by other sources.",attribute,attributeUsageMap.get(attribute));
						}
					} else {
						LOG.debug("Attribute {} is not in Usage Map", attribute);
					}
				}
				
				datarateContainer.save();

			} else {
				sourceUsageMap.put(source, usages - 1);
			}
		}
	}
}
