import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frontend extends JFrame implements ActionListener {
    private JComboBox<String> accountSelector, fromSelector, toSelector;;
    private JTextField amountField, transferAmountField;
    private JTextArea transectionField;
    private JLabel currencyLabel, balanceLabel, selectLabel, amountLabel, transectionLabel, TransferLabel, fromLabel,
            toLabel, transferAmountLabel;
    private JButton depositBtn, withdrawBtn, transferBtn;
    private AccountManager manager;
    JPanel panel;

    public Frontend() {
        super("Currency Account Manager");
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        manager = new AccountManager();
        if (manager.isEmpty()) {
            manager.addAccount(new Account("Wasifur", "USD", 0));
            manager.addAccount(new Account("Polok", "BDT", 0));
            manager.addAccount(new Account("Anik", "USD", 0));
            manager.saveToFile();
        }

        panel = new JPanel();
        panel.setLayout(null);

        selectLabel = new JLabel("Select Account:");
        selectLabel.setBounds(30, 20, 120, 25);
        panel.add(selectLabel);
        accountSelector = new JComboBox<>();
        String[] names = manager.getAccountNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                accountSelector.addItem(names[i]);
            }
        }

        accountSelector.setBounds(150, 20, 200, 25);
        accountSelector.addActionListener(this);
        panel.add(accountSelector);

        transectionLabel = new JLabel("Transaction History:");
        transectionLabel.setBounds(380, 20, 200, 25);
        panel.add(transectionLabel);

        transectionField = new JTextArea();
        transectionField.setBounds(380, 50, 250, 200);
        transectionField.setEditable(false);
        transectionField.setLineWrap(true);
        transectionField.setWrapStyleWord(true);
        transectionField.setBackground(Color.WHITE);
        panel.add(transectionField);

        currencyLabel = new JLabel("Currency: ");
        currencyLabel.setBounds(30, 60, 200, 25);
        panel.add(currencyLabel);

        balanceLabel = new JLabel("Balance: ");
        balanceLabel.setBounds(30, 90, 200, 25);
        panel.add(balanceLabel);

        amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(30, 130, 100, 25);
        panel.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(150, 130, 200, 25);
        panel.add(amountField);

        depositBtn = new JButton("Deposit");
        depositBtn.setBounds(50, 180, 100, 30);
        depositBtn.addActionListener(this);
        panel.add(depositBtn);

        withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(200, 180, 100, 30);
        withdrawBtn.addActionListener(this);
        panel.add(withdrawBtn);

        TransferLabel = new JLabel("Tranfer Funds:");
        TransferLabel.setBounds(30, 240, 100, 25);
        panel.add(TransferLabel);

        fromLabel = new JLabel("From:");
        fromLabel.setBounds(30, 270, 50, 25);
        panel.add(fromLabel);

        fromSelector = new JComboBox<>();
        toSelector = new JComboBox<>();
        String[] accountNames = manager.getAccountNames();
        for (String name : accountNames) {
            if (name != null) {
                fromSelector.addItem(name);
                toSelector.addItem(name);
            }
        }

        fromSelector.setBounds(80, 270, 120, 25);
        panel.add(fromSelector);

        toLabel = new JLabel("To:");
        toLabel.setBounds(210, 270, 30, 25);
        panel.add(toLabel);

        toSelector.setBounds(240, 270, 120, 25);
        panel.add(toSelector);

        transferAmountLabel = new JLabel("Amounts:");
        transferAmountLabel.setBounds(30, 300, 80, 50);
        panel.add(transferAmountLabel);
        transferAmountField = new JTextField();
        transferAmountField.setBounds(100, 315, 200, 25);
        panel.add(transferAmountField);

        transferBtn = new JButton("Transfer");
        transferBtn.setBounds(325, 315, 100, 25);
        transferBtn.addActionListener(this);
        panel.add(transferBtn);

        this.add(panel);
        updateAccountInfo();
    }

    private void updateAccountInfo() {
        String selected = (String) accountSelector.getSelectedItem();

        Account acc = manager.getAccountByName(selected);
        if (acc != null) {
            currencyLabel.setText("Currency: " + acc.getCurrency());
            balanceLabel.setText("Balance: " + String.format("%.2f", acc.getBalance()));
            String[] history = acc.getHistory();
            String historyText = "";
            for (int i = 0; i < history.length; i++) {
                if (history[i] != null) {
                    historyText += history[i] + "\n";
                }
                ;
            }
            transectionField.setText(historyText);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String selected = (String) accountSelector.getSelectedItem();
        String command = e.getActionCommand();
        String from = (String) fromSelector.getSelectedItem();
        String to = (String) toSelector.getSelectedItem();
        Account fromAcc = manager.getAccountByName(from);
        Account toAcc = manager.getAccountByName(to);
        Account acc = manager.getAccountByName(selected);
        if (acc == null)
            return;
        if (command.equals(depositBtn.getText())) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                acc.deposit(amount);
                manager.saveToFile();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid amount");
            }
        } else if (command.equals(withdrawBtn.getText())) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (!acc.withdraw(amount)) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance");
                } else {
                    manager.saveToFile();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid amount");
            }
        } else if (command.equals("Transfer")) {
            try {
                if (from.equals(to)) {
                    JOptionPane.showMessageDialog(this, "Cannot transfer to the same account");
                    return;
                }
                double amount = Double.parseDouble(transferAmountField.getText());

                if (fromAcc == null || toAcc == null) {
                    JOptionPane.showMessageDialog(this, "Invalid accounts selected");
                    return;
                }
                if (!fromAcc.withdraw(amount)) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance in sender account.");
                    return;
                }
                double convertedAmount = Account.TrasfernConvert(fromAcc.getCurrency(), toAcc.getCurrency(), amount);
                toAcc.receive(convertedAmount);
                String sentAmount = String.format("%.2f", amount);
                String receivedAmount = String.format("%.2f", convertedAmount);
                fromAcc.addHistory("Sent " + sentAmount + " " + fromAcc.getCurrency() + " to " + toAcc.getHolderName());
                toAcc.addHistory(
                        "Received " + receivedAmount + " " + toAcc.getCurrency() + " from " + fromAcc.getHolderName());

                manager.saveToFile();
                JOptionPane.showMessageDialog(this, "Transfer successful.");
                transferAmountField.setText("");

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Enter valid amount");
                return;
            }
        } else if (e.getSource() == accountSelector) {
            updateAccountInfo();
        }

        updateAccountInfo();
        amountField.setText("");
    }
}
