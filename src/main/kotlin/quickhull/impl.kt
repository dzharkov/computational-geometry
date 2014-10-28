package ru.spbau.mit.compgeom.quickhull

import ru.spbau.mit.compgeom.Point
import ru.spbau.mit.compgeom.Line
import kotlin.properties.Delegates
import java.util.ArrayList

fun runQuickHull(points: ArrayList<Point>): Int {
    val leftmostPoint = points.min()!!
    val rightmostPoint = points.max()!!

    return points.findHalvesHullsAndMerge(leftmostPoint, rightmostPoint, leftmostPoint, dstPos = 0)
}

private fun ArrayList<Point>.findHalvesHullsAndMerge(
        p1: Point, p2: Point, p3: Point,
        first: Int = 0, last: Int = size,
        dstPos: Int
): Int {
    val pointsOfInnerSemiplanePosFirst = partitionByVector(p1, p2, first, last)
    val pointsOfInnerSemiplanePosSecond = partitionByVector(p2, p3, pointsOfInnerSemiplanePosFirst, last)

    val firstRes = findQuickHullHalf(
            p1, p2,
            first, last = pointsOfInnerSemiplanePosFirst, dstPos = dstPos
    )

    val secondRes = findQuickHullHalf(
            p2, p3,
            first = pointsOfInnerSemiplanePosFirst, last = pointsOfInnerSemiplanePosSecond, dstPos = firstRes
    )

    return secondRes
}

private fun ArrayList<Point>.findQuickHullHalf(p1: Point, p2: Point, first: Int, last: Int, dstPos: Int): Int {
    if (first + 1 == last) {
        swap(dstPos, first)
        return dstPos + 1
    }
    assert(first < last)

    val borderLine = Line(p1, p2)
    val maxIndex = (first..last - 1).maxBy { borderLine.relativeDistance(this[it]) }!!
    val maxPoint = this[maxIndex]

    return findHalvesHullsAndMerge(p1, maxPoint, p2, first, last, dstPos)
}

private fun ArrayList<Point>.partitionByVector(p1: Point, p2: Point, first: Int, last: Int): Int {
    val borderVector = p2 - p1

    return partitionInPlaced(first, last) {
        borderVector.crossProductSign(it - p1)  == -1 || it identityEquals p1
    }
}

inline private fun<T> ArrayList<T>.partitionInPlaced(first: Int, last: Int, predicate: (T) -> Boolean): Int {
    var i = first
    var j = last - 1

    while (i <= j) {
        if (predicate(this[i])) {
            i++
        } else {
            swap(i, j)
            j--
        }
    }

    return i
}

private fun<T> ArrayList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}
