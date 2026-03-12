import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DSAmain extends JFrame {

    private JTextPane eventDisplayArea;
    private JButton addEventBtn, viewEventsBtn, sortBtn, searchBtn;

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
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // UI Setup
        setupUI();
        EventList.loadEventsFromFile();
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
        tbAdd.addActionListener(e -> AddEventDialog.openAddEventDialog(this));
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        
        addEventBtn = new JButton("➕ Add Event");
        addEventBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addEventBtn.setBackground(new Color(76, 175, 80));
        addEventBtn.setForeground(Color.WHITE);
        addEventBtn.setFocusPainted(false);
        addEventBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addEventBtn.addActionListener(e -> AddEventDialog.openAddEventDialog(this));
        
        viewEventsBtn = new JButton("👀 View All");
        viewEventsBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewEventsBtn.setBackground(new Color(33, 150, 243));
        viewEventsBtn.setForeground(Color.WHITE);
        viewEventsBtn.setFocusPainted(false);
        viewEventsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewEventsBtn.addActionListener(e -> displayEventsInGUI());
        
        // ========== CO1: Sort Button ==========
        sortBtn = new JButton("🔤 Sort by Date");
        sortBtn.setFont(new Font("Arial", Font.BOLD, 12));
        sortBtn.setBackground(new Color(255, 152, 0));
        sortBtn.setForeground(Color.WHITE);
        sortBtn.setFocusPainted(false);
        sortBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sortBtn.addActionListener(e -> sortAndDisplay());
        
        // ========== CO1: Search Button ==========
        searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 12));
        searchBtn.setBackground(new Color(156, 39, 176));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> openSearchDialog());
        
        buttonPanel.add(addEventBtn);
        buttonPanel.add(viewEventsBtn);
        buttonPanel.add(sortBtn);
        buttonPanel.add(searchBtn);
        
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
                "<p>Click <b>Add Event</b> to create a new event<br>Click <b>View All</b> to see your schedule</p>" +
                "<p><small style='color:#999;'>Other buttons: Sort by date (CO1), Search events (CO1)</small></p>" +
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

    private void displayEventsInGUI() {
        eventDisplayArea.setText(EventList.generateEventHTML());
    }
    
    // ========== CO1: Sorting Display ==========
    private void sortAndDisplay() {
        EventList.bubbleSortByDate();
        eventDisplayArea.setText("<html><body style='font-family:Arial;font-size:14px;'>" +
                "<h2 style='color:#FF6B00;text-align:center;'>📅 Events Sorted by Date (Bubble Sort)</h2>" +
                "<p style='color:#666;text-align:center;'><em>CO1: Sorting Algorithm - Bubble Sort implemented</em></p>" +
                EventList.generateEventHTML().replace("<h2", "<h3") +
                "</body></html>");
    }
    
    // ========== CO1: Search Functionality ==========
    private void openSearchDialog() {
        String searchTitle = JOptionPane.showInputDialog(this, "Enter event title to search:");
        if (searchTitle != null && !searchTitle.isEmpty()) {
            // Using Linear Search
            EventList.Event foundEvent = EventList.linearSearchByTitle(searchTitle);
            if (foundEvent != null) {
                eventDisplayArea.setText("<html><body style='font-family:Arial;font-size:14px;'>" +
                        "<h2 style='color:#2196F3;text-align:center;'>🔍 Search Result</h2>" +
                        "<p style='color:#666;text-align:center;'><em>CO1: Linear Search Algorithm</em></p>" +
                        "<div style='border:2px solid #2196F3;padding:15px;margin:10px;'>" +
                        "<div style='font-size:16px;font-weight:bold;'>" + foundEvent.title + "</div>" +
                        "<div>Date: " + foundEvent.date + " | Time: " + foundEvent.time + "</div>" +
                        "<div>Type: " + foundEvent.type + "</div>" +
                        "<div style='margin-top:10px;color:#555;'>" + foundEvent.description + "</div>" +
                        "</div></body></html>");
            } else {
                JOptionPane.showMessageDialog(this, "Event '" + searchTitle + "' not found!", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // ========== LISTS: Practical Application ==========
        // Syllabus Topic: Applications of lists, Data Structures integration
        // This Daily Planner demonstrates real-world use of:
        // CO2: Singly Linked List ADT
        // CO1: Sorting and Searching algorithms
        // CO3: Stack (Undo) and Queue (Event Processing)
        // CO4: Hash-based structures (HashMap, ArrayList)
        SwingUtilities.invokeLater(() -> {
            DSAmain frame = new DSAmain();
            frame.setVisible(true);
        });
    }
}