
package account;

class AccountTest
{
    private long accountNo;
    private String name;
    private double balance;
    private long phoneNo;
    private String dateOfBirth;
    
    // Using a constructor class property.
    public AccountTest(long a, String n, double b, long p, String d)
    {
        this.accountNo = a;
        this.name = n;
        this.balance = b;
        this.phoneNo = p;
        this.dateOfBirth = d;
    }
    
    // Using Getter method also Data Hiding Concept.
    public double getAccountNo() {return accountNo;}
    public String getName() {return name;}
    public double getBalance() {return balance;}
    public double getPhoneNo() {return phoneNo;}
    public String getDateOfBirth() {return dateOfBirth; }
    
     // Using Setter method.
    public void setAccountNo(long a) {accountNo = a;}
    public void setName(String n) {name = n;}
    public void setBalance(double b) {balance = b;}
    public void setPhoneNo(long p) {phoneNo = p;}
    public void setDateOfBirth(String d) {dateOfBirth = d;}
 
    // Method of accounts.
   
    
    public void display()
    {
        System.out.println("Account No. : " + accountNo);
        System.out.println("Account Holder Name : "+ name);
        System.out.println("Account Balance : "+ balance);
    }
}

class SavingAccount extends AccountTest 
{
     // Simple FD state
    private double fdAmount = 0.0;
    private int fdMonths = 0;
    private double fdAnnualRate = 0.0;
    private boolean fdActive = false;
    // Constructor to satisfy super-class requirement
    public SavingAccount(long a, String n, double b, long p, String d) {
        super(a, n, b, p, d);
    }

    public double deposit(double amt) 
    {
        setBalance(getBalance() + amt);
        return getBalance();
    }

    public double withdraw(double amt) 
    {
        if (amt <= getBalance()) 
        {
            setBalance(getBalance() - amt);
        } else 
        {
            System.out.print("Insufficient balance ");
        }
        return getBalance();
    }
    public boolean fixedDeposit(double amount, int months, double annualRate) 
    {
    if (fdActive || amount <= 0 || months <= 0 || annualRate < 0 || amount > getBalance()) return false;
    setBalance(getBalance() - amount);
    fdAmount = amount; fdMonths = months; fdAnnualRate = annualRate; fdActive = true;
    return true;
    }
    // Close FD and credit principal + simple interest to balance
    public double liquidate() {
        if (!fdActive) {
            System.out.println("No active FD to liquidate.");
            return getBalance();
        }
        double interest = fdAmount * (fdAnnualRate / 100.0) * (fdMonths / 12.0);
        double payout = fdAmount + interest;
        setBalance(getBalance() + payout);

        // reset FD
        fdAmount = 0.0;
        fdMonths = 0;
        fdAnnualRate = 0.0;
        fdActive = false;
        System.out.println("FD liquidated. Credited: " + payout);
        return getBalance();
    }
}

class LoanAccount extends AccountTest {
    private double principal;      // sanctioned principal (may change with top-up)
    private double annualRate;     // % per annum
    private int tenureMonths;      // original tenure
    private double outstanding;    // current outstanding
    private double emi;            // scheduled EMI

    public LoanAccount(long a, String n, double openingBalance, long p, String d,
                       double principal, double annualRate, int tenureMonths) {
        super(a, n, openingBalance, p, d);
        if (principal <= 0 || annualRate < 0 || tenureMonths <= 0) {
            throw new IllegalArgumentException("Invalid loan parameters");
        }
        this.principal = principal;
        this.annualRate = annualRate;
        this.tenureMonths = tenureMonths;
        this.outstanding = principal;
        this.emi = calcEmi(this.outstanding, this.annualRate, this.tenureMonths);
    }

    private double calcEmi(double p, double annualRate, int months) {
        double r = annualRate / 12.0 / 100.0; // monthly rate
        if (r == 0) return p / months;
        double pow = Math.pow(1 + r, months);
        return p * r * pow / (pow - 1);
    }

    public double getOutstanding() { return outstanding; }
    public double getEmi() { return emi; }
    public double getAnnualRate() { return annualRate; }
    public int getTenureMonths() { return tenureMonths; }
    public double getPrincipal() { return principal; }

    // Pay one EMI or any amount (>= 0). Returns remaining outstanding.
    public double payEmi(double amount) {
        if (amount <= 0) {
            System.out.println("Payment must be positive.");
            return outstanding;
        }
        double r = annualRate / 12.0 / 100.0;
        // First add current month's interest on outstanding, then reduce by payment
        double interestForMonth = outstanding * r;
        double principalReduction = amount - interestForMonth;
        if (principalReduction < 0) principalReduction = 0; // if payment < interest

        outstanding -= principalReduction;
        if (outstanding < 0) outstanding = 0;

        // Recompute EMI for remaining tenure if you want dynamic tenure; here we keep EMI same.
        System.out.println("Paid: " + amount + " | Interest: " + interestForMonth +
                " | Principal reduced: " + principalReduction + " | Outstanding: " + outstanding);
        return outstanding;
    }

    // Increase principal (Top-up loan). Recalculate EMI for remaining tenure.
    public void topupLoan(double amount) {
        if (amount <= 0) {
            System.out.println("Top-up amount must be positive.");
            return;
        }
        principal += amount;
        outstanding += amount;
        // Recalculate EMI over the remaining tenure (keep same tenure)
        this.emi = calcEmi(outstanding, annualRate, tenureMonths);
        System.out.println("Top-up approved: " + amount + ". New outstanding: " + outstanding + ". New EMI: " + emi);
    }

    // Lump-sum repayment (prepayment). Reduces outstanding immediately.
    public double repayment(double amount) {
        if (amount <= 0) {
            System.out.println("Repayment must be positive.");
            return outstanding;
        }
        outstanding -= amount;
        if (outstanding < 0) outstanding = 0;
        // Optionally, recalc EMI or reduce tenure. We keep EMI same for simplicity.
        System.out.println("Prepaid: " + amount + " | Outstanding: " + outstanding);
        return outstanding;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Loan Principal : " + principal);
        System.out.println("Annual Rate    : " + annualRate + "%");
        System.out.println("Tenure (mo)    : " + tenureMonths);
        System.out.println("EMI (approx)   : " + emi);
        System.out.println("Outstanding    : " + outstanding);
    }
}

public class Account {
    public static void main(String[] args) {
        // ----- SavingAccount demo -----
        SavingAccount sa = new SavingAccount(
                1001001L, "Ansh", 5000.0, 9876543210L, "2001-06-02"
        );
        sa.display();
        sa.deposit(2000);
        sa.withdraw(1200);
        sa.fixedDeposit(3000, 12, 6.5); // 12 months @ 6.5% p.a.
        sa.display();
        sa.liquidate();
        sa.display();

        System.out.println("--------------------------------------------------");

        // ----- LoanAccount demo -----
        LoanAccount la = new LoanAccount(
                2002002L, "Ansh", 0.0, 9876543210L, "2001-06-02",
                200000.0, 10.0, 24 // principal, annualRate, tenureMonths
        );
        la.display();
        la.payEmi(la.getEmi());   // pay scheduled EMI
        la.repayment(10000);      // lump-sum prepayment
        la.topupLoan(50000);      // top-up loan
        la.payEmi(la.getEmi());   // next EMI
        la.display();
    }
}