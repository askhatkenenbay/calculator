package kz.calculator.mycalculator.Calculator

import java.math.BigDecimal

internal interface Operators {
    companion object {
        val PLUS: Operator = object : Operator("+", 20, true) {
            override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal? {
                return v1?.add(v2, Calculator.MATH_CONTEXT)
            }
        }
        val MINUS: Operator = object : Operator("-", 20, true) {
            override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal? {
                return v1?.subtract(v2, Calculator.MATH_CONTEXT)
            }
        }
        val MULTIPLY: Operator = object : Operator("*", 30, true) {
            override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal? {
                return v1?.multiply(v2, Calculator.MATH_CONTEXT)
            }
        }
        val DIV: Operator = object : Operator("/", 30, true) {
            override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal? {
                return v1?.divide(v2, Calculator.MATH_CONTEXT)
            }
        }
        val REMAINDER: Operator = object : Operator("%", 30, true) {
            override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal? {
                return v1?.remainder(v2, Calculator.MATH_CONTEXT)
            }
        }
    }
}