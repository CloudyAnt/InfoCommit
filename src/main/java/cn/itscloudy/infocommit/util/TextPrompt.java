package cn.itscloudy.infocommit.util;

import com.intellij.util.ui.JBUI;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * The TextPrompt class will display a prompt over top of a text component when
 * the Document of the text field is empty. The Show property is used to
 * determine the visibility of the prompt.
 * <p>
 * The Font and foreground Color of the prompt will default to those properties
 * of the parent text component. You are free to change the properties after
 * class construction.
 * <a href="http://www.camick.com/java/source/TextPrompt.java">来源</a>
 */
public class TextPrompt extends JLabel implements FocusListener, DocumentListener {
    public enum Show {
        ALWAYS,
        FOCUS_GAINED,
        FOCUS_LOST
    }

    private final JTextComponent host;
    private final Document document;

    @Getter
    @Setter
    private Show show;
    @Setter
    private boolean showPromptOnce;

    private int focusLost;

    public TextPrompt(String text, JTextComponent host) {
        this(text, host, Show.ALWAYS, true, null);
    }

    public TextPrompt(String text, JTextComponent host, Show show, boolean useCompBorderInsets, Insets insets) {
        this.host = host;
        setShow(show);
        document = host.getDocument();

        setText(text);
        setFont(host.getFont());
        setForeground(host.getForeground());
        if (useCompBorderInsets) {
            setBorder(new EmptyBorder(host.getInsets()));
        } else {
            setBorder(JBUI.Borders.empty(insets.top, insets.left, insets.bottom, insets.right));
        }
        setHorizontalAlignment(JLabel.LEADING);
        setVerticalAlignment(JLabel.TOP);

        host.addFocusListener(this);
        document.addDocumentListener(this);

        host.setLayout(new BorderLayout());
        host.add(this);
        checkForPrompt();
    }

    /**
     * Check whether the prompt should be visible or not. The visibility
     * will change on updates to the Document and on focus changes.
     */
    private void checkForPrompt() {
        //  Text has been entered, remove the prompt

        if (document.getLength() > 0) {
            setVisible(false);
            return;
        }

        //  Prompt has already been shown once, remove it

        if (showPromptOnce && focusLost > 0) {
            setVisible(false);
            return;
        }

        //  Check the Show property and component focus to determine if the
        //  prompt should be displayed.

        if (host.hasFocus()) {
            setVisible(show == Show.ALWAYS
                    || show == Show.FOCUS_GAINED);
        } else {
            setVisible(show == Show.ALWAYS
                    || show == Show.FOCUS_LOST);
        }
    }

//  Implement FocusListener

    public void focusGained(FocusEvent e) {
        checkForPrompt();
    }

    public void focusLost(FocusEvent e) {
        focusLost++;
        checkForPrompt();
    }

//  Implement DocumentListener

    public void insertUpdate(DocumentEvent e) {
        checkForPrompt();
    }

    public void removeUpdate(DocumentEvent e) {
        checkForPrompt();
    }

    public void changedUpdate(DocumentEvent e) {
    }
}