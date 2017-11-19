# SirGoose3432-reused
###### \java\seedu\address\ui\AnchorPaneNode.java
``` java

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e ->
                handleCalendarEvent(date));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
```
###### \java\seedu\address\ui\Calendar.java
``` java
/**
 * The UI component that is responsible for implemented Calendar.
 */
public class Calendar {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public Calendar(YearMonth yearMonth, ObservableList<ReadOnlyEvent> eventList) {
        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(600, 400);
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.setPrefSize(200, 200);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
                ap.getStyleClass().add("calendar-color");
            }
        }

        // Days of the week labels
        Text[] dayNames = new Text[]{new Text("Sunday"), new Text("Monday"),
                                     new Text("Tuesday"), new Text("Wednesday"), new Text("Thursday"),
                                     new Text("Friday"), new Text("Saturday")};

        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (Text txt : dayNames) {
            txt.getStyleClass().add("calendar-color");
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }

        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        calendarTitle.getStyleClass().add("calendar-color");
        Button previousMonth = new Button("<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setSpacing(5);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth, eventList);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * Also, used to populate the calendar when switching different months
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth, ObservableList<ReadOnlyEvent> events) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            txt.getStyleClass().add("calendar-color");
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.setStyle("-fx-background-color: transparent;");
            ap.getChildren().add(txt);
            calendarDate = calendarDate.plusDays(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            String newDate = formatter.format(ap.getDate());
            for (ReadOnlyEvent event : events) {
                String date = event.getDate().toString();
                if (newDate.equals(date)) {
                    ap.getChildren();
                    ap.setStyle("-fx-background-color: #ffebcd;");
                }
            }
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }
```
