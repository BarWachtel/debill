package database.dao;

import database.DBConn;
import database.entity.Entity;
import database.interfaces.QueryBuilder;
import database.querybuilder.QueryBuilderFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SampleDAO<T extends Entity> {

    protected final String TABLE_NAME;
    protected final QueryBuilderFactory queryBuilderFactory;

    SampleDAO(String tableName) {
        this.TABLE_NAME = tableName;
        this.queryBuilderFactory = new QueryBuilderFactory();
    }

    protected List<T> getAllEntities() {
        Connection conn = DBConn.getConnection();
        List<T> entities = new ArrayList<>();
        try {
            String query = buildGetAllQuery();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                entities.add(createEntityFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    protected T getEntity(int entityID) {
        Connection conn = DBConn.getConnection();
        T entity = null;
        try {
            String query = buildGetEntityQuery(entityID);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                entity = createEntityFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    protected boolean updateEntity(T entity) {
        Connection conn = DBConn.getConnection();
        int result = 0;
        try {
            String query = buildUpdateQuery(entity);
            PreparedStatement ps = conn.prepareStatement(query);
            setUpdatePreparedStatementParameters(ps, entity);
            result = ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    protected boolean insertEntity(T entity) {
        Connection conn = DBConn.getConnection();
        boolean result = false;
        try {
            String query = buildInsertQuery(entity);
            PreparedStatement ps = conn.prepareStatement(query);
            setInsertPreparedStatementParameters(ps, entity);
            result = ps.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected String buildGetAllQuery() {
        try {
            return queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String buildGetEntityQuery(int id) {
        try {
            return queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .where(getIdColumnName() + " = " + id)
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract String getIdColumnName();

    protected abstract T createEntityFromResultSet(ResultSet rs);

    protected abstract String buildUpdateQuery(T entity);

    protected abstract String buildInsertQuery(T entity);

    protected abstract void setUpdatePreparedStatementParameters(PreparedStatement ps, T entity);

    protected abstract void setInsertPreparedStatementParameters(PreparedStatement ps, T entity);
}
