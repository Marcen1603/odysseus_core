package de.uniol.inf.is.odysseus.core.mep;

public interface IVariable extends IExpression<Object> {

	/**
	 * Return the name of the variable
	 * @return
	 */
	String getIdentifier();

	int getPosition();

	void bind(Object value, int position);

}
