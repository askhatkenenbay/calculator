package kz.calculator.mycalculator.Calculator

internal class Tokenizer(input: String) : MutableIterator<String?> {
    private var pos = 0
    private val input: String
    private var previousToken: String? = null
    override fun hasNext(): Boolean {
        return pos < input.length
    }

    private fun peekNextChar(): Char {
        return if (pos < input.length - 1) {
            input[pos + 1]
        } else {
            '0'
        }
    }

    override fun next(): String {
        val token = StringBuilder()
        if (pos >= input.length) {
            return null!!.also { previousToken = it }
        }
        var ch = input[pos]
        while (Character.isWhitespace(ch) && pos < input.length) {
            ch = input[++pos]
        }
        if (Character.isDigit(ch)) {
            while (isInNumber(token, ch) && pos < input.length) {
                token.append(input[pos++])
                ch = if (pos == input.length) 0.toChar() else input[pos]
            }
        } else if (isOperator(ch)) {
            token.append(Calculator.MINUS_SIGN)
            pos++
            token.append(next())
        } else if (ch == '(' || ch == ')') {
            token.append(ch)
            pos++
        } else {
            while (isNonEvaluable(ch) && pos < input.length) {
                token.append(input[pos])
                pos++
                ch = if (pos == input.length) 0.toChar() else input[pos]
                if (ch == Calculator.MINUS_SIGN) {
                    break
                }
            }
            require(Calculator.OPERATORS.containsKey(token.toString())) { "Unknown operator $token" }
        }
        return token.toString().also { previousToken = it }
    }

    override fun remove() {
        throw IllegalArgumentException("remove() not supported")
    }

    private fun isNonEvaluable(ch: Char): Boolean {
        return (!Character.isLetter(ch)
                && !Character.isDigit(ch)
                && ch != '_' && !Character.isWhitespace(ch)
                && ch != '(' && ch != ')')
    }

    private fun isOperator(ch: Char): Boolean {
        return (ch == Calculator.MINUS_SIGN && Character.isDigit(peekNextChar())
                && ("(" == previousToken || previousToken == null || Calculator.OPERATORS.containsKey(previousToken!!)))
    }

    private fun isInNumber(token: StringBuilder, ch: Char): Boolean {
        return (Character.isDigit(ch)
                || ch == Calculator.DECIMAL_SEPARATOR || ch == 'e' || ch == 'E' || ch == Calculator.MINUS_SIGN && token.length > 0 && ('e' == token[token.length - 1] || 'E' == token[token.length - 1])
                || ch == '+' && token.length > 0 && ('e' == token[token.length - 1] || 'E' == token[token.length - 1]))
    }

    init {
        this.input = input.trim { it <= ' ' }
    }
}
