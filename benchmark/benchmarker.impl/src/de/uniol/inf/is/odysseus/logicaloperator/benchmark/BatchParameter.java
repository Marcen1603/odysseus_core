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
package de.uniol.inf.is.odysseus.logicaloperator.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.USAGE;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.logicaloperator.benchmark.BatchParameter.BatchItem;

public class BatchParameter extends AbstractParameter<BatchItem> {
	private static final long serialVersionUID = 1300351843223441540L;

	public static class BatchItem {
		public BatchItem(int size, long wait) {
			this.size = size;
			this.wait = wait;
		}

		public final int size;
		public final long wait;
	}

	public BatchParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}
	
	public BatchParameter() {
		super();
	}

	@Override
	protected void internalAssignment() {
		List<?> list = (List<?>) this.inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException(
					"wrong number of inputs for batch: " + list.size());
		}
		int size = ((Number) list.get(0)).intValue();
		long wait = (Long) list.get(1);
		BatchItem batchItem = new BatchItem(size, wait);
		setValue(batchItem);
	}

}
