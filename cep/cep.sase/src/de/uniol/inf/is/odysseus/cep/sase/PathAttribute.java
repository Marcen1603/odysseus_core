package de.uniol.inf.is.odysseus.cep.sase;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;

class PathAttribute {
	private String _aggregation;
	private String _attribute;
	private String _kleenePart;
	private String _path;
	private String _fullAttribute;
	private String _statename;

	public PathAttribute(String attribute, String kleenePart, String path,
			String aggregation) {
		init(attribute, kleenePart, path, aggregation);
	}

	private void init(String attribute, String kleenePart, String path,
			String aggregation) {
		//System.out.println(""+attribute+", "+kleenePart+", "+ path+", "+ aggregation);
		_attribute = attribute;
		_kleenePart = kleenePart;
		_path = path;
		_aggregation = aggregation;
		_aggregation.toUpperCase();
		_fullAttribute = _aggregation + "#"
				+ ((kleenePart == null || kleenePart.length() == 0) ? _attribute : _attribute +_kleenePart);
		if (path != null && path.length() > 0){
			_fullAttribute = _fullAttribute+ "." + path;
		}
		_statename = attribute;
		if (kleenePart != null) {
			_statename += (kleenePart.equals("[1]") ? "[1]" : "[i]");
		}
	}

	/**
	 * Constructor used for Creation of Id-Expressions
	 * 
	 * @param statename
	 * @param path
	 */
	public PathAttribute(String statename, String path) {
		int kIndex = statename.indexOf("[");
		if (kIndex > 0) {
			init(statename.substring(0, kIndex), statename.substring(kIndex),
					path, Write.class.getSimpleName());
		} else {
			init(statename, null, path, Write.class.getSimpleName());
		}
	}

	public String getFullAttributeName() {
		return _fullAttribute;
	}

	public String getRealAttributeName() {
		return _attribute + "." + _path;
	}

	public String getAggregation() {
		return _aggregation;
	}

	public String getAttribute() {
		return _attribute;
	}

	public String getKleenePart() {
		return _kleenePart;
	}

	boolean isKleeneAttribute() {
		return _kleenePart != null;
	}

	public String getStatename() {
		return _statename;
	}

	public String getPath() {
		return _path;
	}

	@Override
	public String toString() {
		return _fullAttribute;
	}
}