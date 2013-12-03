package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public interface IUserManagementWritable extends IUserManagement{

	   /**
     * @param name
     * @param caller
     * @return
     */
    IRole createRole(String name, ISession caller);

    /**
     * @param role
     * @param caller
     */
    void deleteRole(IRole role, ISession caller);

    /**
     * @param name
     * @param caller
     * @return
     */
    IUser createUser(String name, ISession caller);

    /**
     * @param user
     * @param password
     * @param caller
     */
    void changePassword(IUser user, byte[] password, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void activateUser(IUser user, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void deactivateUser(IUser user, ISession caller);

    /**
     * @param user
     * @param caller
     */
    void deleteUser(IUser user, ISession caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void grantRole(IUser user, IRole role, ISession caller);

    /**
     * @param user
     * @param role
     * @param caller
     */
    void revokeRole(IUser user, IRole role, ISession caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(IUser user, IPermission permission, String objectURI, ISession caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(IUser user, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void grantPermission(IRole role, IPermission permission, String objectURI, ISession caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void grantPermissions(IRole role, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param user
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(IUser user, IPermission permission, String objectURI, ISession caller);

    /**
     * @param user
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(IUser user, Set<IPermission> permissions, String objectURI, ISession caller);

    /**
     * @param role
     * @param permission
     * @param objectURI
     * @param caller
     */
    void revokePermission(IRole role, IPermission permission, String objectURI, ISession caller);

    /**
     * @param role
     * @param permissions
     * @param objectURI
     * @param caller
     */
    void revokePermissions(IRole role, Set<IPermission> permissions, String objectURI, ISession caller);

	

	
}
