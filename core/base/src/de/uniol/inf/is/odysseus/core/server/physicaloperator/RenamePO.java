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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This operator is only needed as the top operator, if the output of
 * a query should be renamed (not needed in cases of views and not
 * between two operators)
 * 
 * @author Marco Grawunder
 *	
 * Which type to read and to write
 * @param <R>
 */

public class RenamePO<R extends IStreamObject<?>> extends AbstractPipe<R, R> {

	public RenamePO() {
	}

	public RenamePO(RenamePO<R> pipe) {
		super(pipe);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	
	@Override
	protected void process_next(R object, int port) {
		transfer(object);
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new RenamePO<R>(this);
	}

}
