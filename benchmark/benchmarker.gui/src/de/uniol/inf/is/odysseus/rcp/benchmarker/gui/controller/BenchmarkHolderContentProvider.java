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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkResult;

public class BenchmarkHolderContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			List<?> unknownBenchmarkItem = ((List<?>) parentElement);
			if (unknownBenchmarkItem.isEmpty()) {
				return new Object[0];
			} else if (unknownBenchmarkItem.get(0) instanceof BenchmarkGroup) {
				return unknownBenchmarkItem.toArray(new BenchmarkGroup[unknownBenchmarkItem.size()]);
			} else if (unknownBenchmarkItem.get(0) instanceof Benchmark) {
				return unknownBenchmarkItem.toArray(new Benchmark[unknownBenchmarkItem.size()]);
			} else if (unknownBenchmarkItem.get(0) instanceof BenchmarkResult) {
				return unknownBenchmarkItem.toArray(new BenchmarkResult[unknownBenchmarkItem.size()]);
			} else {
				throw new IllegalStateException("Unknown list elements. Type is: "
						+ unknownBenchmarkItem.get(0).getClass().getName());
			}
		}else if (parentElement instanceof BenchmarkGroup) {
			BenchmarkGroup benchmarkGroup = (BenchmarkGroup) parentElement;
			List<Object> benchmarkGroupItems = new ArrayList<Object>(3);
			addIfNotNullAndNotEmpty(benchmarkGroupItems, benchmarkGroup.getBenchmarks());
			return benchmarkGroupItems.toArray(new Object[benchmarkGroupItems.size()]);
			
			
		} else if (parentElement instanceof Benchmark) {
			Benchmark benchmark = (Benchmark) parentElement;
			List<Object> benchmarkItems = new ArrayList<Object>(3);
			addIfNotNullAndNotEmpty(benchmarkItems, benchmark.getParam());
			addIfNotNullAndNotEmpty(benchmarkItems, benchmark.getResults());
			addIfNotNullAndNotEmpty(benchmarkItems, benchmark.getMetadata());
			return benchmarkItems.toArray(new Object[benchmarkItems.size()]);
		}
		return new Object[0];
	}

	private void addIfNotNullAndNotEmpty(List<Object> list, Object obj) {
		if (obj != null) {
			// skip if obj is an empty list
			if (obj instanceof List) {
				if (((List) obj).isEmpty()) {
					return;
				}
			}
			list.add(obj);
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Benchmark) {
			((Benchmark) element).getParentGroup();
		} else 
		if (element instanceof BenchmarkGroup) {
			return BenchmarkHolder.INSTANCE.getBenchmarkGroups();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof List) {
			return !((List<?>) element).isEmpty();
		} else if (element instanceof Benchmark) {
			return true;
		} else if (element instanceof BenchmarkGroup) { //oooooooooooooooooo
			return true; //ooooooooooooooooooooooooooooooooooooooooooooooooo
		}
		return false;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
