package kz.calculator.mycalculator.Calculator

import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import kotlin.collections.HashMap

class Calculator private constructor(expression: String) {
    companion object {
        val MATH_CONTEXT = MathContext.DECIMAL32
        const val DECIMAL_SEPARATOR = '.'
        const val MINUS_SIGN = '-'
        internal val OPERATORS: MutableMap<String, Operator?> = HashMap()
        fun from(expr: String): String? {
            val calculator = Calculator(expr)
            val result: String?
            result = try {
                calculator.evaluate().toPlainString()
            } catch (e: Exception) {
                null
            }
            return result
        }

        init {
            OPERATORS[Operators.PLUS.name()] = Operators.PLUS
            OPERATORS[Operators.MINUS.name()] = Operators.MINUS
            OPERATORS[Operators.MULTIPLY.name()] = Operators.MULTIPLY
            OPERATORS[Operators.DIV.name()] = Operators.DIV
            OPERATORS[Operators.REMAINDER.name()] = Operators.REMAINDER
        }
    }

    private var expression: String? = null
    private var rpn: List<String>? = null
    private fun evaluate(): BigDecimal {
        val stack = Stack<Operand>()
        for (token in rPNExpression!!) {
            if (OPERATORS.containsKey(token)) {
                val argLeft = stack.pop()
                val argRight = stack.pop()
                val operator = OPERATORS[token]
                stack.push(object : Operand {
                    override fun eval(): BigDecimal {
                        return operator!!.eval(argRight.eval(), argLeft.eval())!!
                    }
                })
            } else {
                stack.push(object : Operand {
                    override fun eval(): BigDecimal {
                        return BigDecimal(token, MATH_CONTEXT)
                    }
                })
            }
        }
        return stack.pop().eval()!!.stripTrailingZeros()
    }

    private val rPNExpression: List<String>?
        get() {
            if (rpn == null) {
                rpn = helper(expression)
                validate(rpn)
            }
            return rpn
        }

    private fun isNumber(st: String): Boolean {
        if (st[0] == MINUS_SIGN && st.length == 1) return false
        if (st[0] == '+' && st.length == 1) return false
        if (st[0] == 'e' || st[0] == 'E') return false
        for (ch in st.toCharArray()) {
            if (!Character.isDigit(ch) && ch != MINUS_SIGN && ch != DECIMAL_SEPARATOR && ch != 'e' && ch != 'E' && ch != '+') return false
        }
        return true
    }

    private fun validate(rpn: List<String>?) {
        val params = Stack<Int>()
        var counter = 0
        for (token in rpn!!) {
            if (!params.isEmpty()) {
                params[params.size - 1] = params.peek() + 1
            } else if (OPERATORS.containsKey(token)) {
                counter -= 2
            }
            require(counter >= 0) { "Many operators or functions at: $token" }
            counter++
        }
        require(counter <= 1) { "Many numbers or variables" }
        require(counter >= 1) { "Empty exp" }
    }

    private fun helper(expression: String?): List<String> {
        val outputQueue: MutableList<String> = ArrayList()
        val stack = Stack<String>()
        val tokenizer = Tokenizer(expression!!)
        while (tokenizer.hasNext()) {
            val token = tokenizer.next()
            if (isNumber(token)) {
                outputQueue.add(token)
            } else if (Character.isLetter(token[0])) {
                stack.push(token)
            } else if ("," == token) {
                while (!stack.isEmpty() && "(" != stack.peek()) {
                    outputQueue.add(stack.pop())
                }
            } else if (OPERATORS.containsKey(token)) {
                val operator: Operator? = OPERATORS[token]
                var nextToken = if (stack.isEmpty()) null else stack.peek()
                while (nextToken != null && OPERATORS.containsKey(nextToken)
                        && ((operator!!.isLeftAssoc
                                && operator.precedence <= OPERATORS[nextToken]!!.precedence)
                                || operator.precedence < OPERATORS[nextToken]!!.precedence)) {
                    outputQueue.add(stack.pop())
                    nextToken = if (stack.isEmpty()) null else stack.peek()
                }
                stack.push(token)
            } else if ("(" == token) {
                stack.push(token)
            } else if (")" == token) {
                while (!stack.isEmpty() && "(" != stack.peek()) {
                    outputQueue.add(stack.pop())
                }
                if (stack.isEmpty()) {
                    throw RuntimeException("Wrong parentheses")
                }
                stack.pop()
            }
        }
        while (!stack.isEmpty()) {
            val element = stack.pop()
            if ("(" == element || ")" == element) {
                throw RuntimeException("Wrong parentheses")
            }
            if (!OPERATORS.containsKey(element)) {
                throw RuntimeException("Unknown paramater: $element")
            }
            outputQueue.add(element)
        }
        return outputQueue
    }

    init {
        this.expression = expression
    }
}
