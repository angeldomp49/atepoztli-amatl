package org.makechtec.software.amatl.storage.database;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;
import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.postgres.PostgresEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;

import java.sql.SQLException;
import java.util.logging.Logger;

public class PostgresDatabaseOutput implements Amatl {

    private static final Logger LOG = Logger.getLogger(PostgresDatabaseOutput.class.getName());

    private final ConnectionInformation connectionInformation;
    private final String table;

    public PostgresDatabaseOutput(ConnectionInformation connectionInformation, String table) {
        this.connectionInformation = connectionInformation;
        this.table = table;
    }


    @Override
    public void saveOn(final CharSequence message) throws StorageException {
        var sqlEngine = new PostgresEngine<Void>(connectionInformation);

        try {
            sqlEngine.queryString("INSERT INTO " + table + "(message, created_at) VALUES(?, NOW());")
                    .addParamAtPosition(1, message, ParamType.TYPE_STRING)
                    .isPrepared()
                    .update();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new StorageException(e);
        }
    }

}
