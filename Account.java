public class Account {
    protected String accountName;
    protected String currencyType;
    protected double expense;
    protected double totalBalance;
    Account(String accountName, String currencyType, double totalBalance) {
        this.accountName = accountName;
        this.currencyType = currencyType;
        this.expense = 0.0;
        this.totalBalance = totalBalance;  
    }

    public String getAccountName() {
        return accountName;
    }
    public String getCurrencyType() {
        return currencyType;
    }
    public double getExpense() {
        return expense;
    }
    
}
