package ru.spbau.mit.compgeom

import java.io.FileReader
import java.io.StreamTokenizer
import java.util.StringTokenizer
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.Reader

inline fun repeat(n: Int, block: () -> Unit) {
    for (_ in 1..n)
        block()
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point): Int {
        if (x != other.x) return x.compareTo(other.x)
        return y.compareTo(other.y)
    }

    fun minus(other: Point) = Vector(x - other.x, y - other.y)
    override fun toString() = "($x, $y)"

    fun distance(it: Point): Double = Math.sqrt(Math.pow((it.x - x).toDouble(), 2.0) + Math.pow((it.y - y).toDouble(), 2.0))
}

data class Vector(val x: Int, val y: Int) {
    fun crossProduct(other: Vector): Long = x.toLong() * other.y.toLong() - y.toLong() * other.x.toLong()
    fun crossProductSign(other: Vector): Int = crossProduct(other).sign()
}

data class Line(p1: Point, p2: Point) {
    val a = (p1.y - p2.y).toLong()
    val b = (p2.x - p1.x).toLong()
    val c = - (a * p1.x.toLong() + b * p1.y.toLong())

    fun relativeDistance(p: Point): Long = Math.abs(a * p.x.toLong() + b * p.y.toLong() + c)
}

class DataReader(srcReader: Reader) {
    private val reader = BufferedReader(srcReader)

    var st : StringTokenizer? = null
    fun next() : String? {
        while (st == null || !st!!.hasMoreTokens()) {
            val s = reader.readLine()
            if (s == null)
                return null
            st = StringTokenizer(s)
        }

        return st?.nextToken()
    }

    fun nextLine() = reader.readLine()!!

    fun nextToken() : String {
        return next()!!
    }

    fun nextInt() = nextToken().toInt()
    fun nextLong() = nextToken().toLong()
}

fun solveAll(reader: DataReader, writer: PrintWriter, solver: (DataReader, PrintWriter) -> Unit) {
    solver(reader, writer)

    writer.close()
}

fun solveAll(input: String = "input.txt", output: String = "output.txt", solver: (DataReader, PrintWriter) -> Unit) {
    solveAll(
            DataReader(FileReader(input)),
            PrintWriter(output),
            solver
    )
}

fun solveAllStdin(solver: (DataReader, PrintWriter) -> Unit) {
    solveAll(
            DataReader(InputStreamReader(System.`in`)),
            PrintWriter(System.out),
            solver
    )
}

fun <T> Array<T>.getCyclic(i: Int) = this[(i + this.size) % this.size]
fun <T> Array<T>.zipWithCyclicTail() : Stream<Pair<T, T>> =
        this.stream().zip((1..lastIndex + 1).stream().map { getCyclic(it) })

fun Long.sign() = when {
    this > 0L -> 1
    this < 0L -> -1
    else -> 0
}

fun Int.sign() = when {
    this > 0 -> 1
    this < 0 -> -1
    else -> 0
}

fun DataReader.nextPoint() : Point {
    val token = this.nextLine()
    val splitted = token.substring(1, token.length - 1).split("\\s*,\\s*")
    return Point(splitted[0].toInt(), splitted[1].toInt())
}
