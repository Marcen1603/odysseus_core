package de.uniol.inf.is.odysseus.graph.mep;

import org.graphstream.graph.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Abstract MEP function to add an attribute (string or
 * object) to a graph {@link Element}. The inputs are a graph element (graph,
 * node or edge), the key of the attribute, and its value. The output is the
 * updated graph element.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public abstract class AddAttributeToGraphElement<E extends Element> extends AbstractFunction<E> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 7561763172871242531L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "AddAttributeToGraphElement";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 3;

	/**
	 * Creates a new MEP function.
	 */
	protected AddAttributeToGraphElement(SDFDatatype[][] inputTypes, SDFDatatype outputType) {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public E getValue() {
		try {
			@SuppressWarnings("unchecked")
			E element = (E) getInputValue(0);
			element.addAttribute((String) getInputValue(1), (Object) getInputValue(2));
			return element;
		} catch (Throwable e) {
			log.error("Could not add attribute to graph element!", e);
			return null;
		}
	}

}