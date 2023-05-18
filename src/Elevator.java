import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elevator {

    private int currentFloor;
    private final int maxFloor;
    private List<Integer> stopsUp;
    private List<Integer> stopsDown;
    private Direction direction;
    private List<Integer> sequence;
    private boolean isStop;

    public Elevator(int maxFloor, List<Integer> stopsUp, List<Integer> stopsDown) {
        this.maxFloor = maxFloor;
        this.stopsUp = stopsUp;
        this.stopsDown = stopsDown;
        this.direction = Direction.IDLE;
        this.currentFloor = 0;
        this.sequence = new ArrayList<>();
    }

    public Elevator(int maxFloor) {
        this.maxFloor = maxFloor;
        this.stopsUp = new ArrayList<>();
        this.stopsDown = new ArrayList<>();
        this.direction = Direction.IDLE;
        this.currentFloor = 0;
        this.sequence = new ArrayList<>();
    }

    public void move() {
        if (direction == Direction.UP) {
            if (!stopsUp.isEmpty()) {
                if (stopsUp.get(0) == currentFloor) {
                    sequence.add(currentFloor);
                    editStop(false, true, -1);
                }else {
                    if (currentFloor != maxFloor-1) {
                        set_stop(false);
                        currentFloor++;
                    }
                }
            }
            if (stopsUp.isEmpty())
               direction= Direction.IDLE;

        }else if (direction == Direction.DOWN) {
            if (!stopsDown.isEmpty()) {
                if (stopsDown.get(0) == currentFloor) {
                    sequence.add(currentFloor);
                    editStop(false, false, -1);
                }else {
                    if (currentFloor != 0) {
                        currentFloor--;
                        set_stop(false);
                    }
                }
            }
            if (stopsDown.isEmpty())
                direction= Direction.IDLE;

        }else {
            if (!stopsUp.isEmpty()) {
                direction = Direction.UP;
            }else if (!stopsDown.isEmpty()) {
                direction = Direction.DOWN;
            }
        }

        // set new diection
        if (direction == Direction.UP) {
            if (stopsUp.isEmpty()) {
                direction = Direction.DOWN;
            }
        }else if (direction == Direction.DOWN) {
            if (stopsDown.isEmpty()) {
                direction = Direction.UP;
            }
        }
    }

    public synchronized void editStop(boolean add, boolean up, int floor) {
        if (add) {
            if (floor >= currentFloor) {
                if (!stopsUp.contains(floor)) {
                    stopsUp.add(floor);
                    Collections.sort(stopsUp);
                }
            }else {
                if (!stopsDown.contains(floor)) {
                    stopsDown.add(floor);
                    Collections.sort(stopsDown, Collections.reverseOrder());
                }
            }
        }else {
            if (up) {
                stopsUp.remove(0);
            }else {
                stopsDown.remove(0);
            }
            set_stop(true);
        }
    }

    public void printSequence() {
        System.out.println("------- Elevator Sequnce -------");
        for (Integer i: sequence) {
            System.out.println(i);
        }
        System.out.println("---------------------------------");
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public List<Integer> getStopsUp() {
        return stopsUp;
    }

    public void setStopsUp(List<Integer> stopsUp) {
        this.stopsUp = stopsUp;
    }

    public List<Integer> getStopsDown() {
        return stopsDown;
    }

    public void setStopsDown(List<Integer> stopsDown) {
        this.stopsDown = stopsDown;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }

    public boolean isStop() {
        return isStop;
    }

    public void set_stop(boolean is_stop) {
        this.isStop = is_stop;
    }
}
