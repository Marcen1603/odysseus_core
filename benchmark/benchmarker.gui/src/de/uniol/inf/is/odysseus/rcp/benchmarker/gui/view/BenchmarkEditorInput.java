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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

/**
 * Diese Klasse beinhaltet den Input f�r den Benchmarkparameter-Editor
 * 
 * @author Steffi
 *
 */
public class BenchmarkEditorInput implements IEditorInput {

	private final BenchmarkParam benchmarkParam;

	public BenchmarkEditorInput(final BenchmarkParam param) {
		this.benchmarkParam = param;
	}

	public int getId() {
		return benchmarkParam.getId();
	}

	public BenchmarkParam getBenchmarkParam() {
		return benchmarkParam;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return String.valueOf(benchmarkParam.getId());
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return " ";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((benchmarkParam == null) ? 0 : benchmarkParam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BenchmarkEditorInput other = (BenchmarkEditorInput) obj;
		if (benchmarkParam == null) {
			if (other.benchmarkParam != null)
				return false;
		} else if (!benchmarkParam.equals(other.benchmarkParam))
			return false;
		return true;
	}
}
