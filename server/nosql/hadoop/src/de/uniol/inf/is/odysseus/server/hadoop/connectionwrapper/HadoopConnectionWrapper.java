package de.uniol.inf.is.odysseus.server.hadoop.connectionwrapper;

import com.sun.istack.internal.Nullable;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

/**
 * This class creates the connection to the hadoop filesystem
 */
public class HadoopConnectionWrapper extends NoSQLConnectionWrapper<FileSystem> {

    public HadoopConnectionWrapper(String host, int port, @Nullable String user, @Nullable String password) throws OpenFailedException {
        super(host, port, user, password);
    }

    @Override
    protected FileSystem establishConnection(final String host, final int port, @Nullable String user, @Nullable String password) throws OpenFailedException {

        try {

            if(user == null){
                throw new IllegalStateException("Parameter \"USER\" must be set!");
            }

            UserGroupInformation userGroupInformation = UserGroupInformation.createRemoteUser(user);

            return userGroupInformation.doAs(new PrivilegedExceptionAction<FileSystem>() {

                @Override
                public FileSystem run() throws Exception {

                    Configuration configuration = new Configuration();
                    String link = "hdfs://" + host + ":" + port;
                    configuration.set("fs.defaultFS", link);
                    configuration.set("dfs.replication", "1");

                    return FileSystem.get(configuration);
                }
            });

        } catch (Exception e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    protected void closeConnection(FileSystem connection) {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
