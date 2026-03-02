package org.example.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

class NumericDocumentFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[1-9]")) {
            super.replace(fb, 0, fb.getDocument().getLength(), text, attrs);
        } else if (text.isEmpty()) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}