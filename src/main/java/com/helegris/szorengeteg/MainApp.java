package com.helegris.szorengeteg;

import com.helegris.szorengeteg.controller.MainView;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private final String TITLE = "Szórengeteg";
    private final int WIDTH = 607;
    private final int HEIGHT = 640;

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContainer.getInstance();

        stage.setTitle(TITLE);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(false);
        stage.setScene(createScene(loadMainPane()));

        stage.show();
    }

    /**
     * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     */
    private Pane loadMainPane() {
        MainView mainView = new MainView();

        VistaNavigator.setMainView(mainView);
        mainView.loadContentTopics();

        return mainView;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);

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
