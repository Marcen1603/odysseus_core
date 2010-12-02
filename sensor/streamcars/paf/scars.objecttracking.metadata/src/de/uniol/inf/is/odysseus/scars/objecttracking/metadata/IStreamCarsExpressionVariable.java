package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IStreamCarsExpressionVariable {

	public IStreamCarsExpression getParent();

	public String getName();

	public String getSourceName();

	public String getMetadataInfo();

	public Object getValue();

	public double getDoubleValue();

	public int[] getPath();

	public int[] getPath(boolean copy);

	public void init(SDFAttributeList schema);

	public boolean isSchemaVariable();

	public boolean isSchemaVariable(SDFAttributeList schema);

	public boolean isVirtual();

	public boolean hasMetadataInfo();

	public boolean isInList(int[] pathToList);

	public boolean isInList(TupleIndexPath pathToList);

	public SchemaIndexPath getSchemaIndexPath();

	public void replaceVaryingIndex(int index);

	public void replaceVaryingIndex(int index, boolean copy);

	public void bind(Object value);

	public void bindTupleValue(MVRelationalTuple<?> tuple);

	public String getNameWithoutMetadata();



}
