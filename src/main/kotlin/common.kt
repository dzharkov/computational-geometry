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
