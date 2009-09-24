package de.uniol.inf.is.odysseus.physicaloperator.base;

/**
 * An operator, which acts both a {@link ISource} and as a {@link ISink}.
 * @author Jonas Jacobi
 */
public interface IPipe<Read,Write> extends ISource<Write>, ISink<Read>{
}
