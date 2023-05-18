import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene=new Scene(root);
        controller=loader.getController();
        Elevator elevator1=new Elevator(15);
        Elevator elevator2=new Elevator(15);
        Elevator elevator3=new Elevator(15);
        ElevatorManager elevatorManager=new ElevatorManager(elevator1,elevator2,elevator3);
        controller.setElevatorManager(elevatorManager);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        new ElevatorView("left",elevator1).start(new Stage());
        new ElevatorView("center",elevator2).start(new Stage());
        new ElevatorView("right",elevator3).start(new Stage());
    }

    public static Controller getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
