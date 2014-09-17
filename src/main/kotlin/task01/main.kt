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
    val minX = Math.min(p1.x, p2.x)
    val maxX = Math.max(p1.x, p2.x)
    val isParallelToOX = p1.y == p2.y

    val top = if (p1.y > p2.y) p1 else p2
    val bottom = if (p1.y > p2.y) p2 else p1

    fun includeY(y: Int) = y in bottom.y..top.y

    /**
     * returns cross product of vectors: (top - bottom), (p - bottom)
     */
    fun crossProductSign(p: Point) : Int = (top - bottom).crossProduct(p - bottom).sign()

    fun containsPoint(p: Point) = includeY(p.y) &&
            minX <= p.x && p.x <= maxX &&
            crossProductSign(p) == 0
}

class Polygon(val points : Array<Point>) {
    val segments = points.indices.map { i -> Segment(points[i], points.getCyclic(i + 1)) }

    fun isPointInside(p: Point) : Boolean {
        return segments.count {
            if (it.containsPoint(p)) return @isPointInside true
            isSegmentIntersectedByRay(it, p)
        } % 2 != 0
    }

    fun isSegmentIntersectedByRay(segment: Segment, p: Point): Boolean {
        if (segment.isParallelToOX) {
            return false
        }

        if (segment.top.y == p.y) {
            return segment.top.x < p.x
        }

        if (segment.bottom.y == p.y) {
            // if ray intersects border point it does so for two of segments
            // so we should count it only for first of them
            return false
        }

        val isPointByTheRightSideOfSegment: Boolean = segment.crossProductSign(p) < 0

        return segment.includeY(p.y) && isPointByTheRightSideOfSegment
    }
}

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
