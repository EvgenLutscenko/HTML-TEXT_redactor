package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view1 = new View();
        Controller controller = new Controller(view1);

        view1.setController(controller);
        view1.init();
        controller.init();
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void resetDocument() {
        if (document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }

        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text) {
        resetDocument();
        StringReader reader = new StringReader(text);

        try {
            new HTMLEditorKit().read(reader, document, 0);
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        StringWriter writer = new StringWriter();

        try {
            new HTMLEditorKit().write(writer, document, 0, document.getLength());
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }

        return writer.toString();
    }

    public void init() {
        createNewDocument();
    }

    public void exit() {
        System.exit(0);
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        currentFile = null;
    }

    public void openDocument() {
        try {
            view.selectHtmlTab();
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new HTMLFileFilter());

            if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());

                FileReader reader = new FileReader(currentFile);
                new HTMLEditorKit().read(reader, document, 0);

                view.resetUndo();

            }
        }catch(IOException | BadLocationException e){
            ExceptionHandler.log(e);
        }
    }

    public void saveDocument() {
        try {
            view.selectHtmlTab();

            if (currentFile != null) {
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
                writer.write(getPlainText());
            }else {
                saveDocumentAs();
            }
        }catch(IOException | BadLocationException e){
            ExceptionHandler.log(e);
        }
    }

    public void saveDocumentAs() {
        try {
            view.selectHtmlTab();
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new HTMLFileFilter());

            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                view.setTitle(currentFile.getName());

                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
                writer.write(getPlainText());
            }
        }catch(IOException | BadLocationException e){
            ExceptionHandler.log(e);
        }
    }
}
