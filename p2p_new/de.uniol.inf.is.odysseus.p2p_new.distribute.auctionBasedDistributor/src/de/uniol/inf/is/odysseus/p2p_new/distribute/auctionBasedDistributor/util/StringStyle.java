package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Helper class to format the toString-Output of the advertisments.
 *
 * @author Mustafa Caylak <mustafa.caylak@uni-oldenburg.de>
 */
public class StringStyle {
	public static final ToStringStyle CUSTOMIZED_SHORT_PREFIX_STYLE = new CustomizedShortPrefixStyle();

	private static final class CustomizedShortPrefixStyle extends ToStringStyle {
		private static final long serialVersionUID = 5151251944598832128L;

		public CustomizedShortPrefixStyle() {
			super();
			this.setUseShortClassName(true);
			this.setUseIdentityHashCode(false);
            this.setContentStart("[");
            this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");			
		}

		protected void appendDetail(StringBuffer buffer, String fieldName,
				Object value) {

//			private ID id;
//			private ID sharedQueryID;
//			private String auctionId;
//			private String pqlStatement;
//			private String transCfgName;
//			private PeerID ownerPeerId;
			
//			if(!fieldName.equals("pqlStatement")) 
				buffer.append(value);
		}

		private Object readResolve() {
			return StringStyle.CUSTOMIZED_SHORT_PREFIX_STYLE;
		}
	}
}