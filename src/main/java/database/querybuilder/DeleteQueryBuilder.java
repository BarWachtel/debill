package database.querybuilder;

import database.interfaces.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteQueryBuilder extends ConditionalQueryBuilder implements QueryBuilder {
    private String from = null;

    public DeleteQueryBuilder() {
        this.where = new ArrayList<>();
    }

    public DeleteQueryBuilder from(String from) {
        this.from = from;
        return this;
    }

    public DeleteQueryBuilder where(String where) {
        this.where.add(where);
        return this;
    }

    public DeleteQueryBuilder where(String column, String operator, String value) {
        this.where.add(column + " " + operator + " " + value);
        return this;
    }

    public DeleteQueryBuilder where(Collection<String> where) {
        this.where.addAll(where);
        return this;
    }

    @Override
    public String build() throws QueryBuilderException {

        StringBuilder theQuery = new StringBuilder();

        if (from == null) {
            throw new QueryBuilderException("FROM is empty");
        }

        theQuery.append("DELETE FROM ").append(from);

        appendConditions(theQuery);

        return theQuery.toString();
    }
}
