package org.branlewalk.dao;

import org.branlewalk.dto.UserDTO;

import java.sql.SQLException;

public abstract class DaoIdGenerator<T> {
    protected int findId() throws SQLException {
        for (int id = 1; id < Integer.MAX_VALUE; id++) {
            if(read(id) == null) {
                return id;
            }
        }
        return 0;
    }

    public abstract T read(int id) throws SQLException;
}
