package com.helegris.szorengeteg;

import com.helegris.szorengeteg.controller.MainController;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/temp_main.fxml"));
//
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/Styles.css");
//
//        stage.setTitle("Szórengeteg");
//        stage.setScene(scene);
//        stage.show();

        stage.setTitle("Szórengeteg");
        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );

        stage.show();
    }

    /**
     * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = (Pane) loader.load(
                getClass().getResourceAsStream(VistaNavigator.MAIN_FXML)
        );

        MainController mainController = loader.getController();

        VistaNavigator.setMainController(mainController);
        VistaNavigator.loadVista(VistaNavigator.TOPICS_FXML);

        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
                mainPane
        );

        scene.getStylesheets().setAll(
                getClass().getResource("/styles/Styles.css").toExternalForm()
        );

        return scene;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
