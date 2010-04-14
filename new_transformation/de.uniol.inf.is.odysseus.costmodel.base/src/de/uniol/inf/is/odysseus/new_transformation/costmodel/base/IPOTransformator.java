package de.uniol.inf.is.odysseus.new_transformation.costmodel.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;

public interface IPOTransformator<T extends ILogicalOperator> {
	/**
	 * Checks if the operator can be transformed using the current
	 * configuration.
	 * 
	 * @param logicalOperator
	 *            - the Operator which would later be transformed
	 * @param config
	 *            - the {@link TransformationConfiguration}, containing info
	 *            about the plan
	 * @return true if this transformator can transform the Operator
	 */
	public boolean canExecute(T logicalOperator, TransformationConfiguration config);

	/**
	 * Creates a {@link TempTransformationOperator} representing the PO created
	 * with
	 * {@link #transform(ILogicalOperator, TransformationConfiguration, ITransformation)}
	 * @param incomingStreamCharacteristics 
	 * 
	 * @return
	 */
	public TempTransformationOperator createTempOperator();

	/**
	 * Transforms the given LogicalOperator to the physical implementation
	 * 
	 * @param logicalOperator
	 *            - the Operator to Transform
	 * @param config
	 *            - the {@link TransformationConfiguration}, containing info
	 *            about the plan
	 * @param transformation
	 *            - the {@link ITransformation} used to transform this Operator
	 * @return the transformed physical Operator
	 */
	public TransformedPO transform(T logicalOperator, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException;

	/**
	 * The Priority of this Transformator. Transformators with a higher priority
	 * will be prefered above ones with a lower priority.
	 * 
	 * @return the priority as an int
	 */
	public int getPriority();
}
