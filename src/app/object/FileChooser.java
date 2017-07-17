package app.object;

import javax.swing.*;
import java.io.File;

import static javax.swing.filechooser.FileSystemView.getFileSystemView;

/**
 * Created by David Szilagyi on 2017. 06. 09..
 */
public class FileChooser {
    private JFileChooser fileChooser;

    public FileChooser() {
        setLookAndFeel();
        this.fileChooser = new JFileChooser(getFileSystemView().getParentDirectory(new File("C:\\")));
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }
}
