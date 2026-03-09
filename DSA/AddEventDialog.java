import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class AddEventDialog {
    
    public static void openAddEventDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add New Event", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
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
                
                EventList.addEvent(titleField.getText(), typeCombo.getSelectedItem().toString(), 
                        timeField.getText(), descArea.getText(), dateStr);
                
                JOptionPane.showMessageDialog(dialog, "✅ Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
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
}
