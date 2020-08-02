package kz.calculator.mycalculator.Controller

import kz.calculator.mycalculator.Calculator.Calculator
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.*
import java.net.URL


@RestController
class MainController {
    val query: String = "http://api.wolframalpha.com/v1/simple?appid=35W8Y9-XRJUH34ER4&i=plot"

    @GetMapping(value = ["/calculate/{function}"])
    fun calculate(@PathVariable function: String): ResponseEntity<InputStreamResource>? {
        var newFunction = "";
        if (function[0] == 'x') {
            newFunction = "+" + function;
        } else {
            newFunction = function;
        }
        var finalQuery: String = query + newFunction
        finalQuery = finalQuery.replace("!", "%2F")
        finalQuery = finalQuery.replace("+", "%2B")
        println("FinalQ:" + finalQuery)
        println(newFunction)
        var destFile = "";
        try {
            destFile = saveImage(finalQuery)
        } catch (e: IOException) {
            e.printStackTrace()
            return null;
        }
        val file = File(destFile)
        try {
            val resource = InputStreamResource(FileInputStream(file))
            val headers = HttpHeaders()
            headers.add("Content-Disposition", java.lang.String.format("attachment; filename=\"%s\"", file.name))
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
            headers.add("Pragma", "no-cache")
            headers.add("Expires", "0")
            return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    @GetMapping(value = ["/my/{from}/{to}/{function}"])
    fun myFunction(@PathVariable from: Double, @PathVariable to: Double, @PathVariable function: String): MutableList<Pair<Double, Double>> {
        var newFunction = function.replace("!", "/")
        var curr = from;
        var res = mutableListOf<Pair<Double, Double>>()
        println("${from}-${to}")
        println(newFunction)
        while (curr < to) {
            val temp = Calculator.from(newFunction.replace("x", curr.toString())) ?: break
            res.add(Pair(curr, temp.toDouble()))
            curr += 0.1;
        }
        return res
    }
}

@Throws(IOException::class)
fun saveImage(imageUrl: String?): String {
    val url = URL(imageUrl)
    val fileName = url.file
    val destName = "C:\\Users\\Askhat\\Desktop\\MyCalculator\\figures\\temp.jpg"
    println(destName)
    val `is` = url.openStream()
    val os: OutputStream = FileOutputStream(destName)
    val b = ByteArray(2048)
    var length: Int
    while (`is`.read(b).also { length = it } != -1) {
        os.write(b, 0, length)
    }
    `is`.close()
    os.close()
    return destName
}
