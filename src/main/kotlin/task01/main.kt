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
    fun crossProductSign(p: Point) : Int = (top - bottom).crossProductSign(p - bottom)

    fun containsPoint(p: Point) = includeY(p.y) &&
            minX <= p.x && p.x <= maxX &&
            crossProductSign(p) == 0
}

class Polygon(val points : Array<Point>) {
    val segments = points.indices.map { i -> Segment(points[i], points.getCyclic(i + 1)) }

    fun isPointInside(p: Point) : Boolean {
        return segments.count {
            if (it.containsPoint(p)) return@isPointInside true
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

