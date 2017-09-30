/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.exprevaluator.IQDLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.scoping.IQDLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import java.util.Collection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.scoping.IScope;

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
@SuppressWarnings("all")
public class QDLScopeProvider extends AbstractIQLScopeProvider<IQDLTypeDictionary, IQDLLookUp, IQDLExpressionEvaluator, IQDLTypeUtils> implements IQDLScopeProvider {
  public QDLScopeProvider(final IQDLTypeDictionary typeDictionary, final IQDLLookUp lookUp, final IQDLExpressionEvaluator exprEvaluator, final IQDLTypeUtils typeUtils) {
    super(typeDictionary, lookUp, exprEvaluator, typeUtils);
  }
  
  @Override
  protected Collection<JvmField> getIQLMemberSelectionAttributes(final IQLMemberSelectionExpression expr, final JvmTypeReference typeRef, final boolean isThis, final boolean isSuper) {
    Collection<JvmField> attributes = super.getIQLMemberSelectionAttributes(expr, typeRef, isThis, isSuper);
    if (isThis) {
      Collection<IQLClass> _sourceTypes = this.typeDictionary.getSourceTypes();
      for (final IQLClass source : _sourceTypes) {
        Collection<JvmField> _publicAttributes = this.lookUp.getPublicAttributes(this.typeUtils.createTypeRef(source), false);
        for (final JvmField attr : _publicAttributes) {
          boolean _equalsIgnoreCase = attr.getSimpleName().equalsIgnoreCase(source.getSimpleName());
          if (_equalsIgnoreCase) {
            attributes.add(attr);
          }
        }
      }
    }
    return attributes;
  }
  
  @Override
  protected Collection<JvmIdentifiableElement> getAttributesIQLJvmElementCallExpression(final EObject node, final JvmTypeReference thisType, final Collection<JvmTypeReference> importedTypes) {
    Collection<JvmIdentifiableElement> elements = super.getAttributesIQLJvmElementCallExpression(node, thisType, importedTypes);
    QDLQuery query = EcoreUtil2.<QDLQuery>getContainerOfType(node, QDLQuery.class);
    if (((node instanceof QDLQuery) || (query != null))) {
      Collection<IQLClass> _sourceTypes = this.typeDictionary.getSourceTypes();
      for (final IQLClass source : _sourceTypes) {
        Collection<JvmField> _publicAttributes = this.lookUp.getPublicAttributes(this.typeUtils.createTypeRef(source), false);
        for (final JvmField attr : _publicAttributes) {
          boolean _equalsIgnoreCase = attr.getSimpleName().equalsIgnoreCase(source.getSimpleName());
          if (_equalsIgnoreCase) {
            elements.add(attr);
          }
        }
      }
    }
    return elements;
  }
  
  @Override
  protected IScope getJdtScope(final ResourceSet set, final IIQLJdtTypeProvider typeProvider) {
    return IScope.NULLSCOPE;
  }
}
