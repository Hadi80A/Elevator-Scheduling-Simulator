import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller {
    private final double SPEED =3;
    @FXML private VBox vBox;
    @FXML private Rectangle elevatorCabin1;
    @FXML private Rectangle elevatorCabin2;
    @FXML private Rectangle elevatorCabin3;
    private Rectangle[] elevators;
    private ElevatorManager elevatorManager;
    private StringProperty[] direction;
    private boolean wait;

    public void setElevatorManager(ElevatorManager elevatorManager) {
        this.elevatorManager = elevatorManager;
    }

    public void initialize(){
        elevators=new Rectangle[]{elevatorCabin1,elevatorCabin2,elevatorCabin3};
        direction=new StringProperty[3];
        for (int i = 0; i <3 ; i++) {
            direction[i]=new SimpleStringProperty(" ");
        }
        for (int i = 0; i < 15; i++) {
           Button button= (Button) ((AnchorPane)vBox.getChildren().get(i)).getChildren().get(6);
            int finalI = 14-i;
            button.setOnAction(event -> {
                addRequest(finalI);
                button.setEffect(new SepiaTone());
            });
        }
        DoubleProperty elevatorY1= elevatorCabin1.layoutYProperty();
        vBox.lookupAll("#monitor1").iterator().forEachRemaining(node ->
                ((Label)node).textProperty().bind(
                        Bindings.createDoubleBinding(() ->
                                Math.abs(14-(elevatorY1.get()/43)),elevatorY1
                        ).asString("%3.0f").concat(direction[0])

                )
        );
        DoubleProperty elevatorY2= elevatorCabin2.layoutYProperty();
        vBox.lookupAll("#monitor2").iterator().forEachRemaining(node ->
                ((Label)node).textProperty().bind(
                        Bindings.createDoubleBinding(() ->
                                Math.abs(14-(elevatorY2.get()/43)),elevatorY2
                        ).asString("%3.0f").concat(direction[1])

                )
        );
        DoubleProperty elevatorY3= elevatorCabin3.layoutYProperty();
        vBox.lookupAll("#monitor3").iterator().forEachRemaining(node ->
                ((Label)node).textProperty().bind(
                        Bindings.createDoubleBinding(() ->
                                Math.abs(14-(elevatorY3.get()/43)),elevatorY3
                        ).asString("%3.0f").concat(direction[2])

                )
        );
        Timeline timeline=new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/SPEED), e -> {
            if(!wait) {
                elevatorManager.moveAll();
                for (int i = 0; i < 3; i++) {
                    boolean stop = elevatorManager.isStop(i);
                    moveElevator(i, elevatorManager.getCurrentFloor(i));
                    if (stop) {
                        if(elevatorManager.getDirection(i)!=Direction.IDLE) {
                            wait = true;
                        }
                        openAndClose(i, elevatorManager.getCurrentFloor(i));
                        direction[i].set(" ");
                    } else {
                        if (elevatorManager.getDirection(i) == Direction.UP)
                            direction[i].set("🔼");
                        else if (elevatorManager.getDirection(i) == Direction.DOWN)
                            direction[i].set("🔽");
                    }
                }
            }
        }));
        timeline.play();

    }

    public void addRequest(int n){
        HBox hBox= (HBox) ((AnchorPane)vBox.getChildren().get(14-n)).getChildren().get(5);
        hBox.getChildren().add(new Person());
        elevatorManager.addRequest(n);
    }


    public void moveElevator(int elevator,int floor){
        Rectangle elevatorCabin;
        if(elevator==0)
            elevatorCabin=elevatorCabin1;
        else if(elevator==1)
            elevatorCabin=elevatorCabin2;
        else
            elevatorCabin=elevatorCabin3;
        Timeline timeline=new Timeline();
        double endValue=vBox.getChildren().get(14-floor).getLayoutY();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis((2000-50)/SPEED),
                        new KeyValue(elevatorCabin.layoutYProperty(),endValue)));
        timeline.play();
    }

    public void openAndClose(int elevator,int floor){
        ((Button)vBox.lookupAll("#addButton").toArray()[14-floor]).setEffect(null);
        Timeline timeline=new Timeline();
        HBox hBox= (HBox) ((AnchorPane)vBox.getChildren().get(14-floor)).getChildren().get(5);
        if (!hBox.getChildren().isEmpty()) {
            walk(elevator, floor);
            openDoor(elevator + 1, floor);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds((1 + 4) / SPEED), e -> {
                hBox.getChildren().clear();
                hBox.setTranslateX(0);
                wait=false;
            }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds((2 + 4) / SPEED), e -> closeDoor(elevator + 1, floor)));
            timeline.play();
        }else
            wait=false;
    }


    public void openDoor(int elevator,int n){
        Group doorGroup= (Group) vBox.lookupAll("#door"+elevator).toArray()[14-n];
        Rectangle lDoor= (Rectangle) doorGroup.getChildren().get(2);
        Rectangle rDoor= (Rectangle) ((VBox)doorGroup.getChildren().get(3)).getChildren().get(0);
        Timeline timeline=new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/SPEED),
                        new KeyValue(rDoor.widthProperty(),0)));
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/SPEED),
                        new KeyValue(lDoor.widthProperty(),0)));
        timeline.play();
    }

    public void closeDoor(int elevator,int n){
        Group doorGroup= (Group) vBox.lookupAll("#door"+elevator).toArray()[14-n];
        Rectangle lDoor= (Rectangle) doorGroup.getChildren().get(2);
        Rectangle rDoor= (Rectangle) ((VBox)doorGroup.getChildren().get(3)).getChildren().get(0);
        Timeline timeline=new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/SPEED),
                        new KeyValue(rDoor.widthProperty(),15)));
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/SPEED),
                        new KeyValue(lDoor.widthProperty(),15)));
        timeline.play();
    }

    public void walk(int elevator,int n){
        HBox hBox= (HBox) ((AnchorPane)vBox.getChildren().get(14-n)).getChildren().get(5);
        for (Node child : hBox.getChildren()) {
            Person person=(Person) child;
            Timeline timeline = new Timeline();
            timeline.setCycleCount(40);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5 / SPEED), e -> {
                person.nextImage();
            }));
            timeline.play();
        }
        Timeline t2=new Timeline();
        double endValue=elevators[elevator].getLayoutX()-hBox.getLayoutX()+10;
        t2.getKeyFrames().add(new KeyFrame(Duration.seconds(4/SPEED),
                new KeyValue(hBox.translateXProperty(),endValue)));
        t2.play();
    }
}

class Person extends ImageView{
    private Image[] images;
    private int current;

    public Person() {
        images =new Image[9];
        for (int i = 0; i < images.length; i++) {
            images[i]=new Image(getClass().getResourceAsStream(("/person/"+i+".png")));
        }
        setImage(images[0]);
    }

    public void nextImage(){
        current++;
        setImage(images[current]);
        if(current==8)
            current=1;
    }
}
