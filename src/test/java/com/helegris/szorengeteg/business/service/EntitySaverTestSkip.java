/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.PersistentObject;
import com.helegris.szorengeteg.business.model.Topic;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

/**
 *
 * @author Timi
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EntitySaverTestSkip {

    @Inject
    private EntityManager em;

    @Inject
    private TopicLoader topicLoader;

    @Inject
    private CardLoader cardLoader;

    @Inject
    private EntitySaver entitySaver;

    private final String DIRECTORY = "/dataset/";
    private final String NEW_TOPIC = DIRECTORY + "new_topic.xml";
    private final String WORDS_ADDED_TO_EMPTY_TOPIC
            = DIRECTORY + "words_added_to_empty_topic.xml";
    private final String NEW_TOPIC_WITH_WORDS
            = DIRECTORY + "new_topic_with_words.xml";
    private final String SINGLE_TOPIC = DIRECTORY + "single_topic.xml";
    private final String TOPIC_WITH_WORDS
            = DIRECTORY + "topic_with_words.xml";
    private final String TOPIC_WITH_MORE_WORDS
            = DIRECTORY + "topic_with_more_words.xml";
    private final String EMPTY_DB = DIRECTORY + "empty_db.xml";
    private final String WORDS_WITHOUT_TOPIC
            = DIRECTORY + "words_without_topic.xml";

    private final String TOPIC = "Idő";
    private final String WORD1 = "április";
    private final String WORD2 = "május";
    private final String WORD3 = "június";
    private final String DESCRIPTION1 = "Az év negyedik hónapja";
    private final String DESCRIPTION2 = "Az év ötödik hónapja";
    private final String DESCRIPTION3 = "Az év hatodik hónapja";

    @SuppressWarnings("LeakingThisInConstructor")
    public EntitySaverTestSkip() {
        final Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                install(new JpaPersistModule("test"));
            }
        });
        injector.getInstance(PersistService.class).start();
        injector.injectMembers(this);
    }

    @Before
    public void setUp() {
        clearJpaCache();
    }

    @After
    public void tearDown() {
        clearJpaCache();
    }

    private void clearJpaCache() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Test
    @ExpectedDataSet(value = NEW_TOPIC)
    public void testCreateTopic() {
        Topic topic = new Topic(TOPIC);
        List<Card> cards = new ArrayList<>();
        entitySaver.createTopic(topic, cards);
    }

    @Test
    @ExpectedDataSet(value = NEW_TOPIC_WITH_WORDS)
    public void testCreateTopicWithWords() {
        Topic topic = new Topic(TOPIC);
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(WORD1, DESCRIPTION1));
        cards.add(new Card(WORD2, DESCRIPTION2));
        entitySaver.createTopic(topic, cards);
    }

    @Test
    @DataSet(value = SINGLE_TOPIC)
    @ExpectedDataSet(value = WORDS_ADDED_TO_EMPTY_TOPIC)
    public void testAddWordsToEmptyTopic() {
        List<Topic> topics = topicLoader.loadAll();
        assertEquals(1, topics.size());
        Topic topic = topics.get(0);

        List<Card> cards = new ArrayList<>();
        cards.add(new Card(WORD1, DESCRIPTION1));
        cards.add(new Card(WORD2, DESCRIPTION2));
        cards.stream().forEach(card -> card.setTopic(topic));

        entitySaver.saveTopic(topic, cards);
    }

    @Test
    @DataSet(value = TOPIC_WITH_WORDS)
    @ExpectedDataSet(value = TOPIC_WITH_MORE_WORDS)
    public void testAddWordToTopic() {
        List<Topic> topics = topicLoader.loadAll();
        assertEquals(1, topics.size());
        Topic topic = topics.get(0);

        Card card = new Card(WORD3, DESCRIPTION3);
        card.setTopic(topic);
        List<Card> cards = cardLoader.loadByTopic(topic);
        cards.add(card);

        entitySaver.saveTopic(topic, cards);
    }

    @Test
    @DataSet(value = TOPIC_WITH_WORDS)
    @ExpectedDataSet(value = EMPTY_DB)
    public void testDeleteTopicWithWords() {
        List<Topic> topics = topicLoader.loadAll();
        assertEquals(1, topics.size());
        Topic topic = topics.get(0);

        List<Card> cards = cardLoader.loadByTopic(topic);
        cards.forEach(card -> assertEquals(topic, card.getTopic()));

        List<PersistentObject> toDelete = new ArrayList<>();
        toDelete.add(topic);
        toDelete.addAll(cards);

        entitySaver.delete(toDelete);
    }

    @Test
    @DataSet(value = TOPIC_WITH_WORDS)
    @ExpectedDataSet(value = WORDS_WITHOUT_TOPIC)
    public void testDeleteTopicWithoutItsWords() {
        List<Topic> topics = topicLoader.loadAll();
        assertEquals(1, topics.size());
        Topic topic = topics.get(0);

        List<Card> cards = cardLoader.loadByTopic(topic);
        cards.forEach(card -> assertEquals(topic, card.getTopic()));

        entitySaver.deleteTopicWithoutWords(topic);
    }

}
