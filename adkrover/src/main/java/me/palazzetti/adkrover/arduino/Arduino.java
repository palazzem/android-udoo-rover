package me.palazzetti.adkrover.arduino;

import me.palazzetti.adkrover.adktoolkit.AdkManager;

/**
 * Implements Arduino commands interface
 */
public class Arduino {
    public static void executeCommand(AdkManager adkManager, int command) throws NotImplementedCommand {
        switch (command) {
            default:
                throw new NotImplementedCommand("This command is not available");
        }
    }
}
