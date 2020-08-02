package kz.calculator.mycalculator.Calculator

import java.math.BigDecimal

internal abstract class Operator(private val operatorName: String, val precedence: Int, val isLeftAssoc: Boolean) {

    fun name(): String {
        return operatorName
    }

    abstract fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal?

}