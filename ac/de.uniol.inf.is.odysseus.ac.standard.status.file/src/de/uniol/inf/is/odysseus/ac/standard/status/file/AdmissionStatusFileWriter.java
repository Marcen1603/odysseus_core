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

package de.uniol.inf.is.odysseus.ac.standard.status.file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.ac.standard.AdmissionStatus;
import de.uniol.inf.is.odysseus.ac.standard.IAdmissionStatusListener;
import de.uniol.inf.is.odysseus.ac.standard.StandardAC;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

public class AdmissionStatusFileWriter implements IAdmissionStatusListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdmissionStatusFileWriter.class);
	private static final String FILE_NAME = OdysseusConfiguration.getHomeDir() + "ACStatus" + System.currentTimeMillis() + ".csv";
	private static final String SEPARATOR = ";";
	private static final long WRITE_INTERVAL_MILLIS = 10000;

	private FileWriter outFile;

	private final List<AdmissionStatus> cache = Lists.newArrayList();
	private long lastWrite;
	private boolean headerWritten = false;
	
	@Override
	public void updateAdmissionStatus(StandardAC admissionControl, AdmissionStatus status) {
		cache.add(status);

		if (System.currentTimeMillis() - lastWrite > WRITE_INTERVAL_MILLIS) {
			try {
				outFile = new FileWriter(FILE_NAME, true);
				PrintWriter out = new PrintWriter(outFile);
				
				if( !headerWritten ) {
					writeHeader(out);
					headerWritten = true;
				}
				
				for (AdmissionStatus st : cache) {
					writeACStatus(out, st);
				}
				out.flush();
				out.close();
				cache.clear();
				lastWrite = System.currentTimeMillis();
				
			} catch (IOException ex) {
				LOG.error("Could not write status of admission control to file", ex);
			}
		}
	}

	private static void writeHeader(PrintWriter out) {
		out.print("Timetstamp");
		out.print(SEPARATOR);
		out.print("Elapsed time");
		out.print(SEPARATOR);
		out.print("Added Query Count");
		out.print(SEPARATOR);
		out.print("Running Query Count");
		out.print(SEPARATOR);
		out.print("Stopped Query Count");
		out.print(SEPARATOR);
		out.print("Act MEM");
		out.print(SEPARATOR);
		out.print("Act CPU");
		out.print(SEPARATOR);
		out.print("Max MEM");
		out.print(SEPARATOR);
		out.print("Max CPU");
		out.print(SEPARATOR);
		out.print("Min MEM");
		out.print(SEPARATOR);
		out.println("Min CPU");
	}

	private static void writeACStatus(PrintWriter out, AdmissionStatus status) {
		int added = status.getAddedQueryCount();
		int running = status.getRunningQueryCount();
		int stopped = status.getStoppedQueryCount();

		ICost actCost = status.getActCost();
		ICost maxCost = status.getMaxCost();
		ICost underloadCost = status.getUnderloadCost();
		long timestamp = status.getTimestamp();
		long runningTime = status.getRunningTime();

		out.print(timestamp);
		out.print(SEPARATOR);
		out.print(runningTime);
		out.print(SEPARATOR);
		out.print(added);
		out.print(SEPARATOR);
		out.print(running);
		out.print(SEPARATOR);
		out.print(stopped);
		out.print(SEPARATOR);
		out.print(actCost);
		out.print(SEPARATOR);
		out.print(maxCost);
		out.print(SEPARATOR);
		out.println(underloadCost);
	}
}
