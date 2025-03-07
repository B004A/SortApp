package labo4.tn;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Button paramButton, startButton, pauseButton;
    private static GridPane grid;
    private double scHeight;
    private double scWidth;
    private SortTemplate sortType;
    private HBox barChart;
    private int[] valuesForAlgorithm;
    private String numbersInputValue, algorithmChoice, speedChoice;
    private Thread algorithmThread;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D scBounds = Screen.getPrimary().getVisualBounds();
        scHeight = scBounds.getHeight();
        scWidth = scBounds.getWidth();
        grid = new GridPane();
        scene = new Scene(grid, scWidth * 0.5, scHeight * 0.5);

        RowConstraints rowConstraints = new RowConstraints();
        grid.getRowConstraints().addAll(new RowConstraints(), rowConstraints);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().addAll(colConstraints);
        }
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(0, 5, 5, 5));
        grid.prefHeightProperty().bind(scene.heightProperty());
        grid.prefWidthProperty().bind(scene.widthProperty());
        loadContent(grid);
        // grid.getChildren().forEach(node -> node.setStyle("-fx-border-color: blue"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        paramButton.fire();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private void loadContent(GridPane grid) {
        Image settingsImg = new Image(getClass().getResourceAsStream("/gear.png"));
        ImageView settingsIcon = new ImageView(settingsImg);
        settingsIcon.setFitHeight(15);
        settingsIcon.setFitWidth(15);
        paramButton = new Button("Parametres", settingsIcon);
        paramButton.setContentDisplay(ContentDisplay.LEFT);
        paramButton.setMinHeight(Region.USE_PREF_SIZE);
        paramButton.setMinWidth(Region.USE_PREF_SIZE);
        GridPane.setHgrow(paramButton, Priority.NEVER);
        GridPane.setVgrow(paramButton, Priority.NEVER);
        paramButton.setOnAction(e -> {
            openSettings(paramButton);
            paramButton.setDisable(true);
        });
        paramButton.setStyle(
                "-fx-aligment: center-left; " +
                        "-fx-border-color: transparent; " +
                        "-fx-background-color : transparent;" +
                        "-fx-padding: 0; " +
                        "-fx-text-fill: black; ");
        barChart = new HBox(1);

        grid.add(paramButton, 0, 0);
        grid.add(barChart, 0, 1);
        barChart.prefHeightProperty().bind(grid.heightProperty().multiply(0.75));
        barChart.prefWidthProperty().bind(grid.widthProperty());
        pauseButton = new Button("Arret");
        pauseButton.prefWidthProperty().bind(grid.widthProperty().divide(5));
        pauseButton.setDisable(true);
        startButton = new Button("Demarrer");
        startButton.prefWidthProperty().bind(grid.widthProperty().divide(5));
        startButton.setOnAction(e -> {
            if (algorithmThread != null) {
                sortType.resumeAlgorithm();
                startButton.setDisable(true);
                pauseButton.setDisable(false);
            }
        });
        pauseButton.setOnAction(e -> {
            if (sortType != null) {
                sortType.pauseAlgorithm();
                startButton.setDisable(false);
                pauseButton.setDisable(true);
            }
        });
        GridPane.setHalignment(paramButton, HPos.LEFT);
        paramButton.setPadding(Insets.EMPTY);
        HBox bottomButtons = new HBox(10);
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        row2.setValignment(VPos.BOTTOM);
        grid.getRowConstraints().add(2, row2);

        GridPane.setMargin(bottomButtons, Insets.EMPTY);
        bottomButtons.setMaxHeight(Region.USE_PREF_SIZE);
        bottomButtons.setAlignment(Pos.BOTTOM_RIGHT);
        bottomButtons.getChildren().addAll(pauseButton, startButton);
        grid.add(bottomButtons, 2, 2);
    }

    private void setBarChart(HBox barChart, int[] values) {
        barChart.getChildren().clear();
        Rectangle bar;
        VBox fullBar;
        Text barText;
        barChart.setAlignment(Pos.BOTTOM_CENTER);
        GridPane.setColumnSpan(barChart, 3);
        for (int num : values) {
            fullBar = new VBox();
            bar = new Rectangle((scWidth / 5) / values.length, num, Color.GREY);
            bar.heightProperty().bind(Bindings.createDoubleBinding(
                    () -> Math.min((num * 2) + 20, scHeight * 0.4),
                    fullBar.heightProperty()));
            bar.setStroke(Color.BLACK);
            bar.setStrokeWidth(1);
            bar.setId(String.valueOf(num));
            barText = new Text(String.valueOf(num));
            fullBar.setSpacing(2);
            fullBar.setAlignment(Pos.BOTTOM_CENTER);
            fullBar.getChildren().addAll(bar, barText);
            VBox.setVgrow(fullBar, Priority.NEVER);
            barChart.getChildren().addAll(fullBar);
        }
    }

    private void openSettings(Button settingsButton) {
        startButton.setDisable(true);
        pauseButton.setDisable(true);
        Stage settingsStage = new Stage();
        GridPane settingsGrid = new GridPane();
        settingsGrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.minWidthProperty().bind(settingsGrid.widthProperty().multiply(0.4));
            settingsGrid.getColumnConstraints().add(col);
        }
        Scene settingsScene = new Scene(settingsGrid, scWidth * 0.2, scHeight * 0.2);
        Text explanationText = new Text("Parametres pour la visualisation d'un tri : ");
        Text chosenAlgorithmText = new Text("Algorithme de tri : ");
        ComboBox<String> algorithmChoiceList = new ComboBox<>();
        algorithmChoiceList.getItems().addAll("Merge sort", "Quick sort");
        algorithmChoiceList.setValue(algorithmChoice == null ? "Merge sort" : algorithmChoice);
        Text collectionToSortText = new Text("Collection d'entier a trier : ");
        TextField numbersToSort = new TextField();
        numbersToSort.setPromptText("ex.: 1,5,3,7,9...63");
        if (numbersInputValue != null) {
            numbersToSort.setText(numbersInputValue);
        }
        Text sortSpeedText = new Text("Vitesse de la simulation : ");
        ComboBox<String> sortSpeed = new ComboBox<>();
        sortSpeed.getItems().addAll("Rapide", "Lente", "Standard");
        sortSpeed.setValue("Standard");
        explanationText.setFont(Font.font(explanationText.getFont().getFamily(), FontWeight.BOLD, 16));
        explanationText.setUnderline(true);

        // settings buttons setup
        HBox settingsWindowButtons = new HBox(5);
        Button settingsOkButton = new Button("OK");
        Button settingsApplyButton = new Button("Appliquer");
        Button settingsCancelButton = new Button("Annuler");
        settingsOkButton.prefWidthProperty().bind(settingsWindowButtons.widthProperty().divide(5));
        settingsOkButton.setOnAction(e -> {
            settingsApplyButton.fire();
            // setup thread and sort type
            if (valuesForAlgorithm != null && sortType != null && algorithmThread == null) {
                algorithmThread = new Thread(() -> {
                    sortType.setupAlgorithm(valuesForAlgorithm, 0, valuesForAlgorithm.length - 1);
                    sortType.setSleepTime(1500);
                    sortType.run();
                    Platform.runLater(() -> {
                        startButton.setDisable(true);
                        pauseButton.setDisable(true);
                        paramButton.fire();
                        setBarChart(barChart, valuesForAlgorithm);
                        algorithmThread = null;
                    });
                });
                algorithmThread.start();
            }
            sortType.pauseAlgorithm();
            settingsStage.close();
            paramButton.setDisable(false);
            startButton.setDisable(false);
        });
        settingsCancelButton.prefWidthProperty().bind(settingsWindowButtons.widthProperty().divide(5));
        settingsApplyButton.prefWidthProperty().bind(settingsWindowButtons.widthProperty().divide(5));
        settingsApplyButton.setOnAction(e -> {
            settingsCheck(algorithmChoiceList, sortSpeed, numbersToSort);
        });
        settingsWindowButtons.getChildren().addAll(settingsOkButton, settingsCancelButton, settingsApplyButton);
        settingsWindowButtons.setAlignment(Pos.BOTTOM_RIGHT);

        // Add elements to grid
        settingsGrid.add(explanationText, 0, 1);
        settingsGrid.add(chosenAlgorithmText, 0, 2);
        settingsGrid.add(algorithmChoiceList, 1, 2);
        settingsGrid.add(collectionToSortText, 0, 3);
        settingsGrid.add(numbersToSort, 1, 3);
        settingsGrid.add(sortSpeedText, 0, 4);
        settingsGrid.add(sortSpeed, 1, 4);
        settingsGrid.add(settingsWindowButtons, 0, 5);
        GridPane.setColumnSpan(settingsWindowButtons, 2);
        settingsGrid.setVgap(10);
        settingsGrid.setHgap(10);
        settingsGrid.setPadding(new Insets(10));
        settingsStage.setScene(settingsScene);
        Bounds buttonBounds = settingsButton.localToScreen(settingsButton.getBoundsInLocal());
        settingsStage.setX(buttonBounds.getMinX() - 12);
        settingsStage.setY(buttonBounds.getMinY());
        settingsStage.setResizable(false);
        settingsStage.setOnCloseRequest(e -> {
            paramButton.setDisable(false);
        });
        settingsStage.show();
    }

    private void assignAlgorithm(String choice) {
        switch (choice) {
            case "Merge sort":
                sortType = new MergeSort(this);
                break;
            case "Quick sort":
                sortType = new QuickSort(this);
                break;
        }
    }

    public void highlightItems(int[] itemsToHighlight) {
        for (int i = 0; i < itemsToHighlight.length; i++) {
            Rectangle neededBar = (Rectangle) barChart.lookup("#" + itemsToHighlight[i]);
            if (neededBar != null) {
                neededBar.setFill(Color.PURPLE);
            }

        }
    }

    private void settingsCheck(ComboBox<String> algorithmChoiceList, ComboBox<String> sortSpeed,
            TextField numbersToSort) {

        if (algorithmChoice == null) {
            algorithmChoice = algorithmChoiceList.getValue();
            assignAlgorithm(algorithmChoice);
        }
        if (speedChoice == null) {
            speedChoice = sortSpeed.getValue();
        }
        if (!(numbersToSort.getText().equals(""))) {
            String[] extractedValues = numbersToSort.getText().split(",");
            valuesForAlgorithm = new int[extractedValues.length];
            for (int i = 0; i < extractedValues.length; i++) {
                if (extractedValues[i] != "") {
                    valuesForAlgorithm[i] = Integer.parseInt(extractedValues[i].trim());
                }
            }
            if (numbersInputValue == null) {
                numbersInputValue = numbersToSort.getText();
                setBarChart(barChart, valuesForAlgorithm);
            }
        }
        if (numbersInputValue != null && !(numbersInputValue.equals(numbersToSort.getText()))) {
            numbersInputValue = numbersToSort.getText();
            setBarChart(barChart, valuesForAlgorithm);
        }
        if (!(algorithmChoice.equals(algorithmChoiceList.getValue()))) {
            algorithmChoice = algorithmChoiceList.getValue();
            assignAlgorithm(algorithmChoice);
            if (numbersInputValue != null) {
                setBarChart(barChart, valuesForAlgorithm);
            }
        }
    }

    public void updateContent(int[] valuesBeingSorted) {
        setBarChart(barChart, valuesForAlgorithm);
        highlightItems(valuesBeingSorted);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}