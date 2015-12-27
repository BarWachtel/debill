package database.querybuilder;

import java.util.List;

public abstract class ConditionalQueryBuilder {

    protected List<String> where;

    protected void appendConditions(StringBuilder queryBuilder) {
        if (this.where.size() > 0) {

            queryBuilder.append(" WHERE ");

            for (String _where : this.where) {
                queryBuilder.append(_where).append(" AND ");
            }
            int start = queryBuilder.lastIndexOf("AND");
            int end = start + 2;
            queryBuilder.delete(start, end);
        }
    }
}
