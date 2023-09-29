package end;

public class CapitalStrategyRevolver extends CapitalStrategy {

    @Override
    public double capital(Loan loan) {
        // 有效日不为空，到期日为空，为循环贷款或建议信用额度贷款
        // 若未用份额不为 100%，为信用额度贷款，否则为循环贷款
        return (loan.outstandingRiskAmount() * loan.duration() * riskFactor(loan))
                + (loan.unusedRiskAmount() * loan.duration() * unusedRiskFactor(loan));
    }

    @Override
    public double duration(Loan loan) {
        return yearsTo(loan.getExpiry(), loan);
    }
}
