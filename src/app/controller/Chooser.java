package app.controller;

import app.object.FileChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by David Szilagyi on 2017. 06. 07..
 */
public class Chooser {
    @FXML
    private TextField from, to;
    @FXML
    private Button cancel;
    @FXML
    private CheckBox ow;

    private Stage stage;
    private File sourceFile, destFile;
    private boolean startCopy;

    public Chooser() {
        this.stage = new Stage();
        this.startCopy = false;
    }

    public boolean existsFile() throws IOException {
        if (new File(to.getText()).exists() && !ow.isSelected()) {
            createWarning("/app/image/warning.png", "File/Folder already exists.\nPlease check \"Overwrite?\"\nif you want to replace.", 15.0);
            return true;
        }
        return false;
    }

    public void createWarning(String pic, String text, double yPos) throws IOException {
        Stage stage = new Stage();
        Parent warning = null;
        FXMLLoader fxml = new FXMLLoader();
        warning = fxml.load(getClass().getResource("/app/form/warning.fxml").openStream());
        Warning w = fxml.getController();
        w.setImage(pic);
        w.setLabel(text, yPos);
        stage.setTitle("Important!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(warning, 260, 106));
        stage.show();
    }

    public void openFileChooser(ActionEvent e) throws IOException {
        JFileChooser fc = new FileChooser().getFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        switch (((Button) (e.getSource())).getText()) {
            case "Source":
                int src = fc.showOpenDialog(new JPanel());
                if (src == JFileChooser.APPROVE_OPTION) {
                    from.setText(fc.getSelectedFile().getPath());
                }
                break;
            case "Destination":
                if (!from.getText().equals("")) {
                    fc.setSelectedFile(new File(new File(from.getText()).getName()));
                    int dest = fc.showSaveDialog(new JPanel());
                    if (dest == JFileChooser.APPROVE_OPTION) {
                        to.setText(fc.getSelectedFile().getPath());
                    }
                } else {
                    createWarning("/app/image/warning.png", "Please select \"From\" first", 37.0);
                }
                break;
        }
    }

    public void actionButton(ActionEvent e) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        switch (((Button) (e.getSource())).getText()) {
            case "OK":
                if(!existsFile()) {
                    if (!from.getText().equals("") && !to.getText().equals("")) {
                        this.sourceFile = new File(from.getText());
                        this.destFile = new File(to.getText());
                        this.startCopy = true;
                        stage.close();
                    } else {
                        createWarning("/app/image/error.png", "\"From\" and/or \"To\" is missing!",37.0);
                    }
                }
                break;
            case "Cancel":
                stage.close();
                break;
        }
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public File getDestFile() {
        return destFile;
    }

    public boolean isStartCopy() {
        return startCopy;
    }
}