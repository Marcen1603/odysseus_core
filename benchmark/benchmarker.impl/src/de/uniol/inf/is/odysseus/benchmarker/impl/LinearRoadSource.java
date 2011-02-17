/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LinearRoadSource extends
		AbstractIterableSource<RelationalTuple<? extends IClone>> {

	private String filename = "datafile3hours.dat.raw";
	private ObjectInputStream iStream;
	private RelationalTuple<?> buffer;
	private boolean isDone = false;
	private long time;

	public LinearRoadSource() {
	}

	public LinearRoadSource(String filename) {
		this.filename = filename;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			this.iStream = new ObjectInputStream(new FileInputStream(
					this.filename));
			buffer = null;
			time = System.currentTimeMillis();
		} catch (Exception e) {
			throw new OpenFailedException(e.getMessage());
		}
	}

	@Override
	public boolean hasNext() {
		if (buffer != null) {
			return (Integer) buffer.getAttribute(1) * 1000 + time <= System
					.currentTimeMillis();
		}
		buffer = new RelationalTuple(9);
		try {
			do {
				for (int i = 0; i < 15; ++i) {
					int curVal;
					curVal = this.iStream.readInt();
					if (i < 9) {
						buffer.setAttribute(i, curVal);
					}
				}
			} while ((Integer) buffer.getAttribute(0) != 0);
		} catch (EOFException e) {
			this.isDone = true;
			propagateDone();
			return false;
		} catch (IOException e) {
			return false;
		}
		return (Integer) buffer.getAttribute(1) * 1000 + time <= System
				.currentTimeMillis();
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void transferNext() {
		if (this.buffer == null && !hasNext()) {
			throw new NoSuchElementException();
		}
		transfer(buffer);
		buffer = null;
	}
	
	@Override
	public LinearRoadSource clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

		


}
