package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * This class is a super class for logical clustering operators. It specifies two
 * output schemes that can be used concrete logical clustering operators. One
 * output schema is for clustered data points and one for the clusters.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractClusteringAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 5930667720134167936L;
	protected SDFAttributeList attributes;

	/**
	 * Standard constructor.
	 * 
	 */
	protected AbstractClusteringAO() {

	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the AbstractClusteringAO to copy.
	 */
	public AbstractClusteringAO(AbstractClusteringAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
	}

	/**
	 * Returns the list of attributes that should be used for clustering.
	 * 
	 * @return the list of attributes.
	 */
	public SDFAttributeList getAttributes() {
		return attributes;
	}

	/**
	 * Returns a list of indices identifying the positions of the attributes in
	 * the input schema that should be used for clustering.
	 * 
	 * @return the list of indices identifying the positions of the attributes
	 *         which should be used for clustering.
	 * 
	 */
	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(), attributes);
	}

	/**
	 * Returns a list of indices identifying the positions of the attributes in
	 * the input schema that should be used for clustering.
	 * 
	 * @param inputSchema
	 *            the input scheme.
	 * @param attributes
	 *            the list of attributes that should be used for clustering.
	 * @return the list of indices identifying the positions of the attributes
	 *         which should be used for clustering.
	 */

	public static int[] calcRestrictList(SDFAttributeList inputSchema,
			SDFAttributeList attributes) {
		int[] ret = new int[attributes.size()];
		int i = 0;
		for (SDFAttribute a : attributes) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : inputSchema) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	/**
	 * Sets the list of attributes that should be used for clustering.
	 * 
	 * @param attributes the list of attributes.
	 */
	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema
	 * ()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {

		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute id = new SDFAttribute("cluster_id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		outputSchema.add(id);
		outputSchema.addAll(getInputSchema().clone());
		return outputSchema;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#getOutputSchema(int)
	 */
	@Override
	public SDFAttributeList getOutputSchema(int port) {

		if (port == 0) {
			return getOutputSchema();
		} else {
			SDFAttributeList clusterSchema = new SDFAttributeList();
			SDFAttribute idA = new SDFAttribute("cluster_id");
			idA.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			clusterSchema.add(idA);
			SDFAttribute idCount = new SDFAttribute("cluster_count");
			idCount.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
			clusterSchema.add(idCount);
			clusterSchema.addAll(attributes.clone());
			return clusterSchema;
		}
	}
}
