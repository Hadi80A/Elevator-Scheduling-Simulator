import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ElevatorView extends Application {
    // size of graph
    int graphSize = 150;

    // variables for mouse interaction
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);
    private final Elevator elevator;
    private String name;

    public ElevatorView(String name , Elevator elevator) {
        this.elevator = elevator;
        this.name=name;
    }

    @Override
    public void start(Stage primaryStage) {

        // create axis walls
        Group grid = createGrid(graphSize);

        // initial cube rotation
        grid.getTransforms().addAll(rotateX, rotateY);

        // add objects to scene
        StackPane root = new StackPane();
        root.getChildren().add(grid);

        // scene
        Scene scene = new Scene(root, 240, 295, true, SceneAntialiasing.BALANCED);
        Camera camera=new PerspectiveCamera();
        camera.setNearClip(Double.MIN_VALUE);
        scene.setCamera(camera);

        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle() - (mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle() + (mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        makeZoomable(root);
        primaryStage.setTitle(name);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Axis wall
     */
    public static class Axis extends Pane {

        Rectangle wall;

        public Axis(double size) {

            // wall
            // first the wall, then the lines => overlapping of lines over walls
            // works
            wall = new Rectangle(size, size);
            getChildren().add(wall);
            wall.setMouseTransparent(true);
        }

        public void setFill(Paint paint) {
            wall.setFill(paint);
        }

    }

    public void makeZoomable(StackPane control) {

        final double MAX_SCALE = 20.0;
        final double MIN_SCALE = 0.1;

        control.addEventFilter(ScrollEvent.ANY, event -> {

            double delta = 1.2;

            double scale = control.getScaleX();

            if (event.getDeltaY() < 0) {
                scale /= delta;
            } else {
                scale *= delta;
            }

            scale = clamp(scale, MIN_SCALE, MAX_SCALE);

            control.setScaleX(scale);
            control.setScaleY(scale);

            event.consume();

        });

    }

    /**
     * Create axis walls
     *
     * @param size
     * @return
     */
    private Group createGrid(int size) {

        Group cube = new Group();

        // size of the cube
        Color color = Color.LIGHTGRAY;

        List<Axis> cubeFaces = new ArrayList<>();
        Axis r;

        Image img = new Image(getClass().getResourceAsStream("/wall.jpg"));
        
        // back face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setTranslateZ(0.5 * size);

        cubeFaces.add(r);

        // bottom face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(0);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);

        cubeFaces.add(r);

        // right face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(-1 * size);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);

        // cubeFaces.add( r);

        // left face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(0);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);

        float s=size / 10;


        Rectangle rectangle=new Rectangle(2*s,s);
        Image img2 = new Image(getClass().getResourceAsStream("testmonitor.png"));
        rectangle.setFill(new ImagePattern(img2));
        rectangle.setTranslateX(s*7);
        rectangle.setTranslateY(s*0);
        r.getChildren().add(rectangle);
        Group group=new Group();
        for (int i = 0; i <15 ; i++) {
            Button button=new Button(14-i+"");
            button.setFont(Font.font(10));
            button.setShape(new Circle(s/2));
            if(i!=14) {
                button.setPrefWidth(s);
                button.setMinSize(s, s);
                button.setMaxSize(s, s);
            }else {
                button.setMinSize(2*s, s);
                button.setMaxSize(2*s, s);
            }
            button.setPadding(new Insets(0,0,0,0));
            button.setTranslateZ(0);
            button.setTranslateX(s*(7+(i%2)));
            button.setTranslateY(s*(1+(i/2)));
            button.resize(s,s);
            int finalI = 14-i;
            button.setOnAction(event -> elevator.editStop(true, false, finalI));
            group.getChildren().add(button);
        }
       r.getChildren().add(group);

        cubeFaces.add(r);

        // top face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-1 * size);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);

        // cubeFaces.add( r);

        // front face
        r = new Axis(size);
        r.setFill(new ImagePattern(img));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setTranslateZ(-0.5 * size);

        // cubeFaces.add( r);

        cube.getChildren().addAll(cubeFaces);

        return cube;
    }

    public static double normalizeValue(double value, double min, double max, double newMin, double newMax) {

        return (value - min) * (newMax - newMin) / (max - min) + newMin;

    }

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }

}