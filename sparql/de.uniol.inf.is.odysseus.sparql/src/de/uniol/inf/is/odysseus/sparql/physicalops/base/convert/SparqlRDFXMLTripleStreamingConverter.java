package de.uniol.inf.is.odysseus.sparql.physicalops.base.convert;

import java.io.StringReader;
import java.util.LinkedList;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IClone;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.Container;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.metadata.IMetadataFactory;
import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.MetaTriple;

/**
 * This operator convertes timestamped Strings to timestamped triples
 * 
 * @author Marco Grawunder, Andre Bolles
 * 
 */
public class SparqlRDFXMLTripleStreamingConverter<M extends IClone>
		extends
		AbstractPipe<Container<String, M>, MetaTriple<M>> {

	/**
	 * the next elements to return
	 */
	private LinkedList<MetaTriple<M>> nextElems;
	
	int counter;

	private IMetadataFactory<M, Container<String, M>> mFac;


	public SparqlRDFXMLTripleStreamingConverter(
			SparqlRDFXMLTripleStreamingConverter converter) {
		this.nextElems = converter.nextElems;
		this.counter = converter.counter;
		this.mFac = converter.mFac;
	}

	public SparqlRDFXMLTripleStreamingConverter(IMetadataFactory<M, Container<String, M>> mFac) {
		this.nextElems = new LinkedList<MetaTriple<M>>();
		this.counter = 0;
		this.mFac = mFac;
	}

	@Override
	protected synchronized void process_next(Container<String, M> next, int port, boolean isReadOnly){
		MetaTriple<M> retVal = null;
			
			String page = next.getCargo();

			// TODO: Kann man das eventuell noch beschleunigen? Es muss ja
			// gar nicht zwingend das gesamte Dokument eingelesen werden,
			// bevor Tupel gelesen werden ...
			Model model = ModelFactory.createDefaultModel();
			model
					.setNsPrefix("iec61400-25",
							"http://www.iec.org/61400-25/");
			model.read(new StringReader(page), "RDF/XML");
			StmtIterator stmtIter = model.listStatements();
			String buffer = "";
			while (stmtIter.hasNext()) {
				Triple stmt_0 = stmtIter.nextStatement().asTriple();
				MetaTriple<M> stmt = new MetaTriple<M>(stmt_0.getSubject(), stmt_0
						.getPredicate(), stmt_0.getObject());

				buffer += stmt.toString() + "\n";
				stmt.setMetadata(this.mFac.createMetadata(next));
				//stmt.setMetadata(next.getMetadata());
				retVal = stmt;
				// PN-Ansatz
//					retVal = new TimestampedExchangeElement<Triple>(stmt, next.getValidity(), ElementType.POSITIVE, AccessAO.nextID());
				this.nextElems.addLast(retVal);
			}
		
			while(!this.nextElems.isEmpty()){
				this.transfer(this.nextElems.removeFirst());
			}
	}
	
	public void process_close(){
	}
}
