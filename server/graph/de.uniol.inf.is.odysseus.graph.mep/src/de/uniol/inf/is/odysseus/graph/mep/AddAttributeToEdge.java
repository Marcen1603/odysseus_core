package de.uniol.inf.is.odysseus.graph.mep;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;

/**
 * TODO update javadoc MEP function to get the id of a graph {@link Element}.
 * The input is a graph element (graph, node or edge). The output is the id
 * (String) of the graph element.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class AddAttributeToEdge extends AddAttributeToGraphElement<Edge> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 5924610226723523494L;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFGraphDatatype.EDGE },
			{ SDFDatatype.STRING } , { SDFDatatype.STRING, SDFDatatype.OBJECT } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.EDGE;

	/**
	 * Creates a new MEP function.
	 */
	public AddAttributeToEdge() {
		super(inputTypes, outputType);
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
