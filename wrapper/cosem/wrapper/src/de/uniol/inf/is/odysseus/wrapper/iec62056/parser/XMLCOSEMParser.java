package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.InputStreamReader;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class XMLCOSEMParser extends AbstractCOSEMParser<String>{

	public XMLCOSEMParser(InputStreamReader reader, SDFSchema sdfSchema) {
		super(reader, sdfSchema);
	}

	@Override
	public Iterator<String> process() {
		// TODO Auto-generated method stub
		return null;
	}

}
