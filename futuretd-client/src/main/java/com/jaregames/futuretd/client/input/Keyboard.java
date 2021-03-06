package com.jaregames.futuretd.client.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Accessing the current keyboard status
 */
public class Keyboard implements KeyListener {
    private static final int KEY_COUNT = 256;
    
    private enum KeyState {
        // Not down
        RELEASED,
        // Down, but not the first time
        PRESSED,
        // Down for the first time
        ONCE
    }
    
    // Current state of the keyboard
    private boolean[] currentKeys = null;
    
    // Polled keyboard state
    private static KeyState[] keys = null;
    
    // Typed letter
    private static StringBuilder typedString;
    
    public Keyboard() {
        currentKeys = new boolean[KEY_COUNT];
        keys = new KeyState[KEY_COUNT];
        typedString = new StringBuilder();
        
        for (int i = 0; i < KEY_COUNT; i++) {
            keys[i] = KeyState.RELEASED;
        }
    }
    
    /**
     * Write the current state of the keyboard to the polled keyboard data
     * <p>
     * This method synchronizes the input with our game ticks
     */
    public synchronized void poll() {
        typedString.setLength(0);
        for (int i = 0; i < KEY_COUNT; i++) {
            // Set the key state
            if (currentKeys[i]) {
                // If the key is down now, but was not
                // down last frame, set it to ONCE,
                // otherwise, set it to PRESSED
                
                if (keys[i] == KeyState.RELEASED)
                    keys[i] = KeyState.ONCE;
                else
                    keys[i] = KeyState.PRESSED;
            } else {
                keys[i] = KeyState.RELEASED;
            }
        }
    }
    
    /**
     * @param keyCode The key to check of: On of KeyEvent.VK_
     * @return If the key is currently down
     */
    public static boolean keyDown(int keyCode) {
        return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
    }
    
    /**
     * @param keyCode The key to check of: On of KeyEvent.VK_
     * @return If the key has just changed from up to down
     */
    public static boolean keyDownOnce(int keyCode) {
        return keys[keyCode] == KeyState.ONCE;
    }
    
    /**
     * @return If the ctrl key is currently down
     */
    public static boolean ctrl() {
        return keyDown(KeyEvent.VK_CONTROL);
    }
    
    /**
     * @return If the alt key is currently down
     */
    public static boolean alt() {
        return keyDown(KeyEvent.VK_ALT);
    }
    
    /**
     * @return If the shift key is currently down
     */
    public static boolean shift() {
        return keyDown(KeyEvent.VK_SHIFT);
    }
    
    /**
     * @return The last typed characters
     */
    public static String typedString() {
        return typedString.toString();
    }
    
    // === Listener methods ===
    
    public void keyPressed(KeyEvent e) {
        setKeyPressed(e.getKeyCode());
    }
    
    private synchronized void setKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < KEY_COUNT)
            currentKeys[keyCode] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        setKeyReleased(e.getKeyCode());
    }
    
    public synchronized void setKeyReleased(int keyCode) {
        if (keyCode >= 0 && keyCode < KEY_COUNT)
            currentKeys[keyCode] = false;
    }
    
    public void keyTyped(KeyEvent e) {
        typedString.append(e.getKeyChar());
    }
}

