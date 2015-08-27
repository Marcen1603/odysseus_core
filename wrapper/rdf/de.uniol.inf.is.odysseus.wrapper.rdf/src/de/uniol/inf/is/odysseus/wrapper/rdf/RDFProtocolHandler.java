package de.uniol.inf.is.odysseus.wrapper.rdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.rdf.datamodel.INode;
import de.uniol.inf.is.odysseus.rdf.datamodel.Literal;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;

public class RDFProtocolHandler extends AbstractProtocolHandler<Triple<IMetaAttribute>> {

	final List<Triple<IMetaAttribute>> stored = new ArrayList<>();

	public RDFProtocolHandler() {
	}

	public RDFProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Triple<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		init_internal();
	}

	private void init_internal() {
		// TODO: Parameter

	}

	@Override
	public boolean hasNext() throws IOException {
		return stored.size() > 0 || getTransportHandler().getInputStream().available() > 0;
	}

	@Override
	public Triple<IMetaAttribute> getNext() throws IOException {
		if (stored.size() > 0) {
			return stored.remove(0);
		}

		if (getTransportHandler().getInputStream().available() > 0) {
			List<Triple<IMetaAttribute>> result = readTriplesFromInputStream(getTransportHandler().getInputStream());
			stored.addAll(result);
			if (stored.size() > 0) {
				return stored.remove(0);
			}
		}
		
		return null;
	}

	@Override
	public void process(InputStream message) {
		List<Triple<IMetaAttribute>> result = readTriplesFromInputStream(message);

		for (Triple<IMetaAttribute> t : result) {
			getTransfer().transfer(t);
		}
	}

	private List<Triple<IMetaAttribute>> readTriplesFromInputStream(InputStream message) {
		List<Triple<IMetaAttribute>> result = new ArrayList<>();
		Model model = ModelFactory.createDefaultModel();
		model.read(message, null);
		StmtIterator stmts = model.listStatements();
		while (stmts.hasNext()) {
			Statement stmt = stmts.next();
			INode subject = new Literal(stmt.getSubject().getURI());
			INode predicate = new Literal(stmt.getPredicate().getURI());
			;
			INode object = new Literal(stmt.getObject().toString());

			Triple<IMetaAttribute> t = new Triple<>(subject, predicate, object);
			result.add(t);
		}
		return result;
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
		return "RDF";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

}
