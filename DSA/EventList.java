import java.io.*;
import java.util.*;

// ========== LISTS: Abstract Data Types (ADTs) ==========
// This represents a List ADT node implementation
// Syllabus Topic: List ADT, Abstract Data Types (ADTs)

class Event {
    String title;
    String type;
    String time;
    String description;
    String date;
    Event next;  // Reference to next node - Used in Singly Linked List

    Event(String title, String type, String time, String description, String date) {
        this.title = title;
        this.type = type;
        this.time = time;
        this.description = description;
        this.date = date;
        this.next = null;
    }
}

public class EventList {
    static Event head = null;  // Head pointer - Start of Singly Linked List
    private static final String EVENTS_FILE = "../fwd/events.json";

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
        
        // Save to JSON file
        saveEventsToFile();
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
