/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.l10n;

import org.eclipse.osgi.util.NLS;

public class OdysseusNLS extends NLS {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$

	public static String Login;
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

	public static String WsdlLocation;
	public static String WebService;
	public static String AutomaticallyConnectedTo;
	public static String ServiceNamespace;
	public static String ConnectAutomatically;


	static {
		NLS.initializeMessages(BUNDLE_NAME, OdysseusNLS.class);
	}

	private OdysseusNLS() {

	}

}
