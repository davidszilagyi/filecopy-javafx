package app.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @FXML
    private Pane copies;
    @FXML
    private Label startText;

    private Chooser c;
    private List<Copy> copyList = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/app/form/main.fxml"));
        primaryStage.setTitle("Multi Copier");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 394, 325));
        primaryStage.show();
    }

    public void showChooser() throws IOException {
        Stage stage = new Stage();
        Parent chooser = null;
        FXMLLoader fxml = new FXMLLoader();
        chooser = fxml.load(getClass().getResource("/app/form/chooser.fxml").openStream());
        this.c = fxml.getController();
        stage.setTitle("File Chooser");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(chooser, 300, 120));
        stage.showAndWait();
        Platform.runLater(() -> startCopy());
    }

    public void startCopy() {
        if (c.isStartCopy()) {
            FXMLLoader fxml = new FXMLLoader();
            Pane copyPane = null;
            try {
                copyPane = fxml.load(getClass().getResource("/app/form/copy.fxml").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Copy copy = fxml.getController();
            copyPane.setLayoutX(6.0);
            copyPane.setLayoutY(83 * copyList.size() + 5.0);
            copies.setPrefHeight(copies.getPrefHeight() + 83);
            startText.setText("");
            copyList.add(copy);
            copies.getChildren().add(copyPane);
            copy.copying(c.getSourceFile(), c.getDestFile());
        }
    }

    public void stopCopies() {
        for (Copy c : copyList) {
            if (!c.isFinished()) {
                c.stopProcessFromOutside();
            }
        }
    }
}
