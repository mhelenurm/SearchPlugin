package org.usd.edu.search;

import org.semanticweb.owlapi.model.OWLOntology;

public class TermRunner implements Runnable {
	private OWLOntology _o;
	private Middleman _m;
	
	public TermRunner(OWLOntology o, Middleman m) {
		_o = o;
		_m = m;
	}
	
	public void run() {
		TermAddDisplayer t = new TermAddDisplayer(_o, _m);
		t.setVisible(true);
	}
}