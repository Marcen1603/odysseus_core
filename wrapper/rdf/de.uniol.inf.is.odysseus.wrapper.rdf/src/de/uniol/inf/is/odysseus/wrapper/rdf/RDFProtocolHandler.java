package de.uniol.inf.is.odysseus.wrapper.rdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;

public class RDFProtocolHandler extends AbstractProtocolHandler<Triple<IMetaAttribute>> {

	public static final String NAME = "RDF";
	public static final String FORMAT = "rdf.format";
	
	
	final List<Triple<IMetaAttribute>> stored = new ArrayList<>();
	RDFFormat format;
	
	final RDFParser parser;
	final RDFHandlerBase statementHandler = new RDFHandlerBase(){
		public void handleStatement(org.openrdf.model.Statement st) throws org.openrdf.rio.RDFHandlerException {
			String subject = st.getSubject().stringValue();
			String predicate = st.getPredicate().stringValue();
			String object = st.getObject().stringValue();
			Triple<IMetaAttribute> t = new Triple<>(subject, predicate, object);
			stored.add(t);
			
		};
	};	
	
	public RDFProtocolHandler() {
		parser = null;
	}

	public RDFProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Triple<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		init_internal();
		parser = Rio.createParser(format);
		parser.setRDFHandler(statementHandler);
	}

	private void init_internal() {
		optionsMap.checkRequiredException(FORMAT);
		// Remark RDFFormat.valueOf seems not to work...
		String strFormat = optionsMap.get(FORMAT).toUpperCase();
		if (strFormat.equalsIgnoreCase("BINARY")){
			format = RDFFormat.BINARY;
		}else if (strFormat.equalsIgnoreCase("JSONLD")){
			format = RDFFormat.JSONLD;
		}else if (strFormat.equalsIgnoreCase("N3")){
			format = RDFFormat.N3;
		}else if (strFormat.equalsIgnoreCase("NQUADS")){
			format = RDFFormat.NQUADS;
		}else if (strFormat.equalsIgnoreCase("NTRIPLES")){
			format = RDFFormat.NTRIPLES;
		}else if (strFormat.equalsIgnoreCase("RDFA")){
			format = RDFFormat.RDFA;
		}else if (strFormat.equalsIgnoreCase("RDFJSON")){
			format = RDFFormat.RDFJSON;
		}else if (strFormat.equalsIgnoreCase("RDFXML")){
			format = RDFFormat.RDFXML;
		}else{
			throw new IllegalArgumentException("Not a valid rdf.format "+optionsMap.get(FORMAT));
		}
	}

	@Override
	public boolean hasNext() {
		try {
			return stored.size() > 0 || getTransportHandler().getInputStream().available() > 0;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Triple<IMetaAttribute> getNext() throws IOException {
		if (stored.size() > 0) {
			return stored.remove(0);
		}

		if (getTransportHandler().getInputStream().available() > 0) {
			readTriplesFromInputStream(getTransportHandler().getInputStream());
			if (stored.size() > 0) {
				return stored.remove(0);
			}
		}
		
		return null;
	}

	@Override
	public void process(InputStream message) {
		readTriplesFromInputStream(message);

		for (Triple<IMetaAttribute> t : stored) {
			getTransfer().transfer(t);
		}
	}

	private void readTriplesFromInputStream(InputStream message) {
	    try {
			parser.parse(message, "");
		} catch (RDFParseException | RDFHandlerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void write(Triple<IMetaAttribute> object) throws IOException {
		// TODO: How to write a triple .... --> Options I guess
	}

	@Override
	public IProtocolHandler<Triple<IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<Triple<IMetaAttribute>> dataHandler) {
		return new RDFProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

}

