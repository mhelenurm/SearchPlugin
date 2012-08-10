package org.usd.edu.search;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLAnnotation;


public class TermAddDisplayer extends JPanel {
	private JComboBox _iriComboBox;
	private JComboBox _operatorComboBox;
	private NumberField _textNumberField;
	private OWLOntology _ont;
			
	public TermAddDisplayer(OWLOntology ont) {
		_ont = ont;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screensize = tk.getScreenSize();
		
		_iriComboBox = new JComboBox(getRenderedList());
		_operatorComboBox = new JComboBox(Operator.toList());
		_textNumberField = new NumberField("sample text");
		
		setLayout(null);

		add(_iriComboBox);
		add(_operatorComboBox);
		add(_textNumberField);

		
		_iriComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("go...");
				Object o = _iriComboBox.getSelectedItem();
				_iriComboBox.setModel(new DefaultComboBoxModel(getRenderedList()));
				_iriComboBox.setSelectedItem(o);

			}
		});

		addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				_iriComboBox.setSize((getWidth()-10)/4, getHeight()-10);
				_operatorComboBox.setSize((getWidth()-10)/4, getHeight()-10);
				_textNumberField.setSize((getWidth()-10)/2, getHeight()-10);
				
				_iriComboBox.setLocation(5, 5);
				_operatorComboBox.setLocation((getWidth()-10)/4+5, 5);
				_textNumberField.setLocation((getWidth()-10)/2+5, 5);
			}
		});
		
		_operatorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_textNumberField.setInputNormal(!((Operator)_operatorComboBox.getSelectedItem()).isNumberSpecific());
				
			}
		});
		_textNumberField.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {
				_textNumberField.setSelectionStart(0);
				_textNumberField.setSelectionEnd(_textNumberField.getText().length());
			}
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
	}
	
	
	
	/*
	 This method gets a list of all the OWLAnnotationProperties to be displayed in a list. The list contains every annotation property used in the given ontology. This is a helper method for the display.
	 */
	private IRIWrapper[] getRenderedList() {
		IRIWrapper[] renderedItems = new IRIWrapper[_ont.getAnnotationPropertiesInSignature().size()+1];
		int ct = 0;
		renderedItems[0] = new IRIWrapper(null, null);
		for(OWLAnnotationProperty prop : _ont.getAnnotationPropertiesInSignature()) {
			renderedItems[++ct] = renderAnnotation(prop, _ont);
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