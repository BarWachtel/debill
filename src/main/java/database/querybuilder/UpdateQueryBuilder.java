package database.querybuilder;

import database.interfaces.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class UpdateQueryBuilder extends ConditionalQueryBuilder implements QueryBuilder{
    private String from = null;
    private final List<String> set;

    public UpdateQueryBuilder() {
        this.where = new ArrayList<String>();
        this.set = new ArrayList<String>();
    }

    public UpdateQueryBuilder from(String from) {
        this.from = from;
        return this;
    }

    public UpdateQueryBuilder where(String where) {
        this.where.add(where);
        return this;
    }

    public UpdateQueryBuilder where(String column, String operator, String value) {
        this.where.add(column + " " + operator + " " + value);
        return this;
    }

    public UpdateQueryBuilder where(List<String> where) {
        this.where.addAll(where);
        return this;
    }

    public UpdateQueryBuilder set(String set) {
        this.set.add(set);
        return this;
    }

    public UpdateQueryBuilder set(List<String> set) {
        this.set.addAll(set);
        return this;
    }

    public String build() throws QueryBuilderException {

        StringBuilder theQuery = new StringBuilder();

        if (from == null) {
            throw new QueryBuilderException("FROM is empty");
        }

        if (set.size() == 0) {
            throw new QueryBuilderException("SET is empty");
        }

        theQuery.append("UPDATE ").append(from);
        theQuery.append(" SET ");

        for (String _set : this.set) {
            theQuery.append(_set).append(',');
        }
        theQuery.deleteCharAt(theQuery.lastIndexOf(","));

        appendConditions(theQuery);

        return theQuery.toString();
    }

    private class UpdateQueryBuilderException extends Exception {

        UpdateQueryBuilderException(String message) {
            super(message);
        }
    }
}
