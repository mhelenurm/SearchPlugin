package org.usd.edu.search;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

//import org.apache.log4j.Logger;
import org.protege.editor.owl.model.hierarchy.AssertedClassHierarchyProvider;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.tree.OWLObjectTree;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLClass;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.protege.editor.core.editorkit.*;

import org.protege.editor.core.ui.util.Icons;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

public class SearchComponent extends AbstractOWLViewComponent {
    private static final long serialVersionUID = -4515710047558710080L;
        
    @Override
    protected void initialiseOWLView() throws Exception {
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentMoved(ComponentEvent e) {}
            public void componentResized(ComponentEvent e) {}
        });
    }
    
    public void doSearch()
    {
        ArrayList<OWLClass> thissearch = new ArrayList<OWLClass>();
        int result = 0;
        int resultt = 0;
        _resultPanel.removeAll();
        
        String searchText = _searchInputField.getText();
        if(!SearchTools.verifySearchString(searchText)) {//YES YES YES YES
            JOptionPane.showMessageDialog(null, SearchTools.getError(), "Syntax Error", JOptionPane.ERROR_MESSAGE);//YES YES YES YES
            return;//YES YES YES YES
        }
        SearchTree tree = SearchTools.buildTree(searchText);//YES YES YES YES
        
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLDataFactory df = man.getOWLDataFactory();
        OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
            

        OWLOntology activeOntology = getOWLModelManager().getActiveOntology();
        for (OWLClass cls : (_previousSearch.isSelected())?_previousResults:activeOntology.getClassesInSignature()) { //for each class in the current ontology...
            for (OWLAnnotation annotation : cls.getAnnotations(activeOntology)) { //here's where we run the matcher to find stuff  
                if (annotation.getValue() instanceof OWLLiteral) {
                    OWLLiteral val = (OWLLiteral) annotation.getValue();
                    String lit = val.getLiteral().toLowerCase().trim();
                    //if it has a label, use that instead
                    String name = trimIRI(annotation.getProperty().getIRI() + "");
                    
                    for(OWLAnnotation anno2 : annotation.getProperty().getAnnotations(activeOntology)) {
                        if(anno2.getProperty().isLabel()) {
                            if (anno2.getValue() instanceof OWLLiteral) {
                                OWLLiteral val2 = (OWLLiteral) anno2.getValue();
                                String lit2 = val2.getLiteral();
                                name = lit2;
                            }
                        }
                    }
                    tree.testAnnotation(name, val.getLiteral().toLowerCase().trim()); //YES YES YES YES
                }
                
            }
            String name = trimIRI(cls.getIRI() + "");
            
            for(OWLAnnotation anno : cls.getAnnotations(activeOntology, label)) {
                if (anno.getValue() instanceof OWLLiteral) {
                    OWLLiteral val = (OWLLiteral) anno.getValue();
                    String lit = val.getLiteral();
                    name = lit;
                }
            }
            if(tree.evaluate()) { //YES YES YES YES
                if(result<20) {
                    thissearch.add(cls);
                    SearchResult resultdisplay = new SearchResult(name, "" + cls, getWidth()-40, 70);
                    resultdisplay.setLocation(5, 5 + result*75);
                    _resultPanel.add(resultdisplay);
                    _resultPanel.setPreferredSize(new Dimension(getWidth(), 80 + result*75));
                    _resultPanel.repaint();
                    _searchResultPanel.repaint();
                    result++;
                }
                resultt++;
            }
            tree.clear();
        }
        _resultCountLabel.setText("Showing " + result + " results out of " + resultt);
        _previousResults = thissearch;
        _searchResultPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        _searchResultPanel.repaint();
    }

	@Override
	protected void disposeOWLView() {
	}

}
