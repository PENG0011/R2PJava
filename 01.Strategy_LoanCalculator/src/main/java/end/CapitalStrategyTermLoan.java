package end;

public class CapitalStrategyTermLoan extends CapitalStrategy {

    @Override
    public double capital(Loan loan) {
        // 有效日为空，到期日不为空，为定期贷款
        // 资金计算方法：承诺金额 * 期限 * 风险因素
        return loan.getCommitment() * loan.duration() * riskFactor(loan);
    }

    @Override
    public double duration(Loan loan) {
        return weightedAverageDuration(loan);
    }
}
