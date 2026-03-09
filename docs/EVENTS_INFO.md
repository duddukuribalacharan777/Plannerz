# 🔔 Notifications Page - View Your DSA Events

## How to View Events

1. **Open the Java application** (`java DSAmain` in the DSA folder)
2. **Add events** using the GUI interface with dates and times
3. **Open `notifiy.html`** in your browser
4. **Auto-refresh** - Events update every 3 seconds!

## What You'll See

The notifications page displays:
- 📅 Event dates in a readable format
- ⏰ Event times
- 📝 Event titles  
- 🏷️ Event types (colored by category)
- 📄 Event descriptions

## Color Coding

- 🟢 **Class** - Class events
- 🔵 **Task** - Task events
- 🟠 **Exam** - Exam events
- 🟣 **Extra** - Extra activities
- 🔴 **Meeting** - Meetings
- 🔘 **Other** - Other events

## How It Works

```
Java App (DSAmain)
       ↓
   Create events
       ↓
   Save to events.json
       ↓
   JavaScript (script.js)
       ↓
   Fetch events every 3 seconds
       ↓
   Display in HTML (notifiy.html)
```

## Features

✨ Real-time updates
✨ Beautiful card layout
✨ Responsive design
✨ Sorted by date and time
✨ No page refresh needed!

---

**Note:** Both the Java app and the browser should be running for live updates.
