package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.business.model.Topic;
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
