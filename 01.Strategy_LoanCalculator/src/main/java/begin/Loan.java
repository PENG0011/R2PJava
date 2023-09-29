package begin;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Loan {
    private static final long MILLIS_PER_DAY = 86400000;
    private static final long DAYS_PER_YEAR = 365;

    private Date expiry; 				// 有效日
    private Date maturity; 				// 到期日
    private Date start; 				// 起始日
    private Date today; 				// 当日

    private double commitment; 			// 承诺金额
    private double outstanding; 		// 未清金额
    private double unusedPercentage; 	// 未用份额
    private int riskRating; 			// 风险评级

    private List<Payment> payments; 	// 支付记录

    //////////////////// 对象构造相关 ////////////////////
    // 构造函数
    private Loan(double commitment, double outstanding, Date start, Date expiry, Date maturity, int riskRating) {
        this.setCommitment(commitment);
        this.outstanding = outstanding;
        this.start = start;
        this.setExpiry(expiry);
        this.setMaturity(maturity);
        this.setRiskRating(riskRating);

        this.unusedPercentage = 1.0;
        this.payments = new LinkedList<Payment>();
    }

    // 创建定期贷款
    public static Loan newTermLoan(double commitment, Date start, Date maturity, int riskRating) {
        return new Loan(commitment, commitment, start, null, maturity, riskRating);
    }

    // 创建信用额度贷款
    public static Loan newAdvisedLine(double commitment, Date start, Date expiry, int riskRating) {
        if (riskRating > 3)
            return null;

        Loan advisedLine = new Loan(commitment, 0, start, expiry, null, riskRating);
        advisedLine.setUnusedPercentage(0.1);

        return advisedLine;
    }

    // 创建循环贷款
    public static Loan newRevolver(double commitment, Date start, Date expiry, int riskRating) {
        return new Loan(commitment, 0, start, expiry, null, riskRating);
    }

    //////////////////// 贷款金额周期计算 ////////////////////
    // 贷款金额计算
    public double capital() {
        // 有效日为空，到期日不为空，为定期贷款
        // 资金计算方法：承诺金额 * 期限 * 风险因素
        if (getExpiry() == null && getMaturity() != null)
            return getCommitment() * duration() * riskFactor();

        // 有效日不为空，到期日为空，为循环贷款或建议信用额度贷款
        // 若未用份额不为 100%，为信用额度贷款，否则为循环贷款
        if (getExpiry() != null && getMaturity() == null) {
            if (getUnusedPercentage() != 1.0)
                // 信用额度贷款
                // 资金计算方法：承诺金额 * 未用份额 * 期限 * 风险因素
                return getCommitment() * getUnusedPercentage() * duration() * riskFactor();
            else
                // 循环贷款
                // 资金计算方法：未清风险
                return (outstandingRiskAmount() * duration() * riskFactor())
                        + (unusedRiskAmount() * duration() * unusedRiskFactor());
        }
        return 0.0;
    }

    // 贷款周期计算
    public double duration() {
        if (getExpiry() == null && getMaturity() != null) 		// 定期贷款
            return weightedAverageDuration();
        else if (getExpiry() != null && getMaturity() == null) 	// 循环或建议信用额度贷款
            return yearsTo(getExpiry());
        return 0.0;
    }

    //////////////////// 贷款金额周期计算辅助方法 ////////////////////
    // 未用份额
    double getUnusedPercentage() {
        return unusedPercentage;
    }

    private void setUnusedPercentage(double unusedPercentage) {
        this.unusedPercentage = unusedPercentage;
    }

    // 未清金额
    private double outstandingRiskAmount() {
        return outstanding;
    }

    // 未用风险金额
    private double unusedRiskAmount() {
        return (getCommitment() - outstanding);
    }

    // 加权平均周期
    private double weightedAverageDuration() {
        double duration = 0.0;
        double weightedAverage = 0.0;
        double sumOfPayments = 0.0;

        Iterator<Payment> loanPayments = payments.iterator();
        while (loanPayments.hasNext()) {
            Payment payment = loanPayments.next();
            sumOfPayments += payment.amount();
            weightedAverage += yearsTo(payment.date()) * payment.amount();
        }

        if (getCommitment() != 0.0)
            duration = weightedAverage / sumOfPayments;

        return duration;
    }

    // 年数差
    private double yearsTo(Date endDate) {
        Date beginDate = (today == null ? start : today);
        return ((endDate.getTime() - beginDate.getTime()) / MILLIS_PER_DAY) / DAYS_PER_YEAR;
    }

    // 获取风险因素
    private double riskFactor() {
        return RiskFactor.getFactors().forRating(getRiskRating());
    }

    // 获取未使用风险因素
    private double unusedRiskFactor() {
        return UnusedRiskFactors.getFactors().forRating(getRiskRating());
    }

    ////////////////////贷款支付方法 ////////////////////
    // 支付
    public void payment(double amount, Date date) {
        payments.add(new Payment(amount, date));
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Date getMaturity() {
        return maturity;
    }

    public void setMaturity(Date maturity) {
        this.maturity = maturity;
    }

    public double getCommitment() {
        return commitment;
    }

    public void setCommitment(double commitment) {
        this.commitment = commitment;
    }

    public int getRiskRating() {
        return riskRating;
    }

    public void setRiskRating(int riskRating) {
        this.riskRating = riskRating;
    }
}
