import java.io.*;

public class AccountManager {
    private Account[] accounts = new Account[10];
    private final String fileName = "data.txt";

    public AccountManager() {
        loadFromFile();
    }

    public void addAccount(Account acc) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                accounts[i] = acc;
                saveToFile();
                return;
            }
        }

    }

    public Account getAccountByName(String name) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && accounts[i].getHolderName().equals(name)) {
                return accounts[i];
            }
        }
        return null; // no match found
    }

    public String[] getAccountNames() {
        String[] names = new String[10];
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null) {
                names[i] = accounts[i].getHolderName();
            }
        }
        return names;
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] != null) {
                    pw.println(accounts[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.fromString(line);
                for (int i = 0; i < accounts.length; i++) {
                    if (accounts[i] == null) {
                        accounts[i] = acc;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null) {
                return false;
            }
        }
        return true;
    }

}
