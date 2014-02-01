package me.palazzetti.adkrover.twitter;

/**
 * Malformed Twitter commands
 */

public class MalformedTwitterCommand extends Exception {
    public MalformedTwitterCommand() {}
    public MalformedTwitterCommand(String message) {
        super(message);
    }
}
