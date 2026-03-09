import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DSAmain extends JFrame {

    private JTextPane eventDisplayArea;
    private JButton addEventBtn, viewEventsBtn;

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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        addEventBtn = new JButton("➕ Add Event");
        addEventBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addEventBtn.setBackground(new Color(76, 175, 80));
        addEventBtn.setForeground(Color.WHITE);
        addEventBtn.setFocusPainted(false);
        addEventBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addEventBtn.addActionListener(e -> AddEventDialog.openAddEventDialog(this));
        
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

    private void displayEventsInGUI() {
        eventDisplayArea.setText(EventList.generateEventHTML());
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