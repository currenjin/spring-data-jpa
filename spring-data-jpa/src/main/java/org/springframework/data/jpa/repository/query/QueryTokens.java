/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.query;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Supplier;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Utility class to create query tokens.
 *
 * @author Mark Paluch
 * @since 3.4
 */
class QueryTokens {

	/**
	 * Commonly use tokens.
	 */
	static final QueryToken EMPTY_TOKEN = token("");
	static final QueryToken TOKEN_COMMA = token(", ");
	static final QueryToken TOKEN_SPACE = token(" ");
	static final QueryToken TOKEN_DOT = token(".");
	static final QueryToken TOKEN_EQUALS = token(" = ");
	static final QueryToken TOKEN_OPEN_PAREN = token("(");
	static final QueryToken TOKEN_CLOSE_PAREN = token(")");
	static final QueryToken TOKEN_NEW = expression("new");
	static final QueryToken TOKEN_ORDER_BY = expression("order by");
	static final QueryToken TOKEN_LOWER_FUNC = token("lower(");
	static final QueryToken TOKEN_SELECT_COUNT = token("select count(");
	static final QueryToken TOKEN_COUNT_FUNC = token("count(");
	static final QueryToken TOKEN_DOUBLE_PIPE = token(" || ");
	static final QueryToken TOKEN_OPEN_SQUARE_BRACKET = token("[");
	static final QueryToken TOKEN_CLOSE_SQUARE_BRACKET = token("]");
	static final QueryToken TOKEN_COLON = token(":");
	static final QueryToken TOKEN_DASH = token("-");
	static final QueryToken TOKEN_QUESTION_MARK = token("?");
	static final QueryToken TOKEN_OPEN_BRACE = token("{");
	static final QueryToken TOKEN_CLOSE_BRACE = token("}");
	static final QueryToken TOKEN_DOUBLE_UNDERSCORE = token("__");
	static final QueryToken TOKEN_AS = expression("AS");
	static final QueryToken TOKEN_DESC = expression("desc");
	static final QueryToken TOKEN_ASC = expression("asc");
	static final QueryToken TOKEN_WITH = expression("WITH");
	static final QueryToken TOKEN_NOT = expression("NOT");
	static final QueryToken TOKEN_MATERIALIZED = expression("materialized");

	/**
	 * Creates a {@link QueryToken token} from an ANTLR {@link TerminalNode}.
	 */
	static QueryToken token(TerminalNode node) {
		return token(node.getText());
	}

	/**
	 * Creates a {@link QueryToken token} from an ANTLR {@link Token}.
	 */
	static QueryToken token(Token token) {
		return token(token.getText());
	}

	/**
	 * Creates a {@link QueryToken token} from a string {@code token}.
	 */
	static QueryToken token(String token) {
		return new SimpleQueryToken(token);
	}

	/**
	 * Creates a {@link QueryToken expression} from an ANTLR {@link TerminalNode}.
	 */
	static QueryToken expression(TerminalNode node) {
		return expression(node.getText());
	}

	/**
	 * Creates a {@link QueryToken expression} from an ANTLR {@link Token}.
	 */
	static QueryToken expression(Token token) {
		return expression(token.getText());
	}

	/**
	 * Creates a {@link QueryToken token} from a string {@code expression}.
	 */
	static QueryToken expression(String expression) {
		return new ExpressionToken(expression);
	}

	/**
	 * A value type used to represent a JPA query token. NOTE: Sometimes the token's value is based upon a value found
	 * later in the parsing process, so the text itself is wrapped in a {@link Supplier}.
	 *
	 * @author Greg Turnquist
	 * @author Christoph Strobl
	 * @since 3.1
	 */
	static class SimpleQueryToken implements QueryToken, QueryTokenStream {

		/**
		 * The text value of the token.
		 */
		private final String token;

		SimpleQueryToken(String token) {
			this.token = token;
		}

		public String value() {
			return token;
		}

		@Override
		public final boolean equals(Object object) {
			if (this == object) {
				return true;
			}

			if (!(object instanceof QueryToken that)) {
				return false;
			}

			return value().equalsIgnoreCase(that.value());
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Iterator<QueryToken> iterator() {
			return Collections.singleton((QueryToken) this).iterator();
		}

		@Override
		public boolean isExpression() {
			return false;
		}

		@Override
		public int hashCode() {
			return value().hashCode();
		}

		@Override
		public String toString() {
			return value();
		}
	}

	static class ExpressionToken extends SimpleQueryToken {

		ExpressionToken(String token) {
			super(token);
		}

		@Override
		public boolean isExpression() {
			return true;
		}
	}

}
