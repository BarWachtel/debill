package database.querybuilder;

import database.interfaces.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InsertQueryBuilder implements QueryBuilder{

    private List<String> columns;
    private List<String> values;
    private String into = null;

    public InsertQueryBuilder() {
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public InsertQueryBuilder into(String tableName) {
        this.into = tableName;
        return this;
    }

    public InsertQueryBuilder column(String colName) {
        this.columns.add(colName);
        return this;
    }

    public InsertQueryBuilder column(Collection<String> colsNames) {
        this.columns.addAll(colsNames);
        return this;
    }

    @Override
    public String build() throws QueryBuilderException {

        if(columns.size() == 0) {
            throw new QueryBuilderException("COLS is empty");
        }

        StringBuilder theQuery = new StringBuilder();

        theQuery.append("INSERT INTO ");
        theQuery.append(into);
        theQuery.append("(");

        for(String _col : columns) {
            theQuery.append(_col);
            theQuery.append(",");
        }
        theQuery.deleteCharAt(theQuery.lastIndexOf(","));

        theQuery.append(")");
        theQuery.append(" values ");

        if (values.size() > 0) {
            for(String _val : values) {
                theQuery.append(_val);
                theQuery.append(",");
            }
            theQuery.deleteCharAt(theQuery.lastIndexOf(","));
        }

        return theQuery.toString();
    }
}
