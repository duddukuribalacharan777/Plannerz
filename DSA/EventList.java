import java.io.*;
import java.util.*;

public class EventList {
    // ========== LISTS: Abstract Data Types (ADTs) ==========
    // This represents a List ADT node implementation
    // Syllabus Topic: List ADT, Abstract Data Types (ADTs)
    
    public static class Event {
        public String title;
        public String type;
        public String time;
        public String description;
        public String date;
        public Event next;  // Reference to next node - Used in Singly Linked List

        public Event(String title, String type, String time, String description, String date) {
            this.title = title;
            this.type = type;
            this.time = time;
            this.description = description;
            this.date = date;
            this.next = null;
        }
    }

    // ========== STACK (CO3): Undo/History Stack ==========
    // Syllabus Topic: Stacks, LIFO data structure
    public static class UndoStack {
        private Stack<Event> undoStack = new Stack<>();
        
        void push(Event event) {
            undoStack.push(new Event(event.title, event.type, event.time, event.description, event.date));
        }
        
        Event pop() {
            return undoStack.isEmpty() ? null : undoStack.pop();
        }
        
        boolean isEmpty() {
            return undoStack.isEmpty();
        }
    }

    // ========== QUEUE (CO3): Event Processing Queue ==========
    // Syllabus Topic: Queues, FIFO data structure
    public static class EventQueue {
        private Queue<Event> queue = new LinkedList<>();
        
        void enqueue(Event event) {
            queue.offer(new Event(event.title, event.type, event.time, event.description, event.date));
        }
        
        Event dequeue() {
            return queue.isEmpty() ? null : queue.poll();
        }
        
        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    static Event head = null;  // Head pointer - Start of Singly Linked List
    private static final String EVENTS_FILE = "../DSA/events.json";
    
    // ========== CO4: Hash-Based Structures ==========
    // Syllabus Topic: Hash tables, Java Collections Framework
    static HashMap<String, Event> eventMap = new HashMap<>();  // O(1) lookup by title
    static ArrayList<Event> eventList = new ArrayList<>();      // For efficient iteration
    
    // ========== CO3: Stack and Queue ==========
    static UndoStack undoStack = new UndoStack();
    static EventQueue eventQueue = new EventQueue();

    // ========== LISTS: Singly Linked List Operations ==========
    // Syllabus Topic: Linked list implementation, Singly linked lists, Applications of lists
    // Operation: INSERT
    // Insert event (Linked List)
    static void addEvent(String title, String type, String time, String desc, String date) {
        Event newEvent = new Event(title, type, time, desc, date);

        if (head == null) {
            head = newEvent;
        } else {
            Event temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newEvent;
        }
        
        // ========== CO4: Add to HashMap and ArrayList ==========
        eventMap.put(title, newEvent);
        eventList.add(newEvent);
        
        // ========== CO3: Add to Undo Stack ==========
        undoStack.push(newEvent);
        
        // Save to JSON file
        saveEventsToFile();
    }
    
    // ========== CO1: SORTING ALGORITHMS ==========
    // Syllabus Topic: Sorting algorithms, algorithmic efficiency
    
    // Bubble Sort - Compare and swap adjacent elements
    static void bubbleSortByDate() {
        if (head == null) return;
        
        boolean swapped;
        do {
            swapped = false;
            Event temp = head;
            
            while (temp != null && temp.next != null) {
                if (compareDate(temp.date, temp.next.date) > 0) {
                    // Swap data
                    swapEventData(temp, temp.next);
                    swapped = true;
                }
                temp = temp.next;
            }
        } while (swapped);
    }
    
    // Quick Sort Helper for ArrayList
    static void quickSortByDate(ArrayList<Event> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSortByDate(list, low, pi - 1);
            quickSortByDate(list, pi + 1, high);
        }
    }
    
    static int partition(ArrayList<Event> list, int low, int high) {
        Event pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (compareDate(list.get(j).date, pivot.date) < 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    static int compareDate(String date1, String date2) {
        String[] d1 = date1.split("-");
        String[] d2 = date2.split("-");
        
        int year1 = Integer.parseInt(d1[2]);
        int year2 = Integer.parseInt(d2[2]);
        if (year1 != year2) return year1 - year2;
        
        int month1 = Integer.parseInt(d1[1]);
        int month2 = Integer.parseInt(d2[1]);
        if (month1 != month2) return month1 - month2;
        
        return Integer.parseInt(d1[0]) - Integer.parseInt(d2[0]);
    }
    
    static void swapEventData(Event a, Event b) {
        String tempTitle = a.title;
        String tempType = a.type;
        String tempTime = a.time;
        String tempDesc = a.description;
        String tempDate = a.date;
        
        a.title = b.title;
        a.type = b.type;
        a.time = b.time;
        a.description = b.description;
        a.date = b.date;
        
        b.title = tempTitle;
        b.type = tempType;
        b.time = tempTime;
        b.description = tempDesc;
        b.date = tempDate;
    }
    
    // ========== CO1: SEARCHING ALGORITHMS ==========
    // Syllabus Topic: Searching algorithms, linear search, binary search
    
    // Linear Search - Search through each element
    static Event linearSearchByTitle(String title) {
        Event temp = head;
        while (temp != null) {
            if (temp.title.equalsIgnoreCase(title)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }
    
    // Binary Search on sorted ArrayList
    static Event binarySearchByDate(String date) {
        quickSortByDate(eventList, 0, eventList.size() - 1);
        int left = 0, right = eventList.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Event midEvent = eventList.get(mid);
            int cmp = compareDate(midEvent.date, date);
            
            if (cmp == 0) return midEvent;
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    // ========== LISTS: List Traversal Operation ==========
    // Syllabus Topic: Linked list implementation, Singly linked lists
    // Operation: TRAVERSE/DISPLAY
    static String generateEventHTML() {
        if (head == null) {
            return "<html><body style='font-family:Arial;font-size:14px;color:#555;text-align:center;'>" +
                    "<h3>📌 No events available</h3><p>Click 'Add Event' to create your first event</p></body></html>";
        }

        Event temp = head;
        StringBuilder html = new StringBuilder("<html><body style='font-family:Arial;font-size:14px;color:#333;'>");
        html.append("<h2 style='text-align:center;color:#2255aa'>📅 Your Events</h2>");

        while (temp != null) {
            String color;
            switch (temp.type.toLowerCase()) {
                case "class": color = "#4CAF50"; break;
                case "task": color = "#2196F3"; break;
                case "exam": color = "#FF9800"; break;
                case "extra": color = "#9C27B0"; break;
                case "meeting": color = "#F44336"; break;
                default: color = "#607D8B"; break;
            }
            html.append("<div style='border-left:5px solid ").append(color)
                .append(";background:#fff;margin:10px 0;padding:10px;box-shadow:0 2px 4px rgba(0,0,0,0.1);'>");
            html.append("<div style='font-size:16px;font-weight:bold;color:").append(color).append(";'>").append(temp.title).append("</div>");
            html.append("<div><strong>Date:</strong> ").append(temp.date).append(" &nbsp; ");
            html.append("<strong>Time:</strong> ").append(temp.time).append("</div>");
            html.append("<div><em>").append(temp.type).append("</em></div>");
            html.append("<div style='margin-top:5px;color:#555;'>").append(temp.description).append("</div>");
            html.append("</div>");
            temp = temp.next;
        }

        html.append("</body></html>");
        return html.toString();
    }
    
    // ========== CO4: Hash-based Retrieval ==========
    // Get event by title using HashMap (O(1) lookup)
    static Event getEventByTitle(String title) {
        return eventMap.get(title);
    }
    
    // ========== CO3: Get events from Queue ==========
    static String getQueuedEventsHTML() {
        if (eventQueue.isEmpty()) {
            return "<html><body style='font-family:Arial;font-size:14px;'>" +
                    "<h3>No queued events</h3></body></html>";
        }
        
        Queue<Event> tempQueue = new LinkedList<>();
        StringBuilder html = new StringBuilder("<html><body style='font-family:Arial;font-size:14px;'>");
        html.append("<h3 style='color:#FF6B00;'>⏳ Event Processing Queue (FIFO)</h3>");
        
        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.dequeue();
            tempQueue.offer(e);
            html.append("<div style='margin:5px 0;padding:5px;background:#FFF3E0;'>");
            html.append(e.title).append(" - ").append(e.date);
            html.append("</div>");
        }
        
        // Restore queue
        while (!tempQueue.isEmpty()) {
            eventQueue.enqueue(tempQueue.poll());
        }
        
        html.append("</body></html>");
        return html.toString();
    }

    private static void saveEventsToFile() {
        try {
            StringBuilder jsonContent = new StringBuilder("[\n");
            Event temp = head;
            boolean first = true;
            
            while (temp != null) {
                if (!first) jsonContent.append(",\n");
                
                // Escape special characters
                String title = escapeJson(temp.title);
                String type = escapeJson(temp.type);
                String time = escapeJson(temp.time);
                String description = escapeJson(temp.description);
                String date = escapeJson(temp.date);
                
                jsonContent.append("  {\n");
                jsonContent.append("    \"date\": \"").append(date).append("\",\n");
                jsonContent.append("    \"time\": \"").append(time).append("\",\n");
                jsonContent.append("    \"title\": \"").append(title).append("\",\n");
                jsonContent.append("    \"type\": \"").append(type).append("\",\n");
                jsonContent.append("    \"description\": \"").append(description).append("\"\n");
                jsonContent.append("  }");
                
                temp = temp.next;
                first = false;
            }
            
            jsonContent.append("\n]");
            
            try (FileWriter file = new FileWriter(EVENTS_FILE)) {
                file.write(jsonContent.toString());
                file.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }

    static void loadEventsFromFile() {
        try {
            File file = new File(EVENTS_FILE);
            if (!file.exists()) return;
            
            head = null;
            eventMap.clear();      // Clear CO4: HashMap
            eventList.clear();     // Clear CO4: ArrayList
            
            try (Scanner scanner = new Scanner(file)) {
                StringBuilder content = new StringBuilder();
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
                
                // Simple JSON parser
                String jsonStr = content.toString();
                String[] eventObjects = jsonStr.split("\\{");
                
                for (String eventObj : eventObjects) {
                    if (eventObj.trim().isEmpty()) continue;
                    
                    try {
                        String date = extractJsonValue(eventObj, "date");
                        String time = extractJsonValue(eventObj, "time");
                        String title = extractJsonValue(eventObj, "title");
                        String type = extractJsonValue(eventObj, "type");
                        String description = extractJsonValue(eventObj, "description");
                        
                        if (!title.isEmpty() && !date.isEmpty()) {
                            addEvent(title, type, time, description, date);
                        }
                    } catch (Exception e) {
                        // Skip malformed entries
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        
        if (startIndex == -1) return "";
        
        startIndex = json.indexOf("\"", startIndex + searchKey.length());
        if (startIndex == -1) return "";
        
        startIndex++;
        int endIndex = json.indexOf("\"", startIndex);
        
        if (endIndex == -1) return "";
        
        String value = json.substring(startIndex, endIndex);
        
        // Unescape JSON special characters
        return value.replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t")
                   .replace("\\\"", "\"")
                   .replace("\\\\", "\\");
    }
}
