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
package de.uniol.inf.is.odysseus.test.physicaloperator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.test.TupleTestActivator;
import de.uniol.inf.is.odysseus.test.tuple.ICompareSinkListener;

public class TupleCompareSinkPO extends AbstractSink<Tuple<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(TupleCompareSinkPO.class);

	private String compareFile = null;
	List<Tuple<?>> expectedResults = new LinkedList<Tuple<?>>();	
	TupleDataHandler tupleDataHandler = null;
	String qry;
	private ICompareSinkListener sinkListener = null;
	private File expectedResultsFile = null;

	public TupleCompareSinkPO() {
	}
	
	public TupleCompareSinkPO(File expectedResultsFile, ICompareSinkListener sinkListener) {
		this.expectedResultsFile = expectedResultsFile;
		this.sinkListener = sinkListener;
	}
	
	public TupleCompareSinkPO(String compareFile) {
		this.compareFile = compareFile;
	}

	public TupleCompareSinkPO(TupleCompareSinkPO tupleCompareSinkPO) {
		compareFile = tupleCompareSinkPO.getCompareFile();
		sinkListener = tupleCompareSinkPO.sinkListener;
		expectedResultsFile = tupleCompareSinkPO.expectedResultsFile;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema s = getOutputSchema();
		this.tupleDataHandler = (TupleDataHandler) new TupleDataHandler().getInstance(s);
		
		synchronized (expectedResults) {
			try {
				LOG.debug("Reading Compare File " + compareFile);
				File expected = 
						expectedResultsFile == null? 
								new File(TupleTestActivator.bundlePath + File.separator + compareFile)
								: expectedResultsFile;
									
//				File expected = new File(TupleTestActivator.bundlePath + File.separator + compareFile);
				this.qry = expected.getName();
				BufferedReader reader = new BufferedReader(new FileReader(expected));
				String line = null;
				while ((line = reader.readLine()) != null) {
					this.expectedResults.add(this.tupleDataHandler.readData(line.trim().split(";")));
				}
				LOG.debug("Reading Compare File " + compareFile + " done");
			} catch (IOException e) {
				throw new OpenFailedException("Cannot read result file "
						+ e.getMessage());
			}
		}
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		if (!isDone()) {
			synchronized (expectedResults) {
				Tuple<?> expected = expectedResults.remove(0);

				if (!expected.equals(tuple)) {
					System.err.println(expected);
					System.err.println(tuple);
					System.err.println("Difference at " + expected.compareTo(tuple));
					//sinkListener.processingError(expected.toString(), tuple.toString());
				}

				if (expectedResults.isEmpty()) {
					this.done(port);
					System.out.println("Finished testing of Query: " + qry);
					//sinkListener.processingDone();
				}

			}
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	@Override
	public AbstractSink<Tuple<?>> clone() {
		return new TupleCompareSinkPO(this);
	}
	
	public String getCompareFile() {
		return compareFile;
	}

}
