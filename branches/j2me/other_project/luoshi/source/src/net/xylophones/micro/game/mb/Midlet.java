/*
 * TetrisMIDlet.java
 * 
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package net.xylophones.micro.game.mb;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import net.xylophones.micro.db.SimpleStore;
import net.xylophones.micro.ui.selectslider.Option;
import net.xylophones.micro.ui.selectslider.SelectListener;
import net.xylophones.micro.ui.vibrator.Vibrator;

/**
 * Main midlet class
 *
 * @author william@xylophones.net
 */
public class Midlet extends MIDlet 
                          implements CommandListener, SelectListener {
    
    /**
     * Display object associated with this midlet
     */
    private Display display = null;

    /**
     * The main menu - displays options to start game, turn off sound etc
     */
    private MenuCanvas menuCanvas;
    
    /**
     * Canvas saying "game over"
     */
    private GameOverCanvas gameOverCanvas;
    
    /**
     * Used to persist / retreive sound / vibrate options
     */
    private SimpleStore dataStore;
        
    /**
     * An alert that tells the user a little about this game
     */
    private Alert aboutAlert;
        
    /**
     * Used to switch between different screens - the game screen, menu screeen
     * etc.
     */
    private ScreenChooser screenChooser;
    
    /**
     * The actual game canvas that holds the game
     */
    private TetrisGameCanvas gameCanvas;
    
    /**
     * Pause game command - sent by the device if the user wants to 
     * pause the game
     */
    Command pause = new Command("Pause", Command.SCREEN, 0);
    DynaMenu menu;
    public  Rsm rsm;
    public MusicManager musicManager;
    public  static Midlet MIDLET;
    public RsmManager rsmManager;
    /** 
     * Creates a new instance of TetrisMIDlet 
     */
    public Midlet () {
        init();
    }
    
    /**
     * Initialise the midlet
     */
    private void init() {
    //#if 0
        try {
            dataStore = new SimpleStore("config");           
        } catch (Exception e) {
            System.err.println("Error creating data store: " + e);
        }
    //#endif
        display = Display.getDisplay(this);
        MIDLET = this;
        rsmManager = new RsmManager();
        this.rsm = rsmManager.rsm;
        musicManager = new MusicManager();
        menu = new DynaMenu();
           if(rsm.level > 0) 
               menu.showResumeItem(true);
        menu.goMainMenu();
        //
        screenChooser = new ScreenChooser(this);

        Vibrator.setDisplay(display);
        
        // create the main menu canvas
//#if 0
        menuCanvas = new MenuCanvas(dataStore);
        menuCanvas.getSelectSlider().addSelectListener(this);
//#endif
        // the actual game canvas that contains the board
        gameCanvas = new TetrisGameCanvas(screenChooser);
        //gameCanvas.addCommand(pause);
        //gameCanvas.setCommandListener(this);
        
        // the screen that says "game over"
        gameOverCanvas = new GameOverCanvas(screenChooser);
        
        menu.show_menu();
        // set the frogger game canvas to be the current Displayable
        //display.setCurrent(menuCanvas);
    }

    
    public void  setCurrentDisplay(Displayable d) {   
        display.setCurrent(d);
    }      
    /**
     * Display the main menu, with extra continue option
     */
    public void displayPause() {
    //#if 0
        menuCanvas.setPaused(true);
        gameCanvas.setPaused(true);
        display.setCurrent(menuCanvas);
    //#endif
        musicManager.stopAudioPlayer();
        gameCanvas.setPaused(true);
        menu.showResumeItem(true);
        menu.goMainMenu();
        menu.show_menu();
    }
    
    /**
     * Display the main menu
     */
    public void displayMainMenu() {
    //#if 0
        menuCanvas.setPaused(false);
        gameCanvas.setPaused(true);
        display.setCurrent(menuCanvas);
    //#endif
        displayPause();
    }
    
    /**
     * Display the game board ready for play
     */
    public void displayBoard() {
        //menuCanvas.setPaused(false);
        musicManager.start_bg_music();
        gameCanvas.setPaused(false);
        display.setCurrent(gameCanvas);
    }
        
    /**
     * Display the about alert
     */
    public void displayAbout() {
        gameCanvas.setPaused(true);
        display.setCurrent(getAboutAlert()); 
    }
    
    /**
     * Display the screen saying "game over"
     */
    public void displayGameOver() {
        gameCanvas.setPaused(true);
        gameOverCanvas.setBackground( gameCanvas.getSnapshot() );
        display.setCurrent(gameOverCanvas);
    }
    
    /**
     * midlet lifecycle method - called when the midelt starts
     */
    public void startApp() {      
    }
    
    /**
     * midlet lifecycle method - called when the midelt is paused
     */
    public void pauseApp() {
        displayPause();
    }

    
    /** start the game, called from menu*/
    public void startGame() {
        gameCanvas.newGame();
        if ( ! gameCanvas.isGameThreadRunning() ) {
            gameCanvas.start();
        }
  
        displayBoard();
        //musicManager.start_bg_music();
    }

    /** resume the game, called from menu*/
    public void resumeGame() {
       displayBoard();
    }
    /**
     * SelectListener interface method
     *
     * Called when an option is selected in the main menu
     *
     * @param o
     */
    public void optionSelected(Option o) {
        if (o.getText() == "about") {
            displayAbout();
        } else if (o.getText() == "vibrate is on") {
            Vibrator.setOn(false);
        } else if (o.getText() == "vibrate is off") {
             Vibrator.setOn(true);
        } else if (o.getText() == "new game") {
            //gameCanvas.finishGame();
            gameCanvas.newGame();
            if ( ! gameCanvas.isGameThreadRunning() ) {
                gameCanvas.start();
            }

            displayBoard();
        } else if (o.getText() == "continue game") {
            displayBoard();
        } else if (o.getText() == "exit") {
            //gameCanvas.finishGame();
            destroyApp(true);
            notifyDestroyed();
        }
    }
        
    /**
     * midlet lifecycle method - called when the midelt is destroyed
     *
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {
        if (gameCanvas != null) {
            // stop the game thread
            gameCanvas.stop();
        }
    }

    /**
     * CommandListener interface method - called when the device sends 
     * a command to the midlet
     *
     * @param c
     * @param d
     */
    public void commandAction(Command c, Displayable d) {
        if (c == pause) {
            System.out.println("Pause pressed");
            displayPause();
        }
    }

    /**
     * Get / create the about alert screen
     *
     * @return 
     */
    private Alert getAboutAlert() { 
        if (aboutAlert == null) {
            aboutAlert = new Alert("MegaBlock", 
                                   "Based on Tetris\n\n" +
                                   "Programming by William Robertson\n\n" +
                                   "www.xylophones.net\n\n"
                                   , null, null);
            aboutAlert.setTimeout(Alert.FOREVER);
            aboutAlert.setType(AlertType.INFO);
        }
        
        return aboutAlert;
    }
    
}
