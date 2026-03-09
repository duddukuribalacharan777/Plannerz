# 📅 Plannerz

A full-stack event management application combining **Java Desktop Application** and a **Web-based Frontend** to create, manage, and visualize events efficiently.

## 🎯 Project Overview

Plannerz uses **Data Structures & Algorithms (DSA)** concepts to manage events. It features:
- 📱 Java Swing GUI for event creation
- 🌐 Interactive web interface with calendar view
- 📢 Notifications dashboard with auto-refresh

## 📁 Project Structure

├── DSA/

│ └── DSAmain.java # Java desktop application (GUI)

│ └── AddEventDialog.java

│ └── EventList.java

└── FWD/ # Frontend - Web interface

├── main.html # Calendar view

├── calender.html # Event calendar

├── addevents.html # Event creation form

├── notifiy.html # Notifications dashboard

├── events.json # Event data storage

├── script.js # JavaScript logic

├── style.css # Styling

└── EVENTS_INFO.md # Events documentation


## ⚙️ Features

### (Java - DSA)
- ✅ **Linked List Implementation** - Events stored using singly linked lists (List ADT)
- ✅ **GUI Interface** - User-friendly Swing application for adding events
- ✅ **Event Management** - Create, store, and manage events with dates, times, and descriptions

### (HTML/CSS/JavaScript)
- ✅ **Calendar View** - Visual calendar display of all events
- ✅ **Event Cards** - Beautiful card layout showing event details
- ✅ **Color Coding** - Events categorized by type with distinct colors:
  - 🟢 Class
  - 🔵 Task
  - 🟠 Exam
  - 🟣 Extra Activities
  - 🔴 Meeting
  - 🔘 Other
- ✅ **Real-time Updates** - Automatically refreshes every 3 seconds
- ✅ **Responsive Design** - Works on all devices
- ✅ **Notifications Page** - Dedicated view for event reminders

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Any modern web browser (Chrome, Firefox, Safari, Edge)
- Git (for cloning the repository)

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/duddukuribalacharan777/Plannerz.git
   cd Evensem
2. **Run the Java Application**

       cd DSA
       javac DSAmain.java
       java DSAmain

4. **Open the Web Interface**
   
   Open any HTML file in the FWD folder with your browser:
   
   main.html - Main calendar view

   calender.html - Event calendar

   notifiy.html - Notifications dashboard

📖 How It Works

    Java Application (DSAmain)

        ↓
        
    Add Events (GUI)
    
        ↓
        
    Store in Linked List
    
        ↓
        
    Export to events.json
    
        ↓
        
    Web Browser
    
        ↓
        
    JavaScript fetches JSON
    
        ↓
        
    Display Events (Real-time)


**Workflow:**

Launch Java App: Run DSAmain to open the GUI

Add Events: Fill in event details (title, type, date, time, description)

Save to JSON: Events are automatically stored in events.json

View in Browser: Open the HTML files to see your events

Auto-Refresh: Pages update every 3 seconds automatically


**💾 Data Storage**

Events are stored in JSON format in FWD/events.json:

    [
    {
    "title": "Data Structures Lecture",
    "type": "Class",
    "date": "2024-03-15",
    "time": "10:30",
    "description": "Learn linked lists and arrays"
    },
    {
    "title": "DSA Assignment",
    "type": "Task",
    "date": "2024-03-20",
    "time": "23:59",
    "description": "Implement binary search tree"
    }
    ]
**🛠️ Technologies Used**

Java - Desktop application with Swing GUI

Data Structures - Linked List ADT implementation

HTML5 - Structure and layout

CSS3 - Styling and responsive design

JavaScript - Event handling and dynamic updates




# Happy Planning! 📅✨
