package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 04/12/2015.
 */
public class QueryBuilder {
	String action;
	List<String> where;
	List<String> from;

	public QueryBuilder() {
		where = new ArrayList<String>();
		from = new ArrayList<String>();
	}

	public QueryBuilder action(String action) {
		this.action = action;
		return this;
	}

	public QueryBuilder where(String where) {
		this.where.add(where);
		return this;
	}

	public QueryBuilder where(List<String> where) {
		this.where.addAll(where);
		return this;
	}

	public QueryBuilder from(String from) {
		this.from.add(from);
		return this;
	}

	public QueryBuilder from(List<String> from) {
		this.from.addAll(from);
		return this;
	}

	public String build() throws IllegalQueryException {
		StringBuilder theQuery;

		if (action == null || from.size() == 0) {
			throw new IllegalQueryException();
		}

		theQuery = new StringBuilder();
		theQuery.append(action).append(" * ");
		theQuery.append("FROM ");
		for (String _from : from) {
			theQuery.append(_from).append(',');
		}
		theQuery.deleteCharAt(theQuery.lastIndexOf(","));

		for (String _where : where) {
			theQuery.append(" WHERE ").append(',');
		}

		return theQuery.toString();
	}

	private class IllegalQueryException extends Throwable {
	}
}
