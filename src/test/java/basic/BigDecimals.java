package basic;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 15:27
 */
public class BigDecimals {
    @Test
    public void test() {
        System.out.println(0.05 * 0.05);

    }
    @Test
    public void te() {

        BigDecimal bigDecimal = new BigDecimal("0.05");
        BigDecimal bigDecimal1 = new BigDecimal("0.05");
        System.out.println(bigDecimal.multiply(bigDecimal1));

    }
}
