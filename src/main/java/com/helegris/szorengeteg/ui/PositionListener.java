/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

/**
 * Protocol for positioner usage. A positioner can be used to indicate "up" and
 * "down" movements.
 *
 * @author Timi
 */
public interface PositionListener {

    public void up();

    public void down();
}
