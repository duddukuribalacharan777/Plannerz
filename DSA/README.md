# DSA - Data Structures and Algorithms Implementation

This folder contains the Java implementation of **Plannerz** using Data Structures and Algorithms concepts.

## Overview

The `DSAmain.java` file implements an event management application that demonstrates the use of:
- **Abstract Data Types (ADTs)**: List ADT
- **Singly Linked List**: For storing and managing events
- **Java Swing**: For the graphical user interface

## Features

- Add new events with title, type, time, description, and date
- View all stored events in a formatted display
- Gradient UI design for better user experience

## Prerequisites

- Java Development Kit (JDK) 8 or higher installed
- Java Runtime Environment (JRE) for running the application

## How to Run

1. **Compile the Java file:**
   ```
   javac DSAmain.java
   ```

2. **Run the application:**
   ```
   java DSAmain
   ```

3. **Using the Application:**
   - Click "Add Event" to create a new event
   - Fill in the event details in the dialog
   - Click "View Events" to see all stored events
   - Events are automatically saved to `../fwd/events.json` for web integration

## Expected Output

- A GUI window opens with two buttons: "Add Event" and "View Events"
- Adding an event shows a form dialog for input
- Viewing events displays them in a text area with formatted details
- Events persist between application runs via JSON storage

## Data Structure Used

- **Singly Linked List**: Each `Event` node contains event data and a reference to the next event
- Head pointer manages the list operations (insertion, traversal)
