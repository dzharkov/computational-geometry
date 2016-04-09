package ru.spbau.mit.compgeom.quickhull
import ru.spbau.mit.compgeom.*

import org.junit.Test
import org.junit.Assert

class QuickHullTest {
    @Test
    fun testTriangle0() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0,0), Point(7,3), Point(1,7)
                ),
                arrayOf(
                        Point(1,7), Point(7,3), Point(0,0)
                )
        )
    }

    @Test fun testTriangle1() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0, 0), Point(7, 3), Point(1, 7)
                ),
                arrayOf(
                        Point(0, 0), Point(7, 3), Point(1, 7)
                )
        )
    }

    @Test
    fun testTriangle2() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0, 0), Point(1, -7), Point(7, -3)
                ),
                arrayOf(
                        Point(0, 0), Point(7, -3), Point(1, -7)
                )
        )
    }

    @Test fun testSquare() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0,0), Point(4,0), Point(4,4), Point(0,4)
                ),
                arrayOf(
                        Point(4,0), Point(1,2), Point(1,1), Point(1,1),
                        Point(0,0), Point(4,4), Point(0,4)
                )
        )
    }

    @Test fun testBigConvex0() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0,0), Point(9,-6), Point(12,3), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5)
                ),
                arrayOf(
                        Point(2,3), Point(4,3), Point(12,3), Point(6,6),
                        Point(7,7), Point(6,7), Point(7,6), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5), Point(9,-6),
                        Point(0,5), Point(0,0)
                )
        )
    }

    @Test
    fun testBigConvex1() {
        assertConvexHullIsCorrect(
                arrayOf(
                        Point(0,0), Point(4,-4), Point(9,-6), Point(11,-3),
                        Point(12,3), Point(11,5), Point(9,7), Point(2,8),
                        Point(0,5)
                ),
                arrayOf(
                        Point(2,3), Point(4,3), Point(12,3), Point(6,6),
                        Point(7,7), Point(6,7), Point(7,6), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5), Point(9,-6),
                        Point(0,5), Point(0,0), Point(4,-4), Point(4,-3),
                        Point(4,-4), Point(8,-1), Point(9, 0), Point(11,-3)
                )
        )
    }

    private fun assertConvexHullIsCorrect(
            expectedPoints: Array<Point>, srcPoints: Array<Point>
    ) {
        val srcPointsList = srcPoints.toList().toArrayList()
        val lastPointIndex = runQuickHull(srcPointsList)
        val resultPoints = (0..lastPointIndex - 1).map {
            srcPointsList[it]
        }.toTypedArray()

        Assert.assertArrayEquals(expectedPoints, resultPoints)

        val resultSortedPoints = srcPointsList.sorted().toTypedArray()
        val srcSorted = srcPoints.toList().toArrayList().sorted().toTypedArray()

        Assert.assertArrayEquals(srcSorted, resultSortedPoints)
    }

}
