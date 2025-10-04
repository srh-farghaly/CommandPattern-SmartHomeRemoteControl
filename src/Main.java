import java.util.function.Consumer;

/**
 * COMMAND DESIGN PATTERN DEMONSTRATION
 *
 * This project demonstrates the Command Pattern using a universal remote control
 * that can operate multiple smart home devices (TV, Stereo, Smart Light).
 *
 * Key Concept: The remote control (invoker) doesn't know anything about the devices.
 * It only knows how to execute commands. This decoupling allows us to add new devices
 * without modifying the remote control code.
 */

// ============================================================================
// PART 1: COMMAND INTERFACE (The Contract)
// ============================================================================

/**
 * Command Interface
 *
 * This is the contract that ALL commands must follow. Every command must have
 * an execute() method that performs some action.
 *
 * Think of it as: "Every button on a remote must be pressable"
 *
 * Role: Defines the standard API for executing operations
 */
interface Command {
    void execute();  // The "DO IT" button that all commands must implement
}

// ============================================================================
// PART 2: CONCRETE COMMANDS (The Order Slips / Instructions)
// ============================================================================

/**
 * TurnOnCommand
 *
 * A reusable command that can turn on ANY device. It doesn't know what kind
 * of device it's turning on - it just calls the action it was given.
 *
 * Think of it as: An order slip that says "Turn something on"
 *
 * Role: Wraps the "turn on" action as an executable command
 */
class TurnOnCommand implements Command {
    private Consumer<Void> deviceAction;  // Holds the action to perform

    /**
     * Constructor: Give this command the action it should perform
     * @param deviceAction The method to call when executing (e.g., tv.turnOn())
     */
    public TurnOnCommand(Consumer<Void> deviceAction) {
        this.deviceAction = deviceAction;
    }

    @Override
    public void execute() {
        // When the remote presses this button, we trigger the action
        // This is like a waiter handing an order slip to the chef
        deviceAction.accept(null);
    }
}

/**
 * TurnOffCommand
 *
 * A reusable command that can turn off ANY device.
 * Works the same as TurnOnCommand but for turning things off.
 */
class TurnOffCommand implements Command {
    private Consumer<Void> deviceAction;

    public TurnOffCommand(Consumer<Void> deviceAction) {
        this.deviceAction = deviceAction;
    }

    @Override
    public void execute() {
        deviceAction.accept(null);
    }
}

/**
 * AdjustVolumeCommand
 *
 * A command specific to devices that have volume (like stereos).
 * This shows how you can have device-specific commands alongside generic ones.
 */
class AdjustVolumeCommand implements Command {
    private Runnable stereoAction;  // Runnable = something that can run

    public AdjustVolumeCommand(Runnable stereoAction) {
        this.stereoAction = stereoAction;
    }

    @Override
    public void execute() {
        stereoAction.run();  // Tell the stereo to adjust its volume
    }
}

/**
 * ChangeChannelCommand
 *
 * A command specific to TVs. Shows that some commands are device-specific
 * when the functionality is unique to that device type.
 */
class ChangeChannelCommand implements Command {
    private Runnable tvAction;

    public ChangeChannelCommand(Runnable tvAction) {
        this.tvAction = tvAction;
    }

    @Override
    public void execute() {
        tvAction.run();  // Tell the TV to change its channel
    }
}

/**
 * AdjustBrightness
 *
 * A command specific to smart lights. Allows setting brightness to a specific level.
 * This demonstrates passing parameters to commands.
 */
class AdjustBrightness implements Command {
    private SmartLight light;        // The receiver (who will do the work)
    private int brightnessLevel;     // The parameter for the action

    /**
     * @param smartLight The light to control
     * @param brightnessLevel The brightness level (0-100)
     */
    public AdjustBrightness(SmartLight smartLight, int brightnessLevel) {
        this.light = smartLight;
        this.brightnessLevel = brightnessLevel;
    }

    @Override
    public void execute() {
        // Tell the light to adjust its brightness to the specified level
        light.adjustBrightness(brightnessLevel);
    }
}

/**
 * ChangeColor
 *
 * A command to change the color of a smart light.
 * Another example of a parameterized, device-specific command.
 */
class ChangeColor implements Command {
    private SmartLight light;
    private String color;

    /**
     * @param smartLight The light to control
     * @param color The color to set (e.g., "red", "blue", "white")
     */
    public ChangeColor(SmartLight smartLight, String color) {
        this.light = smartLight;
        this.color = color;
    }

    @Override
    public void execute() {
        // Tell the light to change to the specified color
        light.adjustColor(color);
    }
}

// ============================================================================
// PART 3: DEVICE INTERFACE (Common Device Contract)
// ============================================================================

/**
 * Device Interface
 *
 * Defines the common operations that all devices should support.
 * In this case, all devices can turn on and off.
 *
 * Role: Ensures all devices have basic common functionality
 */
interface Device {
    void turnOn();   // Every device must know how to turn on
    void turnOff();  // Every device must know how to turn off
}

// ============================================================================
// PART 4: CONCRETE RECEIVERS (The Workers Who Do Real Work)
// ============================================================================

/**
 * TV Receiver
 *
 * This is the actual TV that knows HOW to turn itself on, off, and change channels.
 * It contains the real business logic.
 *
 * Think of it as: The chef who knows how to cook
 *
 * Role: Performs the actual operations when commanded
 */
class TV implements Device {
    @Override
    public void turnOn() {
        // THIS IS WHERE REAL WORK HAPPENS
        System.out.println("TV is now on");
        // In reality: power on hardware, initialize display, load settings, etc.
    }

    @Override
    public void turnOff() {
        System.out.println("TV is now off");
        // In reality: save state, power down display, cut power, etc.
    }

    /**
     * TV-specific functionality beyond the Device interface
     */
    public void changeChannel() {
        System.out.println("Channel changed");
        // In reality: tune to different frequency, update display, etc.
    }
}

/**
 * Stereo Receiver
 *
 * The actual stereo system that knows how to control audio playback.
 */
class Stereo implements Device {
    @Override
    public void turnOn() {
        System.out.println("Stereo is now on");
        // In reality: power on amplifier, initialize speakers, load presets, etc.
    }

    @Override
    public void turnOff() {
        System.out.println("Stereo is now off");
        // In reality: fade out audio, power down components, etc.
    }

    /**
     * Stereo-specific functionality
     */
    public void adjustVolume() {
        System.out.println("Volume adjusted");
        // In reality: modify amplifier gain, adjust speaker output, etc.
    }
}

/**
 * SmartLight Receiver
 *
 * A smart light that can turn on/off, change brightness, and change colors.
 * Demonstrates a more complex receiver with multiple operations.
 */
class SmartLight implements Device {
    @Override
    public void turnOn() {
        System.out.println("SmartLight is now on");
        // In reality: power on LED, set to default brightness/color, etc.
    }

    @Override
    public void turnOff() {
        System.out.println("SmartLight is now off");
        // In reality: fade out, save current state, cut power, etc.
    }

    /**
     * Smart light specific functionality: adjust brightness
     * @param level Brightness level (0-100)
     */
    public void adjustBrightness(int level) {
        System.out.println("Brightness set to level " + level + "%");
        // In reality: adjust PWM signal to LED, update display, etc.
    }

    /**
     * Smart light specific functionality: change color
     * @param color The color name (e.g., "red", "blue", "white")
     */
    public void adjustColor(String color) {
        System.out.println("Color changed to: " + color);
        // In reality: adjust RGB values, update LED colors, etc.
    }
}

// ============================================================================
// PART 5: INVOKER (The Button Presser / Trigger)
// ============================================================================

/**
 * RemoteControl (Invoker)
 *
 * This is the universal remote control. It can trigger any command but doesn't
 * know ANYTHING about what the command does. It just calls execute().
 *
 * Think of it as: A waiter who delivers orders but doesn't know how to cook
 *
 * Role: Triggers commands without knowing their implementation details
 *
 * KEY POINT: This class never mentions TV, Stereo, or SmartLight!
 * That's the power of the Command Pattern - complete decoupling.
 */
class RemoteControl {
    private Command command;  // Currently assigned command

    /**
     * Assign a command to this remote button
     * Like programming a universal remote: "This button will do X"
     *
     * @param command The command to assign to the button
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Press the button - execute whatever command is currently assigned
     *
     * The remote doesn't know if it's turning on a TV, adjusting a light,
     * or any other operation. It just says "execute!" and the command handles it.
     */
    public void pressButton() {
        if (command != null) {
            command.execute();  // Trigger the command, don't ask questions!
        } else {
            System.out.println("No command assigned to this button");
        }
    }
}

// ============================================================================
// PART 6: CLIENT (Setup and Demonstration)
// ============================================================================

/**
 * Main Class (Client)
 *
 * This is where we set everything up and demonstrate the Command Pattern.
 * The client creates receivers, creates commands that operate on those receivers,
 * and assigns commands to the invoker.
 *
 * Role: Orchestrates the creation and configuration of the system
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   COMMAND PATTERN DEMONSTRATION        ║");
        System.out.println("║   Smart Home Remote Control System     ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // ====================================================================
        // STEP 1: Create the RECEIVERS (the workers who do real work)
        // ====================================================================
        System.out.println("STEP 1: Creating devices (RECEIVERS)...");
        System.out.println("────────────────────────────────────────");

        TV tv = new TV();                  // TV knows HOW to operate itself
        Stereo stereo = new Stereo();      // Stereo knows HOW to play audio
        SmartLight light = new SmartLight(); // Light knows HOW to illuminate

        System.out.println("✓ TV created");
        System.out.println("✓ Stereo created");
        System.out.println("✓ SmartLight created\n");

        // ====================================================================
        // STEP 2: Create CONCRETE COMMANDS (the instruction wrappers)
        // ====================================================================
        System.out.println("STEP 2: Creating commands (INSTRUCTION WRAPPERS)...");
        System.out.println("────────────────────────────────────────");

        // Generic turn on/off commands - reusable for any device!
        Command turnOnTvCommand = new TurnOnCommand(v -> tv.turnOn());
        Command turnOffTvCommand = new TurnOffCommand(v -> tv.turnOff());
        System.out.println("✓ TV on/off commands created");

        // Device-specific commands
        Command adjustVolumeStereoCommand = new AdjustVolumeCommand(stereo::adjustVolume);
        Command changeChannelTvCommand = new ChangeChannelCommand(tv::changeChannel);
        System.out.println("✓ TV channel and Stereo volume commands created");

        // Smart light commands - reusing generic TurnOn, plus specific commands
        Command turnOnLightCommand = new TurnOnCommand(v -> light.turnOn());
        Command setColorCommand = new ChangeColor(light, "red");
        Command setBrightnessCommand = new AdjustBrightness(light, 75);
        System.out.println("✓ SmartLight commands created\n");

        // ====================================================================
        // STEP 3: Create the INVOKER (the remote control)
        // ====================================================================
        System.out.println("STEP 3: Creating remote control (INVOKER)...");
        System.out.println("────────────────────────────────────────");
        RemoteControl remote = new RemoteControl();
        System.out.println("✓ Universal remote control created");
        System.out.println("  (Notice: Remote knows NOTHING about devices!)\n");

        // ====================================================================
        // STEP 4: Use the system - demonstrate decoupling
        // ====================================================================
        System.out.println("STEP 4: Using the remote control...");
        System.out.println("════════════════════════════════════════\n");

        // Button 1: Turn on TV
        System.out.println("[Button 1] Programming: Turn On TV");
        remote.setCommand(turnOnTvCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 2: Adjust stereo volume
        System.out.println("\n[Button 2] Programming: Adjust Stereo Volume");
        remote.setCommand(adjustVolumeStereoCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 3: Change TV channel
        System.out.println("\n[Button 3] Programming: Change TV Channel");
        remote.setCommand(changeChannelTvCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 4: Turn off TV
        System.out.println("\n[Button 4] Programming: Turn Off TV");
        remote.setCommand(turnOffTvCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 5: Turn on smart light
        System.out.println("\n[Button 5] Programming: Turn On SmartLight");
        remote.setCommand(turnOnLightCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 6: Change light color
        System.out.println("\n[Button 6] Programming: Change Light Color to Red");
        remote.setCommand(setColorCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // Button 7: Adjust light brightness
        System.out.println("\n[Button 7] Programming: Set Brightness to 75%");
        remote.setCommand(setBrightnessCommand);
        System.out.print("Pressing button → ");
        remote.pressButton();

        // ====================================================================
        // Summary and key takeaways
        // ====================================================================
        System.out.println("\n════════════════════════════════════════");
        System.out.println("KEY TAKEAWAYS:");
        System.out.println("════════════════════════════════════════");
        System.out.println("✓ RemoteControl class NEVER mentions TV, Stereo, or SmartLight");
        System.out.println("✓ Same remote controls all devices through Command interface");
        System.out.println("✓ Can add new devices without changing RemoteControl code");
        System.out.println("✓ Commands encapsulate requests as objects");
        System.out.println("✓ Complete decoupling between invoker and receivers");
        System.out.println("\nPattern Components:");
        System.out.println("  • Command Interface → Standard API for all commands");
        System.out.println("  • Concrete Commands → Wrap specific operations");
        System.out.println("  • Receivers → Do the actual work (TV, Stereo, Light)");
        System.out.println("  • Invoker → Triggers commands (RemoteControl)");
        System.out.println("  • Client → Sets up the system (Main)");
        System.out.println("════════════════════════════════════════\n");
    }
}