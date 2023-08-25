package com.dishdiscoverers.core.utility

import org.junit.Assert
import org.junit.Test

class CalculatorTest {
    @Test
    fun evaluatesExpression() {
        val calculator = Calculator()
        val sum = calculator.evaluate("1+2+3")
        Assert.assertEquals(6, sum.toLong())
    }

     @Test
    fun evaluatesExpressionWithFailed() {
        val calculator = Calculator()
        val sum = calculator.evaluate("1+2+3")
        Assert.assertNotEquals(7, sum.toLong())
    }
}