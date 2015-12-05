package database.interfaces;

public interface QueryBuilder {
    public String build() throws QueryBuilderException;

    public class QueryBuilderException extends Exception {
        public QueryBuilderException(String msg) {
            super(msg);
        }
    }
}
