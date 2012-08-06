package org.usd.edu.search;

public class Operator {
	public static final int CONTAINS	= 0;
	public static final int DOESNTCONTAIN	= 1;
	public static final int EQUALS		= 2;
	public static final int DOESNTEQUAL	= 3;
	public static final int GREATERTHAN	= 4;
	public static final int GEQUALTO	= 5;
	public static final int LESSTHAN	= 6;
	public static final int LEQUALTO	= 7;
	private static final String _names[] = {"contains", "doesn't contain", "equal", "not equal", ">", ">=", "<", "<="};
	private static final Operator[] _ops = {new Operator(0), new Operator(1), new Operator(2), new Operator(3), new Operator(4), new Operator(5), new Operator(6), new Operator(7)};

	private int _operator;

	private Operator(int op) { _operator = op; }
	public static Operator[] toList() { return _ops; }
	public int getOp() { return _operator; }
	public boolean isNegative() { return _operator == 1 || _operator == 3 || _operator == 6 || _operator == 7; }
	public boolean isNumberSpecific() {return _operator>=4; }
	public String toString() { return _names[_operator%8]; }
}
