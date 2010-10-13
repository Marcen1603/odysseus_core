package de.uniol.inf.is.odysseus.mep;

public interface IBinaryOperator<T> extends IOperator<T> {
	public boolean isCommutative();

	public boolean isAssociative();

	public boolean isLeftDistributiveWith(IOperator<T> operator);

	public boolean isRightDistributiveWith(IOperator<T> operator);

	public boolean isDistributiveWith(IOperator<T> operator);
}
