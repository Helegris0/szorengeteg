package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.model.entity.Topic;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Timi
 */
public class NewTopicView extends TopicFormView {

    @Override
    protected void prepareTopic() throws FileNotFoundException, IOException {
        topic = new Topic();
        super.prepareTopic();
    }

}
