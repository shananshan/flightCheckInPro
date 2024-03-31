
public class Controller {
//	 private CheckInDesk checkInDesk;
	 private View checkInView;
	 private CheckInDesk checkInDesk1;
	 private CheckInDesk checkInDesk2;
     private CheckInDesk checkInDesk3;
	 
//     public Controller(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3, View checkInView) {
//         this.checkInDesk1 = checkInDesk1;
//         this.checkInDesk2 = checkInDesk2;
//         this.checkInDesk3 = checkInDesk3;
//         this.checkInView = checkInView;
//         
//         // 可以在这里初始化CheckInDesk实例或者监听事件，例如启动值机流程等
//         // 示例：启动每个CheckInDesk的线程
////         new Thread(checkInDesk1).start();
////         new Thread(checkInDesk2).start();
////         new Thread(checkInDesk3).start();
//     }

	public Controller(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3,View checkInView) {
		// TODO Auto-generated constructor stub
		 this.checkInDesk1 = checkInDesk1;
         this.checkInDesk2 = checkInDesk2;
         this.checkInDesk3 = checkInDesk3;
         this.checkInView = checkInView;
         
         
	}

	

	
}
