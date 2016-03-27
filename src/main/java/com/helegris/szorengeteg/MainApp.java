package com.helegris.szorengeteg;

import com.helegris.szorengeteg.controller.MainView;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private final String TITLE = "Szórengeteg";
    private final int WIDTH = 707;
    private final int HEIGHT = 660;

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContainer.getInstance();

        stage.setTitle(TITLE);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(false);
        stage.setScene(new SceneStyler().createScene(loadMainPane()));

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
