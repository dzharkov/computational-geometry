package ru.spbau.mit.compgeom.task02
import org.junit.Assert
import org.junit.Test
import ru.spbau.mit.compgeom.Point
import java.util.*

class TripleComparator<T1 : Comparable<T1>, T2 : Comparable<T2>, T3 : Comparable<T3>> : Comparator<Triple<T1,T2,T3>> {
    override fun compare(o1: Triple<T1, T2, T3>, o2: Triple<T1, T2, T3>): Int =
            intArrayOf(o1.first.compareTo(o2.first), o1.second.compareTo(o2.second), o1.third.compareTo(o2.third)).
                    firstOrNull { it != 0 } ?: 0
}

class Task02Test {
    private fun checkTriangulation(points: Array<Point>, expectedTriangulation: Array<Triple<Int, Int, Int>>) {
        val t =  MonotonePolygon(points).buildTriangulation().map { it.toTriple() }
                        .toTypedArray()
                        .sortedWith(TripleComparator())

        Assert.assertArrayEquals(
                expectedTriangulation.sortedWith(TripleComparator()).toTypedArray(),
                t.toTypedArray()
        )
    }

    @Test fun test01() {
        checkTriangulation(
                arrayOf(
                        Point(-4,0), Point(-2,-3), Point(2,-3),
                        Point(4,0),  Point(2,3), Point(-2,3)
                ),
                arrayOf(Triple(0,1,5), Triple(1,2,5), Triple(2,4,5), Triple(2,3,4))
        )
    }

    @Test fun test02() {
        checkTriangulation(
                arrayOf(
                        Point(-2,-2), Point(2,-2), Point(2, 2), Point(-2, 2)
                ),
                arrayOf(Triple(0,1,3), Triple(1,2,3))
        )
    }

    @Test fun test03() {
        checkTriangulation(
                arrayOf(
                        Point(0,0), Point(5,-4), Point(14,-4), Point(16,-2), Point(18,-4),
                        Point(19,3), Point(20,3), Point(21,-2), Point(21,2), Point(13,4),
                        Point(10,6), Point(6,5), Point(3,7), Point(3,4), Point(2,1)
                ),
                arrayOf(
                        Triple(0,1,14), Triple(1,13,14), Triple(1,12,13), Triple(1,11,12), Triple(1,10,11),
                        Triple(1,9,10), Triple(1,2,9), Triple(2,3,9), Triple(3,4,9), Triple(4,5,9),
                        Triple(5,6,9), Triple(6,8,9), Triple(6,7,8)
                )
        )
    }
}
