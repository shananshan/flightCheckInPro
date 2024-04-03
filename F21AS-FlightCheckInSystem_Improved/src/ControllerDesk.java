public class ControllerDesk {
    // References to the check-in desks and security queues used in the system.
    private View checkInView;
    private CheckInDesk checkInDesk1;
    private CheckInDesk checkInDesk2;
    private CheckInDesk checkInDesk3;
    private Securityqueue BSq; // Business class security queue
    private Securityqueue ESq; // Economy class security queue

    /**
     * Constructs a ControllerDesk object that manages the interactions between the model components
     * (check-in desks and security queues) and the view component.
     * 
     * @param checkInDesk1 Reference to the first check-in desk.
     * @param checkInDesk2 Reference to the second check-in desk.
     * @param checkInDesk3 Reference to the third check-in desk.
     * @param BSq Reference to the business class security queue.
     * @param ESq Reference to the economy class security queue.
     * @param checkInView Reference to the view component of the MVC architecture.
     */
    public ControllerDesk(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3,
                          Securityqueue BSq, Securityqueue ESq, View checkInView) {
        this.checkInDesk1 = checkInDesk1;
        this.checkInDesk2 = checkInDesk2;
        this.checkInDesk3 = checkInDesk3;
        this.checkInView = checkInView;
        this.BSq = BSq;
        this.ESq = ESq;
    }
}
