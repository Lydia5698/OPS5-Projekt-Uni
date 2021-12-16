package controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

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
        pubMedView.getEngine().load("https://pubmed.ncbi.nlm.nih.gov/?term=Cholera%5BMeSH+Major+Topic%5D");
    }



}
