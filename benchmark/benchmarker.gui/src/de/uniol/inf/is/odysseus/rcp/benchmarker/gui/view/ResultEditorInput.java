/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkResult;

/**
 * Diese Klasse beinhaltet den Input für die View ResultEditorPart
 * 
 * @author Stefanie Witzke
 * 
 */
public class ResultEditorInput implements IEditorInput {

	private BenchmarkResult result;
	private int id;
	private Benchmark benchmark;

	public ResultEditorInput(BenchmarkResult result, Benchmark benchmarkOfResult) {
		this.result = result;
		id = result.hashCode();
		this.setBenchmark(benchmarkOfResult);
	}

	public String getResultName() {
		return result.getResultXml();
	}

	public int getResultID() {
		return result.getId();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public String getName() {
		return "Result";
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	private int getId() {
		return getResultID() + id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultEditorInput other = (ResultEditorInput) obj;
		if (getId() == other.getId()) {
			return true;
		}
		return false;
	}

	public void setBenchmark(Benchmark benchmark) {
		this.benchmark = benchmark;
	}

	public Benchmark getBenchmark() {
		return benchmark;
	}

	public BenchmarkResult getResult() {
		return result;
	}
}
