package org.usd.edu.search;

public class LogicalOperator extends BaseOb implements Evaluatable {
	public static final int AND = 0;
	public static final int XOR = 1;
	public static final int OR  = 2;

	private static final String[] _names = {"and", "xor", "or"};
	private static final LogicalOperator _allops[] = {new LogicalOperator(0), new LogicalOperator(1), new LogicalOperator(2)};	

	private int _operator;
	private Evaluatable _eval[];

	public LogicalOperator(int op) { _operator = op; }
	public LogicalOperator[] list() { return _allops; }
	public void populate(Evaluatable eval[]) { _eval = eval; }
	public boolean evaluate() {
		boolean op1 = _eval[0].evaluate();
		boolean op2 = _eval[1].evaluate();
		switch(_operator) {
			case AND:
				return op1&&op2;
			case XOR:
				return op1^op2;
			case OR:
				return op1||op2;
		}
		return false;
	}
	public String toString() { return _names[_operator%3]; }
}
