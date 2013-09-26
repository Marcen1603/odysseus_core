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
package de.uniol.inf.is.odysseus.test.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "TupleCompareSink", minInputPorts = 1, maxInputPorts = 1, doc="Used to compare the output of a stream and a file. ", category = {LogicalOperatorCategory.TEST, LogicalOperatorCategory.SINK})
public class TupleCompareSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -9070744467387287793L;
	private String fileName = null;
	
	public TupleCompareSinkAO(){
		super();
	}

	
	public TupleCompareSinkAO(String fileName) {
		super();
		this.fileName = fileName;
	}

	public TupleCompareSinkAO(TupleCompareSinkAO testSink) {
		super(testSink);		
		this.fileName = testSink.getFileName();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TupleCompareSinkAO(this);
	}
	
	@Parameter(name = "FILENAME", type = StringParameter.class, optional = false)
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
}
