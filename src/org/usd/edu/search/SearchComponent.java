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
    
    //private static final Logger log = Logger.getLogger(SearchComponent.class);
    
    private JTextField _searchInputField;
    private JScrollPane _searchResultPanel;
    private JRadioButton _previousSearch;
    private JPanel _resultPanel;
    private JButton _searchButton;
    private JLabel _resultCountLabel;
    
    private JButton _readmebutton;
    private JLabel helpLabel;
    private ReadmeViewer _readmeframe;
    
    private ArrayList<OWLClass> _previousResults;
    private AbstractOWLViewComponent self;
    
    @Override
    protected void initialiseOWLView() throws Exception {
        
        self = this;
        setMinimumSize(new Dimension(200, 300));
        
        _previousResults = new ArrayList<OWLClass>();
        
        setLayout(null);

        _searchInputField = new JTextField();
        _searchInputField.setLocation(0, 30);
        _searchInputField.setToolTipText("Example: \"comment contains \"I like puppies\"\"");
        _searchInputField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch();
                }
            }
            public void keyReleased(KeyEvent e) {
                
            }
            public void keyTyped(KeyEvent e) {
                
            }
        });
        
        
        _searchInputField.setSize(300, 30);
        add(_searchInputField);
        
        _resultCountLabel = new JLabel("Showing 0 results out of 0");
        _resultCountLabel.setSize(200, 30);
        add(_resultCountLabel);
        
        _previousSearch = new JRadioButton();
        _previousSearch.setLocation(0, 0);
        _previousSearch.setSize(30, 30);
        add(_previousSearch);
        
        _readmeframe = new ReadmeViewer("PURPOSE:\n\nThe Advanced Annotation Search plugin allows user to perform logical search operations on large ontologies to find the entities they need based on existing annotation information.\n\nEXAMPLES:\n\n#1\nIf you know the comment of the entity you want to find contains the string \"I like puppies\", you can use the query (minus the outer quotations): \"comment contains \"i like puppies\"\"\n\n#2\nIf you know the entity has an \"age\" annotation and you know the age of the entity is greater than 3 but less than 9, you can type: \"age > 3 and age < 9\"\n\nSYNTAX:\n\n->	capitalization DOES NOT MATTER. (if you think this should change, please contact me via email at mark.helenurm@usd.edu)\n->	items in QUOTATIONS (\"\") are treated as single terms. If you're searching for a comment containing a string, PUT IT IN QUOTES!\n->	specific search \"terms\" are done in triples.\n->	A triple is in the format: [ANNOTATIONNAME] [OPERATOR] [VALUE]\n->	the ANNOTATIONNAME is the type of annotation (e.g. comment, label, age, etc.)\n->	ANNOTATIONNAME and VALUE should NEVER be any of the LOGICAL or normal OPERATORS described below\n->	if VALUE happens to be the same as a OPERATOR or LOGICAL OPERATOR, put it in quotes (\"\")\n->	the OPERATORS and their appropriate VALUES are described below\n\nALL OPERATORS:\n\nthe OPERATOR column describes the type of operation\nthe USE NAMES column describes the keywords you can use to reference the operator\nthe INPUT column describes what values are appropriate to use with it\n\nOPERATOR			USE NAMES					INPUT VALUE\n----------------------------------------------------------------------------------------------------------\ncontains			contains, ~					String Literals\ndoesn't contain			doesntcontain, !~				String Literals\nequals				equals, isequalto, =, ==			Numbers, String Literals\ndoesn't equal			doesntequal, !=					Numbers, String Literals\ngreater than			greaterthan, >					Numbers\nless than			lessthan, <					Numbers\ngreater than or equal to	greaterthanorequalto, gequalto, gequal, >=	Numbers\nless than or equal to		lessthanorequalto, lequalto, lequal, <=		Numbers\n-----------------------------------------------------------------------------------------------------------\n\nLOGICAL OPERATORS: (these separate specific triple-terms)\n\n->	the ORDER OF OPERATIONS corresponds to the positions on the chart below. (e.g. terms in parentheses are evaluated before AND operations, which are evaluated before OR operations, etc.)\n\nLOGICAL OPERATOR		USE NAMES\n----------------------------------------------------------\nparentheses			(, )\nand				&, &&, and\nexclusive or			^, xor, exclusiveor\nor				|, ||, or\n----------------------------------------------------------\n\n\nCONTACT INFO:\n\nEmail:	Mark.Helenurm@usd.edu\n\nBugs, comments, or complaints? Feel free to email!\n");
        _readmebutton = new JButton("?");
        _readmebutton.setLocation(375, 0);
        _readmebutton.setSize(30, 30);
        add(_readmebutton);
        
        _readmebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                _readmeframe.setVisible(true);
            }
        });
        
        helpLabel = new JLabel("Help");
        helpLabel.setLocation(330, 0);
        helpLabel.setSize(50, 30);
        add(helpLabel);
        
        JLabel prevLabel = new JLabel("Search from results");
        prevLabel.setLocation(30, 0);
        prevLabel.setSize(150, 30);
        add(prevLabel);
        
        _searchButton = new JButton("Search!");
        _searchButton.setLocation(330, 30);
        _searchButton.setSize(80, 30);
        add(_searchButton);
        
        _resultPanel = new JPanel();
        _resultPanel.setLayout(null);
        _resultPanel.setBackground(new Color(192, 192, 192));
        
        _searchResultPanel = new JScrollPane(_resultPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        _searchResultPanel.setLocation(5, 65);
        _searchResultPanel.setSize(400, 200);

        
        _searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                doSearch();
            }
        });
        add(_searchResultPanel);
                
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentMoved(ComponentEvent e) {}
            
            public void componentResized(ComponentEvent e) {
                _resultCountLabel.setLocation(getWidth()/2-100, 0);
                _searchResultPanel.setSize(getWidth()-10, getHeight()-65);
                _searchInputField.setSize(getWidth()-110, 30);
                _searchButton.setLocation(getWidth()-80, 30);
                helpLabel.setLocation(getWidth()-80, 0);
                _readmebutton.setLocation(getWidth()-35, 0);
                _searchResultPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                _searchResultPanel.repaint();
            }
        });
        
        //log.info("Search Component initialized");
    }
    
    public String trimIRI(String iri) { //basically goes back to the last ':', '/', or '#' in the string and cuts it off from there
        int index = 0;
        for(int i = iri.length()-1; i>=0; i--) {
            char c = iri.charAt(i);
            if(c == ':' || c=='/' || c == '#') {
                index = i+1;
                break;
            }
        }
        return iri.substring(index);
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
    
    public String getString(IRI iri)
    {
        String s = iri.toString();
        s = s.substring(s.indexOf("#")+1);
        return s;
    }
    
    class SearchResult extends JPanel 
    {
        private JLabel _classLabel;
        private JScrollPane _scroller;
        private JLabel _annotationLabel;
        
        public SearchResult(String className, String annotation, int width, int height)
        {
            setLayout(null);
            setBackground(new Color(255, 255, 255));
            _classLabel = new JLabel(className);
            String defaultName = _classLabel.getFont().getName();
            defaultName = "Courier";
            _classLabel.setBackground(new Color(255, 250, 209));
            _classLabel.setFont(new Font(defaultName, Font.BOLD, 15));
            _classLabel.setLocation(5, 5);
            _classLabel.setSize(width-10, 20);
            
            
            _annotationLabel = new JLabel(annotation);
            //_annotationLabel.setBackground(new Color(207, 255, 252));
            _annotationLabel.setFont(new Font(defaultName, Font.PLAIN, 13));
            
            _scroller = new JScrollPane(_annotationLabel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            _scroller.setLocation(5, 20);
            _scroller.setSize(width-10, 50);
            //_annotationLabel.setEditable(false);
            add(_scroller);
            add(_classLabel);
            setSize(width,height);
        }
    }
    
    class ReadmeViewer extends JFrame {
        String _toRead;
        public ReadmeViewer(String toRead) {
            _toRead = toRead;
            setTitle("README.txt");
            JTextArea area = new JTextArea(_toRead);
            area.setEditable(false);
            JScrollPane pane = new JScrollPane(area);
            add(pane);
        }
    }

	@Override
	protected void disposeOWLView() {
        _searchInputField = null;
        _searchButton = null;
	}

}
