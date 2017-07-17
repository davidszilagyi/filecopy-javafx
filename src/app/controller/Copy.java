package app.controller;

import app.object.WizardTwo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;

/**
 * Created by David Szilagyi on 2017. 06. 07..
 */
public class Copy {
    @FXML
    private Label copyLabel, doneLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button pause, stop;

    private Thread thread;
    private boolean finished;
    private WizardTwo wizard;

    public void changeCopyLabel(String coping) {
        copyLabel.setText(coping);
    }

    public void setPBValue(double current) {
        progressBar.setProgress(current);
    }

    public void copying(File src, File dest) {
        this.wizard = new WizardTwo(src, dest) {
            double oldValue = 0.00;
            @Override
            public void updatePB(double current) {
                if(current - oldValue > 0.01) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setPBValue(current);
                            changeCopyLabel(String.format("Copying %s to %s (%d%s)", src.getPath(), dest.getPath(), ((int) (current * 100)), "%"));
                            if (progressBar.getProgress() >= 1) {
                                doneLabel.setText("Done");
                                stopProcess();
                            }
                        }
                    });
                }
            }
        };
        thread = new Thread(wizard);
        thread.start();
    }

    public void stopProcess() {
        finished = true;
        pause.setVisible(false);
        pause.setDisable(true);
        stop.setVisible(false);
        stop.setDisable(true);
        doneLabel.setVisible(true);
    }

    public void stopProcessFromOutside() {
        thread.interrupt();
        doneLabel.setText("Stopped");
        stopProcess();
    }

    public boolean isFinished() {
        return finished;
    }

    public void buttonAction(ActionEvent e) {
        switch (((Button) (e.getSource())).getText()) {
            case "Pause":
                pause.setText("Resume");
                synchronized (wizard) {
                    wizard.stopped = true;
                }
                break;
            case "Resume":
                pause.setText("Pause");
                synchronized (wizard) {
                    wizard.stopped = false;
                    wizard.notify();
                }
                break;
            case "Stop":
                thread.interrupt();
                doneLabel.setText("Stopped");
                stopProcess();
        }
    }

}
