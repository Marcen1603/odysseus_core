package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class XMLCOSEMParser extends AbstractCOSEMParser{

	public XMLCOSEMParser(InputStreamReader reader, SDFSchema sdfSchema) {
		super(reader, sdfSchema);
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init(InputStreamReader reader) {
		// TODO Auto-generated method stub
	}

}
