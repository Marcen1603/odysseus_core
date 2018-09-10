package de.uniol.inf.is.odysseus.graph.mep;

import org.graphstream.graph.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to get a certain property of a graph {@link Element}. The input
 * is (1) a graph element (graph, node or edge) and (2) the key of the property.
 * The output is the property (Object) of the graph element.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GetGraphElementProperty extends AbstractFunction<Object> {

	/**
	 * The version of this class for serialization
	 */
	private static final long serialVersionUID = -8602627795419805543L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "GetGraphElementProperty";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] {
			{ SDFGraphDatatype.GRAPH, SDFGraphDatatype.NODE, SDFGraphDatatype.EDGE }, { SDFDatatype.STRING } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public GetGraphElementProperty() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Object getValue() {
		try {
			return ((Element) getInputValue(0)).getAttribute(getInputValue(1));
		} catch (Throwable e) {
			log.error("Could not extract property of graph element!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
