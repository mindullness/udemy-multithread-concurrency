//package locks.semaphore;
//
//import java.util.Arrays;
//import java.util.Random;
//
//public class Worker extends Thread {
//    private long id;
//    private Random random;
//    private boolean isWorking = false;
//    public Worker(Random random) {
//        this.random = random;
//    }
//
//    public Bottle produce() {
//        int min = 10;
//        int max = 50;
//        int choice = random.nextInt(BeerType.values().length);
//        BeerType t = BeerType.values()[choice];
//
//        Bottle bottle = new Bottle();
//        bottle.setName(t.getType());
//        bottle.setIpa((random.nextInt(8)) + random.nextFloat());
//        bottle.setPrice((Math.floor(Math.random() * (max - min + 1) + min)) + random.nextDouble());
//        bottle.setSize(Arrays.asList(9, 12, 14).get(random.nextInt(3)));
//        return bottle;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public boolean isWorking() {
//        return isWorking;
//    }
//
//    public void setWorking(boolean working) {
//        isWorking = working;
//    }
//}
