public interface Observer {
    /**
     * The update method is called to notify the Observer about a change in the observable subject's state.
     * Implementations of this method should define how the observer reacts to these notifications.
     */
    void update();
}


