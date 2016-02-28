package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.model.entity.Topic;
import java.io.IOException;

/**
 *
 * @author Timi
 */
public class NewTopicView extends TopicFormView {

    @Override
    protected void prepareTopic() throws IOException {
        topic = new Topic();
        super.prepareTopic();
        entitySaver.create(topic);
    }

}
