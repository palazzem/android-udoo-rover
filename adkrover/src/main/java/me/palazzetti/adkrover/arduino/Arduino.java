package me.palazzetti.adkrover.arduino;

import me.palazzetti.adkrover.adktoolkit.AdkManager;

/**
 * Implements Arduino commands interface
 */

public class Arduino {

    public static void executeCommand(AdkManager adkManager, int command, int speed) throws NotImplementedCommand {
        // Create a serial command with format DIRECTION-SPEED (D-SSS)
        String serialCommand = String.valueOf(command) + "-" + String.valueOf(speed);
        adkManager.sendText(serialCommand);
    }
}
