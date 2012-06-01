/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.control;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.scai.builder.types.*;

/**
 *
 * @author sb
 */
public class AccessControlP extends ControlModuleP{
	public Boolean createUser(String username, String password, SCAIPermission[] accessPermissions) {
		return false;
	}

	public Boolean removeUser(SCAIReference user) {
		return false;
	}

	public Object getUserData(SCAIReference user, IBuilder builder) {
		return null;
	}

	public Boolean updateUser(SCAIReference user, String username, String password, SCAIPermission[] accessPermissions) {
		return false;
	}

	public Object authenticateUser(String username, String password/*, IBuilder builder*/) {
		return null;
	}

	public Boolean destroySession(String sessionID) {
		return false;
	}
}
