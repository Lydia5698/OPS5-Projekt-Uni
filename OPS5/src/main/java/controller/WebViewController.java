package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

/**
 * Displays the Pub Med article
 */
public class WebViewController {

    @FXML
    private WebView pubMedView;

    /**
     * Initialize the Webpage for the Pub Med search
     */
    @FXML
    public void initialize(){
    }

    /**
     * Loads the PubMed web page using the entered url
     * @param url The PubMed Url
     */
    public void webView(String url){
        pubMedView.getEngine().load(url);
    }

    /**
     * Goes to the previous page in the browser
     */
    public void goBack() {
        Platform.runLater(() -> pubMedView.getEngine().executeScript("history.back()"));
    }

    /**
     * Goes to the next page in the browser
     */
    public void goForward() {
        Platform.runLater(() -> pubMedView.getEngine().executeScript("history.forward()"));
    }


}
