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

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkMetadata;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkResult;

/**
 * Diese Klasse gibt die Namen der TreeViewer-Elemente
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkHolderLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof List) {
			if (((List<?>) element).get(0) instanceof BenchmarkResult) {
				return "Results";
			}
		} else if (element instanceof Benchmark) {
			return "Benchmark (Name: " + ((Benchmark) element).getParam().getName() + ")";
		} else if (element instanceof BenchmarkGroup) {
			return ((BenchmarkGroup) element).getName();
		} else if (element instanceof BenchmarkParam) {
			return "Parameters";
		} else if (element instanceof BenchmarkMetadata) {
			return "Metadata";
		} else if (element instanceof BenchmarkResult) {
			return "Result " + ((BenchmarkResult) element).getId();
		}
		return super.getText(element);
	}
}
