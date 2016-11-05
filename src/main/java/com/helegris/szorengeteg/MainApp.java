package com.helegris.szorengeteg;

import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.SceneStyler;
import com.helegris.szorengeteg.ui.topiclist.MainView;
import com.helegris.szorengeteg.ui.practice.PositionSaver;
import com.helegris.szorengeteg.ui.practice.PracticeSession;
import com.helegris.szorengeteg.ui.practice.PracticeView;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;

public class MainApp extends Application {

    @Inject
    private PositionSaver positionSaver;
    @Inject
    private TopicLoader topicLoader;
    @Inject
    private CardLoader cardLoader;

    private static MainApp instance;

    private Stage stage;

    @SuppressWarnings("LeakingThisInConstructor")
    public MainApp() {
        DIUtils.injectFields(this);
        instance = this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        if (!setPracticeScene()) {
            setTopicListScene();
        }
        stage.setTitle(Messages.msg("title"));
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/images/icon256.png")));
        stage.show();
    }

    /**
     * Checks the saved practicing position, if available, sets the practice
     * scene.
     *
     * @return true if practice scene is set
     */
    public boolean setPracticeScene() {
        Topic topic = topicLoader.loadByOrdinal(positionSaver.getTopicOrdinal());
        int index = positionSaver.getCardOrdinal();
        if (topic != null && cardLoader.loadByTopic(topic).size() > index) {
            setPracticeScene(topic, index);
            return true;
        }
        topic = topicLoader.loadByOrdinal(PositionSaver.getTopicOrdinalDefault());
        if (topic != null && cardLoader.loadByTopic(topic).size() > 0) {
            setPracticeScene(topic);
            return true;
        }
        return false;
    }

    /**
     * Sets practice scene with the first card of a given topic.
     *
     * @param topic
     */
    public void setPracticeScene(Topic topic) {
        setPracticeScene(topic, 0);
    }

    /**
     * Sets practice scene with a given topic and card index.
     *
     * @param topic
     * @param index
     */
    private void setPracticeScene(Topic topic, int index) {
        Scene scene = (new SceneStyler().createScene(new PracticeView(
                new PracticeSession(topic, index)),
                SceneStyler.Style.PRACTICE));
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setMaximized(true);
    }

    public void setTopicListScene() {
        Pane root = loadMainPane();
        Scene scene = new SceneStyler().createScene(root, SceneStyler.Style.TOPIC_LIST);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setMaximized(true);
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

    public static MainApp getInstance() {
        return instance;
    }
}
