package app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Created by David Szilagyi on 2017. 06. 08..
 */
public class Warning {
    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private ImageView image;

    public void setImage(String pic) {
        image.setImage(new Image(pic));
    }

    public void setLabel(String text, double yPos) {
        label.setText(text);
        label.setLayoutY(yPos);
    }

    public void closeWarning(ActionEvent e) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}
