public class Account {
    private String holderName;
    private String currency;
    private double balance;

    public Account(String holderName, String currency, double balance) {
        this.holderName = holderName;
        this.currency = currency;
        this.balance = balance;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void receive(double amount) {
        balance += amount;
    }

    public static double convert(double amount, String from, String to) {
        double rate = 1.0;
        if (from.equals("USD") && to.equals("BDT"))
            {rate = 110;}
        else if (from.equals("BDT") && to.equals("USD"))
            {rate = 1.0 / 110;}
        return amount * rate;
    }

    public String toString() {
        return holderName + "," + currency + "," + balance;
    }

    public static Account fromString(String line) {
        String[] parts = line.split(",");
        return new Account(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}
