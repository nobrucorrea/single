package br.com.alertafiscal.opusprimum.log.impl;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

/**
 *
 * @author bastosbf
 */
public class LoggerGUI extends LoggerAbstrato {

    private JTextArea textArea;
    private JLabel label;

    public LoggerGUI(JTextArea textArea, JLabel label) {
        this.textArea = textArea;
        this.label = label;
    }
    
    public LoggerGUI(JTextArea textArea) {
        this.textArea = textArea;
        //this.label = label;
    }

    

    @Override
    public void info(String string, Object... os) {
        super.info(string, os);
        textArea.append(String.format(string, os) + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
        trunkTextArea();
    }

    @Override
    public void trace(String string, Object... os) {
        super.trace(string, os);
        label.setText(String.format(string, os));
    }

    final int SCROLL_BUFFER_SIZE = 100;

    public void trunkTextArea() {
        int numLinesToTrunk = textArea.getLineCount() - SCROLL_BUFFER_SIZE;
        if (numLinesToTrunk > 0) {
            try {
                int posOfLastLineToTrunk = textArea.getLineEndOffset(numLinesToTrunk - 1);
                textArea.replaceRange("", 0, posOfLastLineToTrunk);
            } catch (BadLocationException ex) {
                error("Erro no TextArea", ex);
            }
        }
    }

}
