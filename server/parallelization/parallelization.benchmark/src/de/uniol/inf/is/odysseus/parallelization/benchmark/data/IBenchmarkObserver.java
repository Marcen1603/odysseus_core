package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.Observer;

public interface IBenchmarkObserver extends Observer{

	IBenchmarkEvaluation getBenchmarkEvaluation();
}
