import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.text.BadLocationException;

class Editor {
    private RSyntaxTextArea textArea;
    private Snippet snippetToEdit;

    Editor(Snippet snippet) {
        snippetToEdit = snippet;

        textArea = new RSyntaxTextArea(snippetToEdit.getContent());
        textArea.setSyntaxEditingStyle("text/"+snippetToEdit.getLanguage().toLowerCase());

        JFrame frame = new JFrame("Snippo: Editing " + snippetToEdit.getTitle());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Snippet");
        JMenuItem saveButton = new JMenuItem("Save");
        JMenuItem quitButton = new JMenuItem("Quit");

        saveButton.addActionListener(e -> onSave());
        quitButton.addActionListener(e -> frame.dispose());

        menu.setMnemonic('s');
        saveButton.setMnemonic('s');
        quitButton.setMnemonic('q');

        menu.add(saveButton);
        menu.add(quitButton);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        frame.add(textArea);

        frame.setSize(500, 500);
        frame.setVisible(true);

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

    private void onSave() {
        snippetToEdit.setContent(getFullEditorContent());
        System.out.println("Edited snippet"+snippetToEdit.getTitle()+"saved");
    }
}
