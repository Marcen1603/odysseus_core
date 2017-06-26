package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

import com.google.common.base.Strings;

/**
 * This parameter is similar to the {@see NamedExpressionParameter} but has
 * strings in contrast to expressions. This can be used to have "raw"
 * expressions which have to be changed before they can be parsed. Used for
 * example for the {@see CreateUpdateExpressionsPunctuationAO}.
 * 
 * @author Tobias Brandt
 *
 */
public class NamedStringParameter extends AbstractParameter<NamedString> {

	private static final long serialVersionUID = -5772279973825161381L;

	@Override
	protected void internalAssignment() {
		if (this.inputValue instanceof List) {
			@SuppressWarnings("rawtypes")
			List input = (List) this.inputValue;
			if (input.size() == 2 && input.get(0) instanceof String && input.get(1) instanceof String) {
				String content = (String) input.get(0);
				String name = (String) input.get(1);
				NamedString namedString = new NamedString(name, content);
				this.setValue(namedString);
			}
		} else if (this.inputValue instanceof NamedString) {
			this.setValue((NamedString) this.inputValue);
		} else {
			throw new RuntimeException("Parameter input must be of type NamedString.");
		}
	}

	@Override
	protected String getPQLStringInternal() {
		if (this.inputValue instanceof NamedString) {
			NamedString namedString = (NamedString) this.inputValue;
			if (!Strings.isNullOrEmpty(namedString.getName())) {
				return namedString.toString();
			} else {
				return "'" + namedString.getContent() + "'";
			}
		}
		return "'" + this.inputValue + "'";
	}

}
