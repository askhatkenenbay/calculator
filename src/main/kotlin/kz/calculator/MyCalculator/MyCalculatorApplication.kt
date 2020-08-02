package kz.calculator.mycalculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyCalculatorApplication

fun main(args: Array<String>) {
	runApplication<MyCalculatorApplication>(*args)
}
