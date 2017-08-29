package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * An interface for expressions
 *
 * @author Marco Grawunder
 *
 */

public interface IExpression<T extends IStreamObject<M>, M extends IMetaAttribute> {

	/**
	 * Method evaluates the expression with the input stream object
	 *
	 * @param object
	 *            The input that should be used for unbounded values inside the
	 *            expression
	 * @param sessions
	 *            A set of sessions the are currently active. Could be null
	 * @param history
	 *            A set of former objects that could be used together with the
	 *            input object, in most cases this will be null
	 * @return The result of the expression evaluation
	 */
	Object evaluate(T object, List<ISession> sessions, List<T> history);

}
