package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

/**
 * Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class ConsoleSinkPO extends AbstractSink<IStreamObject<?>> {

	private boolean dumpPunctuation = false;
	private boolean printPort = false;

	public ConsoleSinkPO() {
	}

	public ConsoleSinkPO(ConsoleSinkPO original) {
		super();
	}

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		if (printPort) {
			System.out.print("Port:" + port + ", Object:");
		}
		System.out.println(object.toString());
	}

	@Override
	public ConsoleSinkPO clone() {
		return new ConsoleSinkPO(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (dumpPunctuation) {
			System.out
					.println("Port:" + port + ", PUNCTUATION: " + punctuation);
		}
	}

	public void setPrintPort(boolean printPort){
		this.printPort = printPort;
	}
	
	public void setDumpPunctuation(boolean dumpPunctuation) {
		this.dumpPunctuation = dumpPunctuation;
	}

}
