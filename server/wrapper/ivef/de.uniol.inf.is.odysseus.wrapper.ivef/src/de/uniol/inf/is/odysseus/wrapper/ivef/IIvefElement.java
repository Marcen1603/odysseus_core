/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.ivef;


import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author msalous
 *
 */
public interface IIvefElement {
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map);
}
