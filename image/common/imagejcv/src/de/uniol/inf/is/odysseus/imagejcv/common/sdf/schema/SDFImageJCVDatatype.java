package de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Kristian Bruns
 */
public class SDFImageJCVDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3731348964335903793L;
	
	public SDFImageJCVDatatype (final String URI) {
		super(URI);
	}
	
	public SDFImageJCVDatatype (final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}
	
	public SDFImageJCVDatatype (final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema);
	}
	
	public static final SDFDatatype IMAGEJCV = new SDFDatatype("ImageJCV");
	
	public boolean isImage() {
		return this.getURI().equals(SDFImageJCVDatatype.IMAGEJCV.getURI());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean compatibleTo(final SDFDatatype other) {
		if (other instanceof SDFImageJCVDatatype) {
			final SDFImageJCVDatatype otherDatatype = (SDFImageJCVDatatype) other;
			if (this.isImage() && otherDatatype.isImage()) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}
	
	public static List<SDFDatatype> getTypes() {
		final List<SDFDatatype> types = new ArrayList<>();
		types.addAll(SDFDatatype.getTypes());
		types.add(SDFImageJCVDatatype.IMAGEJCV);
		
		return types;
	}
}
