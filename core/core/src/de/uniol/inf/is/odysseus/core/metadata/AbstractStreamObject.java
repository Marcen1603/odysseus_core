package de.uniol.inf.is.odysseus.core.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.Order;

abstract public class AbstractStreamObject<T extends IMetaAttribute> implements
		IStreamObject<T> {

	private static final long serialVersionUID = 1480009485404803793L;

	final private Map<String, Object> metadataMap = new HashMap<String, Object>();
	private T metadata = null;
	final private Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();

	public AbstractStreamObject() {
	}

	@SuppressWarnings("unchecked")
	protected AbstractStreamObject(AbstractStreamObject<T> other) {
		this.metadataMap.putAll(other.metadataMap);
		if (other.metadata != null) {
			this.metadata = (T) other.metadata.clone();
		}
		this.additionalContent.putAll(other.additionalContent);
	}

	@Override
	public Object getMetadata(String name) {
		return metadataMap.get(name);
	}

	@Override
	public void setMetadata(String name, Object content) {
		metadataMap.put(name, content);
	}
	
	@Override
	public void setMetadataMap(Map<String, Object> metaMap){
		this.metadataMap.clear();
		this.metadataMap.putAll(metaMap);
	}
	
	@Override
	public Map<String, Object> getMetadataMap(){
		return Collections.unmodifiableMap(metadataMap);
	}

	@Override
	public final T getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}

	// -----------------------------------------------------------------
	// additional content
	// -----------------------------------------------------------------
	@Override
	public Serializable getAdditionalContent(String name) {
		return this.additionalContent.get(name);
	}

	@Override
	public void setAdditionalContent(String name, Serializable content) {
		this.additionalContent.put(name, content);
	}

	@Override
	public Map<String, Serializable> getAdditionalContent() {
		return additionalContent;
	}

	@Override
	public void setAdditionalContent(Map<String, Serializable> additionalContent) {
		this.additionalContent.putAll(additionalContent);
	}

	// -------------------------------------
	// Merge
	// -------------------------------------

	public IStreamObject<T> merge(IStreamObject<T> left,
			IStreamObject<T> right, IMetadataMergeFunction<T> metamerge,
			Order order) {
		IStreamObject<T> ret = process_merge(left, right, order);
		ret.setMetadata(metamerge.mergeMetadata(left.getMetadata(),
				right.getMetadata()));
		
		// TODO: Merge function in cases where key is same!!
		
		// Use from right and overwrite with left
		if (order == Order.LeftRight) {
			ret.setAdditionalContent(right.getAdditionalContent());
			for (Entry<String, Serializable> a: left.getAdditionalContent().entrySet()){
				ret.setAdditionalContent(a.getKey(), a.getValue());
			}
			ret.setMetadataMap(right.getMetadataMap());
			for (Entry<String, Object> a: left.getMetadataMap().entrySet()){
				ret.setMetadata(a.getKey(), a.getValue());
			}
		} else if (order == Order.RightLeft) { // Use from Left and overwrite with right
			ret.setAdditionalContent(left.getAdditionalContent());
			for (Entry<String, Serializable> a: right.getAdditionalContent().entrySet()){
				ret.setAdditionalContent(a.getKey(), a.getValue());
			}
			ret.setMetadataMap(left.getMetadataMap());
			for (Entry<String, Object> a: right.getMetadataMap().entrySet()){
				ret.setMetadata(a.getKey(), a.getValue());
			}

		}

		return ret;
	}

	protected IStreamObject<T> process_merge(IStreamObject<T> left,
			IStreamObject<T> right, Order order) {
		throw new IllegalArgumentException("Results cannot be merged!");
	}

	abstract public AbstractStreamObject<T> clone();

}
