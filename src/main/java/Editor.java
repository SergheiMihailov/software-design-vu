import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.*;

class Editor {
    private JFrame frame;
    private RSyntaxTextArea textArea;

    Editor(Snippet snippet, SnippetManager snippetManager) {
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

        saveButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             snippet.setContent(getFullEditorContent());
                                             snippetManager.writeSnippetsToJson();
                                         }
                                     }
        );

        quitButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             JFrame frame2 = new JFrame();
                                             String[] options = new String[2];
                                             options[0] = new String("Save");
                                             options[1] = new String("Cancel");
                                             int test = JOptionPane.showOptionDialog(frame.getContentPane(),"Do you wan to save current content?","Save?", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
                                             if (test == 0) {
                                                 snippet.setContent(getFullEditorContent());
                                                 snippetManager.writeSnippetsToJson();
                                             }
                                             frame.dispose();
                                         }
                                     }
        );

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
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
