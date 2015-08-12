package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.IIQLExpressionParser
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils

abstract class AbstractIQLStatementCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionCompiler<G>, U extends IIQLTypeUtils, P extends IIQLExpressionParser, L extends IIQLLookUp> implements IIQLStatementCompiler<G>{
	protected H helper;
	protected E exprCompiler;
	protected T typeCompiler;
	protected P exprParser;
	protected L lookUp;
	protected U typeUtils;
	
	
	new (H helper, E exprCompiler, T typeCompiler, U typeUtils, P exprParser, L lookUp) {
		this.helper = helper;
		this.exprCompiler = exprCompiler;
		this.typeCompiler = typeCompiler;
		this.typeUtils = typeUtils;
		this.exprParser = exprParser;
		this.lookUp = lookUp;	
				
	}
	
	override String compile(IQLStatement s, G c) {
		if (s instanceof IQLStatementBlock) {
			return compile(s as IQLStatementBlock, c);
		} else if (s instanceof IQLExpressionStatement) {
			return compile(s as IQLExpressionStatement, c);
		} else if (s instanceof IQLIfStatement) {
			return compile(s as IQLIfStatement, c);
		} else if (s instanceof IQLWhileStatement) {
			return compile(s as IQLWhileStatement, c);
		} else if (s instanceof IQLDoWhileStatement) {
			return compile(s as IQLDoWhileStatement, c);
		} else if (s instanceof IQLForStatement) {
			return compile(s as IQLForStatement, c);
		} else if (s instanceof IQLForEachStatement) {
			return compile(s as IQLForEachStatement, c);
		} else if (s instanceof IQLSwitchStatement) {
			return compile(s as IQLSwitchStatement, c);
		} else if (s instanceof IQLVariableStatement) {
			return compile(s as IQLVariableStatement, c);
		} else if (s instanceof IQLBreakStatement) {
			return compile(s as IQLBreakStatement, c);
		} else if (s instanceof IQLContinueStatement) {
			return compile(s as IQLContinueStatement, c);
		} else if (s instanceof IQLReturnStatement) {
			return compile(s as IQLReturnStatement, c);
		} else if (s instanceof IQLConstructorCallStatement) {
			return compile(s as IQLConstructorCallStatement, c);
		} else if (s instanceof IQLJavaStatement) {
			return compile(s as IQLJavaStatement, c);
		}else {
			return "";
		}
	}
	
	def String compile(IQLJavaStatement s, G c) {
		var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(s.text))
		'''«text»'''			
	}
	
	def String compile(IQLStatementBlock s, G c) {
		'''
		{
			«FOR e : s.statements»
				«compile(e, c)»
			«ENDFOR»
		}
		'''			
	}
	
	def String compile(IQLExpressionStatement s, G c) {
		'''«exprCompiler.compile(s.expression, c)»;'''
	}	
	
	def String compile(IQLIfStatement s, G c) {
		'''
		if(«exprCompiler.compile(s.predicate, c)»)
			«compile(s.thenBody, c)»
		«IF s.elseBody != null»
		else
			«compile(s.elseBody, c)»
		«ENDIF»
		'''
	}
	
	def String compile(IQLWhileStatement s, G c) {
		'''
		while(«exprCompiler.compile(s.predicate, c)»)
			«compile(s.body, c)»
		'''
	}
	
	def String compile(IQLDoWhileStatement s, G c) {
		'''
		do
			«compile(s.body, c)»
		while(«exprCompiler.compile(s.predicate, c)»);
		'''
	}
	
	def String compile(IQLForStatement s, G c) {
		'''
		for («compile(s.^var, c)» «compile(s.predicate, c)» «exprCompiler.compile(s.updateExpr, c)»)
			«compile(s.body, c)»
		'''
	}
	
	def String compile(IQLForEachStatement s, G c) {
		'''
		for («compile(s.^var as IQLVariableDeclaration, c)» : «exprCompiler.compile(s.forExpression, c)»)
			«compile(s.body, c)»
		'''
	}
	
	def String compile(IQLSwitchStatement s, G c) {
		'''
		switch («exprCompiler.compile(s.expr, c)») {
			«FOR ca : s.cases»
				«compile(ca, c)»
			«ENDFOR»
			«IF s.statements != null && s.statements.size > 0»
				default :
					«FOR stmt : s.statements»
						«compile(stmt, c)»
					«ENDFOR»
			«ENDIF»
		}
		'''
	}
	
	def String compile(IQLCasePart cp, G c) {
		'''
		case «exprCompiler.compile(cp.expr, c)» :
			«FOR stmt : cp.statements»
				«compile(stmt, c)»
			«ENDFOR»
		'''	
	}
	
	
	def String compile(IQLVariableStatement s, G c) {
		var leftVar = s.^var as IQLVariableDeclaration
		var leftType = leftVar.ref
		if (s.init != null && s.init.argsList == null && s.init.argsMap == null) {			
			var right = exprParser.getType(s.init.value, leftType);
			if (right.isNull || lookUp.isAssignable(leftType, right.ref)){
				'''«compile(leftVar, c)»«IF s.init != null» = «compile(s.init, leftType, c)»«ENDIF»;'''
			} else if (right.isNull || lookUp.isCastable(leftType, right.ref)){
				var target = typeCompiler.compile(leftType, c, false)
				'''«compile(leftVar, c)»«IF s.init != null» = ((«target»)«compile(s.init, leftType, c)»«ENDIF»);'''
			} else {
				'''«compile(leftVar, c)»«IF s.init != null» = «compile(s.init, leftType, c)»«ENDIF»;'''				
			}					
		} else if (s.init != null) {
			'''«compile(leftVar, c)» = «compile(s.init, leftType, c)»;'''
			
		} else {
			'''«compile(leftVar, c)»;'''
			
		}
	}
	
	def String compile(IQLBreakStatement s, G c) {
		'''break;'''
	}
	
	def String compile(IQLContinueStatement s, G c) {
		'''continue;'''
	}
	
	def String compile(IQLReturnStatement s, G c) {
		'''return «exprCompiler.compile(s.expression, c)»;'''
	}
	
	def String compile(IQLConstructorCallStatement s, G c) {
		var type = helper.getClass(s);
		if (type != null) {
			var typeRef = typeUtils.createTypeRef(type)
			if (s.keyword.equalsIgnoreCase("super")) {
				typeRef = type.extendedClass
			} 
			var constructor = lookUp.findConstructor(typeRef, s.args.elements);
			if (constructor != null) {
				'''«s.keyword»(«IF s.args!=null»«exprCompiler.compile(s.args,constructor.parameters, c)»«ENDIF»);'''					
			}
 		} else {
 			'''«s.keyword»(«IF s.args!=null»«exprCompiler.compile(s.args, c)»«ENDIF»);''' 			
 		}	
	}
	
	
	def String compile(IQLVariableDeclaration decl, G context) {
		var type = decl.ref
		'''«typeCompiler.compile(type, context, false)» «decl.name»'''
	}
	
	override String compile(IQLVariableInitialization init, JvmTypeReference typeRef, G context) {
		var result = "";
		context.expectedTypeRef = typeRef		
		if (init.argsMap != null && init.argsMap.elements.size > 0) {
			var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
			if (constructor != null) {
				result = '''get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, constructor.parameters,context)»), «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
			} else {
				result = '''get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, context)»), «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
			}
		} else if (init.argsList != null) {
			var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
			if (constructor != null) {
				result = '''new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList,constructor.parameters, context)»)'''			
			} else {
				result = '''new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, context)»)'''			
			}
		} else {
			result = '''«exprCompiler.compile(init.value, context)»'''
		}
		context.expectedTypeRef = null
		return result;		
		
	}
}