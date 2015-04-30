package me.palazzetti.adkrover.robots;

import me.palazzetti.adktoolkit.AdkManager;

public class Rover {

    /**
     * Create a serial command with format DIRECTION-SPEED (D-SSS)
     * @param command used in Arduino
     * @param speed with range 0-400
     * @return a valid Arduino command
     */

    public static String commandBuilder(int command, int speed) {
        return String.valueOf(command) + "-" + String.valueOf(speed);
    }

    /**
     * Sends a command to Arduino board
     * @param adkManager used to send serial command
     * @param serialCommand valid Arduino command
     */

    public static void executeCommand(AdkManager adkManager, String serialCommand) {
        adkManager.write(serialCommand);
    }
}
