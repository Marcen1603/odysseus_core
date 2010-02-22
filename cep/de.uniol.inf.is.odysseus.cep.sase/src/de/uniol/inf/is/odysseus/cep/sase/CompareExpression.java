package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;

class CompareExpression {
	private List<PathAttribute> _attributes = null;
	private String _fullExpression = null;

	public CompareExpression(List<PathAttribute> attributes,
			String fullExpression) {
		super();
		_attributes = attributes;
		_fullExpression = fullExpression;
	}

	@Override
	public String toString() {

		return _attributes + " " + _fullExpression;
	}

	boolean contains(String attrib) {
		return get(attrib) != null;
	}

	PathAttribute get(String attrib) {
		for (PathAttribute a : _attributes) {
			if (attrib.startsWith(a.getStatename())) {
				return a;
			}
		}
		return null;
	}

	public String getFullExpression() {
		return _fullExpression;
	}

	public void setFullExpression(String fullExpression) {
		_fullExpression = fullExpression;
	}

	public List<PathAttribute> getAttributes() {
		return _attributes;
	}

}