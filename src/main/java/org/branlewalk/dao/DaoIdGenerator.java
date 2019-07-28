package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;

import java.sql.*;




public abstract class DaoIdGenerator<T> {
    final Connection connection;
    private final String table;
    private final String id;

    public DaoIdGenerator(Connection connection, String table, String id) {
        this.connection = connection;
        this.table = table;
        this.id = id;
    }

    protected int findId() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT MAX(%s) as maxId FROM %s", id, table));
        if (resultSet.next()) {
            return resultSet.getInt("maxId")+1;
        }
        return 0;
    }
}
