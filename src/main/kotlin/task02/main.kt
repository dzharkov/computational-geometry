package ru.spbau.mit.compgeom.task02

import java.io.PrintWriter
import ru.spbau.mit.compgeom.*
import kotlin.support.AbstractIterator
import kotlin.properties.Delegates
import java.util.LinkedList
import java.util.Stack
import java.util.ArrayList

fun main(args: Array<String>) {
    solveAllStdin(solver = ::solver)
}

fun solver(reader: DataReader, writer: PrintWriter) {
    val polygonSize = reader.nextInt()
    val polygon = MonotonePolygon(
            Array(polygonSize) { reader.nextPoint() }
    )
    val triangulation = polygon.buildTriangulation()
    assert(checkTriangulationIsCorrect(polygon, triangulation))

    for (t in triangulation) {
        writer.println(t.toTriple())
    }
}

class MonotonePolygon(val points: Array<Point>) {
    {
        assert(points.indices.minBy { points[it] } == 0)
    }

    inner class Triangle(val a1Index: Int, val a2Index: Int, val a3Index: Int) {
        fun doubledArea(): Long = (points[a2Index] - points[a1Index]).
                                    crossProduct(points[a3Index] - points[a1Index])

        fun toTriple() = Triple(a1Index, a2Index, a3Index)
    }

    private val rightIndex: Int by Delegates.lazy {
        points.indices.maxBy { points[it] }!!
    }

    private fun buildSortedIterator() : IntIterator = object : IntIterator() {
        var topIndex = points.lastIndex
        var bottomIndex = 0

        override fun hasNext() = topIndex >= bottomIndex
        override fun nextInt() =
            if (points[topIndex] < points[bottomIndex]) {
                topIndex--
            } else {
                bottomIndex++
            }
    }

    fun buildTriangulation(): List<Triangle> {
        val result = arrayListOf<Triangle>()
        val stack = arrayListOf<Int>()
        val iterator = buildSortedIterator()
        stack.push(iterator.nextInt())
        stack.push(iterator.nextInt())

        for (i in iterator) {
            val currentPoint = points[i]
            if ((i - rightIndex).sign() == (stack.top() - rightIndex).sign()) {
                // in the same chain
                while (stack.size > 1) {
                    val topPoint = points[stack.top()]
                    val prevPoint = points[stack.second()]

                    if ((topPoint - prevPoint).crossProductSign(currentPoint - topPoint) * (rightIndex - i) > 0) {
                        // is convex
                        result.add(buildTriangle(i, stack.top(), stack.second()))
                        stack.pop()
                    } else {
                        break
                    }
                }

                stack.push(i)
            } else {
                // different chains or i is the rightmost point
                val topIndex = stack.top()
                for (j in 0..stack.lastIndex - 1) {
                    result.add(buildTriangle(stack[j], stack[j+1], i))
                }
                stack.clear()
                stack.addAll(listOf(topIndex, i))
            }
        }

        return result
    }

    fun doubledArea(): Long {
        var result = 0L
        for ((p1, p2) in points.zipWithCyclicTail()) {
            result += (p1.y + p2.y).toLong() * (p2.x - p1.x).toLong()
        }
        return result
    }

    private fun buildTriangle(a: Int, b: Int, c: Int): Triangle {
        val x = intArray(a, b, c).toSortedList()
        return Triangle(x[0], x[1], x[2])
    }
}

fun checkTriangulationIsCorrect(polygon: MonotonePolygon, triangulation: List<MonotonePolygon.Triangle>): Boolean {
    var trianglesDoubledArea = triangulation.fold(0L) { x, y -> x + y.doubledArea() }
    return triangulation.size == polygon.points.size - 2 &&
            polygon.doubledArea() == trianglesDoubledArea
}

fun <T> ArrayList<T>.push(el: T) = add(el)
fun <T> ArrayList<T>.top(): T = last!!
fun <T> ArrayList<T>.second(): T = get(lastIndex - 1)
fun <T> ArrayList<T>.pop(): T = remove(lastIndex)


