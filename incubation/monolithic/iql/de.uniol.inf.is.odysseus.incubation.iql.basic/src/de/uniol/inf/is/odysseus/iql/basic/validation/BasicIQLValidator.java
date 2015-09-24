package de.uniol.inf.is.odysseus.iql.basic.validation;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;


public class BasicIQLValidator extends AbstractBasicIQLValidator {
	

	public static String DUPLICATE_ATTRIBUTES = "duplicateAttributes";
	public static String DUPLICATE_METHOD= "duplicateMethods";
	public static String TYPE_MISMATCH= "typeMismatch";
	public static String DUPLICATE_LOCAL_VARIABLE= "duplicateLocalVariable";
	public static String DUPLICATE_PARAMETERS= "duplicateParameters";
	public static String CONSTRUCTOR_UNDEFINED= "constructorUndefined";

	private static final Logger LOG = LoggerFactory.getLogger(BasicIQLValidator.class);
	
	@Inject
	private IIQLMethodFinder methodFinder;
	
	@Inject
	private IIQLExpressionEvaluator exprEvaluator;
	
	@Inject
	private IIQLLookUp lookUp;
	
	@Inject
	private IIQLTypeUtils typeUtils;

	@Check(CheckType.NORMAL)
	void checkIQLMethodDuplicateParameters(IQLMethod method) {
		Set<String> parameters = new HashSet<>();
		for (JvmFormalParameter parameter : method.getParameters()) {
			if (parameters.contains(parameter.getName())) {
				error("Duplicate parameter "+parameter.getName(), parameter, TypesPackage.eINSTANCE.getJvmFormalParameter_Name(), DUPLICATE_PARAMETERS);
			} else {
				parameters.add(parameter.getName());
			}
		}
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLMethodDeclarationDuplicateParameters(IQLMethodDeclaration methodDecl) {
		Set<String> parameters = new HashSet<>();
		for (JvmFormalParameter parameter : methodDecl.getParameters()) {
			if (parameters.contains(parameter.getName())) {
				error("Duplicate parameter "+parameter.getName(), parameter, TypesPackage.eINSTANCE.getJvmFormalParameter_Name(), DUPLICATE_PARAMETERS);
			} else {
				parameters.add(parameter.getName());
			}
		}
	}

	
	
	@Check(CheckType.NORMAL)
	void checkIQLVariableStatementDuplicateLocalVariable(IQLVariableStatement varStmt) {
		IQLVariableDeclaration varDecl = (IQLVariableDeclaration) varStmt.getVar();
		String varName = varDecl.getName();
		EObject container = varStmt.eContainer();
		boolean result = false;
		while (container != null && !(container instanceof JvmDeclaredType)) {
			for (EObject obj : container.eContents()) {				
				if (obj instanceof IQLVariableDeclaration) {
					IQLVariableDeclaration var = (IQLVariableDeclaration) obj;
					if (varDecl != var && varName.equalsIgnoreCase(var.getName())) {
						result = true;
						break;
					}
				} else if (obj instanceof JvmFormalParameter) {
					JvmFormalParameter parameter = (JvmFormalParameter) obj;
					if (varName.equalsIgnoreCase(parameter.getName())) {
						result = true;
						break;
					}
				} else if (obj instanceof IQLVariableStatement) {
					IQLVariableStatement stmt = (IQLVariableStatement) obj;
					IQLVariableDeclaration var = (IQLVariableDeclaration) stmt.getVar();
					if (varDecl != var && varName.equalsIgnoreCase(var.getName())) {
						result = true;
						break;
					}					
				}					
			}
			if (result) {
				break;
			} else {
				container = container.eContainer();
			}
		}
		if (result) {
			error("Duplicate local variable "+varName, varStmt.getVar(), BasicIQLPackage.eINSTANCE.getIQLVariableDeclaration_Name(), DUPLICATE_LOCAL_VARIABLE);
		}	
	}
	
	
	@Check(CheckType.NORMAL)
	void checkDuplicateAttributes(IQLArgumentsMapKeyValue keyValue) {
		JvmTypeReference leftTypeRef = null;

		if (keyValue.getKey() instanceof JvmField) {
			JvmField field = (JvmField) keyValue.getKey();
			leftTypeRef = field.getType();
		} else if (keyValue.getKey() instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) keyValue.getKey();
			leftTypeRef = op.getParameters().get(0).getParameterType();
		}
		TypeResult typeResult = exprEvaluator.eval(keyValue.getValue(), leftTypeRef);
		if (typeResult.hasError()) {
			LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
		} else if(!typeResult.isNull()) {
			JvmTypeReference rightTypeRef = typeResult.getRef();
			if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
				error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), keyValue, BasicIQLPackage.eINSTANCE.getIQLArgumentsMapKeyValue_Value(), TYPE_MISMATCH);
			}
		}
	}

	
	@Check(CheckType.NORMAL)
	void checkDuplicateAttributes(JvmGenericType type) {
		Set<String> names = new HashSet<>();
		for (IQLAttribute attr : EcoreUtil2.getAllContentsOfType(type, IQLAttribute.class)) {
			if (names.contains(attr.getSimpleName())) {
				error("Duplicate attribute "+attr.getSimpleName(), attr, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_ATTRIBUTES);
			} else {
				names.add(attr.getSimpleName());
			}
		}
	}
	
	@Check(CheckType.NORMAL)
	void checkDuplicateMethod(JvmGenericType type) {
		Set<String> names = new HashSet<>();
		for (IQLMethod meth : EcoreUtil2.getAllContentsOfType(type, IQLMethod.class)) {			 
			String name = methodFinder.createExecutableID(meth);
			if (names.contains(name)) {
				if (meth.getSimpleName() != null) {
					error("Duplicate method "+meth.getSimpleName(), meth, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_METHOD);
				} else {
					error("Duplicate method", meth, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_METHOD);
				}
			} else {
				names.add(name);
			}
		}
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLAttributeArguments(IQLAttribute attr) {
		if (attr.getInit() != null &&attr.getInit().getArgsList() != null && attr.getInit().getArgsList().getElements().size() > 0) {
			JvmExecutable constructor = lookUp.findPublicConstructor(attr.getType(), attr.getInit().getArgsList().getElements());
			if (constructor != null) {
				int i = 0;
				for (JvmFormalParameter parameter : constructor.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = attr.getInit().getArgsList().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), attr.getInit(), BasicIQLPackage.eINSTANCE.getIQLVariableInitialization_ArgsList(), TYPE_MISMATCH);
						}
					}										
				}
			} else {
				LOG.error("Could not find constructor");
			}
		}		
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLAttributeValue(IQLAttribute attr) {		
		if (attr.getInit() != null && attr.getInit().getValue() != null) {			
			JvmTypeReference leftTypeRef = attr.getType();
			TypeResult typeResult = exprEvaluator.eval(attr.getInit().getValue(), leftTypeRef);
			if (typeResult.hasError()) {
				LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
			} else if(!typeResult.isNull()) {
				JvmTypeReference rightTypeRef = typeResult.getRef();
				if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
					error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), attr.getInit(), BasicIQLPackage.eINSTANCE.getIQLVariableInitialization_Value(), TYPE_MISMATCH);
				}
			}
		}		
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLVariableStatementArguments(IQLVariableStatement varStmt) {
		if (varStmt.getInit() != null && varStmt.getInit().getArgsList() != null && varStmt.getInit().getArgsList().getElements().size() > 0) {
			JvmTypeReference typeRef = ((IQLVariableDeclaration)varStmt.getVar()).getRef();
			JvmExecutable constructor = lookUp.findPublicConstructor(typeRef, varStmt.getInit().getArgsList().getElements());
			if (constructor != null) {
				int i = 0;
				for (JvmFormalParameter parameter : constructor.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = varStmt.getInit().getArgsList().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), varStmt.getInit(), BasicIQLPackage.eINSTANCE.getIQLVariableInitialization_ArgsList(), TYPE_MISMATCH);
						}
					}										
				}
			} else {
				LOG.error("Could not find constructor");
			}
		}
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLVariableStatementValue(IQLVariableStatement varStmt) {
		if (varStmt.getInit() != null && varStmt.getInit().getValue() != null) {
			JvmTypeReference leftTypeRef = ((IQLVariableDeclaration)varStmt.getVar()).getRef();
			TypeResult typeResult = exprEvaluator.eval(varStmt.getInit().getValue(), leftTypeRef);
			if (typeResult.hasError()) {
				LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
			} else if(!typeResult.isNull()) {
				JvmTypeReference rightTypeRef = typeResult.getRef();
				if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
					error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), varStmt.getInit(), BasicIQLPackage.eINSTANCE.getIQLVariableInitialization_Value(), TYPE_MISMATCH);
				}
			}
		}
	}

	
	@Check(CheckType.NORMAL)
	void checkIQLConstructorCallStatement(IQLConstructorCallStatement stmt) {
		if (stmt.getArgs() != null && stmt.getArgs().getElements().size() > 0) {
			
			JvmGenericType type = EcoreUtil2.getContainerOfType(stmt, JvmGenericType.class);
			JvmExecutable constructor = null;
			if (stmt.isSuper()) {
				constructor = lookUp.findSuperConstructor(typeUtils.createTypeRef(type), stmt.getArgs().getElements());
			} else {
				constructor = lookUp.findDeclaredConstructor(typeUtils.createTypeRef(type), stmt.getArgs().getElements());
			}
			if (constructor != null) {
				int i = 0;
				for (JvmFormalParameter parameter : constructor.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = stmt.getArgs().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), stmt, BasicIQLPackage.eINSTANCE.getIQLConstructorCallStatement_Args(), TYPE_MISMATCH);
						}
					}										
				}
			} else {
				LOG.error("Could not find constructor");
			}
		}
	}

	
	@Check(CheckType.NORMAL)
	void checkIQLJvmElementCallExpression(IQLJvmElementCallExpression expr) {
		if (expr.getElement() instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) expr.getElement();
			if (expr.getArgs() != null && expr.getArgs().getElements().size() == op.getParameters().size()) {
				int i = 0;
				for (JvmFormalParameter parameter : op.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = expr.getArgs().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), expr, BasicIQLPackage.eINSTANCE.getIQLJvmElementCallExpression_Args(), TYPE_MISMATCH);
						}
					}			
				}
 			}
		}
	}
	
	@Check(CheckType.NORMAL)
	void checkIQLNewExpression(IQLNewExpression expr) {
		if (expr.getArgsList() != null && expr.getArgsList().getElements().size() > 0) {
			
			JvmExecutable constructor = lookUp.findPublicConstructor(expr.getRef(), expr.getArgsList().getElements());
			if (constructor != null) {
				int i = 0;
				for (JvmFormalParameter parameter : constructor.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = expr.getArgsList().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), expr, BasicIQLPackage.eINSTANCE.getIQLNewExpression_ArgsList(), TYPE_MISMATCH);
						}
					}										
				}
			} else {
				StringBuilder b = new StringBuilder();
				int i = 0;
				for (IQLExpression e : expr.getArgsList().getElements()) {
					if (i > 0) {
						b.append(", ");
					}
					TypeResult typeResult = exprEvaluator.eval(e);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate expression. "+typeResult.getDiagnostic());
					}
					if (typeResult.isNull()) {
						b.append("null");
					} else {
						b.append(typeUtils.getShortName(typeResult.getRef(), true));
					}
					i++;
				}				
				error("The constructor "+typeUtils.getShortName(expr.getRef(), true)+"("+b.toString()+") is undefined", expr, BasicIQLPackage.eINSTANCE.getIQLNewExpression_ArgsList(), CONSTRUCTOR_UNDEFINED);
			}
		}
	}	
	
	@Check(CheckType.NORMAL)
	void checkIQLMemberSelection(IQLMemberSelection memberSelection) {
		if (memberSelection.getMember() instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) memberSelection.getMember();
			if (memberSelection.getArgs() != null && memberSelection.getArgs().getElements().size() == op.getParameters().size()) {
				int i = 0;
				for (JvmFormalParameter parameter : op.getParameters()) {
					JvmTypeReference leftTypeRef = parameter.getParameterType();
					IQLExpression parameterExpr = memberSelection.getArgs().getElements().get(i++);
					TypeResult typeResult = exprEvaluator.eval(parameterExpr, leftTypeRef);
					if (typeResult.hasError()) {
						LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
					} else if(!typeResult.isNull()) {
						JvmTypeReference rightTypeRef = typeResult.getRef();
						if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
							error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), memberSelection, BasicIQLPackage.eINSTANCE.getIQLMemberSelection_Args(), TYPE_MISMATCH);
						}
					}					
				}
 			}
		}
	}
	
	
	@Check(CheckType.NORMAL)
	void checkAssignmentExpression(IQLAssignmentExpression expr) {
		if (expr.getLeftOperand() instanceof IQLMemberSelectionExpression) {
			checkAssignmentExpression(expr, (IQLMemberSelectionExpression)expr.getLeftOperand() , expr.getRightOperand());
		} else if (expr.getLeftOperand() instanceof IQLJvmElementCallExpression) {
			checkAssignmentExpression(expr, (IQLJvmElementCallExpression)expr.getLeftOperand() , expr.getRightOperand());
		} else if (expr.getLeftOperand() instanceof IQLArrayExpression) {
			checkAssignmentExpression(expr, (IQLArrayExpression)expr.getLeftOperand() , expr.getRightOperand());
		}
	}
	
	void checkAssignmentExpression(IQLAssignmentExpression expr, IQLMemberSelectionExpression leftExpr, IQLExpression rightExpr) {
		if (leftExpr.getSel().getMember() instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) leftExpr.getSel().getMember();
			if (op.getParameters().size() == 1) {
				checkAssignmentExpression(expr, op.getParameters().get(0).getParameterType(), rightExpr);
			} 
		} else if (leftExpr.getSel().getMember() instanceof JvmField) {
			JvmField field = (JvmField) leftExpr.getSel().getMember();
			checkAssignmentExpression(expr, field.getType(), rightExpr);
		}
	}
	
	void checkAssignmentExpression(IQLAssignmentExpression expr, IQLJvmElementCallExpression leftExpr, IQLExpression rightExpr) {
		if (leftExpr.getElement() instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) leftExpr.getElement();
			if (op.getParameters().size() == 1) {
				checkAssignmentExpression(expr, op.getParameters().get(0).getParameterType(), rightExpr);
			}
		} else if (leftExpr.getElement() instanceof JvmField) {
			JvmField field = (JvmField) leftExpr.getElement();
			checkAssignmentExpression(expr, field.getType(), rightExpr);
		} else if (leftExpr.getElement() instanceof IQLVariableDeclaration) {
			IQLVariableDeclaration var = (IQLVariableDeclaration) leftExpr.getElement();
			checkAssignmentExpression(expr, var.getRef(), rightExpr);
		} else if (leftExpr.getElement() instanceof JvmFormalParameter) {
			JvmFormalParameter parameter = (JvmFormalParameter) leftExpr.getElement();
			checkAssignmentExpression(expr, parameter.getParameterType(), rightExpr);
		}
	}
	
	void checkAssignmentExpression(IQLAssignmentExpression expr, IQLArrayExpression leftExpr, IQLExpression rightExpr) {
		TypeResult typeResult = exprEvaluator.eval(leftExpr);
		if (typeResult.hasError()) {
			LOG.error("Could not evaluate left expression. "+typeResult.getDiagnostic());
		} else if (!typeResult.isNull()) {
			checkAssignmentExpression(expr,typeResult.getRef(), rightExpr);
		}
	}
	
	void checkAssignmentExpression(IQLAssignmentExpression expr, JvmTypeReference leftTypeRef, IQLExpression rightExpr) {
		TypeResult typeResult = exprEvaluator.eval(rightExpr, leftTypeRef);
		if (typeResult.hasError()) {
			LOG.error("Could not evaluate right expression. "+typeResult.getDiagnostic());
		} else if (!typeResult.isNull()){
			JvmTypeReference rightTypeRef = typeResult.getRef();
			if (!expr.getOp().equals("=")) {
				 if(typeUtils.isArray(leftTypeRef)) {
					JvmTypeReference typeRef = typeUtils.getComponentType(leftTypeRef);
					if (!(lookUp.isAssignable(typeRef, rightTypeRef) || lookUp.isCastable(typeRef, rightTypeRef))) {
						error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(typeRef, true), expr, BasicIQLPackage.eINSTANCE.getIQLAssignmentExpression_RightOperand(), TYPE_MISMATCH);
					}
				} else if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
					error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), expr, BasicIQLPackage.eINSTANCE.getIQLAssignmentExpression_RightOperand(), TYPE_MISMATCH);
				}
			} else {
				if (!(lookUp.isAssignable(leftTypeRef, rightTypeRef) || lookUp.isCastable(leftTypeRef, rightTypeRef))) {
					error("Type mismatch: cannot convert from "+typeUtils.getShortName(rightTypeRef, true)+" to "+typeUtils.getShortName(leftTypeRef, true), expr, BasicIQLPackage.eINSTANCE.getIQLAssignmentExpression_RightOperand(), TYPE_MISMATCH);
				}
			}	
		}
	}
}
