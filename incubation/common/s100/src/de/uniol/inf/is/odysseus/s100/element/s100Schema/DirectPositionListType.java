package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * DirectPositionList instances hold the coordinates for a sequence of direct
 * positions within the same coordinate reference system (CRS).
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:06
 */
public class DirectPositionListType extends doubleList {

	/**
	 * "count" allows to specify the number of direct positions in the list. If the
	 * attribute “count” is present then the attribute “srsDimension” shall be present,
	 * too.
	 */
	public int count;
	public SRSReferenceGroup m_SRSReferenceGroup;

	public DirectPositionListType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}