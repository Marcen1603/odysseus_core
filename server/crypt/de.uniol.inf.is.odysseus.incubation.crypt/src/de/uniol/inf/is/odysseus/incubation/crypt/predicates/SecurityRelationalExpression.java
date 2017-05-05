package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import java.util.Set;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedPredicatesPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedValue;

/**
 * Security version of RelationalExpression, which provides features for
 * crypting.
 * 
 * @author MarkMilster
 *
 */
public class SecurityRelationalExpression<T extends IMetaAttribute> extends RelationalExpression<T>
		implements ISecurityPredicate<Tuple<T>> {

	private static final long serialVersionUID = -7127164706383555587L;
	private String operatorName;

	/**
	 * Copy constructor
	 * 
	 * @param other
	 *            The SecurityRelationalExpression, which will be copied
	 */
	public SecurityRelationalExpression(SecurityRelationalExpression<T> other) {
		super(other);
		this.operatorName = other.operatorName;
	}

	/**
	 * Constructor
	 * 
	 * @param operatorName
	 *            The name of the operator, this Predicate will be used in
	 * @param original
	 *            RelationalExpression, which will be copied
	 */
	public SecurityRelationalExpression(String operatorName, RelationalExpression<T> original) {
		super(original);
		this.operatorName = operatorName;
	}

	@Override
	public IPunctuation processPunctuation(IPunctuation punctuation) {
		punctuation = super.processPunctuation(punctuation);
		if (punctuation instanceof CryptedPredicatesPunctuation) {
			CryptedPredicatesPunctuation cryptPunctuation = (CryptedPredicatesPunctuation) punctuation;
			String expressionStr = this.getExpression().getExpressionString();
			// TODO nice to have: parse the expressionStr in a more user friedly
			// mode
			// TODO you could set the name of the Operators in Crypt_Predicates
			// and the Punctuation
			Set<String> keys = cryptPunctuation.getCryptedPredicates().keySet();
			String newExpressionStr = new String(expressionStr);
			for (String key : keys) {
				CryptedValue cryptedValue = cryptPunctuation.getCryptedPredicates().get(key);
				String cryptedExprPart = new String(key);
				cryptedExprPart = cryptedExprPart.replaceAll(Pattern.quote(cryptedValue.getOldValue()),
						cryptedValue.getNewValue());
				newExpressionStr = newExpressionStr.replaceAll(Pattern.quote(key), cryptedExprPart);
			}
			this.expression = new SDFExpression(newExpressionStr, this.getAttributeResolver(),
					this.getExpressionParser());
		}
		return punctuation;
	}

	@Override
	public String getOperatorName() {
		return this.operatorName;
	}

	@Override
	public SecurityRelationalExpression<T> clone() {
		return new SecurityRelationalExpression<>(operatorName, this);
	}

}
