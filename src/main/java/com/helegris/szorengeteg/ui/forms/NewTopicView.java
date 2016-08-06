package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.DefaultImage;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Timi
 */
public class NewTopicView extends TopicFormView {

    @Override
    protected void initialize() {
        super.initialize();
        imageView.setImage(DefaultImage.getInstance());
        int ordinal = topicLoader.loadAll().size() + 1;
        lblOrdinal.setText(ordinal + ". ");
    }

    @Override
    protected void prepareTopic() throws FileNotFoundException, IOException {
        topic = new Topic();
        super.prepareTopic();
    }
}
