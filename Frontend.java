import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frontend extends JFrame implements ActionListener {
    private JComboBox<String> accountSelector;
    private JTextField amountField, transectionField;
    private JLabel currencyLabel, balanceLabel, selectLabel, amountLabel, transectionLabel;
    private JButton depositBtn, withdrawBtn;
    private AccountManager manager;
    JPanel panel;

    public Frontend() {
        super("Currency Account Manager");
        this.setSize(700, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        manager = new AccountManager();
        if (manager.getAccountNames().length == 0) {
            manager.addAccount(new Account("Alice", "USD", 100));
            manager.addAccount(new Account("Bob", "BDT", 10000));
        }

        panel = new JPanel();
        panel.setLayout(null);

        selectLabel = new JLabel("Select Account:");
        selectLabel.setBounds(30, 20, 120, 25);
        panel.add(selectLabel);

        transectionLabel = new JLabel("Transaction History:");
        transectionLabel.setBounds(380, 20, 200, 25);
        panel.add(transectionLabel);
        
        transectionField = new JTextField();
        transectionField.setBounds(380, 50, 250, 200);
        transectionField.setEditable(false);
        transectionField.setBackground(Color.WHITE);;
        panel.add(transectionField);

        accountSelector = new JComboBox<>(manager.getAccountNames());
        accountSelector.setBounds(150, 20, 200, 25);
        accountSelector.addActionListener(this);
        panel.add(accountSelector);

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

        this.add(panel);
        updateAccountInfo();
    }

    private void updateAccountInfo() {
        String selected = (String) accountSelector.getSelectedItem();
        Account acc = manager.getAccountByName(selected);
        if (acc != null) {
            currencyLabel.setText("Currency: " + acc.getCurrency());
            balanceLabel.setText("Balance: " + acc.getBalance());
        }
    }

    public void actionPerformed(ActionEvent e) {
        String selected = (String) accountSelector.getSelectedItem();
        String command = e.getActionCommand();
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
        } else if (e.getSource() == accountSelector) {
            updateAccountInfo();
        }

        updateAccountInfo();
        amountField.setText("");
    }
}
