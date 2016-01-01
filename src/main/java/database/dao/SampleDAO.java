package database.dao;

import database.DBConn;
import database.entity.Entity;
import database.interfaces.QueryBuilder;
import database.querybuilder.QueryBuilderFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SampleDAO<T extends Entity> {
    protected static String TABLE_NAME = null;
    protected static final String SQL_GET_ALL_QUERY;
    protected static QueryBuilderFactory queryBuilderFactory;

    SampleDAO() {
    }

    static {
        SampleDAO.queryBuilderFactory = new QueryBuilderFactory();
        SQL_GET_ALL_QUERY = buildGetAllQueryString();
    }

    protected List<T> getAllEntities() {
        Connection conn = DBConn.getConnection();
        List<T> entities = new ArrayList<>();
        try {
            String query = SQL_GET_ALL_QUERY;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                entities.add(createEntityFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("SampleDAO -> getAllEntities -> Exception: " + e.getMessage());
        }
        return entities;
    }

    protected T getEntity(int entityID) {
        Connection conn = DBConn.getConnection();
        T entity = null;
        try {
            String query = buildGetEntityByIdQuery();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, entityID);
            ResultSet rs = ps.executeQuery(query);
            if (rs.next()) {
                entity = createEntityFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("SampleDAO -> getEntity -> Exception: " + e.getMessage());
        }
        return entity;
    }

    private String buildGetEntityByIdQuery() {
        try {
            return queryBuilderFactory.select().from(TABLE_NAME).where(getIdColumnName() + "=?").build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T updateEntity(T entity) {
        Connection conn = DBConn.getConnection();
        T entityToReturn = null;
        try {
            String query = buildUpdateQuery();
            PreparedStatement ps = conn.prepareStatement(query);
            setUpdatePreparedStatementParameters(ps, entity);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                entityToReturn = getEntity(entity.getID());
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("SampleDAO -> updateEntity -> Exception: " + e.getMessage());
        }
        return entityToReturn;
    }

    protected void insertEntity(T entity) {
        Connection conn = DBConn.getConnection();
        try {
            String query = buildInsertQuery();
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setInsertPreparedStatementParameters(ps, entity);
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()) {
                    entity.setID(generatedKeys.getInt(1));
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("SampleDAO -> insertEntity -> Exception: " + e.getMessage());
        }
    }

    protected boolean deleteEntity(int entityToDeleteID) {
        Connection conn = DBConn.getConnection();
        boolean result = false;
        try {
            String query = buildDeleteQuery(entityToDeleteID);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, entityToDeleteID);
            result = ps.execute();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SampleDAO -> deleteEntry -> Exception: " + e.getMessage());
        }
        return result;
    }

    protected static String buildGetAllQueryString() {
        try {
            return queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("SampleDAO -> buildGetAllQueryString -> Exception: " + e.getMessage());
        }
        return null;
    }

    protected String buildUpdateQuery() {
        try {
            return queryBuilderFactory
                    .update()
                    .from(TABLE_NAME)
                    .set(getColumnsForUpdate())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("SampleDAO -> buildUpdateQuery -> Exception: " + e.getMessage());
        }
        return null;
    }

    protected String buildInsertQuery() {
        String query = null;
        try {
            query = queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .column(getColumnsForInsert())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("SampleDAO -> buildInsertQuery -> Exception: " + e.getMessage());
        }
        return query;
    }

    private String buildDeleteQuery(int id) {
        try {
            return queryBuilderFactory
                    .delete()
                    .from(TABLE_NAME)
                    .where(getIdColumnName() + " = " + id)
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("SampleDAO -> buildDeleteQuery -> Exception: " + e.getMessage());
        }
        return null;
    }

    protected abstract String getIdColumnName();

    protected abstract T createEntityFromResultSet(ResultSet rs);

    protected abstract Collection<String> getColumnsForUpdate();

    protected abstract Collection<String> getColumnsForInsert();

    protected abstract void setUpdatePreparedStatementParameters(PreparedStatement ps, T entity);

    protected abstract void setInsertPreparedStatementParameters(PreparedStatement ps, T entity);
}