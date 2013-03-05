package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Predicate;


public 	class ChoroplethLegendEntry{
	private Predicate predicate;
	private ChoroplethStyle style;
	public ChoroplethLegendEntry(Predicate predicate, ChoroplethStyle style) {
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
