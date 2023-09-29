package begin;

import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class LoanTest_Begin {
    private static final double LOAN_AMOUNT = 3000.00;
    private static final double TWO_DIGIT_PRECISION = 0.01;

    private static final int LOW_RISK_RATING = 2;
    private static final int HIGH_RISK_RATING = 7;

    @Test
    public void testTermLoanCapital() {
        Date start = new Date(2003, 11, 20);
        Date maturity = new Date(2006, 11, 20);
        Loan termLoan = Loan.newTermLoan(LOAN_AMOUNT, start, maturity, HIGH_RISK_RATING);
        termLoan.payment(1000.00, new Date(2004, 11, 20));
        termLoan.payment(1000.00, new Date(2005, 11, 20));
        termLoan.payment(1000.00, new Date(2006, 11, 20));
        assertEquals("duration", 2.0, termLoan.duration(), TWO_DIGIT_PRECISION);
        assertEquals("capital", 210.00, termLoan.capital(), TWO_DIGIT_PRECISION);
    }

    @Test
    public void testAdvisedLineLoanCapital() {
        Date start = new Date(2003, 11, 20);
        Date expiry = new Date(2006, 11, 20);
        Loan advisedLineLoan = Loan.newAdvisedLine(LOAN_AMOUNT, start, expiry, LOW_RISK_RATING);
        assertEquals("capital", 9.0, advisedLineLoan.capital(), TWO_DIGIT_PRECISION);
    }

    @Test
    public void testRevolverLoanCapital() {
        Date start = new Date(2003, 11, 20);
        Date expiry = new Date(2006, 11, 20);
        Loan revolverLoan = Loan.newRevolver(LOAN_AMOUNT, start, expiry, LOW_RISK_RATING);
        assertEquals("capital", 90.0, revolverLoan.capital(), TWO_DIGIT_PRECISION);
    }
}
