package org.usd.edu.search;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument;

public class NumberField extends JTextField {

	public NumberField(String text) {
		super(text);
		setInputNormal(true);
	}

	public void setInputNormal(boolean isText) {
		if(isText) {
			String s = getText();
			setDocument(new NormalDocument());
			setText(s);
		} else {
			try {
				Double.parseDouble(getText());
			} catch(Exception e) {
				setText("");
			}
			setDocument(new NumberDocument());
		}
	}

	static class NumberDocument extends PlainDocument {
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			String official = "";

			AbstractDocument.Content content = getContent();
			String con = content.getString(0, content.length());
			
			boolean allowsperiods = !(con.contains(".") || con.contains(","));

			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if(i==0&&offs==0 && c == '-' && !con.contains("-")) {
					official += c;
				}
				if (c >= '0' && c <= '9') {
					official += c;
				}
				if ((c == '.' || c == ',') && allowsperiods) {
					official += c;
				}
			}
			super.insertString(offs, official, a);
		}
	}
	
	static class NormalDocument extends PlainDocument {
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			
			super.insertString(offs, str, a);
		}
	}
}
