package kz.calculator.mycalculator

import kz.calculator.mycalculator.Calculator.Calculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MyCalculatorApplicationTests {

    @Test
    fun emptyInput() {
        val res = Calculator.from("")
        assertEquals(null, res)
    }

    @Test
    fun p1Input() {
        val res = Calculator.from("(((")
        assertEquals(null, res)
    }

    @Test
    fun p2Input() {
        val res = Calculator.from(")))")
        assertEquals(null, res)
    }

    @Test
    fun p3Input() {
        val res = Calculator.from("(()")
        assertEquals(null, res)
    }

    @Test
    fun c1Input() {
        val res = Calculator.from("11+1")?.toDouble()
        assertEquals(12.0, res)
    }

    @Test
    fun c2Input() {
        val res = Calculator.from("11-1")?.toDouble()
        assertEquals(10.0, res)
    }

    @Test
    fun c3Input() {
        val res = Calculator.from("11*2")?.toDouble()
        assertEquals(22.0, res)
    }

    @Test
    fun c4Input() {
        val res = Calculator.from("12/2")?.toDouble()
        assertEquals(6.0, res)
    }

    @Test
    fun complex1Input() {
        val res = Calculator.from("((45-23)*(2*6)+(-25)/(-3)-(2+5)/((1+1)*6)+(-5))")?.toDouble()
        assertEquals(266.75, res)
    }

    @Test
    fun complex2Input() {
        val res = Calculator.from("(5*6)/(2-3)+((5-3)*(4+2))+((-2))")?.toDouble()
        assertEquals(-20.0, res)
    }

    @Test
    fun complex3Input() {
        val res = Calculator.from("(4+4)-4*3*4+4/(2*4)+(-4)+2")?.toDouble()
        assertEquals(-41.5, res)
    }

    @Test
    fun complex4Input() {
        val res = Calculator.from("((((-1))))+(((-1))+((-1))+(-1))")?.toDouble()
        assertEquals(-4.0, res)
    }

}
