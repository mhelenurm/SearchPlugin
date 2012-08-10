package org.usd.edu.search;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.tree.OWLObjectTree;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


public class SearchComponent extends AbstractOWLViewComponent implements OWLOntologyChangeListener {
	private static final long serialVersionUID = -4515710047558710080L;
	private SearchTree _tree;
	private TermAddDisplayer _disp1;

	@Override
	protected void initialiseOWLView() throws Exception {
		_tree = null;
		setLayout(null);
		
		_disp1 = new TermAddDisplayer(getOWLModelManager().getActiveOntology());
		_disp1.setLocation(5, 5);
		
		add(_disp1);
		
		addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				_disp1.setSize(getWidth()-10, 40);
			}
		});
	}
    
	public void doSearch()
	{
		if(_tree == null)
			return;
		
		List ls = new ArrayList<OWLClass>();
		
		OWLOntology activeOntology = getOWLModelManager().getActiveOntology();
		for (OWLClass cls : activeOntology.getClassesInSignature()) {
			for (OWLAnnotation annotation : cls.getAnnotations(activeOntology)) {
				if (annotation.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) annotation.getValue();
					_tree.test(annotation.getProperty().getIRI(), val.getLiteral());
				}
			}
			if(_tree.evaluate()) //if the tree evaluates to true (enough properties are satisfied)
				if(!ls.contains(cls)) //if the list doesn't already contain the element (I don't know Protege well enough to know about the possibilities of double-entered data. Might as well be cautious!)
					ls.add(cls); //add the class to the results

			_tree.reset(); //make sure we don't reuse results from the last search
		}
	}
	
	public void ontologiesChanged(java.util.List<? extends org.semanticweb.owlapi.model.OWLOntologyChange> changelist) {
		System.out.println("yeah");
	}

	@Override
	protected void disposeOWLView() {
	}
}
