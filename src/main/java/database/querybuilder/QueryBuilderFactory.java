package database.querybuilder;

public class QueryBuilderFactory {
	public SelectQueryBuilder select() {
		return new SelectQueryBuilder();
	}

	public UpdateQueryBuilder update() {
		return new UpdateQueryBuilder();
	}

	public DeleteQueryBuilder delete() {
		return new DeleteQueryBuilder();
	}

	public InsertQueryBuilder insert() {
		return new InsertQueryBuilder();
	}
}
