package database.dao;

import database.DBConn;
import database.entity.Entity;
import database.interfaces.QueryBuilder;
import database.querybuilder.QueryBuilderFactory;

import java.sql.*;
import java.util.*;

public abstract class EntityDAO<T extends Entity> extends CommonDAO {

    protected static QueryBuilderFactory queryBuilderFactory;

    EntityDAO() {
    }

    static {
        EntityDAO.queryBuilderFactory = new QueryBuilderFactory();
    }

    protected List<T> getAllEntities() {
        Connection conn = DBConn.getConnection();
        List<T> entities = new ArrayList<>();
        try {
            String query = buildGetAllQueryString();
            Statement st = conn.createStatement();
            System.out.println("EntityDAO -> Get All Entities Query: " + query);
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                entities.add(createEntityFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("EntityDAO -> getAllEntities -> Exception: " + e.getMessage());
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
            System.out.println("EntityDAO -> Get Entity Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                entity = createEntityFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("EntityDAO -> getEntity -> Exception: " + e.getMessage());
        }
        return entity;
    }

    protected T updateEntity(T entity) {
        Connection conn = DBConn.getConnection();
        T entityToReturn = null;
        try {
            String query = buildUpdateQuery(entity.getID());
            PreparedStatement ps = conn.prepareStatement(query);
            setUpdatePreparedStatementParameters(ps, entity);
            System.out.println("EntityDAO -> Update Query: " + ps.toString());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                entityToReturn = getEntity(entity.getID());
            }
        } catch (SQLException e) {
            System.out.println("EntityDAO -> updateEntity -> Exception: " + e.getMessage());
        }
        return entityToReturn;
    }

    protected T insertEntity(T entity) {
        Connection conn = DBConn.getConnection();
		T entityToReturn = null;
        try {
            String query = buildInsertQuery();
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setInsertPreparedStatementParameters(ps, entity);
            System.out.println("EntityDAO -> Insert Query: " + ps.toString());
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()) {
                    entity.setID(generatedKeys.getInt(1));
					entityToReturn = entity;
                }
            }
        } catch (SQLException e) {
            System.out.println("EntityDAO -> insertEntity -> Exception: " + e.getMessage());
        }

		return entityToReturn;
    }

    protected boolean deleteEntity(int entityToDeleteID) {
        Connection conn = DBConn.getConnection();
        boolean result = false;
        try {
            String query = buildDeleteQuery(entityToDeleteID);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, entityToDeleteID);
            System.out.println("EntityDAO -> Delete Query: " + ps.toString());
            result = ps.execute();
        } catch (SQLException e) {
            System.out.println("EntityDAO -> deleteEntry -> Exception: " + e.getMessage());
        }
        return result;
    }

    protected String buildGetAllQueryString() {
        try {
            return queryBuilderFactory
                    .select()
                    .from(getTableName())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("EntityDAO -> buildGetAllQueryString -> Exception: " + e.getMessage());
        }
        return null;
    }

    private String buildGetEntityByIdQuery() {
		return buildGetEntityByColumnName(getIdColumnName());
    }

	protected String buildGetEntityByColumnName(String columnName) {
        try {
			return queryBuilderFactory.select().from(getTableName()).where(columnName + " = ?").build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String buildUpdateQuery(int id) {
        try {
            return queryBuilderFactory
                    .update()
                    .from(getTableName())
                    .set(getColumnsForUpdate())
					.where(getIdColumnName(), " = ", String.valueOf(id))
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("EntityDAO -> buildUpdateQuery -> Exception: " + e.getMessage());
        }
        return null;
    }

    protected String buildInsertQuery() {
        String query = null;
        try {
            query = queryBuilderFactory
                    .insert()
                    .into(getTableName())
                    .column(getColumnsForInsert())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("EntityDAO -> buildInsertQuery -> Exception: " + e.getMessage());
        }
        return query;
    }

    private String buildDeleteQuery(int id) {
        try {
            return queryBuilderFactory
                    .delete()
                    .from(getTableName())
                    .where(getIdColumnName() + " = " + id)
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            System.out.println("EntityDAO -> buildDeleteQuery -> Exception: " + e.getMessage());
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