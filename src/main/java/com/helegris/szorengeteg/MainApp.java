package com.helegris.szorengeteg;

import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.SceneStyler;
import com.helegris.szorengeteg.ui.mainpages.MainView;
import com.helegris.szorengeteg.ui.practice.PositionSaver;
import com.helegris.szorengeteg.ui.practice.PracticeSession;
import com.helegris.szorengeteg.ui.practice.PracticeView;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;

public class MainApp extends Application {

    @Inject
    private PositionSaver positionSaver;
    @Inject
    private TopicLoader topicLoader;

    @SuppressWarnings("LeakingThisInConstructor")
    public MainApp() {
        DIUtils.injectFields(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene;
        Topic topic = topicLoader.loadByOrdinal(positionSaver.getTopicOrdinal());

        if (topic != null) {
            scene = (new SceneStyler().createScene(new PracticeView(
                    new PracticeSession(topic, positionSaver.getCardOrdinal())),
                    SceneStyler.Style.PRACTICE));
            stage.setTitle(topic.getName() + " " + Messages.msg("practice.title"));
        } else {
            Pane root = loadMainPane();
            scene = new SceneStyler().createScene(root, SceneStyler.Style.MAIN);
            stage.setTitle(Messages.msg("menu.title"));
        }

        stage.setMaximized(true);
        stage.setScene(scene);
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
