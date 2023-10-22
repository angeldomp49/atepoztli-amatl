package org.makechtec.software.amatl.storage.database;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;
import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;

import java.util.logging.Logger;

public class MySQLDatabaseOutput implements Amatl {

    private static final Logger LOG = Logger.getLogger(MySQLDatabaseOutput.class.getName());

    private final ConnectionInformation connectionInformation;
    private final String table;

    public MySQLDatabaseOutput(ConnectionInformation connectionInformation, String table) {
        this.connectionInformation = connectionInformation;
        this.table = table;
    }


    @Override
    public void saveOn(final CharSequence message) throws StorageException {
        var sqlEngine = new MysqlEngine<Void>(connectionInformation);

        sqlEngine.queryString("INSERT INTO " + table + "(message, created_at) VALUES(?, NOW());")
                .addParamAtPosition(1, message, ParamType.TYPE_STRING)
                .isPrepared()
                .update();
    }

}
