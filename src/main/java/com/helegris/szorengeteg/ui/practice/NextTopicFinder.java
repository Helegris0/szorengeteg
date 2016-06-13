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
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class NextTopicFinder {

    @Inject
    private TopicLoader topicLoader;
    @Inject
    private CardLoader cardLoader;

    @SuppressWarnings("LeakingThisInConstructor")
    public NextTopicFinder() {
        DIUtils.injectFields(this);
    }

    public Topic getNextTopic(Topic currentTopic) {
        List<Topic> topics = topicLoader.loadAllOrdered().stream()
                .filter(topic -> cardLoader.loadByTopic(topic).size() > 0)
                .collect(Collectors.toList());
        int currentIndex = topics.indexOf(currentTopic);
        if (currentIndex < topics.size() - 1) {
            return topics.get(currentIndex + 1);
        }
        return topics.get(0);
    }
}
