package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class NewFilenamePunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = 6424816957991015360L;
	final private String filename;
	final static byte NUMBER = 2; 
	
	public static final SDFSchema schema;
	
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("NewFilenamePunctuation", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("NewFilenamePunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("NewFilenamePunctuation", "filename", SDFDatatype.STRING));
		schema = SDFSchemaFactory.createNewSchema("NewFilenamePunctuation", Tuple.class, attributes);
	}


	public NewFilenamePunctuation(PointInTime p, String filename) {
		super(p);
		this.filename = filename;
	}
	
	public NewFilenamePunctuation(Tuple<?> input){
		super(new PointInTime((long)input.getAttribute(1)));
		this.filename = input.getAttribute(2);
	}

	public String getFilename() {
		return filename;
	}
	
	@Override
	public AbstractPunctuation clone() {
		return new NewFilenamePunctuation(getTime(),filename);
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new NewFilenamePunctuation(newTime, filename);
	}
	
	@Override
	public String toString() {
		return super.toString()+" "+filename;
	}

	@Override
	public byte getNumber() {
		return NUMBER;
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple(3, false);
		ret.setAttribute(0, NUMBER);
		ret.setAttribute(1, getTime().getMainPoint());
		ret.setAttribute(2, filename);
		return ret;
	}
	
	static public NewFilenamePunctuation createNewInstance(Tuple<?> input) {
		return new NewFilenamePunctuation(input);
	}
}
