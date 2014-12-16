package de.uniol.inf.is.odysseus.s100.element.XLinks;

/**
 * 
 *        The 'actuate' attribute is used to communicate the desired timing
 *        of traversal from the starting resource to the ending resource;
 *        it's value should be treated as follows:
 *        onLoad - traverse to the ending resource immediately on loading
 *                 the starting resource
 *        onRequest - traverse from the starting resource to the ending
 *                    resource only on a post-loading event triggered for
 *                    this purpose
 *        other - behavior is unconstrained; examine other markup in link
 *                for hints
 *        none - behavior is unconstrained
 * 
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:06
 */
public class actuate {

	/**
	 * @author Christoph Dibbern
	 * @version 1.0
	 * @created 15-Dez-2014 20:43:06
	 */
	@SuppressWarnings("unused")
	private enum SimpleTypeClass2 {
		ONLOAD,
		ONREQUEST,
		OTHER,
		NONE;
	}

	public actuate(){

	}

	public void finalize() throws Throwable {

	}

}