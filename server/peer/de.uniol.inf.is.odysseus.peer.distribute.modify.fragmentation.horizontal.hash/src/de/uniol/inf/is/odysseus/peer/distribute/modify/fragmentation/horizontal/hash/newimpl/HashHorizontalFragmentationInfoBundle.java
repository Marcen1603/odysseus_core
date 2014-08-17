package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.newimpl;

import java.util.List;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;

/**
 * An fragmentation info bundle is an ADT for objects and informations relevant
 * for fragmentation.
 * 
 * @author Michael Brand
 */
public class HashHorizontalFragmentationInfoBundle extends
		FragmentationInfoBundle {
	
	/**
	 * The attributes for the hash key, if given.
	 */
	private Optional<List<String>> mKeyAttrbutes;
	
	/**
	 * The attributes for the hash key.
	 * 
	 * @param attributes A list of attribute names for the hash key.
	 */
	public void setKeyAttributes(Optional<List<String>> attributes) {

		this.mKeyAttrbutes = attributes;

	}

	/**
	 * The attributes for the hash key.
	 * 
	 * @return  list of attribute names for the hash key or {@link Optional#absent()} if none is given.
	 */
	public Optional<List<String>> getKeyAttributes() {

		return this.mKeyAttrbutes;

	}
	
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append("\nAttributes to build the hash key: "
				+ this.mKeyAttrbutes);
		return buffer.toString();

	}

}