package de.uniol.inf.is.odysseus.graph.mep;

import org.graphstream.graph.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to get the id of a graph {@link Element}. The input is a graph
 * element (graph, node or edge). The output is the id (String) of the graph
 * element.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GetGraphElementIdMEP extends AbstractFunction<String> {

	/**
	 * The version of this class for serialization
	 */
	private static final long serialVersionUID = 693962323658751098L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "GetGraphElementId";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] {
			{ SDFGraphDatatype.GRAPH, SDFGraphDatatype.NODE, SDFGraphDatatype.EDGE } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFDatatype.STRING;

	/**
	 * Creates a new MEP function.
	 */
	public GetGraphElementIdMEP() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public String getValue() {
		try {
			return ((Element) getInputValue(0)).getId();
		} catch (Throwable e) {
			log.error("Could not extract id of graph element!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
