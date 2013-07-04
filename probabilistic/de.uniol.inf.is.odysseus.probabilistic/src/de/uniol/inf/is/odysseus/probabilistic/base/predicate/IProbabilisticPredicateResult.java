package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

public interface IProbabilisticPredicateResult<T> {
double getProbability();
T getValue();
}
