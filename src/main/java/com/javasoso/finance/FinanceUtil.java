package com.javasoso.finance;

/**
 * 金融财务使用公式 pmt, fv, ppmt, ipmt, fv, pv, npv, nper.
 *
 * @author jasonzhu
 * @date 2020-01-20
 */
public class FinanceUtil {
    private static final int MAX_ITERATION_COUNT = 20;
    private static final double ABSOLUTE_ACCURACY = 1E-7;
    /**
     * 返回年金的定期支付金额
     * Emulates Excel/Calc's PMT(interest_rate, number_payments, PV, FV, Type)
     * function, which calculates the payments for a loan or the future value of an investment
     *
     * @param r    - periodic interest rate represented as a decimal.
     * @param nper - number of total payments / periods.
     * @param pv   - present value -- borrowed or invested principal.
     * @param fv   - future value of loan or annuity.
     * @param type - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing periodic payment amount.
     */
    // http://arachnoid.com/lutusp/finance.html
    static public double pmt(double r, int nper, double pv, double fv, int type) {
        return -r * (pv * Math.pow(1 + r, nper) + fv) / ((1 + r * type) * (Math.pow(1 + r, nper) - 1));
    }

    /**
     * 返回年金的定期支付金额
     * Overloaded pmt() call omitting type, which defaults to 0.
     *
     * @see #pmt(double, int, double, double, int)
     */
    static public double pmt(double r, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv, 0);
    }

    /**
     * 返回年金的定期支付金额
     * Overloaded pmt() call omitting fv and type, which both default to 0.
     *
     * @see #pmt(double, int, double, double, int)
     */
    static public double pmt(double r, int nper, double pv) {
        return pmt(r, nper, pv, 0);
    }


    /**
     * 返回一笔投资在给定期间内支付的利息
     * Emulates Excel/Calc's IPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that is the interest on previous balance.
     *
     * @param r    - periodic interest rate represented as a decimal.
     * @param per  - period (payment number) to check value at.
     * @param nper - number of total payments / periods.
     * @param pv   - present value -- borrowed or invested principal.
     * @param fv   - future value of loan or annuity.
     * @param type - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing interest portion of payment.
     * @see #pmt(double, int, double, double, int)
     * @see #fv(double, int, double, double, int)
     */
    // http://doc.optadata.com/en/dokumentation/application/expression/functions/financial.html
    static public double ipmt(double r, int per, int nper, double pv, double fv, int type) {
        double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type), pv, type) * r;
        if (type == 1) ipmt /= (1 + r);
        return ipmt;
    }

    /**
     * 返回一笔投资在给定期间内支付的利息
     */
    static public double ipmt(double r, int per, int nper, double pv, double fv) {
        return ipmt(r, per, nper, pv, fv, 0);
    }

    /**
     * 返回一笔投资在给定期间内支付的利息
     */
    static public double ipmt(double r, int per, int nper, double pv) {
        return ipmt(r, per, nper, pv, 0);
    }

    /**
     * 返回一笔投资在给定期间内偿还的本金
     * Emulates Excel/Calc's PPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that will apply to principal.
     *
     * @param r    - periodic interest rate represented as a decimal.
     * @param per  - period (payment number) to check value at.
     * @param nper - number of total payments / periods.
     * @param pv   - present value -- borrowed or invested principal.
     * @param fv   - future value of loan or annuity.
     * @param type - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing principal portion of payment.
     * @see #pmt(double, int, double, double, int)
     * @see #ipmt(double, int, int, double, double, int)
     */
    static public double ppmt(double r, int per, int nper, double pv, double fv, int type) {
        return pmt(r, nper, pv, fv, type) - ipmt(r, per, nper, pv, fv, type);
    }

    /**
     * 返回一笔投资在给定期间内偿还的本金
     */
    static public double ppmt(double r, int per, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv) - ipmt(r, per, nper, pv, fv);
    }

    /**
     * 返回一笔投资在给定期间内偿还的本金
     */
    static public double ppmt(double r, int per, int nper, double pv) {
        return pmt(r, nper, pv) - ipmt(r, per, nper, pv);
    }

    /**
     * 返回一笔投资的未来值
     * Emulates Excel/Calc's FV(interest_rate, number_payments, payment, PV,
     * Type) function, which calculates future value or principal at period N.
     *
     * @param r    - periodic interest rate represented as a decimal.
     * @param nper - number of total payments / periods.
     * @param pmt  - periodic payment amount.
     * @param pv   - present value -- borrowed or invested principal.
     * @param type - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing future principal value.
     */
    //http://en.wikipedia.org/wiki/Future_value
    static public double fv(double r, int nper, double pmt, double pv, int type) {
        return -(pv * Math.pow(1 + r, nper) + pmt * (1 + r * type) * (Math.pow(1 + r, nper) - 1) / r);
    }

    /**
     * 返回一笔投资的未来值
     * Overloaded fv() call omitting type, which defaults to 0.
     *
     * @see #fv(double, int, double, double, int)
     */
    static public double fv(double r, int nper, double c, double pv) {
        return fv(r, nper, c, pv, 0);
    }

    /**
     * 返回一笔投资的未来值
     * Future value of an amount given the number of payments, rate, amount
     * of individual payment, present value and boolean value indicating whether
     * payments are due at the beginning of period
     * (false => payments are due at end of period)
     *
     * @param r rate
     * @param n num of periods
     * @param y pmt per period
     * @param p present value
     * @param t type (true=pmt at beginning of period, false=pmt at end of period)
     */
    public static double fv(double r, double n, double y, double p, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1 * (p + (n * y));
        } else {
            double r1 = r + 1;
            retval = ((1 - Math.pow(r1, n)) * (t ? r1 : 1) * y) / r
                    -
                    p * Math.pow(r1, n);
        }
        return retval;
    }

    /**
     * 返回投资的现值
     * Present value of an amount given the number of future payments, rate, amount
     * of individual payment, future value and boolean value indicating whether
     * payments are due at the beginning of period
     * (false => payments are due at end of period)
     *
     * @param r rate
     * @param n num of periods
     * @param y pmt per period
     * @param f future value
     * @param t type (true=pmt at beginning of period, false=pmt at end of period)
     */
    public static double pv(double r, double n, double y, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1 * ((n * y) + f);
        } else {
            double r1 = r + 1;
            retval = (((1 - Math.pow(r1, n)) / r) * (t ? r1 : 1) * y - f)
                    /
                    Math.pow(r1, n);
        }
        return retval;
    }

    /**
     * 返回基于一系列定期的现金流和贴现率计算的投资的净现值
     * calculates the Net Present Value of a principal amount
     * given the discount rate and a sequence of cash flows
     * (supplied as an array). If the amounts are income the value should
     * be positive, else if they are payments and not income, the
     * value should be negative.
     *
     * @param r   rate
     * @param cfs cashflow amounts
     */
    public static double npv(double r, double[] cfs) {
        double npv = 0;
        double r1 = r + 1;
        double trate = r1;
        for (int i = 0, iSize = cfs.length; i < iSize; i++) {
            npv += cfs[i] / trate;
            trate *= r1;
        }
        return npv;
    }

    /**
     * 返回投资的期数
     *
     * @param r rate
     * @param y pmt per period
     * @param p present value
     * @param f future value
     * @param t type (true=pmt at beginning of period, false=pmt at end of period)
     */
    public static double nper(double r, double y, double p, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1 * (f + p) / y;
        } else {
            double r1 = r + 1;
            double ryr = (t ? r1 : 1) * y / r;
            double a1 = ((ryr - f) < 0)
                    ? Math.log(f - ryr)
                    : Math.log(ryr - f);
            double a2 = ((ryr - f) < 0)
                    ? Math.log(-p - ryr)
                    : Math.log(p + ryr);
            double a3 = Math.log(r1);
            retval = (a1 - a2) / a3;
        }
        return retval;
    }

    /**
     * 内部收益率
     * Computes the internal rate of return using an estimated irr of 10 percent.
     *
     * @param income the income values.
     * @return the irr.
     */
    public static double irr(double[] income) {
        return irr(income, 0.1d);
    }


    /**
     * 内部收益率
     * Calculates IRR using the Newton-Raphson Method.
     * <p>
     * Starting with the guess, the method cycles through the calculation until the result
     * is accurate within 0.00001 percent. If IRR can't find a result that works
     * after 20 tries, the Double.NaN<> is returned.
     * </p>
     */
    public static double irr(double[] values, double guess) {

        double x0 = guess;

        for (int i = 0; i < MAX_ITERATION_COUNT; i++) {

            // the value of the function (NPV) and its derivate can be calculated in the same loop
            final double factor = 1.0 + x0;
            double denominator = factor;
            if (denominator == 0) {
                return Double.NaN;
            }

            double fValue = values[0];
            double fDerivative = 0;
            for (int k = 1; k < values.length; k++) {
                final double value = values[k];
                fValue += value / denominator;
                denominator *= factor;
                fDerivative -= k * value / denominator;
            }

            // the essence of the Newton-Raphson Method
            if (fDerivative == 0) {
                return Double.NaN;
            }
            double x1 =  x0 - fValue/fDerivative;

            if (Math.abs(x1 - x0) <= ABSOLUTE_ACCURACY) {
                return x1;
            }

            x0 = x1;
        }
        // maximum number of iterations is exceeded
        return Double.NaN;
    }
}
