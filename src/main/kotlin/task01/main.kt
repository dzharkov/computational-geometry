package ru.spbau.mit.compgeom.task01

import java.io.PrintWriter
import ru.spbau.mit.compgeom.*

fun main(args: Array<String>) {
    solveAllStdin(solver = ::solver)
}

fun solver(reader: DataReader, writer: PrintWriter) {
    val polygonSize = reader.nextInt()
    val polygon = Polygon(
            Array(polygonSize) { reader.nextPoint() }
    )

    val queriesCount = reader.nextInt()
    repeat(queriesCount) {
        val p = reader.nextPoint()
        writer.println(if (polygon.isPointInside(p)) "yes" else "no")
    }
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point): Int {
        if (x != other.x) return x.compareTo(other.x)
        return y.compareTo(other.y)
    }

    fun minus(other: Point) = Vector(x - other.x, y - other.y)
}

data class Vector(val x: Int, val y: Int) {
    fun crossProduct(other: Vector) : Long = x.toLong() * other.y.toLong() - y.toLong() * other.x.toLong()
}

data class Segment(val p1: Point, val p2: Point) {
    val minX : Int get() = Math.min(p1.x, p2.x)
    val maxX : Int get() = Math.max(p1.x, p2.x)
    val isParallelToOX : Boolean get() = p1.y == p2.y

    fun includeY(y: Int) = Math.min(p1.y, p2.y) <= y && y <= Math.max(p1.y, p2.y)

    /**
     * returns cross product of vectors: (p2 - p1), (p - p1)
     */
    fun crossProductSign(p: Point) : Int = (p2 - p1).crossProduct(p - p1).sign()

    fun containsPoint(p: Point) = includeY(p.y) &&
            minX <= p.x && p.x <= maxX &&
            crossProductSign(p) == 0
}

class Polygon(pointsUnshifted: Array<Point>) {
    val points : Array<Point>
    val mergedSegments: MutableList<Segment> = arrayListOf();

    {
        val minIndex = pointsUnshifted.indices.minBy { i -> pointsUnshifted[i] }!!

        points = Array(pointsUnshifted.size) {
            i -> pointsUnshifted.getCyclic(minIndex + i)
        }

        var i = 0
        while (i < points.lastIndex) {
            var j = i + 1

            if (points[i].y == points[i+1].y) {
                while (j + 1 <= points.lastIndex && points[i].y == points[j + 1].y) {
                    j++
                }
            }

            mergedSegments.add(Segment(points[i], points[j]))
            i = j
        }

        mergedSegments.add(Segment(points.last(), points.first()))
    }

    fun isPointInside(p: Point) = mergedSegments.any { s -> s.containsPoint(p) } ||
            (mergedSegments.indices.count { isSegmentIntersected(p, it) }) % 2 != 0

    fun isSegmentIntersected(p: Point, segmentIndex: Int) : Boolean {
        val segment = mergedSegments[segmentIndex]
        val nextSegment = mergedSegments.getCyclic(segmentIndex + 1)
        val prevSegment = mergedSegments.getCyclic(segmentIndex - 1)

        // trace horizontal beam to the left

        if (segment.isParallelToOX) {
            if (segment.p1.y == p.y && segment.minX <= p.x) {
                return belongToDifferentHorizontalSemiplanes(nextSegment, prevSegment, p.y)
            }
            return false
        }

        if (segment.p1.y == p.y) {
            return segment.p1.x <= p.x && belongToDifferentHorizontalSemiplanes(segment, prevSegment, p.y)
        }

        if (segment.p2.y == p.y) {
            // if beam intersects border point it does so for two of segments
            // so we should count it only for first of them
            return false
        }

        val isPointByTheRightSideOfSegment: Boolean = segment.crossProductSign(p) * (segment.p2.y - segment.p1.y) < 0

        return segment.includeY(p.y)
                && segment.minX <= p.x
                && isPointByTheRightSideOfSegment
    }
}

fun belongToDifferentHorizontalSemiplanes(a: Segment, b: Segment,  y: Int) : Boolean {
    val deltas = listOf(a.p1.y, a.p2.y, b.p1.y, b.p2.y).map { it - y }.sort()

    return deltas.first!! < 0 && deltas.last!! > 0
}

fun <T> MutableList<T>.getCyclic(i: Int) = this[(i + this.size) % this.size]
fun <T> Array<T>.getCyclic(i: Int) = this[(i + this.size) % this.size]
fun Long.sign() : Int {
    if (this > 0L) return 1
    if (this < 0L) return -1
    return 0
}

fun DataReader.nextPoint() : Point {
    val token = this.nextLine()
    val splitted = token.substring(1, token.length - 1).split("\\s*,\\s*")
    return Point(splitted[0].toInt(), splitted[1].toInt())
}
