# justinpoh-reused
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Toggles the list panel based on the input panel.
     */
    public void handleToggle(String selectedPanel) {
        if (selectedPanel.equals(EventsCommand.COMMAND_WORD)) {
            eventListPanelPlaceholder.toFront();
        } else if (selectedPanel.equals(ListCommand.COMMAND_WORD)) {
            personListPanelPlaceholder.toFront();
        }
    }
```
