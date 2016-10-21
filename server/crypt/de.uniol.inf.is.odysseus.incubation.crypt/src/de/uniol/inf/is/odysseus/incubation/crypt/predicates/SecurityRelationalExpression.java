package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import java.util.Set;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedPredicatePunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptedValue;

/**
 * @author MarkMilster
 *
 */
public class SecurityRelationalExpression<T extends IMetaAttribute> extends RelationalExpression<T>
		implements ISecurityPredicate<Tuple<T>> {

	private static final long serialVersionUID = -7127164706383555587L;
	private String operatorName;

	public SecurityRelationalExpression(String operatorName, SDFExpression expression) {
		super(expression);
		this.operatorName = operatorName;
	}

	public SecurityRelationalExpression(SecurityRelationalExpression<T> other) {
		super(other);
		this.operatorName = other.operatorName;
	}

	public SecurityRelationalExpression(String operatorName, RelationalExpression<T> original) {
		super(original);
		this.operatorName = operatorName;
	}

	@Override
	public IPunctuation processPunctuation(IPunctuation punctuation) {
		punctuation = super.processPunctuation(punctuation);
		if (punctuation instanceof CryptedPredicatePunctuation) {
			CryptedPredicatePunctuation cryptPunctuation = (CryptedPredicatePunctuation) punctuation;
			String expressionStr = this.getExpression().getExpressionString();
			// TODO nice to have: so machen, dass man nicht die richtige form
			// eingeben muss, sondern so tippen kann, wie sonst auch
			// TODO in punc kann man wenn man moechte noch den name des
			// operators setzen
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
