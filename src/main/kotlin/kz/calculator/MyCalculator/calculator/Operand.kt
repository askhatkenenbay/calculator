package kz.calculator.mycalculator.Calculator

import java.math.BigDecimal

internal interface Operand {
    fun eval(): BigDecimal?
}