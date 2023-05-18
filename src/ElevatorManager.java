import java.util.List;

public class ElevatorManager {
    public Elevator[] elevators;

    public ElevatorManager(Elevator elevator1, Elevator elevator2, Elevator elevator3) {
        elevators =new Elevator[]{elevator1,elevator2,elevator3};
    }

    public void moveAll(){
        for (Elevator elevator : elevators) {
            elevator.move();
        }
    }

    public void addRequest(int floor){
        int n=findBestElevator(floor);
        elevators[n].editStop(true, false, floor);
    }

    public void addRequest(int elevatorNumber,int floor){
        elevators[elevatorNumber].editStop(true, false, floor);
    }

    public int findBestElevator(int floor){
        int n = 0;
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < elevators.length; i++) {
            Elevator elevator = elevators[i];
            int cost = 0;
            if ((elevator.getCurrentFloor() < floor && elevator.getDirection() == Direction.UP)
                    || (elevator.getCurrentFloor() > floor && elevator.getDirection() == Direction.DOWN)
                    || elevator.getDirection() == Direction.IDLE) {
                cost = Math.abs(elevator.getCurrentFloor() - floor);
            } else {
                cost += 100;
                List<Integer> list;
                if (elevator.getDirection() == Direction.UP)
                    list = elevator.getStopsUp();
                else
                    list = elevator.getStopsDown();
                cost += Math.abs(list.get(list.size() - 1) - floor);
            }
            if (cost < min) {
                min = cost;
                n = i;
            }
        }
        return n;
    }

    public int getCurrentFloor(int elevator){
        return elevators[elevator].getCurrentFloor();
    }

    public Direction getDirection(int elevator){
        return elevators[elevator].getDirection();
    }

    public boolean isStop(int elevator){
        return elevators[elevator].isStop();
    }


}
