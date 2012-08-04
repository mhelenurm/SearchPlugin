package org.usd.edu.search;

import java.util.ArrayList;
import org.semanticweb.owlapi.model.IRI;
/*
 This class interfaces with the doSearch() method, it allows for dynamic expression matching and reseting
 */
public class SearchTree {
	private ArrayList<Expression> _explist;
	private Evaluatable _root;
	
	public SearchTree(ArrayList<BaseOb> input) {
		_explist = new ArrayList<Expression>();
		for(BaseOb b : input)
			if(b instanceof Expression)
				_explist.add((Expression)b);
		_root = parseBit(input, 0);
	}
	
	private Evaluatable parseBit(ArrayList<BaseOb> in, int start) {
		ArrayList<Evaluatable> evals = new ArrayList<Evaluatable>();
		ArrayList<LogicalOperator> ops = new ArrayList<LogicalOperator>();
		
		int level = 0;
		for(int i = start; i < in.size(); i++) {
			BaseOb ob = in.get(i);
			if(ob instanceof LParenthesis) {
				level++;
				evals.add(parseBit(in, i+1));
			} else if(ob instanceof RParenthesis) {
				level--;
				if(level<0)
					break;
			} else if(level == 0) {
				if(ob instanceof LogicalOperator)
					ops.add((LogicalOperator)ob);
				if(ob instanceof Expression)
					evals.add((Expression)ob);
			}
		}
		
		while(evals.size()>1) {
			Evaluatable evs[] = {evals.get(0), evals.get(1)};
			LogicalOperator o = ops.get(0);
			o.populate(evs);
			evals.remove(0);
			evals.remove(0);
			ops.remove(0);
			evals.add(0, o);
		}
		return evals.get(0);
	}
	
	public boolean evaluate() {
		return _root.evaluate();
	}
	public void test(IRI iri, String val) {
		for(Expression ex : _explist) ex.test(iri, val);
	}
	
	public void reset() {
		for(Expression ex : _explist) ex.reset();
	}
}
