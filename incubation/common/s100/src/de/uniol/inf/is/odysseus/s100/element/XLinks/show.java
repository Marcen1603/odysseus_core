package de.uniol.inf.is.odysseus.s100.element.XLinks;

/**
 * 
 *        The 'show' attribute is used to communicate the desired presentation
 *        of the ending resource on traversal from the starting resource; it's
 *        value should be treated as follows:
 *        new - load ending resource in a new window, frame, pane, or other
 *              presentation context
 *        replace - load the resource in the same window, frame, pane, or
 *                  other presentation context
 *        embed - load ending resource in place of the presentation of the
 *                starting resource
 *        other - behavior is unconstrained; examine other markup in the
 *                link for hints
 *        none - behavior is unconstrained
 * 
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:09
 */
public class show {

	/**
	 * @author Christoph Dibbern
	 * @version 1.0
	 * @created 15-Dez-2014 20:43:09
	 */
	@SuppressWarnings("unused")
	private enum SimpleTypeClass1 {
		NEW,
		REPLACE,
		EMBED,
		OTHER,
		NONE;
	}

	public show(){

	}

	public void finalize() throws Throwable {

	}

}