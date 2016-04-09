/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.helegris.szorengeteg.business.model.Topic;

/**
 *
 * @author Timi
 */
public class TopicLoader extends EntityLoader<Topic> {

    @Override
    protected Class<Topic> getEntityClass() {
        return Topic.class;
    }

}
