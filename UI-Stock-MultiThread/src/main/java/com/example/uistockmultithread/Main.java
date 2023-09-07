package com.example.uistockmultithread;

import com.example.uistockmultithread.stock.reentrant.Labels;
import com.example.uistockmultithread.stock.reentrant.PriceUpdater;
import com.example.uistockmultithread.stock.reentrant.PricesContainer;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

        stage.setTitle("Cryptocurrency Prices");

        GridPane grid = createGrid();
        Map<String, Label> cryptoLabels = createCryptoPriceLabels();
        addLabelsToGrid(cryptoLabels, grid);

        double width = 300;
        double height = 250;

        StackPane root = new StackPane();
        Rectangle background = createBackGroundRectangleWithAnimation(width, height);
        root.getChildren().add(background);
        root.getChildren().add(grid);
        stage.setScene(new Scene(root, width, height));

        PricesContainer pricesContainer = new PricesContainer();
        PriceUpdater priceUpdater = new PriceUpdater(pricesContainer);

        // AnimationTimer is a Java effects component which, when added to the UI thread,
        // call the handle callback on every frame that is shown to the user
        AnimationTimer animationTimer = new AnimationTimer() {
            // So if the UI thread shows 30 frames/second,
            // this handle is going to be called 30 times/second
            @Override
            public void handle(long l) {
                if(pricesContainer.getLockObject().tryLock()) {
                    try {
                        Label bitcoinLabel = cryptoLabels.get(Labels.BTC.name());
                        bitcoinLabel.setText(String.valueOf(pricesContainer.getBitcoin()));
                        Label etherLabel = cryptoLabels.get(Labels.ETH.name());
                        etherLabel.setText(String.valueOf(pricesContainer.getEther()));
                        Label liteCoin = cryptoLabels.get(Labels.LTC.name());
                        liteCoin.setText(String.valueOf(pricesContainer.getLiteCoin()));
                        Label bitcoinCashLabel = cryptoLabels.get(Labels.BCH.name());
                        bitcoinCashLabel.setText(String.valueOf(pricesContainer.getBitcoinCash()));
                        Label rippleLabel = cryptoLabels.get(Labels.XRP.name());
                        rippleLabel.setText(String.valueOf(pricesContainer.getRipple()));
                    } finally {
                        pricesContainer.getLockObject().unlock();
                    }
                }
            }
        };
        animationTimer.start();
        priceUpdater.start();
        stage.show();
    }

    private void addLabelsToGrid(Map<String, Label> cryptoLabels, GridPane grid) {
        int row = 0;
        for(Map.Entry<String, Label> entry : cryptoLabels.entrySet()) {
            String cryptoName = entry.getKey();
            Label nameLabel = new Label(cryptoName);
            nameLabel.setTextFill(Color.BLUE);
            nameLabel.setOnMousePressed(event -> nameLabel.setTextFill(Color.RED));
            nameLabel.setOnMouseReleased((EventHandler) event -> nameLabel.setTextFill(Color.BLUE));

            grid.add(nameLabel, 0, row);
            grid.add(entry.getValue(), 1, row);
            row++;
        }
    }

    private Map<String, Label> createCryptoPriceLabels() {
        Label bitcoinPrice = new Label("0");
        bitcoinPrice.setId(Labels.BTC.name());
        Label etherPrice = new Label("0");
        etherPrice.setId(Labels.ETH.name());
        Label liteCoinPrice = new Label("0");
        liteCoinPrice.setId(Labels.LTC.name());
        Label bitcoinCashPrice = new Label("0");
        bitcoinCashPrice.setId(Labels.BCH.name());
        Label ripplePrice = new Label("0");
        ripplePrice.setId(Labels.XRP.name());

        Map<String, Label> cryptoLabelsMap = new HashMap<>();
        cryptoLabelsMap.put(Labels.BTC.name(), bitcoinPrice);
        cryptoLabelsMap.put(Labels.ETH.name(), etherPrice);
        cryptoLabelsMap.put(Labels.LTC.name(), liteCoinPrice);
        cryptoLabelsMap.put(Labels.BCH.name(), bitcoinCashPrice);
        cryptoLabelsMap.put(Labels.XRP.name(), ripplePrice);

        return cryptoLabelsMap;
    }

    private Rectangle createBackGroundRectangleWithAnimation(double width, double height) {
        Rectangle background = new Rectangle(width, height);
        FillTransition fillTransition = new FillTransition(Duration.millis(2000), background, Color.LIGHTGREEN, Color.LIGHTBLUE);
        fillTransition.setCycleCount(Timeline.INDEFINITE);
        fillTransition.setAutoReverse(true);
        fillTransition.play();
        return background;
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    public static void main(String[] args) {
        launch();
    }
}