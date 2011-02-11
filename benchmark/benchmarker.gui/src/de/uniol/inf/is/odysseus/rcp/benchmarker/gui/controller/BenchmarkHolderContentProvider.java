package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
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
			List<?> benchmarks = ((List<?>) parentElement);
			if (benchmarks.isEmpty()) {
				return new Object[0];
			} else if (benchmarks.get(0) instanceof Benchmark) {
				return benchmarks.toArray(new Benchmark[benchmarks.size()]);
			} else if (benchmarks.get(0) instanceof BenchmarkResult) {
				return benchmarks.toArray(new BenchmarkResult[benchmarks.size()]);
			} else {
				throw new IllegalStateException("Unknown list elements. Type is: "
						+ benchmarks.get(0).getClass().getName());
			}
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
			return BenchmarkHolder.INSTANCE.getBenchmarks();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof List) {
			return !((List<?>) element).isEmpty();
		} else if (element instanceof Benchmark) {
			return true;
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
