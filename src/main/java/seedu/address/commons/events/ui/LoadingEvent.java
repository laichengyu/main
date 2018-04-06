//@@author laichengyu

package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event to indicate that the app is loading data
 */
public class LoadingEvent extends BaseEvent {

    public final boolean isLoading;

    public LoadingEvent(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
