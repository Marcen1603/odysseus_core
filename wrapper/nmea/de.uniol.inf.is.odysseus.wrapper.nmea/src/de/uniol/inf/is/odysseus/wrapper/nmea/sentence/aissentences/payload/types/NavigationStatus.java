package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types;

public enum NavigationStatus {
	UnderwayUsingEngine (0),
	AtAnchor(1),
	NotUnderCommand(2),
	RestrictedManoeuverability(3),
	ConstrainedByHerDraught(4),
	Moored(5),
	Aground(6),
	EngagedInFising(7),
	UnderwaySailing(8),
	ReservedForFutureUse9(9),
	ReservedForFutureUse10(10),
	ReservedForFutureUse11(11),
	ReservedForFutureUse12(12),
	ReservedForFutureUse13(13),
	ReservedForFutureUse14(14),
	NotDefined(15);

	private final Integer status;
	
	NavigationStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}

	public String getValue() {
	    return toString();
	}

	public static NavigationStatus fromInteger(Integer integer) {
		if (integer != null) {
			for (NavigationStatus b : NavigationStatus.values()) {
				if (integer.equals(b.status)) {
					return b;
				}
			}
		}
		return null;
	}
}
