package seedu.address.model.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

//@@author archthegit

/**
 * Predicate class checks if a given event falls within the month
 */
public class EventPredicate implements Predicate<ReadOnlyEvent> {

    /**
     * Default constructor for EventPredicate Class
     */
    public EventPredicate() {
    }

    /**
     * Checks if event falls in the month
     * @param event
     * @return
     * @throws ParseException
     */
    public boolean checkIfEventFallsInMonth(ReadOnlyEvent event) throws ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(event.getDate().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return ((cal.get(Calendar.MONTH)) == Calendar.getInstance().get(Calendar.MONTH));
    }


    @Override
    public boolean test(ReadOnlyEvent event) {
        boolean val = false;
        try {
            val = checkIfEventFallsInMonth(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventPredicate); // instanceof handles nulls
    }
}


