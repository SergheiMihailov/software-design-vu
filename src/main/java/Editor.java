import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.text.BadLocationException;

class Editor {
    private JFrame frame;
    private RSyntaxTextArea textArea;

    Editor(Snippet snippet) {
        frame = new JFrame("Snippo: Editing " + snippet.getTitle());
        textArea = new RSyntaxTextArea(snippet.getContent());
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Snippet");
        menu.setMnemonic('s');
        JMenuItem saveButton = new JMenuItem("Save");
        saveButton.setMnemonic('s');
        JMenuItem quitButton = new JMenuItem("Quit");
        quitButton.setMnemonic('q');

        menu.add(saveButton);
        menu.add(quitButton);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        frame.add(textArea);

        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.requestFocus();

        saveButton.addActionListener(e -> {
            snippet.setContent(getFullEditorContent());
            System.out.println("saved");
        });

        quitButton.addActionListener(e -> frame.dispose());

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
    }

    private String getFullEditorContent() {
        int len = textArea.getDocument().getLength();
        try {
            return textArea.getDocument().getText(0,len);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}