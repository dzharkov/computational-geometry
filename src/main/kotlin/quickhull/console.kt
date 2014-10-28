package ru.spbau.mit.compgeom.quickhull
import ru.spbau.mit.compgeom.repeat
import java.util.Random
import ru.spbau.mit.compgeom.Point
import ru.spbau.mit.compgeom.DataReader
import java.io.PrintWriter
import ru.spbau.mit.compgeom.nextPoint
import ru.spbau.mit.compgeom.solveAll

fun consoleApp(args: Array<String>) {
    warmUp()

    if (args.size > 0 && args[0] == "random") {
        println("Random set time: " + runOnRandomSet().nanoToSeconds)
        return
    }

    solveAll(input = "input.txt", output = "output.txt", solver = ::solver)
}

fun solver(reader: DataReader, writer: PrintWriter) {
    val n = reader.nextInt()
    val points = (1..n).map { reader.nextPoint() }.toArrayList()
    val result = measureTime { runQuickHull(points) }

    writer.println(result.first)
    for (i in 0..result.first - 1) {
        writer.println(points[i])
    }

    println("Time: ${result.second.nanoToSeconds}")
}

private val random = Random(123)
private fun Random.nextCoordinate(): Int = nextInt(300000) - 150000

private fun warmUp(warmupIterations: Int = 10) {
    for (iter in 1..warmupIterations) {
        println("$iter warmup iteration: ${runOnRandomSet().nanoToSeconds}")
    }
}

fun runOnRandomSet(): Long {
    val points = (1..1000000).map { Point(random.nextCoordinate(), random.nextCoordinate()) }.toArrayList()

    val result = measureTime { runQuickHull(points) }
    if (result.first == 1234567) {
        throw AssertionError("Forced calculations")
    }

    return result.second
}


inline private fun<T> measureTime(block: () -> T): Pair<T, Long> {
    val begin = System.nanoTime()
    val result = block()
    val end = System.nanoTime()
    return Pair(result, end - begin)
}

val Long.nanoToSeconds: Double get() = this.toDouble() * 10e-9
