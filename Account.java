public class Account {
    private String holderName;
    private String currency;
    private double balance;
    private String[] history = new String[100];

    public Account(String holderName, String currency, double balance) {
        this.holderName = holderName;
        this.currency = currency;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addHistory("Deposited " + amount + " " + currency);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addHistory("Withdrew " + amount + " " + currency);
            return true;
        }
        return false;
    }

    public void addHistory(String h) {
        for (int i = 0; i < history.length; i++) {
            if (history[i] == null) {
                history[i] = h;
                break;
            }
        }
    }

    public String[] getHistory() {
        return history;
    }

    public String getHistoryText() {
        String text = "";
        for (int i = 0; i < history.length; i++) {
            if (history[i] != null) {
                text += history[i];
            }
        }
        return text;
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

    public void receive(double amount) {
        balance += amount;
    }

    public static double TrasfernConvert(String from, String to, double amount) {
        double rate = 1.0;
        if (from.equals("USD") && to.equals("BDT")) {
            rate = 110;

        } else if (from.equals("BDT") && to.equals("USD")) {
            rate = 1.0 / 110;
        }
        return amount * rate;
    }

    public String toString() {
        String result = holderName + "," + currency + "," + balance;
        for (int i = 0; i < history.length; i++) {
            if (history[i] != null) {
                result += "," + history[i];
            }
        }
        return result;
    }

    public static Account fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            return null;

        }
        String name = parts[0];
        String currency = parts[1];
        double balance = Double.parseDouble(parts[2]);

        Account acc = new Account(name, currency, balance);
        for (int i = 3; i < parts.length; i++) {
            acc.addHistory(parts[i]);
        }
        return acc;
    }
}
