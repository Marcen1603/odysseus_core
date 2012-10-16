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

package de.uniol.inf.is.odysseus.costmodel.operator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public final class MemoryUsageSaver {

	private static final int DEFAULT_MEMORY_USAGE_BYTES = 4;
	private static final int DEFAULT_MEMORY_USAGE_OVERHEAD_BYTES = 40;
	private static final Logger LOG = LoggerFactory.getLogger(MemoryUsageSaver.class);
	private static final String FILENAME = "ac_operatorMemory.conf";
	
	private static final Map<String, Long> memUsages = Maps.newHashMap();
	
	private static final Map<Class<?>, Integer> BYTE_MAP = ImmutableMap.<Class<?>, Integer>builder()
			.put(Long.class, 8)
			.put(Integer.class, 4)
			.put(Object.class, 4)
			.put(Double.class, 8)
			.put(Float.class, 4)
			.put(String.class, 20)
			.put(Boolean.class, 1)
			.put(Byte.class, 1)
			.build();
	
	static void load() {
		String filename = determineFilename();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();
			while (line != null) {

				String[] parts = line.split("\\=");

				Long d = new Long(parts[1]);
				memUsages.put(parts[0], d);

				line = br.readLine();
			}
			LOG.debug("File {} with memoryUsages loaded", filename);
			
			br.close();
		} catch (FileNotFoundException ex) {
			File file = new File(filename);
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOG.error("Could not create file {}", filename, e );
			}
		} catch (IOException e) {
			LOG.error("Could not load file {}", filename, e );
		} 
	}
	
	static void save() {
		String filename = determineFilename();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));

			for (String str : memUsages.keySet()) {
				Long memUsage = memUsages.get(str);
				bw.write(str + "=" + memUsage + "\n");
				bw.flush();
			}

			LOG.debug("File {} with memoryUsages saved", filename);
		} catch (IOException e) {
			LOG.error("Could not save {}", filename, e);
		} finally {
			try {
				bw.close();
			} catch (IOException ex) {}
		}
	}
		
	public static long get(IPhysicalOperator op) {
		Preconditions.checkNotNull(op, "Physical operator to save memory usage must not be null!");
		
		String className = op.getClass().getSimpleName();
		if (!memUsages.containsKey(className)) {
			
			long mem = estimateBytes(op);
			memUsages.put(className, mem);
			
			LOG.debug("Estimated memory usage for {} : {}", className, mem);
			return mem; 
		}

		return memUsages.get(className);
	}

	private static String determineFilename() {
		return OdysseusConfiguration.getHomeDir() + FILENAME;
	}
	
	private static long estimateBytes( Object obj ) {
		Field[] fields = obj.getClass().getDeclaredFields();
		
		long byteSum = 4;
		for( Field field : fields ) {
			field.setAccessible(true);
			
			byteSum += estimateFieldBytes(field.getType());
			
			field.setAccessible(false);
		}
		
		return DEFAULT_MEMORY_USAGE_OVERHEAD_BYTES + byteSum;
	}
	
	private static long estimateFieldBytes( Class<?> clazz ) {
		Integer bytes = BYTE_MAP.get(clazz);
		return bytes != null ? bytes : DEFAULT_MEMORY_USAGE_BYTES;
	}
}
