package race.condition.deadlock;

import java.util.Random;

public class TrainB implements Runnable{
    private Intersection intersection;
    private Random random = new Random();

    public TrainB(Intersection intersection) {
        this.intersection = intersection;
    }

    @Override
    public void run() {
        while (true){
            long sleepingTime = random.nextInt(5);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
            intersection.takeRoadB();
        }
    }
}
