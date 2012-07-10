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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class UserDefinedOperatorPO<R,W> extends AbstractPipe<R, W> {

	IUserDefinedFunction<R,W> udf;
	private String initString;
	
	public UserDefinedOperatorPO(UserDefinedOperatorPO<R, W> udopo) {
		super(udopo);
		this.udf = udopo.udf;
		this.initString = udopo.initString;
	}

	public UserDefinedOperatorPO() {
	}

	@Override
	public OutputMode getOutputMode() {
		return udf.getOutputMode();
	}

	public void setInitString(String initString) {
		this.initString = initString;
		setName(null);
		setName(getName()+" "+udf+" "+initString);
	}
	
	public void setUdf(IUserDefinedFunction<R, W> udf) {
		this.udf = udf;
		setName(null);
		setName(getName()+" "+udf+" "+initString);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		udf.init(initString);
	}
	
	@Override
	protected void process_next(R object, int port) {
		W result = udf.process(object, port);
		if (result != null){
			transfer(result);
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO: Was macht man mit Punctuations bei UDFs?
		sendPunctuation(timestamp);
	}

	@Override
	public AbstractPipe<R, W> clone() {
		return new UserDefinedOperatorPO<R, W>(this);
	}

	
}
