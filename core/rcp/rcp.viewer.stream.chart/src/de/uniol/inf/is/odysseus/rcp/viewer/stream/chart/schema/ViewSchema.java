package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

public class ViewSchema<T> {

	private SDFAttributeList outputSchema;
	private SDFMetaAttributeList metadataSchema;

	private List<IViewableAttribute> viewableAttributes = new ArrayList<IViewableAttribute>();

	private List<IViewableAttribute> choosenAttributes = new ArrayList<IViewableAttribute>();

	public ViewSchema(SDFAttributeList outputSchema, SDFMetaAttributeList metaSchema) {
		this.outputSchema = outputSchema;
		this.metadataSchema = metaSchema;

		init();
	}

	private void init() {
		
		for (SDFAttribute a : this.outputSchema) {
			IViewableAttribute attribute = new ViewableSDFAttribute(a);
			if (isAllowedDataType(attribute.getSDFDatatype())) {
				viewableAttributes.add(attribute);
			}
		}
		this.choosenAttributes = new ArrayList<IViewableAttribute>(viewableAttributes);

		for (SDFMetaAttribute m : this.metadataSchema) {
			for (Method method : m.getMetaAttributeClass().getMethods()) {
				if (!method.getName().endsWith("hashCode")) {
					IViewableAttribute attribute = new ViewableMetaAttribute(m, method);
					if (method.getParameterTypes().length == 0 && isAllowedDataType(attribute.getSDFDatatype())) {
						viewableAttributes.add(new ViewableMetaAttribute(m, method));
					}
				}
			}
		}
		
		

	}

	private boolean isAllowedDataType(SDFDatatype sdfDatatype) {
		return ViewableDatatypeRegistry.getInstance().isAllowedDataType(sdfDatatype);
	}

	@SuppressWarnings("unchecked")
	public List<T> convertToViewableFormat(RelationalTuple<? extends IMetaAttribute> tuple) {
		List<T> values = new ArrayList<T>();
		for (int index = 0; index < this.viewableAttributes.size(); index++) {
			IViewableAttribute viewable = this.viewableAttributes.get(index);
			
			Object value = viewable.evaluate(index, tuple);
			IViewableDatatype<?> converter = ViewableDatatypeRegistry.getInstance().getConverter(viewable.getSDFDatatype());			
			values.add((T)converter.convertToValue(value));
		}
		return values;
	}

	public List<T> convertToChoosenFormat(List<T> objects) {
		List<T> restricted = new ArrayList<T>();

		for (IViewableAttribute viewable : this.choosenAttributes) {
			int index = this.viewableAttributes.indexOf(viewable);
			if (index >= 0) {
				T value = objects.get(index);				
				restricted.add(value);
			}
		}
		return restricted;
	}

	

	public List<IViewableAttribute> getChoosenAttributes() {
		return choosenAttributes;
	}

	public void setChoosenAttributes(List<IViewableAttribute> choosenAttributes) {
		this.choosenAttributes = choosenAttributes;
	}

	public List<IViewableAttribute> getViewableAttributes() {
		return viewableAttributes;
	}

}
