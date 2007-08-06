package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;
import java.util.Collection;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberInPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFStringCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFStringInPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFStringPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFExpression;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFQueryVoc;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFSchema;
import mg.dynaquest.sourcedescription.sdm.SourceDescriptionManager;
import mg.dynaquest.support.RDFHelper;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class SDFQueryFactory {
    
    static Logger logger = Logger.getLogger(SDFQueryFactory.class);

    /**
	 * @uml.property  name="sdm"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SourceDescriptionManager sdm = null;


	private WeightedAttribute readWeightedAttribute(Resource r)
			throws Exception {
		SDFAttribute attribute = null;
		float weighting = -1;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFQueryVoc.concernsAttribute)) {
				//System.out.println(object.toString());
				attribute = sdm.getAttribute(object.toString());
				if (attribute == null) {
					System.out
							.println("Attribut nicht im SDM gefunden Exception");
					System.out.println("ToDo");
				}
			}
			if (predicate.getURI().equals(SDFQueryVoc.weighting)) {
				//System.out.println(object.toString());
				weighting = SDFImportanceLevel.getLevel(object.toString());
			}
		}
		return new WeightedAttribute(attribute, weighting);
	}

	private SDFStringPredicate readStringPredicate(Resource r) throws Exception {
		SDFAttribute attribute = null;
		SDFStringCompareOperator compareOP = null;
		SDFStringConstant value = null;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFPredicates.predAttribute)) {
				//System.out.println(object.toString());
				attribute = sdm.getAttribute(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predStringOperator)) {
				//System.out.println(object.toString());
				compareOP = (SDFStringCompareOperator) SDFCompareOperatorFactory
						.getCompareOperator(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predValue)) {
				//System.out.println(object.toString());
				value = new SDFStringConstant("",object.toString());
			}
		}

		return new SDFStringPredicate(r.getURI(), attribute, compareOP, value);
	}

	private SDFConstantList readConstantSet(Resource r) throws Exception {
		// Anm.: Wenn der Anwender einer Konstante verwendet, dann muss
		// diese im globalen Schema vorkommen (damit ein Mapping möglich ist)
		SDFConstantList set = new SDFConstantList(r.getURI());
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFSchema.hasConstant)) {
				//System.out.println(object.toString());
				set.add(sdm.getConstant(object.toString()));
			}
		}
		return set;
	}

	private SDFStringInPredicate readStringInPredicate(Resource r)
			throws Exception {
		SDFAttribute attribute = null;
		SDFConstantList set = null;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFPredicates.predAttribute)) {
				//System.out.println(object.toString());
				attribute = sdm.getAttribute(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predConstantSet)) {
				//System.out.println(object.toString());
				set = readConstantSet((Resource) object);
			}
		}

		return new SDFStringInPredicate(r.getURI(), attribute, set);
	}

	private SDFNumberPredicate readNumberPredicate(Resource r) throws Exception {
		SDFAttribute attribute = null;
		SDFNumberCompareOperator compareOP = null;
		SDFExpression value = null;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFPredicates.predAttribute)) {
				//System.out.println(object.toString());
				attribute = sdm.getAttribute(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predNumberOperator)) {
				//System.out.println(object.toString());
				compareOP = (SDFNumberCompareOperator) SDFCompareOperatorFactory
						.getCompareOperator(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predValue)) {
				//System.out.println(object.toString());
				value = new SDFExpression("",object.toString());
			}
		}

		return new SDFNumberPredicate(r.getURI(), attribute, compareOP, value);
	}

	private SDFNumberInPredicate readNumberInPredicate(Resource r)
			throws Exception {
		SDFAttribute attribute = null;
		SDFConstantList set = null;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFPredicates.predAttribute)) {
				//System.out.println(object.toString());
				attribute = sdm.getAttribute(object.toString());
			}
			if (predicate.getURI().equals(SDFPredicates.predConstantSet)) {
				//System.out.println(object.toString());
				set = readConstantSet((Resource) object);
			}
		}

		return new SDFNumberInPredicate(r.getURI(), attribute, set);
	}

	private SDFSimplePredicate readSimplePredicate(Resource r) throws Exception {
		SDFSimplePredicate sPred = null;

		StmtIterator iter = r.listProperties(RDF.type);
		//StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
//			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			//System.out.println(object.toString());
			if (object.toString().equals(SDFPredicates.StringPredicate)) {
				sPred = readStringPredicate(r);
			}
			if (object.toString().equals(SDFPredicates.NumberPredicate)) {
				sPred = readNumberPredicate(r);
			}
			if (object.toString().equals(SDFPredicates.StringInPredicate)) {
				sPred = readStringInPredicate(r);
			}
			if (object.toString().equals(SDFPredicates.NumberInPredicate)) {
				sPred = readNumberInPredicate(r);
			}
		}
		return sPred;
	}

	private WeightedSimplePredicate readQueryPredicate(Resource r)
			throws Exception {
		SDFSimplePredicate sPred = null;
		float weighting = -1;
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (predicate.getURI().equals(SDFQueryVoc.hasPredicate)) {
				//System.out.println(object.toString());
				sPred = readSimplePredicate((Resource) object);
			}
			if (predicate.getURI().equals(SDFQueryVoc.weighting)) {
				//System.out.println(object.toString());
				weighting = SDFImportanceLevel.getLevel(object.toString());
			}
		}
		return new WeightedSimplePredicate(sPred, weighting);
	}

	private SDFQuery readQuery(Resource r) throws Exception {
		SDFQuery query = new SDFQuery(r.getURI());
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
//			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object

			if (SDFQueryVoc.hasQueryAttribute.equals(predicate.getURI())) {
				// Attribute verarbeiten
				WeightedAttribute wattrib = readWeightedAttribute((Resource) object);
				query.addWAttribute(wattrib);
			}
			if (SDFQueryVoc.hasQueryPredicate.equals(predicate.getURI())) {
				query.addWSimplePredicate(readQueryPredicate((Resource) object));
			}
			if (SDFQueryVoc.respectQuality.equals(predicate.getURI())) {
				// Qualitätsanforderungen verarbeiten
			}
			if (SDFQueryVoc.respectUserQualityRating.equals(predicate.getURI())) {
				// QualitätsEinschätzungen des Nutzers bzgl. eines
				// Qualitätsattributs verarbeiten
			}
			if (SDFQueryVoc.respectUserSourceRating.equals(predicate.getURI())) {
				// QualitätsEinschätzungen des Nutzers bzgl. einer Quelle
				// verarbeiten
			}
			if (SDFQueryVoc.orderBy.equals(predicate.getURI())){
				SDFAttributeList orderAttribs = readOrderAttributes((Resource) object);
				boolean oderAsc = readOrderAsc((Resource) object);
				query.setOrderBy(orderAttribs, oderAsc);
			}

		}
		return query;
	}

	
	private SDFAttributeList readOrderAttributes(Resource r) throws Exception {
		SDFAttributeList attributes = new SDFAttributeList(); 
		
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			if (SDFQueryVoc.hasOrderAttribute.equals(predicate.getURI())) {
				//System.out.println(object.toString());
				attributes.add(sdm.getAttribute(object.toString()));
			}
		}
		return attributes;
	}

	private boolean readOrderAsc(Resource r) throws Exception {
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			if (SDFQueryVoc.hasSortOrder.equals(predicate.getURI())) {
				return object.toString().equals(SDFQueryVoc.SortOrderAsc);
			}
		}
		// Default ist aufsteigende Sortierung
		return true; 
	}

	
	public Collection<SDFQuery> readQuerys(String queryURI)
			throws Exception {
		Collection<SDFQuery> ret = new ArrayList<SDFQuery>();
		Model queryModel = RDFHelper.readModel(queryURI);
		ArrayList nodeList = RDFHelper.getNodesWithRDFType(queryModel, SDFQueryVoc.Query);
		for (int i = 0; i < nodeList.size(); i++) {
			ret.add(readQuery((Resource) nodeList.get(i)));
		}
		return ret;
	}

	public SDFQueryFactory(SourceDescriptionManager sdm) {
		this.sdm = sdm;
	}


}