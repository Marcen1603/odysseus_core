package de.uniol.inf.is.odysseus.new_transformation.relational.transformators;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class MapAOTransformator implements IPOTransformator<MapAO> {
	@Override
	public boolean canExecute(MapAO logicalOperator, TransformationConfiguration config) {
		return config.getDataType() == "relational";
	}	

	@Override
	public TransformedPO transform(MapAO mapAO, TransformationConfiguration config, ITransformation transformation) throws TransformationException {

		RelationalMapPO<?> mapPO = new RelationalMapPO(mapAO.getInputSchema(), mapAO.getExpressions().toArray(
				new SDFExpression[0]));
		
		return new TransformedPO(mapPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("MapAO");
		return to;
	}
}
