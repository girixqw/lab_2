class AnimalThread extends Thread {
    private volatile int distance = 0;
    private static final int MAX_DISTANCE = 100;

    public AnimalThread(String name, int priority) {
        setName(name);
        setPriority(priority);
    }

    @Override
    public void run() {
        while (distance < MAX_DISTANCE) {
            try {
                Thread.sleep(Math.max(10, 100 - getPriority() * 10));
                distance += (getPriority() + 1);
                System.out.println(getName() + ": " + distance + " м (приоритет: " + getPriority() + ")");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(getName() + " финишировал!");
    }

    public int getDistance() {
        return distance;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnimalThread rabbit = new AnimalThread("Кролик", 10);
        AnimalThread turtle = new AnimalThread("Черепаха", 1);

        rabbit.start();
        turtle.start();

        while (rabbit.isAlive() || turtle.isAlive()) {
            Thread.sleep(500);

            if (turtle.isAlive() && turtle.getDistance() < rabbit.getDistance()) {
                int old = turtle.getPriority();
                turtle.setPriority(Math.min(10, old + 1));
                if (turtle.getPriority() != old)
                    System.out.println(">> Черепаха отстаёт! Приоритет: " + old + " -> " + turtle.getPriority());
            }

            if (rabbit.isAlive() && rabbit.getDistance() < turtle.getDistance()) {
                int old = rabbit.getPriority();
                rabbit.setPriority(Math.min(10, old + 1));
                if (rabbit.getPriority() != old)
                    System.out.println(">> Кролик отстаёт! Приоритет: " + old + " -> " + rabbit.getPriority());
            }

            System.out.println("--- Кролик: " + rabbit.getDistance() + " м, Черепаха: " + turtle.getDistance() + " м ---");
        }

        System.out.println("\nГонка завершена!");
        System.out.println("Кролик: " + rabbit.getDistance() + " м");
        System.out.println("Черепаха: " + turtle.getDistance() + " м");
    }
}