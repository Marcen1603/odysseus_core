package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
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
			List<?> listOfElements = (List<?>) element;
			if (listOfElements.isEmpty()) {
				return "";
			} else if (listOfElements.get(0) instanceof Benchmark) {
				return "Steffi";
			} else if (listOfElements.get(0) instanceof BenchmarkResult) {
				return "Results";
			}
		} else if (element instanceof Benchmark) {
			return ((Benchmark) element).getParam().getName();
		} else if (element instanceof BenchmarkParam) {
			return "Parameters";
		} else if (element instanceof BenchmarkMetadata) {
			return "Metadata";
		}
		return super.getText(element);
	}
}
