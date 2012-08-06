package org.usd.edu.search;

import org.semanticweb.owlapi.model.IRI;

public class IRIWrapper {
	private IRI _iri;
	private String _label;
	
	public IRIWrapper(IRI iri, String label) {
		_label = label;
		_iri = iri;
	}
	
	public IRI getIRI() {
		return _iri;
	}
	
	/*
	 Acts as a rendering agent
	 */
	public String toString() {
		if(_label == null && _iri==null)
			return "anything";
		if(_label!=null)
			return "[" + _label+ "] " +_iri;
		else
			return _iri.toString();
	}
}