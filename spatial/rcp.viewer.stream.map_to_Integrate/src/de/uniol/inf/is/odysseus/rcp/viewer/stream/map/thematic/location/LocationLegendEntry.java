package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Predicate;

public class LocationLegendEntry {
	private Predicate predicate;
	private LocationStyle style;
	public LocationLegendEntry(Predicate predicate, LocationStyle style) {
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
	public LocationStyle getStyle() {
		return style;
	}
	public void setStyle(LocationStyle style) {
		this.style = style;
	}
	@Override
	public String toString() {
		return "LocationLegendEntry [predicate=" + predicate + ", style="
				+ style + "]";
	}
}
