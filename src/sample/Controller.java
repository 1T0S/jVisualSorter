package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button buttonSort, buttonExit, buttonFile;
    @FXML
    public RadioButton radioBubbleSort, radioSelectionSort, radioInsertionSort;
    @FXML
    public Slider sliderGenerate;
    @FXML
    public Canvas myCanvas;
    @FXML
    public Label labelCols;

    private Sort s;
    private File out = new File("default.txt");

    // Runnables that are used in thread after user starts sorting.
    private Runnable bubble = (() -> {
        try {
            s.execBubbleSort();
            s.outputFile(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            buttonSort.setVisible(true);
        }
    });

    private Runnable selection = (() -> {
        try {
            s.execSelectionSort();
            s.outputFile(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            buttonSort.setVisible(true);
        }
    });

    private Runnable insertion = (() -> {
        try {
            s.execInsertionSort();
            s.outputFile(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            buttonSort.setVisible(true);
        }
    });


    // Add listener to slider
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sliderGenerate.valueProperty().addListener((observable, oldValue, newValue) -> {
            labelCols.setText(newValue.intValue() + " cols");
        });
    }


    // After user pushes Start button, Thread that controls visualization is started.
    @FXML
    public void sort() throws Exception {
        buttonSort.setVisible(false);
        Thread t;
        s = new Sort((int) sliderGenerate.getValue(), myCanvas);
        // Need to assign thread to variable, so it can be set as a Daemon.
        if (radioBubbleSort.isSelected())
            t = new Thread(bubble);
        else if (radioSelectionSort.isSelected())
            t = new Thread(selection);
        else if (radioInsertionSort.isSelected())
            t = new Thread(insertion);
        else
            t = new Thread(bubble);

        t.setDaemon(true);
        t.start();
    }


    public void exit() {
        System.exit(0);
    }


    public void setOutput() {
        Stage stageFileSave = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file:");
        out = fileChooser.showSaveDialog(stageFileSave);
        if (out == null)
            out = new File("default.txt");
    }

}
