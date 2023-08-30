package com.dishdiscoverers.core.common

class Calculator {
    fun evaluate(expression: String): Int {
        var sum = 0
        for (summand in expression.split("\\+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) sum += Integer.valueOf(summand)
        return sum
    }
}