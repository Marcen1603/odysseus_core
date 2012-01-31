package de.uniol.inf.is.odysseus.rcp.l10n;

import org.eclipse.osgi.util.NLS;

public class OdysseusNLS extends NLS {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$

	public static String LoginTitle;
	public static String Username;
	public static String Password;
	public static String OK;
	public static String Cancel;
	public static String LoginAutomatically;
	public static String Exception;
	public static String AnErrorHasOccured;
	public static String CloseApplication;
	public static String ShowStackTrace;
	public static String HideStackTrace;
	public static String Continue;
	public static String CausedBy;
	public static String CreatedBy;

	public static String ID;
	public static String User;
	public static String Parser;
	public static String Priority;
	public static String QueryText;
	public static String Running;
	public static String Stopped;
	public static String Active;
	public static String Inactive;
	public static String Opened;
	public static String Status;
	public static String Queries;
	public static String IteratableSources;
	public static String Roots;
	public static String BasePriority;
	public static String CurrentPriority;
	public static String SLAInfos;
	public static String NoExecutorFound;
	public static String NoSchedulerFound;
	public static String Privilege;
	public static String Permission;
	public static String AutomaticallyLoggedInAs;
	public static String Executor;
	public static String Ready;



	static {
		NLS.initializeMessages(BUNDLE_NAME, OdysseusNLS.class);
	}

	private OdysseusNLS() {

	}

}
