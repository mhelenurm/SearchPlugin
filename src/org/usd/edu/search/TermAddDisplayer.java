package org.usd.edu.search;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFrame;

import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLAnnotation;


public class TermAddDisplayer extends JFrame {
	/*
	 |----------------------------------|
	 |	      ADD TERM              |
	 |----------------------------------|
	 |                                  |
	 |Annotation IRI:                   |
	 |     (      Combo Box        )    |
	 |                                  |
	 |Action:                           |
	 |     (      Combo Box        )    |
	 |                                  |
	 |Text:                             |
	 |     (      Text Field       )    |
	 |                                  |
	 |                                  |
	 |                                  |
	 |     (        Button         )    |
	 |----------------------------------|
	 */
	
	private JComboBox _iriComboBox;
	private JComboBox _operatorComboBox;
	private NumberField _textNumberField;
	private JButton _returnButton;
	
	private Middleman _middleman;
		
	public TermAddDisplayer(OWLOntology ont, Middleman m) {
		_middleman = m;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screensize = tk.getScreenSize();
		
		setTitle("Add Term");
		setSize(300, 421);
		setLocation((int)(screensize.getWidth()/2-300/2), (int)(screensize.getHeight()/2-521/2));
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_iriComboBox = new JComboBox(getRenderedList(ont));
		_operatorComboBox = new JComboBox(Operator.toList());
		_textNumberField = new NumberField("text");
		_returnButton = new JButton("Okay!");
		
		JLabel tempiri = new JLabel("Annotation IRI:");
		JLabel tempaction = new JLabel("Action:");
		JLabel temptext = new JLabel("Text:");
		
		tempiri.setSize(290, 30);
		tempaction.setSize(290, 30);
		temptext.setSize(290, 30);
		_iriComboBox.setSize(290, 30);
		_operatorComboBox.setSize(290, 30);
		_textNumberField.setSize(290, 30);
		_returnButton.setSize(290, 50);
		
		tempiri.setLocation(5, 5);
		_iriComboBox.setLocation(5, 40);
		tempaction.setLocation(5, 75);
		_operatorComboBox.setLocation(5, 110);
		temptext.setLocation(5, 145);
		_textNumberField.setLocation(5, 180);
		_returnButton.setLocation(5, 421-21-55);
		
		getContentPane().setLayout(null);
		
		getContentPane().add(tempiri);
		getContentPane().add(tempaction);
		getContentPane().add(temptext);
		getContentPane().add(_iriComboBox);
		getContentPane().add(_operatorComboBox);
		getContentPane().add(_textNumberField);
		getContentPane().add(_returnButton);
		
		_operatorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((Operator)_operatorComboBox.getSelectedItem()).isNumberSpecific()) {
					_textNumberField.setInputNormal(NumberField.DOCUMENT_NUMBERS);
				} else {
					_textNumberField.setInputNormal(NumberField.DOCUMENT_ALLTEXT);
				}
			}
		});
		
		_returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Expression exp;
				if(_iriComboBox.getSelectedItem() == null)
					exp = null;
				else
					exp = new Expression(((IRIWrapper)_iriComboBox.getSelectedItem()).getIRI(), (Operator)_operatorComboBox.getSelectedItem(), _textNumberField.getText());
				_middleman.put(exp);
				setVisible(false);
			}
		});
		
	}
	
	
	
	/*
	 This method gets a list of all the OWLAnnotationProperties to be displayed in a list. The list contains every annotation property used in the given ontology. This is a helper method for the display.
	 */
	private IRIWrapper[] getRenderedList(OWLOntology ontology) {
		IRIWrapper[] renderedItems = new IRIWrapper[ontology.getAnnotationPropertiesInSignature().size()];
		int ct = 0;
		for(OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			renderedItems[ct++] = renderAnnotation(prop, ontology);
		}
		return renderedItems;
	}
	
	/*
	 This method takes an annotation property and outputs its label (if applicable), with the IRI string appended
	 */
	private IRIWrapper renderAnnotation(OWLAnnotationProperty p, OWLOntology ontology) {
		String labelss = null;
		for(OWLAnnotation o : p.getAnnotations(ontology)) {
			if(o.getProperty().isLabel()) {
				labelss = ((OWLLiteral)o.getValue()).getLiteral();
			}
		}
		return new IRIWrapper(p.getIRI(), labelss);
	}
}