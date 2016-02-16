package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Physical operator to be placed directly after source access operators. <br />
 * <br />
 * It transfers all incoming elements without delay. But it stores the last
 * transfered element as operator state. All elements before shall not be
 * considered for this operator/query in case of recovery, because they are
 * either not processed (initial state) or before the last protection point.
 * 
 * @author Michael Brand
 * 
 * @param <StreamObject>
 *            The type of stream elements to process.
 */
public class SourceBackupPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> implements IStatefulPO {

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	protected final AbstractAccessAO mSourceAccess;

	/**
	 * Dummy stream object, because you can not synchronize a null object (sync
	 * of {@link #lastSeenElement}).
	 */
	protected final IStreamObject<IMetaAttribute> dummyStreamObj = new IStreamObject<IMetaAttribute>() {

		private static final long serialVersionUID = -6661422766286598958L;

		@Override
		public boolean isPunctuation() {
			return false;
		}

		@Override
		public IClone clone() {
			throw new NotImplementedException();
		}

		@Override
		public IMetaAttribute getMetadata() {
			throw new NotImplementedException();
		}

		@Override
		public void setMetadata(IMetaAttribute metadata) {
			throw new NotImplementedException();
		}

		@Override
		public void setKeyValue(String name, Object content) {
			throw new NotImplementedException();
		}

		@Override
		public Object getKeyValue(String name) {
			throw new NotImplementedException();
		}

		@Override
		public boolean hasKeyValue(String name) {
			throw new NotImplementedException();
		}

		@Override
		public void setKeyValueMap(Map<String, Object> metaMap) {
			throw new NotImplementedException();
		}

		@Override
		public Map<String, Object> getGetValueMap() {
			throw new NotImplementedException();
		}

		@Override
		public boolean isTimeProgressMarker() {
			throw new NotImplementedException();
		}

		@Override
		public void setTimeProgressMarker(boolean timeProgressMarker) {
			throw new NotImplementedException();
		}

		@Override
		public IStreamObject<IMetaAttribute> merge(IStreamObject<IMetaAttribute> left,
				IStreamObject<IMetaAttribute> right, IMetadataMergeFunction<IMetaAttribute> metamerge, Order order) {
			throw new NotImplementedException();
		}

		@Override
		public IStreamable newInstance() {
			throw new NotImplementedException();
		}

		@Override
		public int restrictedHashCode(int[] attributeNumbers) {
			throw new NotImplementedException();
		}

		@Override
		public boolean equalsTolerance(Object o, double tolerance) {
			throw new NotImplementedException();
		}

		@Override
		public boolean equals(IStreamObject<IMetaAttribute> o, boolean compareMeta) {
			return super.equals(o);
		}

		@Override
		public String toString(boolean printMetadata) {
			return super.toString();
		}

	};

	/**
	 * The last element received in {@link #process_next(IStreamObject, int)}.
	 */
	protected IStreamObject<IMetaAttribute> lastSeenElement = this.dummyStreamObj;

	/**
	 * The last seen element at the last checkpoint (loaded as operator state).
	 */
	protected IStreamable loadedLastSeenElement;

	/**
	 * Creates a new {@link SourceBackupPO}.
	 * 
	 * @param logical
	 *            A logical operator to be transformed to this physical
	 *            operator.
	 */
	public SourceBackupPO(SourceRecoveryAO logical) {
		super();
		this.mSourceAccess = logical.getSource();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator obj) {
		if (obj == null || !SourceBackupPO.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SourceBackupPO<StreamObject> other = (SourceBackupPO<StreamObject>) obj;
		return this.mSourceAccess.equals(other.mSourceAccess);
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		this.transfer(object, port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation, port);
	}

	@Override
	public IOperatorState getState() {
		synchronized (this.lastSeenElement) {
			@SuppressWarnings("unchecked")
			IStreamObject<IMetaAttribute> reference = (IStreamObject<IMetaAttribute>) this.lastSeenElement.clone();
			reference.setMetadata(null);
			// TODO syserr
			System.err.println(reference);
			return new SourceRecoveryState(reference);
		}
	}

	@Override
	public void setState(Serializable state) {
		this.loadedLastSeenElement = ((SourceRecoveryState) state).getLastSeenElement();
	}
	
	@Override
	public void transfer(StreamObject object, int sourceOutPort) {
		synchronized (this.lastSeenElement) {
			super.transfer(object, sourceOutPort);
			this.lastSeenElement = object;
		}
	}

}