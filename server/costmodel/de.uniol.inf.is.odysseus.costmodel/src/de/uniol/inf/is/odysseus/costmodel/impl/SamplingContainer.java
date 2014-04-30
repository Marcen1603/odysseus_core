package de.uniol.inf.is.odysseus.costmodel.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class SamplingContainer {

	private static final Logger LOG = LoggerFactory.getLogger(SamplingContainer.class);
	
	private final Map<SDFAttribute, ISampling> samplingMap = Maps.newHashMap();

	public void addSampler( SDFAttribute attribute, ISampling sampling ) {
		Preconditions.checkNotNull(attribute, "Attribute must not be null!");
		Preconditions.checkNotNull(sampling, "Sampler must not be null!");
		
		LOG.debug("Begin sampling of attribute {}", attribute);
		samplingMap.put(attribute, sampling);
		
		String attributeFileName = determineFilename(attribute);
		File file = new File(Config.SAMPLING_FILES_PATH + attributeFileName);
		if( file.exists() ) {
			
			try {
				readSampledValuesFromFile(sampling, file);
				LOG.debug("Read sampled values for attribute {}. It contains {} values.", attribute, sampling.getSampleSize());
			} catch (IOException e) {
				LOG.debug("Could not read sampled values for attribute {}", attribute);
			}
		}
	}

	public void removeSampler(SDFAttribute attribute) {
		Preconditions.checkNotNull(attribute, "Attribute to remove must not be null!");
		
		ISampling sampling = samplingMap.remove(attribute);
		if( sampling != null ) {
			LOG.debug("Stopped sampling of attribute '{}'. It contains {} values.", attribute, sampling.getSampleSize());
			
			String attributeFileName = determineFilename(attribute);
			File file = new File(Config.SAMPLING_FILES_PATH + attributeFileName);
			
			try {
				writeSampledValuesToFile(sampling, file);
				LOG.debug("Saved sampled values of attribute {} to file {}", attribute, file);
			} catch (IOException e) {
				LOG.error("Could not save the sampled data of attribute {}", attribute, e);
			}
		}
	}
	
	public Collection<SDFAttribute> getSampledAttributes() {
		return Lists.newArrayList(samplingMap.keySet());
	}
	
	public Optional<ISampling> getSampler(SDFAttribute attribute ) {
		return Optional.fromNullable(samplingMap.get(attribute));
	}

	private static String determineFilename(SDFAttribute attribute) {
		return attribute.toString().replace(":", "_") + ".att";
	}

	private static void writeSampledValuesToFile(ISampling sampling, File file) throws IOException {
		int valueCount = sampling.getSampleSize();
		List<Double> values = sampling.getSampledValues();
		
		ByteBuffer bb = ByteBuffer.allocate(4 + ( valueCount * 8));
		bb.putInt(valueCount);
		for( int i = 0; i < valueCount; i++ ) {
			bb.putDouble(values.get(i));
		}
		
		byte[] bytes = bb.array();
		
		Files.write(bytes, file);
	}
	
	private static void readSampledValuesFromFile(ISampling sampling, File file) throws IOException {
		byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		
		int valueCount = bb.getInt();
		for( int i = 0; i < valueCount; i++ ) {
			sampling.addValue(bb.getDouble());
		}
	}

}
