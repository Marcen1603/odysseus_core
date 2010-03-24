package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.QueryAccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPartition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class CreateAccessAOVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;

	private Map<SDFSource, AccessAO> sources;

	public CreateAccessAOVisitor() {
		super();
		init();
	}

	public final void init() {
		this.attributeResolver = new AttributeResolver();
		this.sources = new HashMap<SDFSource, AccessAO>();
	}

	public AttributeResolver getAttributeResolver() {
		return this.attributeResolver;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) {
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();
		SDFSource source = DataDictionary.getInstance().getSource(sourceString);
		if (source.getSourceType().equals("RelationalStreaming")) {
			relationalStreamingSource(node, source, sourceString);
			return null;
		} else if (source.getSourceType().equals("brokerStreaming")) {
			brokerStreamingSource(node, data);
			return null;
		} else {
			throw new RuntimeException("unknown type of source '"
					+ source.getSourceType() + "' for source: " + sourceString);
		}

		// case Relational:
		// relationalSource(node, source, sourceString);
		// return null;
		// case OSGI:
		// WrapperArchitectureAO access = (WrapperArchitectureAO)
		// sources.get(source);
		// this.attributeResolver.addSource(node.getAlias(), access);
		// return null;
		// default:

	}

	// FIXME funktioniert nicht mehr, da datadictionary nicht mehr auf db basis
	// arbeitet
	// und noch keine neue repraesentation von relationsinfos da ist
	@SuppressWarnings("unused")
	private void relationalSource(ASTSimpleSource node, SDFSource source,
			String sourceString) {
		QueryAccessAO access = (QueryAccessAO) this.sources.get(source);
		if (access == null) {
			access = new QueryAccessAO(source);
			// SDFAttributeList attributes = DataDictionary.getInstance()
			// .attributesOfRelation(sourceString);
			// access.setOutputSchema(attributes);
			this.sources.put(source, access);
		}

		AbstractLogicalOperator operator = access;
		if (node.hasAlias()) {
			sourceString = node.getAlias();
			operator = new RenameAO();
			SDFAttributeList newSchema = createAliasSchema(node.getAlias(),
					access);
			operator.subscribeToSource(access, 0, 0, newSchema);
		}

		this.attributeResolver.addSource(sourceString, operator);
	}

	private void relationalStreamingSource(ASTSimpleSource node,
			SDFSource source, String sourceName) {
		AccessAO access = this.sources.get(source);
		if (access == null) {
			access = new AccessAO(source);
			SDFEntity entity = null;
			try {
				entity = DataDictionary.getInstance().getEntity(sourceName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			access.setOutputSchema(entity.getAttributes());

			this.sources.put(source, access);
		}

		AbstractLogicalOperator inputOp = access;
		if (node.hasAlias()) {
			inputOp = new RenameAO();
			inputOp.subscribeToSource(access, 0, 0, access.getOutputSchema());
			((RenameAO) inputOp).setOutputSchema(createAliasSchema(node
					.getAlias(), access));
			sourceName = node.getAlias();
		}

		if (node.hasWindow()) {
			WindowAO window = createWindow(node.getWindow(), inputOp);
			inputOp = window;
		}
		this.attributeResolver.addSource(sourceName, inputOp);
	}

	private WindowAO createWindow(ASTWindow windowNode,
			AbstractLogicalOperator inputOp) {
		WindowAO window = new WindowAO();
		window.subscribeToSource(inputOp, 0, 0, inputOp.getOutputSchema());

		if (windowNode.isPartitioned()) {
//			if (containsWindow(inputOp)) {
//				throw new IllegalArgumentException(
//						"redefinition of window in subselect");
//			}
			ASTPartition partition = windowNode.getPartition();
			ArrayList<SDFAttribute> partitionAttributes = new ArrayList<SDFAttribute>();
			for (int i = 0; i < partition.jjtGetNumChildren(); ++i) {
				String attributeName = partition.jjtGetChild(i).toString();
				boolean found = false;
				for (SDFAttribute curAttribute : inputOp.getOutputSchema()) {
					if (curAttribute.getPointURI().equals(attributeName)) {
						partitionAttributes.add(curAttribute);
						found = true;
						break;
					}
				}
				if (!found) {
					throw new IllegalArgumentException(
							"invalid partioning attribute");
				}
			}
			window.setPartitionBy(partitionAttributes);
		}

		window.setWindowType(windowNode.getType());
		ASTIdentifier onId = windowNode.getOn();
		if (onId != null) {
			AttributeResolver tmpResolver = new AttributeResolver();
			for (SDFAttribute attribute : inputOp.getOutputSchema()) {
				tmpResolver.addAttribute((SDFAttribute) attribute);
			}
			SDFAttribute onAttribute = tmpResolver.getAttribute(onId.getName());
			if (onAttribute == null) {
				throw new RuntimeException("invalid attribute in ON: "
						+ onId.getName());
			}
			window.setWindowOn(onAttribute);
		}
		if (!windowNode.isUnbounded()) {
			window.setWindowSize(windowNode.getSize());
			Long advance = windowNode.getAdvance();
			window.setWindowAdvance(advance != null ? advance : 1);
			if (windowNode.getSlide() != null) {
				window.setWindowSlide(windowNode.getSlide());
			}
		}
		return window;
	}

	private boolean containsWindow(ILogicalOperator inputOp) {
		if (inputOp instanceof WindowAO) {
			return true;
		}
		int numberOfInputs = inputOp.getSubscribedToSource().size();
		if (inputOp instanceof ExistenceAO) {
			numberOfInputs = 1;// don't check subselects in existenceaos
		}
		for (int i = 0; i < numberOfInputs; ++i) {
			if (containsWindow(inputOp.getSubscribedToSource(i).getTarget())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {
		ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node
				.jjtGetChild(0);
		CQLParser v = new CQLParser();
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(
				childNode, null);

		Node asNode = node.jjtGetChild(1);
		if (asNode instanceof ASTWindow) {
			WindowAO window = createWindow((ASTWindow) asNode, result);
			result = window;
			asNode = node.jjtGetChild(2);
		}
		ASTIdentifier asIdentifier = (ASTIdentifier) asNode.jjtGetChild(0);
		RenameAO rename = new RenameAO();
		rename.subscribeToSource(result, 0, 0, result.getOutputSchema());
		rename
				.setOutputSchema(createAliasSchema(asIdentifier.getName(),
						result));
		this.attributeResolver.addSource(asIdentifier.getName(), rename);
		return null;
	}

	private SDFAttributeList createAliasSchema(String alias,
			ILogicalOperator access) {
		SDFAttributeList attributes = new SDFAttributeList();
		for (SDFAttribute attribute : access.getOutputSchema()) {
			SDFAttribute newAttribute = (SDFAttribute) attribute.clone();
			newAttribute.setSourceName(alias);
			attributes.add(newAttribute);
		}
		return attributes;
	}

	@Override
	public Object visit(ASTDBSelectStatement node, Object data) {
		Class<?> dbClass;
		try {
			dbClass = Class
					.forName("de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateDatabaseAOVisitor");
			IDatabaseAOVisitor dbVisitor = (IDatabaseAOVisitor) dbClass
					.newInstance();
			AbstractLogicalOperator dbOp = (AbstractLogicalOperator) dbVisitor
					.visit(node, data);
			this.attributeResolver.addSource(dbVisitor.getAlias(), dbOp);

		} catch (Exception e) {
			throw new RuntimeException("missing database plugin for cql parser");
		}
		return null;
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) {
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTBrokerSource.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);
			Node child = node.jjtGetChild(0);
			ASTIdentifier ident = (ASTIdentifier) child.jjtGetChild(child.jjtGetNumChildren()-1);
			String name = ident.getName();
			this.attributeResolver.addSource(name, sourceOp);			
		}catch (ClassNotFoundException ex){
			throw new RuntimeException("Brokerplugin is missing in CQL parser.", ex.getCause());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while accessing broker as source.",e.getCause());
		}

		return null;
	}
		
	private Object brokerStreamingSource(ASTSimpleSource node, Object data) {
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTSimpleSource.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);

			ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
			String name = ident.getName();
			this.attributeResolver.addSource(name, sourceOp);			
		}catch (ClassNotFoundException ex){
			throw new RuntimeException("Brokerplugin is missing in CQL parser.", ex.getCause());
		}catch (Exception e) {
			throw new RuntimeException("Error while creating broker as source.",e.getCause());
		}

		return null;
	}
}
