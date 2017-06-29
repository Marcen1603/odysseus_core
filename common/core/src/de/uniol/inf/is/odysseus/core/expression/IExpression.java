package de.uniol.inf.is.odysseus.core.expression;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * A marker interface for all possible expressions
 * @author Marco Grawunder
 *
 */

public interface IExpression<T extends IStreamObject<M>, M  extends IMetaAttribute> {

	Object evaluate(T object, List<ISession> sessions, List<T> history);

}
