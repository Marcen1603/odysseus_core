package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

public class ViewSchema<T> {

	private SDFAttributeList outputSchema;
	private SDFMetaAttributeList metadataSchema;

	private List<IViewableAttribute<T>> viewableAttributes = new ArrayList<IViewableAttribute<T>>();

	private List<IViewableAttribute<T>> choosenAttributes = new ArrayList<IViewableAttribute<T>>();

	public ViewSchema(SDFAttributeList outputSchema, SDFMetaAttributeList metaSchema) {
		this.outputSchema = outputSchema;
		this.metadataSchema = metaSchema;

		init();
	}

	private void init() {
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute<T> attribute = new ViewableSDFAttribute<T>(a);
			if (isAllowedDataType(attribute.getSDFDatatype())) {
				viewableAttributes.add(attribute);
			}
		}
		this.choosenAttributes = new ArrayList<IViewableAttribute<T>>(viewableAttributes);

		for (SDFMetaAttribute m : this.metadataSchema) {
			for (Method method : m.getMetaAttributeClass().getMethods()) {
				IViewableAttribute<T> attribute = new ViewableMetaAttribute<T>(m, method);
				if (method.getParameterTypes().length==0 && isAllowedDataType(attribute.getSDFDatatype())) {
					viewableAttributes.add(new ViewableMetaAttribute<T>(m, method));
				}
			}
		}

	}

	public List<T> convertToViewableFormat(RelationalTuple<? extends IMetaAttribute> tuple) {
		List<T> values = new ArrayList<T>();
		for (int index = 0; index < this.viewableAttributes.size(); index++) {
			values.add(this.viewableAttributes.get(index).evaluate(index, tuple));
		}
		return values;
	}

	
	public List<T> convertToChoosenFormat(List<T> objects) {
		List<T> restricted = new ArrayList<T>();

		for (IViewableAttribute<?> viewable : this.viewableAttributes) {
			int index = this.choosenAttributes.indexOf(viewable);
			if (index >= 0) {			
				T value = objects.get(index);
				
				
				// TODO: noch "unsauber"
				if(value instanceof Integer){
					value = (T) new Double(((Integer)value).doubleValue());
				}else{
					if(value instanceof Long){
						value = (T) new Double(((Long)value).doubleValue());
					}
				}
				restricted.add(value);
			}
		}
		return restricted;
	}

	protected boolean isAllowedDataType(SDFDatatype datatype) {
		if (datatype.equals(SDFDatatypeFactory.getDatatype("Double")) || datatype.equals(SDFDatatypeFactory.getDatatype("Long"))
				|| datatype.equals(SDFDatatypeFactory.getDatatype("Integer")) || datatype.equals(SDFDatatypeFactory.getDatatype("StartTimestamp"))
				|| datatype.equals(SDFDatatypeFactory.getDatatype("EndTimestamp")) || datatype.equals(SDFDatatypeFactory.getDatatype("Timestamp"))
				|| datatype.equals(SDFDatatypeFactory.getDatatype("PointInTime"))) {
			return true;
		}
		return false;
	}

	public List<IViewableAttribute<T>> getChoosenAttributes() {
		return choosenAttributes;
	}

	public void setChoosenAttributes(List<IViewableAttribute<T>> choosenAttributes) {
		this.choosenAttributes = choosenAttributes;
	}

	public List<IViewableAttribute<T>> getViewableAttributes() {
		return viewableAttributes;
	}

}
