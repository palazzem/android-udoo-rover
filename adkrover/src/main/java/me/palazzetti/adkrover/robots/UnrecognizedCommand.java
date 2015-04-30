package me.palazzetti.adkrover.robots;

public class UnrecognizedCommand extends Exception {
    public UnrecognizedCommand() {
        super("Unrecognized command");
    }

    public UnrecognizedCommand(String message) {
        super(message);
    }
}
