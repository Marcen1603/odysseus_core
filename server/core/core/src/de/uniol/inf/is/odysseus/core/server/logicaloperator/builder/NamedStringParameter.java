package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import com.google.common.base.Strings;

public class NamedStringParameter extends AbstractParameter<NamedString> {

	private static final long serialVersionUID = -5772279973825161381L;

	@Override
	protected void internalAssignment() {
		if (this.inputValue instanceof NamedString) {
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
