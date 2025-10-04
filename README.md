# Command Design Pattern - Smart Home Remote Control

Hands-on practice from UAlberta’s Software Design & Architecture specialization:
A comprehensive implementation of the Command Design Pattern in Java, demonstrated through a universal remote control system for smart home devices.

## Table of Contents
- [What is the Command Pattern?](#what-is-the-command-pattern)
- [When to Use It](#when-to-use-it)
- [Project Structure](#project-structure)
- [Components Explained](#components-explained)
- [How It Works](#how-it-works)
- [Running the Code](#running-the-code)
- [Adding New Devices](#adding-new-devices)
- [Real-World Applications](#real-world-applications)

## What is the Command Pattern?

The Command Pattern is a behavioral design pattern that turns a request into a stand-alone object containing all information about the request. This transformation lets you:

- **Decouple** the sender (invoker) from the receiver
- **Queue** operations for later execution
- **Support undo/redo** functionality
- **Log** operations for auditing
- **Compose** complex operations from simple ones

### The Restaurant Analogy

Think of it like a restaurant:
- **Customer** (Client) places an order
- **Order Slip** (Command) contains the details
- **Waiter** (Invoker) delivers the slip to the kitchen
- **Chef** (Receiver) prepares the food

The waiter doesn't need to know how to cook - they just deliver orders. The chef doesn't need to know who ordered - they just execute orders.

## When to Use It

Use the Command Pattern when you need to:
- Decouple objects that invoke operations from objects that perform them
- Queue, log, or schedule operations
- Support undo/redo functionality
- Build systems with macro commands (composite operations)
- Implement callback mechanisms

## Project Structure

```
├── Command (Interface)
│   └── execute()
│
├── Concrete Commands
│   ├── TurnOnCommand
│   ├── TurnOffCommand
│   ├── AdjustVolumeCommand
│   ├── ChangeChannelCommand
│   ├── AdjustBrightness
│   └── ChangeColor
│
├── Device (Interface)
│   ├── turnOn()
│   └── turnOff()
│
├── Receivers (Concrete Devices)
│   ├── TV
│   ├── Stereo
│   └── SmartLight
│
├── Invoker
│   └── RemoteControl
│
└── Client
    └── Main
```

## Components Explained

### 1. Command Interface
The contract that all commands must follow. Every command must have an `execute()` method.

```java
interface Command {
    void execute();
}
```

**Role**: Defines the API for executing operations

### 2. Concrete Commands
Specific implementations that wrap receiver actions. These are "order slips" that contain instructions.

```java
class TurnOnCommand implements Command {
    private Consumer<Void> deviceAction;
    
    public void execute() {
        deviceAction.accept(null);
    }
}
```

**Role**: Encapsulate a request as an object

### 3. Receiver
The object that performs the actual work. Contains the business logic.

```java
class TV implements Device {
    public void turnOn() {
        System.out.println("TV is now on");
        // Actual hardware initialization here
    }
}
```

**Role**: Knows how to perform the operations

### 4. Invoker
Triggers commands without knowing their details. Responsible for executing commands.

```java
class RemoteControl {
    private Command command;
    
    public void pressButton() {
        command.execute();
    }
}
```

**Role**: Asks the command to carry out the request

### 5. Client
Sets up the command objects and associates them with receivers.

```java
Command turnOnTV = new TurnOnCommand(v -> tv.turnOn());
remote.setCommand(turnOnTV);
remote.pressButton();
```

**Role**: Creates and configures command objects

## How It Works

### Example Flow: Turning on a TV

1. **Client creates receiver**:
   ```java
   TV tv = new TV();
   ```

2. **Client creates command with receiver**:
   ```java
   Command turnOnCommand = new TurnOnCommand(v -> tv.turnOn());
   ```

3. **Client assigns command to invoker**:
   ```java
   remote.setCommand(turnOnCommand);
   ```

4. **Invoker executes command**:
   ```java
   remote.pressButton();  // Calls command.execute()
   ```

5. **Command calls receiver**:
   ```java
   tv.turnOn();  // Actual work happens here
   ```

### The Key Benefit

Notice that `RemoteControl` never mentions `TV`, `Stereo`, or `SmartLight`. It only knows about the `Command` interface. This means:
- You can add new devices without changing the remote
- The same remote button can control any device
- Commands can be stored, queued, or logged

## Running the Code

### Prerequisites
- Java 8 or higher
- Any Java IDE or command line

### Compile and Run

```bash
# Compile
javac Main.java

# Run
java Main
```

### Expected Output

```
========================================
COMMAND PATTERN DEMONSTRATION
========================================

STEP 1: Creating devices (RECEIVERS)...
✓ TV and Stereo and smartLight created

STEP 2: Creating commands (ORDER SLIPS)...
✓ Created: TurnOnTvCommand
✓ Created: TurnOffTvCommand
...

STEP 4: Pressing buttons!
========================================

[Button 1] Assigning: Turn On TV
Pressing button... TV is now on

[Button 5] Assigning: Turn On smartLight
Pressing button... SmartLight is now on

[Button 7] Assigning: Adjust color on smartLight
Pressing button... Now You are viewing the screen in the color red
```

## Adding New Devices

### Step 1: Create the Receiver

```java
class AirConditioner implements Device {
    @Override
    public void turnOn() {
        System.out.println("AC is now on");
    }
    
    @Override
    public void turnOff() {
        System.out.println("AC is now off");
    }
    
    public void setTemperature(int temp) {
        System.out.println("Temperature set to " + temp + "°C");
    }
}
```

### Step 2: Create Commands (if needed)

For temperature control:
```java
class SetTemperatureCommand implements Command {
    private AirConditioner ac;
    private int temperature;
    
    public SetTemperatureCommand(AirConditioner ac, int temp) {
        this.ac = ac;
        this.temperature = temp;
    }
    
    public void execute() {
        ac.setTemperature(temperature);
    }
}
```

For turn on/off, reuse existing commands:
```java
Command turnOnAC = new TurnOnCommand(v -> ac.turnOn());
```

### Step 3: Use It

```java
AirConditioner ac = new AirConditioner();
Command setTemp = new SetTemperatureCommand(ac, 22);

remote.setCommand(setTemp);
remote.pressButton();  // Output: Temperature set to 22°C
```

**No changes needed to `RemoteControl` class!**

## Real-World Applications

### 1. GUI Buttons
```java
// Every menu item or button is a command
Command saveCommand = new SaveCommand(document);
saveButton.setAction(saveCommand);
```

### 2. Transaction Systems
```java
// Database transactions as commands
Command transaction = new TransferMoneyCommand(fromAccount, toAccount, amount);
transactionQueue.add(transaction);
```

### 3. Undo/Redo
```java
class Command {
    void execute();
    void undo();  // Reverse the operation
}

Stack<Command> history = new Stack<>();
command.execute();
history.push(command);

// Later: undo
Command last = history.pop();
last.undo();
```

### 4. Macro Commands
```java
class MacroCommand implements Command {
    private List<Command> commands;
    
    public void execute() {
        for (Command cmd : commands) {
            cmd.execute();
        }
    }
}

// Movie mode: turn off lights, turn on TV, adjust stereo
Command movieMode = new MacroCommand(Arrays.asList(
    new TurnOffCommand(light),
    new TurnOnCommand(tv),
    new AdjustVolumeCommand(stereo)
));
```

## Key Takeaways

1. **Separation of Concerns**: The invoker doesn't know about receivers, and receivers don't know about invokers
2. **Flexibility**: Easy to add new commands without modifying existing code
3. **Single Responsibility**: Each command class has one job
4. **Open/Closed Principle**: Open for extension (new commands), closed for modification (existing code)

## Design Principles Used

- **Encapsulation**: Commands encapsulate requests as objects
- **Loose Coupling**: Invoker and receiver are decoupled
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Can extend with new commands without modifying invoker

## Common Pitfalls to Avoid

1. **Overuse**: Don't use Command Pattern for simple method calls
2. **Too Many Classes**: Each command needs its own class - consider if it's worth it
3. **Forgetting the Receiver**: Commands should delegate to receivers, not contain business logic

## Further Reading

- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns) (Gang of Four)
- [Refactoring Guru - Command Pattern](https://refactoring.guru/design-patterns/command)
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/0596007124/)

