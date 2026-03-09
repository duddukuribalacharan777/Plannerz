/* ========== CO3 & CO4: JavaScript Programming & DOM Interactivity ========== */
/* CO3: JavaScript Programming Essentials - Variables, functions, control flow */
/* CO4: JavaScript Interactivity & DOM Manipulation */
/* CO5: Advanced Web Development - localStorage API */

// ===== DSA EVENT LOADING FROM JAVA APPLICATION =====
/* CO3: Variable declaration for event data */
let dsaEvents = [];  // Events from Java DSA application

/* CO3 & CO4: Async Function - Load DSA Events from JSON File */
async function loadDSAEvents() {
    try {
        // Try modern fetch first
        try {
            const response = await fetch('./events.json', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            
            if (response.ok) {
                const eventData = await response.json();
                dsaEvents = Array.isArray(eventData) ? eventData : [];
                console.log('✅ DSA Events loaded (fetch):', dsaEvents);
                updateDSANotifications();
                return;
            }
        } catch (fetchError) {
            console.log('Fetch failed, trying XMLHttpRequest...');
        }
        
        // Fallback to XMLHttpRequest for file:// URLs
        return new Promise((resolve) => {
            const xhr = new XMLHttpRequest();
            xhr.open('GET', './events.json', true);
            xhr.onload = function() {
                if (xhr.status === 200 || xhr.status === 0) {
                    try {
                        const eventData = JSON.parse(xhr.responseText);
                        dsaEvents = Array.isArray(eventData) ? eventData : [];
                        console.log('✅ DSA Events loaded (XHR):', dsaEvents);
                        updateDSANotifications();
                    } catch (e) {
                        console.log('Error parsing JSON:', e);
                    }
                }
                resolve();
            };
            xhr.onerror = function() {
                console.log('XHR failed - file not yet created');
                resolve();
            };
            xhr.send();
        });
    } catch (error) {
        console.log('ℹ️ Could not load DSA events:', error.message);
    }
}

/* CO4: Update Notifications Display with DSA Events */
function updateDSANotifications() {
    const notifySection = document.getElementById('eventsNotification');  // CO4: DOM Selection
    const eventCountEl = document.getElementById('eventCount');  // CO4: DOM Selection
    
    if (!notifySection) return;  // CO3: Conditional Logic
    
    if (dsaEvents.length === 0) {  // CO3: Conditional Logic
        notifySection.innerHTML = '<div class="no-events">📌 No events scheduled. Create events in DSA application!</div>';
        if (eventCountEl) eventCountEl.textContent = '📅 0 events';
        return;
    }
    
    /* CO3: Array Sort - Sort events by date and time */
    const sortedEvents = [...dsaEvents].sort((a, b) => {  // CO3: Spread operator & sorting
        const dateCompare = a.date.localeCompare(b.date);
        if (dateCompare !== 0) return dateCompare;
        return a.time.localeCompare(b.time);
    });
    
    /* CO3 & CO4: Map & Template Literals - Build event cards */
    const eventCards = sortedEvents.map(event => `
        <div class="event-card ${event.type.toLowerCase()}">
            <div class="event-date">📅 ${formatDate(event.date)}</div>
            <div class="event-time">⏰ ${event.time}</div>
            <div class="event-title">${event.title}</div>
            <div class="event-type">${event.type.toUpperCase()}</div>
            ${event.description ? `<div class="event-description">📝 ${event.description}</div>` : ''}
        </div>
    `).join('');  // CO4: Join and create HTML
    
    notifySection.innerHTML = eventCards;  // CO4: Update DOM
    if (eventCountEl) eventCountEl.textContent = `📅 ${dsaEvents.length} event${dsaEvents.length !== 1 ? 's' : ''}`;
}

/* CO3 & CO4: Utility Function - Format Date */
function formatDate(dateStr) {
    try {
        const date = new Date(dateStr + 'T00:00:00');  // CO3: Date parsing
        const options = { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric' };  // CO3: Intl object options
        return date.toLocaleDateString('en-US', options);  // CO3: Date formatting
    } catch (e) {
        return dateStr;
    }
}

// ===== CALENDAR FUNCTIONALITY =====
/* CO5: localStorage API - Advanced Web Development */
let currentDate = new Date();  // CO3: Variable declaration
let events = JSON.parse(localStorage.getItem('plannerEvents')) || [];  // CO5: Data persistence

/* CO4: DOM Event Initialization */
// Initialize calendar
function initCalendar() {
    renderCalendar();
    setupCalendarControls();
}

/* CO3 & CO4: Function Definition & DOM Manipulation */
function renderCalendar() {
    const calendarGrid = document.getElementById('calendarGrid');  // CO4: DOM Selection
    const currentMonthEl = document.getElementById('currentMonth');  // CO4: DOM Selection
    
    // Return early if calendar elements don't exist on this page
    if (!calendarGrid || !currentMonthEl) return;  // CO3: Conditional Logic
    
    // Clear existing calendar grid
    calendarGrid.innerHTML = '';  // CO4: Clear DOM Content
    
    const year = currentDate.getFullYear();  // CO3: Variable & Method
    const month = currentDate.getMonth();  // CO3: Variable & Method
    
    // Update month display
    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];  // CO3: Array
    currentMonthEl.textContent = `${monthNames[month]} ${year}`;  // CO4: DOM Update
    
    // Get first day of month and number of days
    const firstDay = new Date(year, month, 1).getDay();  // CO3: Date Object
    const daysInMonth = new Date(year, month + 1, 0).getDate();  // CO3: Date Object
    
    // CO3: Loop - Add empty cells before first day
    for (let i = 0; i < firstDay; i++) {
        const emptyCell = document.createElement('div');  // CO4: Create DOM Element
        emptyCell.className = 'calendar-day empty';  // CO4: Set Element Class
        calendarGrid.appendChild(emptyCell);  // CO4: Append DOM Element
    }
    
    /* CO3: Loop - Day Cell Creation */
    // Add day cells
    for (let day = 1; day <= daysInMonth; day++) {
        const dayCell = document.createElement('div');  // CO4: DOM Element Creation
        dayCell.className = 'calendar-day';  // CO4: Set Attributes
        dayCell.innerHTML = `<span class="day-number">${day}</span>`;  // CO4: Update HTML Content
        
        // Check if today
        const today = new Date();  // CO3: Date Object
        if (year === today.getFullYear() && month === today.getMonth() && day === today.getDate()) {  // CO3: Conditional Logic
            dayCell.classList.add('today');  // CO4: Modify CSS Classes
        }
        
        // Add event indicators
        const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;  // CO3: String Operations
        const dayEvents = events.filter(e => e.date === dateStr);  // CO3: Array Filter Method
        if (dayEvents.length > 0) {
            const indicator = document.createElement('div');  // CO4: Create DOM Element
            indicator.className = 'event-indicator';
            indicator.textContent = dayEvents.length;  // CO4: Update Text Content
            dayCell.appendChild(indicator);  // CO4: Append Element
        }
        
        /* CO4: Event Listener - Click Handler */
        // Click handler
        dayCell.addEventListener('click', () => selectDate(year, month, day));  // CO4: Event Listener
        calendarGrid.appendChild(dayCell);  // CO4: Append Element
    }
}

/* CO4: Event Listener Setup */
function setupCalendarControls() {
    const prevBtn = document.getElementById('prevMonth');  // CO4: DOM Selection
    const nextBtn = document.getElementById('nextMonth');  // CO4: DOM Selection
    
    // Return early if buttons don't exist on this page
    if (!prevBtn || !nextBtn) return;  // CO3: Conditional Logic
    
    /* CO4: Event Listeners for Navigation */
    prevBtn.addEventListener('click', () => {  // CO4: Click Event
        currentDate.setMonth(currentDate.getMonth() - 1);  // CO3: Date Manipulation
        renderCalendar();  // CO4: DOM Re-render
    });
    
    nextBtn.addEventListener('click', () => {  // CO4: Click Event
        currentDate.setMonth(currentDate.getMonth() + 1);  // CO3: Date Manipulation
        renderCalendar();  // CO4: DOM Re-render
    });
}

/* CO4: Date Selection & DOM Update */
function selectDate(year, month, day) {
    const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;  // CO3: String Formatting
    const date = new Date(year, month, day);  // CO3: Date Object
    
    const selectedDateEl = document.getElementById('selectedDate');  // CO4: DOM Selection
    const eventsList = document.getElementById('eventsList');  // CO4: DOM Selection
    
    // Return early if elements don't exist on this page
    if (!selectedDateEl || !eventsList) return;  // CO3: Conditional Logic
    
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };  // CO3: Object
    selectedDateEl.textContent = date.toLocaleDateString('en-US', options);  // CO4: DOM Update
    
    // Show events for this date
    const dayEvents = events.filter(e => e.date === dateStr);  // CO3: Array Filter
    
    if (dayEvents.length === 0) {  // CO3: Conditional Logic
        eventsList.innerHTML = '<p>No events scheduled</p>';  // CO4: Update HTML
    } else {
        /* CO3: Array Map & Template Literals */
        eventsList.innerHTML = dayEvents.map(event => `
            <div class="event-item ${event.completed ? 'completed' : ''}" data-event-id="${event.id}" onclick="selectEvent(${event.id})">
                <div class="event-time">${event.time}</div>
                <div class="event-content">
                    <div class="event-title">${event.title} ${event.completed ? '✅' : ''}</div>
                    <div class="event-priority" style="color: ${getPriorityColor(event.priority)};">
                        Priority: ${event.priority}
                    </div>
                    ${event.description ? `<div class="event-desc">${event.description}</div>` : ''}
                    ${event.completed ? `<div class="event-completed">Completed on ${formatDate(event.completedDate)}</div>` : ''}
                </div>
                ${!event.completed ? `<div class="event-actions" id="actions-${event.id}" style="display: none;">
                    <button class="action-btn complete-btn" onclick="completeEvent(${event.id}); event.stopPropagation();">✓ Complete</button>
                    <button class="action-btn delete-btn" onclick="deleteEvent(${event.id}); event.stopPropagation();">🗑️ Delete</button>
                </div>` : `<div class="event-actions" id="actions-${event.id}" style="display: none;">
                    <button class="action-btn delete-btn" onclick="deleteEvent(${event.id}); event.stopPropagation();">🗑️ Delete</button>
                </div>`}
            </div>
        `).join('');  // CO4: Update DOM
    }
}

/* CO3: Utility Function - Return Values */
function getPriorityColor(priority) {
    const colors = { 'High': '#d32f2f', 'Medium': '#f57c00', 'Low': '#388e3c' };  // CO3: Object Lookup
    return colors[priority] || '#666';  // CO3: Conditional Return
}

/* CO4: Event Selection - Show/Hide Action Buttons */
function selectEvent(eventId) {
    const selectedActions = document.getElementById(`actions-${eventId}`);
    const selectedEvent = document.querySelector(`[data-event-id="${eventId}"]`);
    
    // Check if this event is already selected
    const isCurrentlySelected = selectedEvent && selectedEvent.classList.contains('selected');
    
    // Hide all action buttons first
    const allActions = document.querySelectorAll('.event-actions');
    allActions.forEach(actions => actions.style.display = 'none');
    
    // Remove selected class from all events
    const allEvents = document.querySelectorAll('.event-item');
    allEvents.forEach(event => event.classList.remove('selected'));
    
    // If it wasn't selected, select it now
    if (!isCurrentlySelected && selectedActions && selectedEvent) {
        selectedActions.style.display = 'flex';
        selectedEvent.classList.add('selected');
    }
    // If it was already selected, it stays deselected (all actions hidden)
}

/* CO4: Delete Event Function */
function deleteEvent(eventId) {
    // Find the event
    const eventIndex = events.findIndex(e => e.id === eventId);
    if (eventIndex === -1) return;
    
    const event = events[eventIndex];
    
    // Show confirmation popup
    if (!confirm(`Are you sure you want to delete "${event.title}"?`)) {
        return;
    }
    
    // Remove from events array
    events.splice(eventIndex, 1);
    
    // Save to localStorage
    localStorage.setItem('plannerEvents', JSON.stringify(events));
    
    // Show notification
    alert(`🗑️ Event "${event.title}" has been deleted!`);
    
    // Refresh calendar and notifications
    renderCalendar();
    initNotifications();
}

/* CO4: Complete Event Function */
function completeEvent(eventId) {
    // Find the event
    const eventIndex = events.findIndex(e => e.id === eventId);
    if (eventIndex === -1) return;
    
    const event = events[eventIndex];
    
    // Mark as completed
    event.completed = true;
    event.completedDate = new Date().toISOString().split('T')[0];
    
    // Save to localStorage
    localStorage.setItem('plannerEvents', JSON.stringify(events));
    
    // Refresh calendar and notifications to show green color
    renderCalendar();
    initNotifications();
    
    // Show popup notification after the UI updates
    setTimeout(() => {
        alert(`✅ Event "${event.title}" has been completed!`);
    }, 100);
}

/* CO4: Initialization & DOM Manipulation */
// Initialize calendar when page loads
function initNotifications() {
    const notifyContainer = document.querySelector('.notify-container');  // CO4: DOM Selection
    if (!notifyContainer) return; // no notifications section on this page  // CO3: Conditional

    const todayStr = new Date().toISOString().split('T')[0];  // CO3: String/Date Methods
    /* CO3: Array Filter & Sort - Advanced JavaScript */
    // include events today and future dates, exclude completed events
    const upcomingEvents = events
        .filter(e => e.date >= todayStr && !e.completed)  // CO3: Filter Method
        .sort((a,b) => {  // CO3: Sort Method with Comparison
            if (a.date === b.date) return a.time.localeCompare(b.time);
            return a.date.localeCompare(b.date);
        });

    if (upcomingEvents.length === 0) {  // CO3: Conditional Logic
        notifyContainer.innerHTML = '<h2>Notifications</h2><p>No new notifications</p>';  // CO4: DOM Update
    } else {
        let html = '<h2>Notifications</h2>';  // CO3: String Concatenation
        /* CO3: Array Map with Template Literals */
        html += upcomingEvents.map(e => `
            <div class="notification-item">
                <div class="notification-time">${e.date} ${e.time}</div>
                <div class="notification-title">${e.title}</div>
                <div class="notification-priority" style="color:${getPriorityColor(e.priority)};">${e.priority}</div>
            </div>
        `).join('');
        notifyContainer.innerHTML = html;  // CO4: DOM Update
    }
}

/* CO4: Document Ready Event */
if (document.readyState === 'loading') {
    /* CO4: DOM Content Loaded Event Listener */
    document.addEventListener('DOMContentLoaded', () => {
        initCalendar();
        initNotifications();
        setupButtonHandlers();
        setupNotificationPageRefresh();
        renderWeeklyBarChart();
        
        // Load DSA events on page initialization
        loadDSAEvents();  // Load events from Java application
    });
} else {
    initCalendar();
    initNotifications();
    setupButtonHandlers();
    setupNotificationPageRefresh();
    renderWeeklyBarChart();
    
    // Load DSA events on page initialization
    loadDSAEvents();  // Load events from Java application
} 

// ========== CO4 & CO5: Auto-Refresh Notifications with setInterval =====
// ===== AUTO-REFRESH NOTIFICATIONS PAGE =====
/* CO4: setInterval for periodic DOM updates (Advanced Interactivity) */
function setupNotificationPageRefresh() {
    // Check if we're on the notifications page
    const notifySection = document.getElementById('notify');  // CO4: DOM Selection
    if (notifySection) {
        // Refresh notifications every 2 seconds
        setInterval(() => {  // CO5: setInterval - Advanced Web Development
            initNotifications();  // CO4: DOM Update
        }, 2000);
        
        // Reload DSA events every 3 seconds for real-time updates
        setInterval(() => {  // CO5: setInterval for DSA event refresh
            loadDSAEvents();  // Reload events from Java application
        }, 3000);
    }
}

// ========== CO3 & CO4: Advanced Canvas & Chart Rendering ==========
// ===== WEEKLY BAR CHART =====
/* CO5: Canvas API & Chart.js Library Integration */
function renderWeeklyBarChart() {
    const canvas = document.getElementById('weeklyBarChart');  // CO4: DOM Selection
    if (!canvas) return;

    // ensure canvas has pixel dimensions matching styled size
    canvas.width = canvas.clientWidth || 700;  // CO3: Property Access & Conditional
    canvas.height = canvas.clientHeight || 300;  // CO3: Property Access & Conditional

    // sample values
    const labels = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];  // CO3: Array
    const values = [2,4,3,5,1,0,6];  // CO3: Array

    // CO5: Conditional Library Usage - Chart.js
    // attempt Chart.js if available
    if (window.Chart) {  // CO3: Conditional - Check for Library
        const data = { labels, datasets:[{label:'Tasks Completed', backgroundColor:'#ff9800', data: values}] };  // CO3: Object
        if (window.weeklyChart) window.weeklyChart.destroy();  // CO3: Conditional Cleanup
        window.weeklyChart = new Chart(canvas,{type:'bar',data:data,options:{responsive:true,scales:{y:{beginAtZero:true}}}});  // CO5: Library Usage
        return;
    }

    // CO3 & CO4: Fallback Canvas Drawing
    // fallback: draw simple bars
    const ctx = canvas.getContext('2d');  // CO4: Canvas Context API
    const w = canvas.width;  // CO3: Variable
    const h = canvas.height;  // CO3: Variable
    ctx.clearRect(0,0,w,h);  // CO4: Canvas Drawing
    const max = Math.max(...values,1);  // CO3: Spread Operator & Math Function
    const barWidth = w / (values.length*2);  // CO3: Math Calculation
    /* CO3: Loop - Draw bars */
    values.forEach((v,i)=>{  // CO3: forEach Loop
        const x = barWidth + i*barWidth*2;  // CO3: Math Calculation
        const barH = (v/max) * (h - 20);  // CO3: Math Calculation
        ctx.fillStyle = '#ff9800';  // CO4: Canvas Styling
        ctx.fillRect(x, h - barH - 10, barWidth, barH);  // CO4: Canvas Drawing
        ctx.fillStyle='#000';
        ctx.textAlign='center';
        ctx.fillText(labels[i], x + barWidth/2, h - 2);  // CO4: Canvas Text
    });
}

// ========== CO4: Toast Notifications - Advanced Interactivity ==========
// ===== NOTIFICATION TOAST =====
/* CO4: Dynamic DOM Creation for Toast Messages */
function showNotificationToast(title, message, duration = 3000) {  // CO3: Function Parameters with Default Values
    // Create toast container if it doesn't exist
    let toastContainer = document.getElementById('toastContainer');  // CO4: DOM Selection
    if (!toastContainer) {  // CO3: Conditional Logic
        toastContainer = document.createElement('div');  // CO4: Create DOM Element
        toastContainer.id = 'toastContainer';  // CO4: Set Attribute
        toastContainer.style.cssText = `  // CO4: Inline CSS via JavaScript
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 10000;
            max-width: 350px;
        `;
        document.body.appendChild(toastContainer);  // CO4: Append DOM Element
    }
    
    // CO4: Create and customize toast element
    // Create toast element
    const toast = document.createElement('div');  // CO4: Create DOM Element
    toast.style.cssText = `  // CO4: Style Element via JavaScript
        background: linear-gradient(90deg, #ff6a32 60%, #ffb88c 100%);
        color: white;
        padding: 16px 20px;
        border-radius: 8px;
        margin-bottom: 10px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        animation: slideIn 0.3s ease;
    `;
    
    /* CO3 & CO4: Template Literals for Dynamic Content */
    toast.innerHTML = `
        <div style="font-weight: 600; margin-bottom: 4px;">${title}</div>
        <div style="font-size: 0.9em; opacity: 0.95;">${message}</div>
    `;
    toastContainer.appendChild(toast);  // CO4: Append Child Element
    
    /* CO3: setTimeout - Execute code after delay */
    // Remove toast after duration
    setTimeout(() => {  // CO5: Asynchronous Execution
        toast.style.animation = 'slideOut 0.3s ease';  // CO4: Animate Removal
        setTimeout(() => toast.remove(), 300);  // CO4: Remove Element
    }, duration);
}

/* CO4: Inject CSS Animations via JavaScript */
// Add styles for toast animations
if (!document.getElementById('toastStyles')) {  // CO3: Conditional DOM Check
    const style = document.createElement('style');  // CO4: Create Style Element
    style.id = 'toastStyles';
    style.textContent = `
        @keyframes slideIn {
            from {
                transform: translateX(400px);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        @keyframes slideOut {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(400px);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);  // CO4: Inject CSS
}

// ========== CO3, CO4, CO5: Event Form Handling & Data Persistence ==========
// ===== EVENT FORM FUNCTIONALITY =====
/* CO4: Form Submit Event Listener with preventDefault */
const eventForm = document.querySelector('.event-Form');  // CO4: DOM Selection
if (eventForm) {  // CO3: Conditional
    /* CO4: Event Listener for Form Submission */
    eventForm.addEventListener('submit', (e) => {
        e.preventDefault();  // CO4: Prevent Default Form Behavior
        
        /* CO4: Extract Form Values - DOM Selection */
        const title = document.getElementById('eventTitle').value;  // CO4: GetInput Value
        const date = document.getElementById('eventDate').value;
        const time = document.getElementById('eventTime').value;
        const priority = document.getElementById('eventPriority').value;
        const description = document.getElementById('eventDesc').value;
        
        /* CO3: Create Object Literal */
        // Create event object
        const newEvent = {  // CO3: Object Definition
            id: Date.now(),  // CO3: Unique ID Generation
            title,
            date,
            time,
            priority,
            description
        };
        
        /* CO3: Array Methods - Push */
        // Add to events array
        events.push(newEvent);  // CO3: Array Push Method
        
        /* CO5: localStorage - Data Persistence */
        // Save to localStorage
        localStorage.setItem('plannerEvents', JSON.stringify(events));  // CO5: Serialize & Store Data
        
        /* CO4: DOM Manipulation - Clear Form */
        // Clear form
        eventForm.reset();  // CO4: Reset Form Elements
        
        /* CO4: Call Toast Notification Function */
        // Show success notification toast
        showNotificationToast(
            '✓ Event Added!',  // CO3: Template Literals
            `"${title}" scheduled for ${date} at ${time}`
        );
        
        /* CO4: Refresh UI Components */
        // Refresh calendar if it's visible
        renderCalendar();  // CO4: Re-render Calendar
        // update notifications area if present
        initNotifications();  // CO4: Update Notifications
    });
}