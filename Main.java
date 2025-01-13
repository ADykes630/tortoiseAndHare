class Tortoise extends Thread {
    private Race race;

    public Tortoise(Race race) {
        this.race = race;
    }

    public void run() {
        synchronized (race) {
            for (int lap = 1; lap <= 10; lap++) {
                System.out.println("Hare running lap " + lap);
                if (lap == 11) {
                    System.out.println("Hare takes a nap!");
                    try {
                        race.wait(); // Hare takes a nap, so it releases the lock
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(120); // Simulating time taken for a lap
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Hare finishes the race!");
        }
    }
}



class Hare implements Runnable {
    private Race race;

    public Hare(Race race) {
        this.race = race;
    }

    public void run() {
        synchronized (race) {
            try {
                race.wait();
                for (int lap = 1; lap <= 10; lap++) {
                    System.out.println("Tortoise running lap " + lap);
                    Thread.sleep(30); // Simulating time taken for a lap
                }
                System.out.println("Tortoise finishes the race!");
                race.notify(); // Notify the race has finished
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class Race {
    public static void main(String[] args) {
        Race race = new Race();
        Tortoise tortoise = new Tortoise(race);
        Hare hare = new Hare(race);

        tortoise.start();
        hare.run();

        synchronized (race) {
            race.notify(); // Start the race
        }

        try {
            tortoise.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}