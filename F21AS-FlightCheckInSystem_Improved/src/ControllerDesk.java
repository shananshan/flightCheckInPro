
public class ControllerDesk {
//	 private CheckInDesk checkInDesk;
	 private View checkInView;
	 private CheckInDesk checkInDesk1;
	 private CheckInDesk checkInDesk2;
     private CheckInDesk checkInDesk3;
     private Secutiryqueue BSq;
     private Secutiryqueue ESq;


	public ControllerDesk(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3,
			Secutiryqueue BSq, Secutiryqueue ESq, View checkInView) {
		// TODO Auto-generated constructor stub
		 this.checkInDesk1 = checkInDesk1;
         this.checkInDesk2 = checkInDesk2;
         this.checkInDesk3 = checkInDesk3;
         this.checkInView = checkInView;
         this.BSq = BSq;
         this.ESq = ESq;
	}

}