package database.querybuilder;

import database.interfaces.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class SelectQueryBuilder extends ConditionalQueryBuilder implements QueryBuilder {
	List<String> columns;
	List<String> from;

	public SelectQueryBuilder() {
		this.columns = new ArrayList<String>();
		this.where = new ArrayList<String>();
		this.from = new ArrayList<String>();
	}

	public SelectQueryBuilder column(String column) {
		this.columns.add(column);
		return this;
	}

	public SelectQueryBuilder column(List<String> columns) {
		this.columns.addAll(columns);
		return this;
	}

	public SelectQueryBuilder where(String where) {
		this.where.add(where);
		return this;
	}

	public SelectQueryBuilder where(List<String> where) {
		this.where.addAll(where);
		return this;
	}

	public SelectQueryBuilder from(String from) {
		this.from.add(from);
		return this;
	}

	public SelectQueryBuilder from(List<String> from) {
		this.from.addAll(from);
		return this;
	}

	@Override
	public String build() throws QueryBuilderException {

		StringBuilder theQuery;

		if (from.size() == 0) {
			throw new QueryBuilderException("No ACTION or FROM in SelectQueryBuilder");
		}

		theQuery = new StringBuilder();
		theQuery.append("SELECT ");

		if(this.columns.size() == 0 ) {
			theQuery.append(" * ");
		} else {
			for (String _column : this.columns) {
				theQuery.append(_column).append(',');
			}
			theQuery.deleteCharAt(theQuery.lastIndexOf(","));
		}

		theQuery.append(" FROM ");
		for (String _from : from) {
			theQuery.append(_from).append(',');
		}
		theQuery.deleteCharAt(theQuery.lastIndexOf(","));

		appendConditions(theQuery);

		return theQuery.toString();
	}
}
