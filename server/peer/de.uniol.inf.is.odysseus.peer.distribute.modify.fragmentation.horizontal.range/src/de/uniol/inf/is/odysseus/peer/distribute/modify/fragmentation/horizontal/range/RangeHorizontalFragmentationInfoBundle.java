package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range;

import java.util.List;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;

/**
 * An fragmentation info bundle is an ADT for objects and informations relevant
 * for fragmentation.
 * 
 * @author Michael Brand
 */
public class RangeHorizontalFragmentationInfoBundle extends
		FragmentationInfoBundle {

	/**
	 * The attribute for the ranges.
	 */
	private String mAttribute;

	/**
	 * The lower limits for the ranges.
	 */
	private List<String> mRanges;

	/**
	 * The attribute for the ranges.
	 * 
	 * @param attributes
	 *            The name of the attribute for the ranges.
	 */
	public void setAttributeForRanges(String attribute) {

		this.mAttribute = attribute;

	}

	/**
	 * The attribute for the ranges.
	 * 
	 * @return The name of the attribute for the ranges.
	 */
	public String getAttributeForRanges() {

		return this.mAttribute;

	}

	/**
	 * The lower limits for the ranges.
	 * 
	 * @param ranges
	 *            The lower limits for the ranges.
	 */
	public void setRanges(List<String> ranges) {

		this.mRanges = ranges;

	}

	/**
	 * The lower limits for the ranges.
	 * 
	 * @return The lower limits for the ranges.
	 */
	public List<String> getRanges() {

		return this.mRanges;

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append("\nAttribute to build the ranges: " + this.mAttribute);
		buffer.append("\nLower limits for the ranges: " + this.mRanges);
		return buffer.toString();

	}

}