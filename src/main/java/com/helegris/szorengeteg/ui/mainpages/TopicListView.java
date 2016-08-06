/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.mainpages;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.PersistentObject;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.practice.PositionSaver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class TopicListView extends AnchorPane {

    private static final String FXML = "fxml/topic_list.fxml";

    @Inject
    private TopicLoader topicLoader;
    @Inject
    private CardLoader cardLoader;
    @Inject
    private EntitySaver entitySaver;
    @Inject
    private PositionSaver positionSaver;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private Button btnNewTopic;
    @FXML
    private Button btnModifyOrder;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnResetSelected;
    @FXML
    private Button btnResetAll;
    @FXML
    private Button btnDeleteSelected;
    @FXML
    private Button btnDeleteAll;

    private List<Topic> topics;
    private List<TopicBox> topicBoxes;

    private TopicBox highlighted;

    private final TopicClickListener clickListener = (TopicBox topicBox) -> {
        topicBoxes.stream()
                .filter(box -> box.getTopic()
                        .equals(PositionSaver.getCurrentTopic()))
                .findFirst()
                .get()
                .highlight(false);
        topicBox.highlight(true);
        positionSaver.setTopic(topicBox.getTopic());
        positionSaver.setCardOrdinal(0);
    };

    private final TopicBoxMoveListener moveListener = new TopicBoxMoveListener() {

        @Override
        public void moveUp(TopicBox topicBox) {
            int index = vBox.getChildren().indexOf(topicBox);
            if (index > 0) {
                Collections.swap(topicBoxes, index, index - 1);
                setTopicBoxes();
            }
        }

        @Override
        public void moveDown(TopicBox topicBox) {
            int index = vBox.getChildren().indexOf(topicBox);
            if (index < vBox.getChildren().size() - 1) {
                Collections.swap(topicBoxes, index, index + 1);
                setTopicBoxes();
            }
        }
    };

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicListView() {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
        Platform.runLater(() -> requestFocus());
    }

    @FXML
    private void initialize() {
        loadTopics();
        createTopicBoxes();
        btnNewTopic.setOnAction(this::newTopic);
        btnModifyOrder.setOnAction(this::allowModifyOrder);
        btnSave.setOnAction(this::saveOrder);
        btnCancel.setOnAction(this::cancel);
        btnResetSelected.setOnAction(this::resetSelected);
        btnResetAll.setOnAction(this::resetAll);
        btnDeleteSelected.setOnAction(this::deleteSelected);
        btnDeleteAll.setOnAction(this::deleteAll);
    }

    private void loadTopics() {
        topics = topicLoader.loadAll();
        topics.sort((t1, t2) -> t1.getOrdinal() != null ? t1.getOrdinal() - t2.getOrdinal() : 1);
    }

    private void createTopicBoxes() {
        if (topics.isEmpty()) {
            vBox.getChildren().add(new Label(Messages.msg("topics.no_topics")));
        } else {
            topicBoxes = new ArrayList<>();
            topics.stream().forEach(topic -> {
                TopicBox topicBox = new TopicBox(topic, clickListener, moveListener);
                topicBoxes.add(topicBox);
                if (topic.equals(PositionSaver.getCurrentTopic())) {
                    topicBox.highlight(true);
                    highlighted = topicBox;
                }
            });
            setTopicBoxes();

            List<TopicBox> withoutOrdinals = topicBoxes.stream()
                    .filter(topicBox -> (topicBox.getTopic().getOrdinal() == null))
                    .collect(Collectors.toList());
            if (!withoutOrdinals.isEmpty()) {
                saveOrder();
                withoutOrdinals.stream().forEach(topicBox -> topicBox.setNameLabel());
            }

            if (highlighted != null) {
//                double h = scrollPane.getContent().getBoundsInLocal().getHeight();
//                double y = (highlighted.getBoundsInParent().getMaxY()
//                        + highlighted.getBoundsInParent().getMinY()) / 2.0;
//                double v = scrollPane.getViewportBounds().getHeight();
//                scrollPane.setVvalue(scrollPane.getVmax() * ((y - 0.5 * v) / (h - v)));
            }
        }
    }

    private void setTopicBoxes() {
        vBox.getChildren().clear();
        topicBoxes.stream().forEach(box -> vBox.getChildren().add(box));
    }

    private void newTopic(ActionEvent event) {
        VistaNavigator.getMainView().loadContentNewTopic();
    }

    private void allowModifyOrder(ActionEvent event) {
        orderModVisibility(true);
        topicBoxes.stream().forEach(topicBox -> topicBox.setModifyable(true));
    }

    private void saveOrder(ActionEvent event) {
        orderModVisibility(false);
        saveOrder();
    }

    private void saveOrder() {
        topicBoxes.stream().forEach(topicBox -> {
            topicBox.setModifyable(false);
            topicBox.getTopic().setOrdinal(topicBoxes.indexOf(topicBox) + 1);
            topicBox.setNameLabel();
        });
        entitySaver.saveTopics(topics);
    }

    private void cancel(ActionEvent event) {
        orderModVisibility(false);
        createTopicBoxes();
    }

    private void orderModVisibility(boolean modifying) {
        btnModifyOrder.setVisible(!modifying);
        btnSave.setVisible(modifying);
        btnCancel.setVisible(modifying);
    }

    private void resetSelected(ActionEvent event) {
        if (highlighted != null) {
            Topic topic = highlighted.getTopic();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Messages.msg("topics.reset_selected"));
            alert.setHeaderText(Messages.msg("topics.confirm_reset_selected",
                    topic.getName()));

            ButtonType typeYes = new ButtonType(Messages.msg("common.yes"), ButtonBar.ButtonData.YES);
            ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(typeYes);
            alert.getButtonTypes().add(typeCancel);

            alert.setX(getScene().getWidth() / 2);
            alert.setY(100);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == typeYes) {
                List<Card> cards = cardLoader.loadByTopic(topic);
                cards.forEach(card -> card.reset());
                entitySaver.saveTopic(topic, cards);
            }
        }
    }

    private void resetAll(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Messages.msg("topics.reset_all"));
        alert.setHeaderText(Messages.msg("topics.confirm_reset_all"));

        ButtonType typeYes = new ButtonType(Messages.msg("common.yes"), ButtonBar.ButtonData.YES);
        ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeYes);
        alert.getButtonTypes().add(typeCancel);

        alert.setX(getScene().getWidth() / 2);
        alert.setY(100);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == typeYes) {
            List<Card> cards = cardLoader.loadAll();
            cards.forEach(card -> card.reset());
            entitySaver.saveCards(cards);
        }
    }

    private void deleteSelected(ActionEvent event) {
        if (highlighted != null) {
            Topic topic = highlighted.getTopic();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Messages.msg("topics.confirm_delete_selected"));
            alert.setHeaderText(Messages.msg("topics.confirm_reset_selected",
                    topic.getName()));
            alert.setContentText(Messages.msg("topics.confirm_delete_selected_more"));

            ButtonType typeDel = new ButtonType(Messages.msg("common.delete"), ButtonBar.ButtonData.YES);
            ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(typeDel);
            alert.getButtonTypes().add(typeCancel);

            alert.setX(getScene().getWidth() / 2);
            alert.setY(100);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == typeDel) {
                List<PersistentObject> toDelete = new ArrayList<>();
                cardLoader.loadByTopic(topic).stream().forEach(toDelete::add);
                toDelete.add(topic);
                entitySaver.delete(toDelete);
                vBox.getChildren().remove(highlighted);
                highlighted = null;
            }
        }
    }

    private void deleteAll(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Messages.msg("topics.confirm_delete_all"));
        alert.setHeaderText("Biztosan szeretne MINDENT törölni? A művelet nem vonható vissza.");
        alert.setContentText(Messages.msg("Biztosan szeretne MINDEN témakört, leírást, képet és hangfájlt törölni? A művelet NEM vonható vissza!"));

        ButtonType typeDel = new ButtonType(Messages.msg("common.delete"), ButtonBar.ButtonData.YES);
        ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeDel);
        alert.getButtonTypes().add(typeCancel);

        alert.setX(getScene().getWidth() / 2);
        alert.setY(100);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == typeDel) {
            List<PersistentObject> toDelete = new ArrayList<>();
            cardLoader.loadAll().stream().forEach(toDelete::add);
            topicLoader.loadAll().stream().forEach(toDelete::add);
            entitySaver.delete(toDelete);
            vBox.getChildren().clear();
            highlighted = null;
        }
    }
}
