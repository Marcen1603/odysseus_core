package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IClone;

public class Solid3D extends AbstractSpatialObject implements IClone,
		Serializable {

	/**
	 * All the facets building the solid.
	 */
	private ArrayList<Facet3D> facets;

	/**
	 * All the segments building the facets. There are no duplicates. So every
	 * segment is occurs only once in the segment.
	 */
	private ArrayList<Segment3D> segments;

	public ArrayList<Segment3D> getSegments() {
		return segments;
	}

	/**
	 * Indicates whether the state of this solid is correct or not. A state of a
	 * solid is correct if and only if all the facets meet and there are no open
	 * bounds of a facet that do not meet another facet.
	 * 
	 * For simplification we assume that no more than one facet meets another
	 * facet at the same segment.
	 */
	private boolean correct;

	public Solid3D(ArrayList<Facet3D> facets, boolean check) {
		this.facets = facets;
		this.separateSegments();
		if (check) {
			this.checkState();
		} else {
			this.correct = true;
		}
	}

	public Solid3D(boolean check, Facet3D... facet3Ds) {
		this.facets = new ArrayList<Facet3D>();
		for (Facet3D f : facet3Ds) {
			this.facets.add(f);
		}
		this.separateSegments();
		if (check) {
			this.checkState();
		} else {
			this.correct = true;
		}
	}

	private Solid3D(Solid3D original) {
		this.facets = new ArrayList<Facet3D>();
		for (Facet3D f : original.facets) {
			this.facets.add(f.clone());
		}
		this.segments = new ArrayList<Segment3D>();
		for (Segment3D s : original.segments) {
			this.segments.add(s.clone());
		}
		this.correct = original.correct;
	}

	public Solid3D clone() {
		return new Solid3D(this);
	}

	private void separateSegments() {
		this.segments = new ArrayList<Segment3D>();
		for (Facet3D f : this.facets) {
			for (Segment3D s : f.getSegments()) {
				boolean contained = false;
				for (Segment3D sin : this.segments) {
					if (sin.weakEquals(s)) {
						contained = true;
						break;
					}
				}
				if (!contained) {
					this.segments.add(s);
				}
			}
		}
	}

	/**
	 * Indicates whether the state of this solid is correct or not. A state of a
	 * solid is correct if and only if all the facets meet and there are no open
	 * bounds of a facet that do not meet another facet.
	 * 
	 * For simplification we assume that no more than one facet meets another
	 * facet at the same segment.
	 * 
	 * This is a very expensive method. So it would be better not to use this
	 * method but check at generating that the resulting facet will be valid.
	 */
	public boolean checkState() {
		// the smallest solid must
		// have at least 4 facets.
		// Otherwise there would
		// be at least on open side
		// in the solid. Think of
		// a pyramide consisting
		// of 3 triangulars and
		// a rectangle for the ground
		if (this.facets.size() < 4) {
			this.correct = false;
		}

		for (int i = 0; i < this.facets.size(); i++) {
			Facet3D f1 = this.facets.get(i);
			for (int u = i + 1; u < this.facets.size(); u++) {
				Facet3D f2 = this.facets.get(u % this.facets.size());
				for (int t = 0; t < f1.getSegments().size(); t++) {
					Segment3D s1 = f1.getSegment(t, false);
					boolean found = false;
					for (int v = 0; v < f2.getSegments().size(); v++) {
						Segment3D s2 = f2.getSegment(v, false);
						if (s1.weakEquals(s2)) {
							found = true;
							break;
						}
					}
					// There was no matching segment
					// so there must be an open
					// boundary of the solid. That means
					// that the solid is not valid.
					if (!found) {
						this.correct = false;
						return this.correct;
					}
				}
			}
		}
		this.correct = true;
		return this.correct;
	}

	/**
	 * Returns the facets that form the solid object
	 */
	public ArrayList<Facet3D> getFacets() {
		return this.facets;
	}

	public boolean intersects(Segment3D s) {
		return false; //SpatialFraction.intersect(s, this);
	}

	public boolean intersects(Line3D l) {
		return false; //SpatialFraction.intersect(l, this);
	}


	public boolean intersects(Facet3D f) {
		throw new RuntimeException("Not Implemented");
	}

	public boolean intersects(Solid3D sd) {
		throw new RuntimeException("Not Implemented");
	}
}
