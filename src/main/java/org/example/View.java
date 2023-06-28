package org.example;
import org.example.listeners.FrameListener;
import org.example.listeners.TabbedPaneChangeListener;
import org.example.listeners.UndoListener;

import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View(){
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        }catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);
        }

    }

    public UndoableEditListener getUndoListener() {
        return undoListener;
    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected(){
        if(tabbedPane.getSelectedIndex() == 0){
            return true;
        }
        return false;
    }

    public void undo(){
        try {
            undoManager.undo();
        }catch (CannotUndoException e){
            ExceptionHandler.log(e);
        }
    }

    public void redo(){
        try {
            undoManager.redo();
        }catch (CannotRedoException e){
            ExceptionHandler.log(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comand = e.getActionCommand();

        if(comand.equals("Новый")){
            controller.createNewDocument();
        }else if(comand.equals("Открыть")){
            controller.openDocument();
        }else if(comand.equals("Сохранить")){
            controller.saveDocument();
        }else if(comand.equals("Выход")){
            controller.exit();
        }else if(comand.equals("Сохранить как...")){
            controller.saveDocumentAs();
        }else if(comand.equals("О программе")){
            showAbout();
        }
    }

    public void initMenuBar(){
        JMenuBar bar = new JMenuBar();
        MenuHelper.initFileMenu(this, bar);
        MenuHelper.initEditMenu(this, bar);
        MenuHelper.initStyleMenu(this, bar);
        MenuHelper.initAlignMenu(this, bar);
        MenuHelper.initColorMenu(this, bar);
        MenuHelper.initFontMenu(this, bar);
        MenuHelper.initHelpMenu(this, bar);

        getContentPane().add(bar, BorderLayout.NORTH);
    }

    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane scrollhtml = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", scrollhtml);
        JScrollPane scrolltext = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", scrolltext);

        tabbedPane.setPreferredSize(new Dimension(400, 300));
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

    public Controller getController() {
        return controller;
    }

    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
    }

    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update(){
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout(){
        JOptionPane.showMessageDialog(null, "программа написана мною Евгением Луценко в июне 23 года\n" +
                "програма представляет возможность редактирования html документа\n" +
                "можете пользоватся.......","информация", JOptionPane.INFORMATION_MESSAGE);

    }

    public void exit(){
        controller.exit();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void selectedTabChanged() {
        if(tabbedPane.getSelectedIndex() == 0){
            controller.setPlainText(plainTextPane.getText());
        }else if(tabbedPane.getSelectedIndex() == 1){
            plainTextPane.setText(controller.getPlainText());
        }

        resetUndo();
    }

    public boolean canUndo() {return undoManager.canUndo();}

    public boolean canRedo() {return undoManager.canRedo();}
}
