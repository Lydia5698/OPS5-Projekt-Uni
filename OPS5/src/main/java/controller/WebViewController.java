package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.net.URL;

/**
 * Displays the Pub Med article
 */
public class WebViewController {

    @FXML
    private WebView pubMedView;

    /**
     * initialize the Webpage for the Pub Med search
     */
    @FXML
    public void initialize(){
    }

    public void webView(String url){
        pubMedView.getEngine().load(url);
    }

    public void goBack() {
        Platform.runLater(() -> {
            pubMedView.getEngine().executeScript("history.back()");
        });
    }

    public void goForward() {
        Platform.runLater(() -> {
            pubMedView.getEngine().executeScript("history.forward()");
        });
    }


}
