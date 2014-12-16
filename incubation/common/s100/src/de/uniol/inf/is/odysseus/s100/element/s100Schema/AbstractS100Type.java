package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * All complexContent S-100 elements are directly or indirectly derived from this
 * abstract supertype	to establish a hierarchy of S-100 types that may be
 * distinguished from other XML types by their ancestry.
 * 	   Elements in this hierarchy may have an ID and are thus referenceable.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:06
 */
public abstract class AbstractS100Type {

	public id ext_ref1;
	public update m_update;

	public AbstractS100Type(){

	}

	public void finalize() throws Throwable {

	}

}