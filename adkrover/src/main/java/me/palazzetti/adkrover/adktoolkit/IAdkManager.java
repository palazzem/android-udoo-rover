package me.palazzetti.adkrover.adktoolkit;

import android.hardware.usb.UsbAccessory;

/**
 * Defines ADK interfaces
 */

public interface IAdkManager {
    void openAccessory(UsbAccessory usbAccessory);
    void closeAccessory();
    String readText();
    void sendText(String text);

    // Activity related interfaces
    void closeAdk();
    void resumeAdk();
}
