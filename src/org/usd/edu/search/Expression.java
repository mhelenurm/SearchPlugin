package org.usd.edu.search;

import org.semanticweb.owlapi.model.IRI;

public class Expression extends BaseOb implements Evaluatable {
	private IRI _iri;
	private Operator _op;
	private String _exp;
	private boolean _output;

	public Expression(IRI iri, Operator op, String exp) {
		_iri = iri;
		_op = op;
		_exp = exp;
		_output = false;
	}

	public void test(IRI iri, String val) {
		if(iri!=_iri)
			return;
		boolean bothnums = true;
		double val1 = 0, val2 = 0;
		try {
			val1 = Double.parseDouble(_exp);
			val2 = Double.parseDouble(val);
		} catch(Exception e) {
			bothnums = false;
		}

		int op = _op.getOp();
		if(op == Operator.CONTAINS || op == Operator.DOESNTCONTAIN) {
			if(val.contains(_exp)) {
				_output = true;
			}
		}
		if(op == Operator.EQUALS || op == Operator.DOESNTEQUAL) {
			if(bothnums) {
				if(val1 == val2)
					_output = true;
			} else {
				if(val.equals(_exp))
					_output = true;
			}
		}
		if(op == Operator.GREATERTHAN || op == Operator.LEQUALTO) {
			if(bothnums)
				if(val2>val1)
					_output = true;
		}
		if(op == Operator.LESSTHAN || op == Operator.GEQUALTO) {
			if(bothnums)
				if(val2>=val1)
					_output = true;
		}
	}
	public void reset() { _output = false; }
	public boolean evaluate() { return _op.isNegative()?!_output:_output; }
}
