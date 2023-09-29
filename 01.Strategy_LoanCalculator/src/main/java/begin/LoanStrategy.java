package begin;

import static sun.security.krb5.Config.duration;

public class LoanStrategy {

    //////////////////// 贷款金额周期计算 ////////////////////
    // 贷款金额计算
    public double capital(Loan loan) {
        // 有效日为空，到期日不为空，为定期贷款
        // 资金计算方法：承诺金额 * 期限 * 风险因素
        if (loan.getExpiry() == null && loan.getMaturity() != null)
            return loan.getCommitment() * loan.duration() * riskFactor();

        // 有效日不为空，到期日为空，为循环贷款或建议信用额度贷款
        // 若未用份额不为 100%，为信用额度贷款，否则为循环贷款
        if (loan.getExpiry() != null && loan.getMaturity() == null) {
            if (loan.getUnusedPercentage() != 1.0)
                // 信用额度贷款
                // 资金计算方法：承诺金额 * 未用份额 * 期限 * 风险因素
                return loan.getCommitment() * loan.getUnusedPercentage() * duration() * riskFactor();
            else
                // 循环贷款
                // 资金计算方法：未清风险
                return (outstandingRiskAmount() * duration() * riskFactor())
                        + (unusedRiskAmount() * duration() * unusedRiskFactor());
        }
        return 0.0;
    }

    private double riskFactor() {
        return RiskFactor.getFactors().forRating(getRiskRating());
    }


}
