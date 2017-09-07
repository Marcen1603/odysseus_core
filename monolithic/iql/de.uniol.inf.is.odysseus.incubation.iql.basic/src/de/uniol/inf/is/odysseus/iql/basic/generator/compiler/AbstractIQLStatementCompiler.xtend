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
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator
import org.eclipse.xtext.common.types.JvmExecutable
import java.util.ArrayList

abstract class AbstractIQLStatementCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, E extends IIQLExpressionCompiler<G>, U extends IIQLTypeUtils, P extends IIQLExpressionEvaluator, L extends IIQLLookUp> implements IIQLStatementCompiler<G>{
	protected H helper;
	protected E exprCompiler;
	protected T typeCompiler;
	protected P exprEvaluator;
	protected L lookUp;
	protected U typeUtils;
	
	
	new (H helper, E exprCompiler, T typeCompiler, U typeUtils, P exprEvaluator, L lookUp) {
		this.helper = helper;
		this.exprCompiler = exprCompiler;
		this.typeCompiler = typeCompiler;
		this.typeUtils = typeUtils;
		this.exprEvaluator = exprEvaluator;
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
		var text = s.java.text
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
	
	def String createTryCatchBlock(String content, G c) {
		var result = 
		'''
		try {
			«content»
		} catch (Exception e) {
			e.printStackTrace();
		}
		'''
		c.clearExceptions
		return result;
	}
	
	def String compile(IQLExpressionStatement s, G c) {
		var content = '''«exprCompiler.compile(s.expression, c)»;'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}		
	}	
	
	def String compile(IQLIfStatement s, G c) {
		var content = 
		'''
		if(«exprCompiler.compile(s.predicate, c)»)
			«compile(s.thenBody, c)»
		«IF s.elseBody !== null»
		else
			«compile(s.elseBody, c)»
		«ENDIF»
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLWhileStatement s, G c) {
		var content = 
		'''
		while(«exprCompiler.compile(s.predicate, c)»)
			«compile(s.body, c)»
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLDoWhileStatement s, G c) {
		var content = 
		'''
		do
			«compile(s.body, c)»
		while(«exprCompiler.compile(s.predicate, c)»);
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLForStatement s, G c) {
		var content = 
		'''
		for («compile(s.^var as IQLVariableDeclaration, c)» = «exprCompiler.compile(s.value, c)»; «exprCompiler.compile(s.predicate, c)»; «exprCompiler.compile(s.updateExpr, c)»)
			«compile(s.body, c)»
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLForEachStatement s, G c) {
		var content = 
		'''
		for («compile(s.^var as IQLVariableDeclaration, c)» : «exprCompiler.compile(s.forExpression, c)»)
			«compile(s.body, c)»
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLSwitchStatement s, G c) {
		var content = 
		'''
		switch («exprCompiler.compile(s.expr, c)») {
			«FOR ca : s.cases»
				«compile(ca, c)»
			«ENDFOR»
			«IF s.statements !== null && s.statements.size > 0»
				default :
					«FOR stmt : s.statements»
						«compile(stmt, c)»
					«ENDFOR»
			«ENDIF»
		}
		'''
		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}
	}
	
	def String compile(IQLCasePart cp, G c) {
		'''
		case «exprCompiler.compile(cp.expr, c)» :
			«FOR stmt : cp.statements»
				«compile(stmt, c)»
			«ENDFOR»
		'''	
	}
	
		
	def String compile(IQLBreakStatement s, G c) {
		'''break;'''
	}
	
	def String compile(IQLContinueStatement s, G c) {
		'''continue;'''
	}
	
	def String compile(IQLReturnStatement s, G c) {
		var typeResult = exprEvaluator.eval(s.expression, c.expectedTypeRef);
		var content = 
		'''return «IF s.expression !== null»«exprCompiler.compile(s.expression, c)»«ENDIF»;'''
		if (c.hasException) {
			'''
			«createTryCatchBlock(content, c)»
			return «getDefaultLiteral(typeResult.ref)»;
			''' 		
		} else {
			return content;
		}
	}
	
	def String getDefaultLiteral(JvmTypeReference typeRef) {
		if (typeRef === null) {
			return "null";
		} else if (typeUtils.isPrimitive(typeRef)) {
			if (typeUtils.isByte(typeRef)) {
				return "0";
			} else if (typeUtils.isShort(typeRef)) {
				return "0";
			} else if (typeUtils.isInt(typeRef)) {
				return "0";
			} else if (typeUtils.isLong(typeRef)) {
				return "0";
			} else if (typeUtils.isFloat(typeRef)) {
				return "0.0f";
			} else if (typeUtils.isDouble(typeRef)) {
				return "0.0";
			} else if (typeUtils.isBoolean(typeRef)) {
				return "false";
			} else if (typeUtils.isCharacter(typeRef)) {
				return "0";
			} 
		} else {
			return "null";
		}
	}
	
	def String compile(IQLConstructorCallStatement s, G c) {
		var type = helper.getClass(s);
		var content = "";
		if (type !== null) {
			var typeRef = typeUtils.createTypeRef(type) 
			var JvmExecutable constructor = null;
			if (s.isSuper) {
				constructor = lookUp.findSuperConstructor(typeRef, s.args.elements);
			} else {
				constructor = lookUp.findDeclaredConstructor(typeRef, s.args.elements);
			}
			if (constructor !== null && s.isSuper) {
				c.addExceptions(constructor.exceptions)
				content = '''super(«IF s.args!==null»«exprCompiler.compile(s.args,constructor.parameters, c)»«ENDIF»);'''					
			} else if (constructor !== null) {
				c.addExceptions(constructor.exceptions)
				content = '''this(«IF s.args!==null»«exprCompiler.compile(s.args,constructor.parameters, c)»«ENDIF»);'''					
			}
 		} else {
 			if (s.isSuper) {
 				content = '''super(«IF s.args!==null»«exprCompiler.compile(s.args, c)»«ENDIF»);''' 			
 			} else {
 				content = '''this(«IF s.args!==null»«exprCompiler.compile(s.args, c)»«ENDIF»);''' 			
 			}
 		}
 		if (c.hasException) {
			return createTryCatchBlock(content, c);		
		} else {
			return content;
		}	
	}
	
	def String compile(IQLVariableStatement s, G c) {
		var leftVar = s.^var as IQLVariableDeclaration
		var leftType = leftVar.ref
		var content = "";
		if (s.init !== null && s.init.argsList === null && s.init.argsMap === null) {			
			var right = exprEvaluator.eval(s.init.value, leftType);
			if (right.isNull || lookUp.isAssignable(leftType, right.ref)){
				content =''' = «compile(s.init, leftType, c)»;'''
			} else if (right.isNull || lookUp.isCastable(leftType, right.ref)){
				var target = typeCompiler.compile(leftType, c, false)
				content =''' = ((«target»)«compile(s.init, leftType, c)»);'''
			} else {
				content =''' = «compile(s.init, leftType, c)»;'''				
			}					
		} else if (s.init !== null) {
			content = ''' = «compile(s.init, leftType, c)»;'''
			
		} 
		
		if (c.hasException) {
			'''
			«compile(leftVar, c)» = «getDefaultLiteral(leftType)»;
			«createTryCatchBlock(leftVar.name+content, c)»
			'''			
		} else {
			return compile(leftVar, c) + content;
		}
	}
	
	def String compile(IQLVariableDeclaration decl, G context) {
		var type = decl.ref
		'''«typeCompiler.compile(type, context, false)» «decl.name»'''
	}
	
	override String compile(IQLVariableInitialization init, JvmTypeReference typeRef, G context) {
		var result = "";
		context.expectedTypeRef = typeRef		
		if (init.argsList !== null && init.argsMap !== null && init.argsMap.elements.size > 0) {
			var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
			if (constructor !== null) {
				context.addExceptions(constructor.exceptions)				
				result = '''get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, constructor.parameters,context)»), «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
			} else {
				result = '''get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, context)»), «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
			}
		} else if (init.argsMap !== null && init.argsMap.elements.size > 0) {
			var constructor = lookUp.findPublicConstructor(typeRef, new ArrayList())
			if (constructor !== null) {
				context.addExceptions(constructor.exceptions)
			}
			result = '''get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(new «typeCompiler.compile(typeRef, context, true)»(), «exprCompiler.compile(init.argsMap, typeRef, context)»)'''
		} else if (init.argsList !== null) {
			var constructor = lookUp.findPublicConstructor(typeRef, init.argsList.elements)
			if (constructor !== null) {
				context.addExceptions(constructor.exceptions)	
				result = '''new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList,constructor.parameters, context)»)'''			
			} else {
				if (typeUtils.isArray(typeRef)) {
					context.addImport(ArrayList.canonicalName)
					result = '''new «ArrayList.simpleName»<>()'''
				} else {
					result = '''new «typeCompiler.compile(typeRef, context, true)»(«exprCompiler.compile(init.argsList, context)»)'''			
				}
			}
		} else {
			result = '''«exprCompiler.compile(init.value, context)»'''
		}
		context.expectedTypeRef = null
		return result;		
		
	}
}
