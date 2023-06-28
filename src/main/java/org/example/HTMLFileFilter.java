package org.example;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if ((f.getName().toLowerCase().endsWith(".htm") || f.getName().toLowerCase().endsWith(".html")) || f.isDirectory()){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
