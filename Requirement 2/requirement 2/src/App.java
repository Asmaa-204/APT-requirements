/***********************************Requirement description**********************************************/
// •	There is a bookstore that has different branches, each of them sell a number of books, these books are supplied by the Supplier. 
// •	A bookstore shouldn’t sell a book when the number of books is 0, it should block and notify the Supplier to provide more
// •	A supplier shouldn’t provide a book when the max count of books is reached, when it provides a book, it should tell stores that there are more books available.
// •	Modify the requirement.java code to reflect this behaviour.
// •	All threads should execute in parallel, you cannot allow a thread to stop another thread (should guarantee progress)
// •	Follow the 8 TODOs in the Code
/***********************************************************************************************/

class App {
    public static void main(String... args) throws InterruptedException {
        BookStock b = new BookStock(10);

        // TODO-1: Create 4 threads,
        // 1 for Supplier
        Thread supplier = new Thread(new Supplier(b));

        // 3 for StoreBranches and Name them as the following: Giza branch, Cairo
        // branch, Daqahley branch
        Thread gizaBranch = new Thread(new StoreBranch(b));
        gizaBranch.setName("Giza branch");

        Thread cairoBranch = new Thread(new StoreBranch(b));
        cairoBranch.setName("Cairo branch");

        Thread daqahleyBranch = new Thread(new StoreBranch(b));
        daqahleyBranch.setName("Daqahley branch");

        // TODO-2: Run the 4 threads
        supplier.start();
        gizaBranch.start();
        cairoBranch.start();
        daqahleyBranch.start();
    }
}

class BookStock {
    private int book_count;
    private int max;

    public BookStock(int max) {
        this.max = max;
    }

    public void produce() {
        book_count++;
    }

    public void consume() {
        book_count--;
    }

    public int getCount() {
        return book_count;
    }

    public int getMax() {
        return max;
    }
}

// TODO-3: should it implement or extend anything?
class Supplier implements Runnable {
    private BookStock b;

    public Supplier(BookStock b) {
        this.b = b;
    }

    // TODO-4:is a function missing ?
    public void run() {
        doWork();
    }

    public void doWork() {
        while (true) {
            // TODO-5: how to make it stop producing when it reaches max, without adding
            // extra sleeps or busy waiting ?
            synchronized (b) {
                while (b.getCount() == b.getMax()) {
                    try {
                        b.wait();
                    } catch (Exception e) {
                    }
                }
                b.produce();
                System.out.println(Thread.currentThread().getName() + " provided a book, total " + b.getCount());
                b.notifyAll();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "is awaken");
            }
        }
    }

}

// TODO-6: should it implement or extend anything?
class StoreBranch implements Runnable {
    private BookStock b;

    public StoreBranch(BookStock b) {
        this.b = b;
    }

    // TODO-7: is a function missing ?
    public void run() {
        doWork();
    }

    public void doWork() {
        while (true) {
            // TODO-8: how to make it stop consuming when the store is empty, without adding
            // extra sleeps or busy waiting ?
            synchronized (b) {
                while (b.getCount() == 0) {
                    try {
                        b.wait();
                    } catch (Exception e) {
                    }
                }
                b.consume();
                System.out.println(Thread.currentThread().getName() + " sold a book");
                b.notifyAll();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "is awaken");
            }
        }
    }
}