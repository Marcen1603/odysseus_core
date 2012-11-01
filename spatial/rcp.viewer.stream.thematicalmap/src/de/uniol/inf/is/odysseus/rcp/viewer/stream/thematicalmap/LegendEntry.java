package de.uniol.inf.is.odysseus.rcp.viewer.stream.thematicalmap;


public 	class LegendEntry{
	private Predicate predicate;
	private ChoroplethStyle style;
	public LegendEntry(Predicate predicate, ChoroplethStyle style) {
		super();
		this.predicate = predicate;
		this.style = style;
	}
	public Predicate getPredicate() {
		return predicate;
	}
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}
	public ChoroplethStyle getStyle() {
		return style;
	}
	public void setStyle(ChoroplethStyle style) {
		this.style = style;
	}
	@Override
	public String toString() {
		return "LegendEntry [predicate=" + predicate + ", style=" + style
				+ "]";
	}
}
