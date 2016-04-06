package com.helegris.szorengeteg.controller.forms;

import com.helegris.szorengeteg.model.entities.Topic;
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
