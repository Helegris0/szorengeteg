/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.service.TopicLoader;
import javax.inject.Inject;

/**
 * Class for checking if there's a topic and a card with given ordinals.
 *
 * @author Timi
 */
public class SessionJump {

    @Inject
    private TopicLoader topicLoader;
    @Inject
    private CardLoader cardLoader;

    private final int topicOrdinal;
    private final int cardOrdinal;

    private Topic topic;

    @SuppressWarnings("LeakingThisInConstructor")
    public SessionJump(int topicOrdinal, int cardOrdinal) {
        this.topicOrdinal = topicOrdinal;
        this.cardOrdinal = cardOrdinal;
        DIUtils.injectFields(this);
    }

    /**
     * Loads the the desired topic.
     *
     * @return true if the topic exists as well as the card index
     */
    public boolean isValid() {
        topic = topicLoader.loadByOrdinal(topicOrdinal);
        return topic != null && cardOrdinal < cardLoader.loadByTopic(topic).size();
    }

    public Topic getTopic() {
        return topic;
    }

    public int getLength() {
        return cardLoader.loadByTopic(topic).size();
    }
}
