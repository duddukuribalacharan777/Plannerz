import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

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

public class DSAmain extends JFrame {

    static Event head = null;  // Head pointer - Start of Singly Linked List
    private JTextPane eventDisplayArea;
    private JButton addEventBtn, viewEventsBtn;
    private static final String EVENTS_FILE = "../FWD/events.json";

    // gradient background panel class
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color c1 = new Color(230, 240, 250);
            Color c2 = new Color(200, 220, 255);
            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }
    }

    public DSAmain() {
        setTitle("📅 Daily Planner - DSA Event Manager");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // UI Setup
        setupUI();
        loadEventsFromFile();
    }

    private void setupUI() {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // toolbar with icons
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton tbAdd = new JButton("Add");
        tbAdd.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
        tbAdd.setFocusable(false);
        tbAdd.addActionListener(e -> openAddEventDialog());
        JButton tbView = new JButton("View");
        tbView.setIcon(UIManager.getIcon("FileView.fileIcon"));
        tbView.setFocusable(false);
        tbView.addActionListener(e -> displayEventsInGUI());
        toolBar.add(tbAdd);
        toolBar.add(tbView);

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel headerLabel = new JLabel("🎯 Daily Planner - Interactive Event Manager");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(25, 100, 200));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        addEventBtn = new JButton("➕ Add Event");
        addEventBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addEventBtn.setBackground(new Color(76, 175, 80));
        addEventBtn.setForeground(Color.WHITE);
        addEventBtn.setFocusPainted(false);
        addEventBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addEventBtn.addActionListener(e -> openAddEventDialog());
        
        viewEventsBtn = new JButton("👀 View All Events");
        viewEventsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewEventsBtn.setBackground(new Color(33, 150, 243));
        viewEventsBtn.setForeground(Color.WHITE);
        viewEventsBtn.setFocusPainted(false);
        viewEventsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewEventsBtn.addActionListener(e -> displayEventsInGUI());
        
        buttonPanel.add(addEventBtn);
        buttonPanel.add(viewEventsBtn);
        
        // Display Area (HTML)
        eventDisplayArea = new JTextPane();
        eventDisplayArea.setContentType("text/html");
        eventDisplayArea.setEditable(false);
        eventDisplayArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        eventDisplayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        eventDisplayArea.setBackground(new Color(250, 250, 250));
        eventDisplayArea.setBorder(new LineBorder(new Color(150, 150, 150), 2));
        eventDisplayArea.setText("<html><body style='font-family:Arial;font-size:14px;color:#333'>" +
                "<h2 style='text-align:center;color:#2255aa'>Welcome to Daily Planner!</h2>" +
                "<p>Click <b>Add Event</b> to create a new event<br>Click <b>View All Events</b> to see your schedule</p>" +
                "</body></html>");
        
        JScrollPane scrollPane = new JScrollPane(eventDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Add components
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(toolBar, BorderLayout.PAGE_START);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        // status bar
        JLabel statusBar = new JLabel("Ready");
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void openAddEventDialog() {
        JDialog dialog = new JDialog(this, "Add New Event", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));
        
        // Date Picker
        JLabel dateLabel = new JLabel("📅 Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        
        // Title
        JLabel titleLabel = new JLabel("📝 Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField titleField = new JTextField();
        
        // Type
        JLabel typeLabel = new JLabel("🏷️ Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        String[] types = {"Class", "Task", "Exam", "Extra", "Meeting", "Other"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        
        // Time
        JLabel timeLabel = new JLabel("⏰ Time:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField timeField = new JTextField("HH:MM (e.g., 14:30)");
        
        // Description
        JLabel descLabel = new JLabel("📄 Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        
        // Buttons
        JButton saveBtn = new JButton("✅ Save Event");
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 12));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton cancelBtn = new JButton("❌ Cancel");
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveBtn.addActionListener(e -> {
            try {
                if (titleField.getText().isEmpty() || timeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date selectedDate = (Date) dateSpinner.getValue();
                String dateStr = sdf.format(selectedDate);
                
                addEvent(titleField.getText(), typeCombo.getSelectedItem().toString(), 
                        timeField.getText(), descArea.getText(), dateStr);
                
                JOptionPane.showMessageDialog(dialog, "✅ Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                displayEventsInGUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(dateLabel);
        panel.add(dateSpinner);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(typeLabel);
        panel.add(typeCombo);
        panel.add(timeLabel);
        panel.add(timeField);
        panel.add(descLabel);
        panel.add(new JScrollPane(descArea));
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

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
    private void displayEventsInGUI() {
        if (head == null) {
            eventDisplayArea.setText("<html><body style='font-family:Arial;font-size:14px;color:#555;text-align:center;'>" +
                    "<h3>📌 No events available</h3><p>Click 'Add Event' to create your first event</p></body></html>");
            return;
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
        eventDisplayArea.setText(html.toString());
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

    private static void loadEventsFromFile() {
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

    public static void main(String[] args) {
        // ========== LISTS: Practical Application ==========
        // Syllabus Topic: Applications of lists
        // This Daily Planner demonstrates real-world use of Singly Linked List
        SwingUtilities.invokeLater(() -> {
            DSAmain frame = new DSAmain();
            frame.setVisible(true);
        });
    }
}