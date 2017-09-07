package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.context.IODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory
import org.eclipse.xtext.common.types.JvmMember
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates
import java.util.List
import java.util.ArrayList
import de.uniol.inf.is.odysseus.core.predicate.IPredicate
import java.util.Map
import java.util.HashMap
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLPO
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAORule
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute

class ODLCompiler extends AbstractIQLCompiler<IODLCompilerHelper, IODLGeneratorContext, IODLTypeCompiler, IODLStatementCompiler, IODLTypeDictionary, IODLTypeUtils> implements IODLCompiler{
	
	@Inject
	private IODLMetadataAnnotationCompiler metadataAnnotationCompiler
	
	@Inject
	private IODLLookUp lookUp;
	
	@Inject
	new(IODLCompilerHelper helper, IODLTypeCompiler typeCompiler, IODLStatementCompiler stmtCompiler, IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(helper, typeCompiler, stmtCompiler, typeDictionary, typeUtils)
	}
	
	override String compileAO(ODLOperator o, IODLGeneratorContext context) {
		context.ao = true	
		var builder = new StringBuilder()
		builder.append(compileAOIntern(o, context))
		context.addImport(AbstractLogicalOperator.canonicalName)	
		context.addImport(LogicalOperatorCategory.canonicalName)	
		context.addImport(LogicalOperator.canonicalName)	
		context.addImport(Parameter.canonicalName)	

		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	override String compilePO(ODLOperator o, IODLGeneratorContext context) {	
		var builder = new StringBuilder()
		var element = o.eContainer as IQLModelElement;
		builder.append(compilePOIntern(element, o, context))
			
			
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	override String compileAORule(ODLOperator o, IODLGeneratorContext context) {	
		var builder = new StringBuilder()
		builder.append(compileAORuleIntern(o, context))
		context.addImport(AbstractTransformationRule.canonicalName)
		context.addImport(IRuleFlowGroup.canonicalName)	
		context.addImport(TransformRuleFlowGroup.canonicalName)	
		context.addImport(TransformationConfiguration.canonicalName)	
		context.addImport(IODLAORule.canonicalName)	
			
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	
	def String compileAOIntern(ODLOperator o, IODLGeneratorContext context) {	
		var opName = o.simpleName+IODLCompilerHelper.AO_OPERATOR
		var superClass = AbstractLogicalOperator.simpleName;
		var members = helper.getAOMembers(o);	
		var attributes = helper.getAOAttributes(o);	
		var parameters = helper.getParameters(o);
		var operatorValidate = helper.hasOperatorValidate(o);
		var parametersToValidate = new ArrayList();
		for (p : parameters) {
			if (helper.hasValidateMethod(o, p)) {
				parametersToValidate.add(p);
			}
		}
		
		var hasPredicate = helper.hasPredicate(o);
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
		
		var predicates = helper.getPredicates(o);
		var predicateArrays = helper.getPredicateArrays(o);

		context.addImport(List.canonicalName)
		context.addImport(ArrayList.canonicalName)
		context.addImport(Map.canonicalName)
		context.addImport(HashMap.canonicalName)
		context.addImport(IODLAO.canonicalName)

		if (hasPredicate) {
			context.addImport(IHasPredicates.canonicalName)			
		}
		'''
		
		@SuppressWarnings("all")
		@«LogicalOperator.simpleName»(«metadataAnnotationCompiler.getAOAnnotationElements(o, context)»)
		public class «opName» extends «superClass» implements «IODLAO.simpleName»«IF hasPredicate», «IHasPredicates.simpleName»«ENDIF» {
						
			private «Map.simpleName»<«String.simpleName», «List.simpleName»<«Object.simpleName»>> metadata = new «HashMap.simpleName»<>();
			
			public «opName»() {
				super();
			}
			
			public «opName»(«opName» ao) {
				super(ao);
				«FOR attr : attributes»
					«createCloneStatements(attr, "ao", context)»
				«ENDFOR»
			}
			
			«FOR member : members»
				«compile(member as JvmMember, context)»
			«ENDFOR»
			
			«FOR p : parameters»
				«var pName = helper.firstCharUpperCase(p.simpleName)»
				«var type = typeCompiler.compile(p.type, context, false)»
				@«Parameter.simpleName»(«metadataAnnotationCompiler.getParameterAnnotationElements(p, context)»)
				public void set«pName»(«type» «p.simpleName») {
					this.«p.simpleName» = «p.simpleName»;
				}
			«ENDFOR»
			
			«FOR attr : attributes»
				«var attrName = helper.firstCharUpperCase(attr.simpleName)»
				«var type = typeCompiler.compile(attr.type, context, false)»
				public «type» get«attrName»() {
					return this.«attr.simpleName»;
				}
			«ENDFOR»
			
			«IF operatorValidate ||  parametersToValidate.size > 0»
			@Override
			public boolean isValid() {
				return super.isValid()«IF operatorValidate» && validate()«ENDIF»«IF parametersToValidate.size > 0» && validateParameters()«ENDIF»;
			}
			«ENDIF»
			
			«IF parametersToValidate.size > 0»
			protected boolean validateParameters() {
				return «parametersToValidate.map[p | "this.validate"+p.simpleName+"()"].join("&&")»;
			}
			«ENDIF»
			
			@Override
			public «superClass» clone() {
				return new «opName»(this);
			}
			
			«IF hasPredicate»
			@Override
			public «List.simpleName»<IPredicate<?>> getPredicates() {
				«List.simpleName» result = new «ArrayList.simpleName»<>();
				«FOR pred : predicates»
				result.add(this.«pred»);
				«ENDFOR»
				«FOR pred : predicateArrays»
				for («IPredicate.simpleName» p : «pred») {
					result.add(p);	
				}
				«ENDFOR»
				return result;
			}
			«ENDIF»
			«FOR e : newExpressions»
				«IF e.argsMap !== null && e.argsMap.elements.size > 0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : attributes»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
					«createGetterMethod(a.type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : varStmts»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
					«var decl = a.^var as IQLVariableDeclaration»
					«var type = decl.ref»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			@Override
			public «Map.simpleName»<«String.simpleName», «List.simpleName»<«Object.simpleName»>> getMetadata() {
				return metadata;
			}

			@Override
			public void addMetadata(«String.simpleName» key, «Object.simpleName» value) {
				«List.simpleName»<«Object.simpleName»> valueList = metadata.get(key);
				if (valueList == null) {
					valueList = new «ArrayList.simpleName»<>();
				}
				valueList.add(value);
			}
		}
		'''
	}	
	
	def String compileAORuleIntern(ODLOperator o, IODLGeneratorContext context) {	
		var name = o.simpleName+IODLCompilerHelper.AO_RULE_OPERATOR
		var superClass = AbstractTransformationRule.simpleName;
		var aoName = o.simpleName+IODLCompilerHelper.AO_OPERATOR
		var poName = o.simpleName+IODLCompilerHelper.PO_OPERATOR		
		var transform = TransformationConfiguration.simpleName
		'''
		
		@SuppressWarnings("all")
		public class «name» extends «superClass»<«aoName»> implements «IODLAORule.simpleName»<«aoName»> {
			
			@Override
			public void execute(«aoName» operator, «transform» config) {
				defaultExecute(operator, new «poName»(operator), config, true, true);
			}
			
			
			@Override
			public boolean isExecutable(«aoName» operator,TransformationConfiguration config) {
				return operator.isAllPhysicalInputSet();
			}

			@Override
			public IRuleFlowGroup getRuleFlowGroup() {
				return TransformRuleFlowGroup.TRANSFORMATION;
			}
		}
		'''
	}

	def String compilePOIntern(IQLModelElement element, ODLOperator o, IODLGeneratorContext context) {
		var opName = o.simpleName+IODLCompilerHelper.PO_OPERATOR
		var aoName = o.simpleName+IODLCompilerHelper.AO_OPERATOR	
		var superClass = AbstractPipe	
		var read = helper.determineReadType(o)
		var write = read
		var meta = helper.determineMetadataType(o)		
		var members = helper.getPOMembers(o);		
		var attributes = helper.getPOAttributes(o)	
		var aoAttributes = helper.getAOAndPOAttributes(o)				
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
		var outputmode = helper.determineOutputMode(o);
		var hasProcessNext = helper.hasProcessNext(o);
		var hasProcessPunctuation = helper.hasProcessPunctuation(o);
		var hasInit = helper.hasPOInitMethod(o);
		
		context.addImport(superClass.canonicalName)
		context.addImport(outputmode.class.canonicalName)		
		context.addImport(IODLPO.canonicalName)		
		context.addImport(IPunctuation.canonicalName)		
		
		'''

		«FOR j : element.javametadata»
		«var text = j.java.text»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «opName» extends «superClass.simpleName»<«typeCompiler.compile(read, context, false)»<«typeCompiler.compile(meta, context, false)»>,«typeCompiler.compile(write, context, false)»<«typeCompiler.compile(meta, context, false)»>> implements «IODLPO.simpleName»<«typeCompiler.compile(read, context, false)»<«typeCompiler.compile(meta, context, false)»>,«typeCompiler.compile(write, context, false)»<«typeCompiler.compile(meta, context, false)»>> {
			
			public «opName»() {
				super();				
			}
			
			public «opName»(«opName» po) {
				super(po);
				«FOR attr : attributes»
					«createCloneStatements(attr, "po", context)»
				«ENDFOR»			
			}
			
			public «opName»(«aoName» ao) {
				«FOR attr : aoAttributes»
					«createCloneStatements(attr, "ao", context)»
				«ENDFOR»	
				«IF hasInit»
					initialize();
				«ENDIF»			
			}
			
			«FOR m : members»	
			«compile(m, context)»
			«ENDFOR»
			
			@«Override.simpleName»
			public «OutputMode.simpleName» getOutputMode() {
				return «OutputMode.simpleName».«outputmode.toString»;
			}
			
			«FOR attr : attributes»
				«var pName = helper.firstCharUpperCase(attr.simpleName)»
				«var type = typeCompiler.compile(attr.type, context, false)»
				public «type» get«pName»() {
					return this.«attr.simpleName»;
				}
			«ENDFOR»
			
			«IF !hasProcessPunctuation»
			@Override
			public void processPunctuation(«IPunctuation.simpleName» punctuation, int port) {
				sendPunctuation(punctuation);
			}
			
			«ENDIF»
			
			«IF !hasProcessNext»
			@Override
			protected void process_next(«typeCompiler.compile(read, context, false)» object, int port) {
				transfer(object);
			}
			
			«ENDIF»
			
			«FOR e : newExpressions»
				«IF e.argsMap !== null && e.argsMap.elements.size > 0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : attributes»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
					«createGetterMethod(a.type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
		
			«FOR a : varStmts»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
					«var decl = a.^var as IQLVariableDeclaration»
					«var type = decl.ref»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
		}
		'''
	}	
	
	override String compile(JvmMember m, IODLGeneratorContext context) {
		if (m instanceof ODLParameter) {
			compile(m as ODLParameter, context);
		} else if (m instanceof ODLMethod) {
			compile(m as ODLMethod, context);			
		} else {
			super.compile(m, context);
		}
	}
	
	def String compile(ODLParameter a, IODLGeneratorContext context) {
		'''
		private «typeCompiler.compile(a.type, context, false)» «a.simpleName»«IF a.init !== null» = «stmtCompiler.compile(a.init, a.type, context)»«ENDIF»;
		
		'''
	}

	
	def String compile(ODLMethod m,IODLGeneratorContext context) {
		if (m.validate && m.simpleName !== null) {
			'''
			private boolean validate«m.simpleName»()
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} else if (m.validate && m.simpleName === null) {
			'''
			protected boolean validate()
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} else if (m.on){
			var className = helper.getClassName(m);
			var returnT = "";
			var eventMethod = EventMethodsFactory.getInstance.getEventMethod(context.ao, m.simpleName, m.parameters);
			if (m.returnType !== null && !m.simpleName.equalsIgnoreCase(className)) {
				returnT = typeCompiler.compile(m.returnType, context, false)
			} else if (m.returnType === null && !m.simpleName.equalsIgnoreCase(className)) {
				returnT = "void"
			}
			'''
			«IF eventMethod.isOverride»
			@Override
			«ENDIF»
			public «returnT» «eventMethod.methodName»(«IF m.parameters !== null»«m.parameters.map[p | compile(p, context)].join(", ")»«ENDIF»)
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} else {
			super.compile(m, context)
		}
	}
	
	def String createCloneStatements(IQLAttribute attr, String varName, IODLGeneratorContext context) {
		var name = attr.simpleName
		var pName = helper.firstCharUpperCase(name)
		var type = attr.type
		var content = "";
		var ifStmt = true;
		if (lookUp.isList(type)) {
			context.addImport(IQLUtils.canonicalName)			
			content = '''this.«name» = «IQLUtils.simpleName».copyList(«varName».get«pName»());'''

		} else if (lookUp.isMap(type)) {
			context.addImport(IQLUtils.canonicalName)				
			content = '''this.«name» = «IQLUtils.simpleName».copyMap(«varName».get«pName»());'''		
		} else if (lookUp.isClonable(type)) {
			content = '''this.«name» = «varName».get«pName»().clone();'''
		} else {
			ifStmt = false;
			content = '''this.«name» = «varName».get«pName»();'''
		}
		if (ifStmt) {
			'''
			if («varName».get«pName»() !== null)
				«content»
			'''
		} else {
			'''«content»'''
		}
	}	

}
