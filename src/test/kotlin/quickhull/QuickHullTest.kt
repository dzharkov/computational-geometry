package ru.spbau.mit.compgeom.quickhull
import ru.spbau.mit.compgeom.*

import org.junit.Test as test
import org.junit.Assert

class QuickHullTest {
    private fun assertConvexHullIsCorrect(
        expectedPoints: Array<Point>, srcPoints: Array<Point>
    ) {
        val srcPointsList = srcPoints.toArrayList()
        val lastPointIndex = runQuickHull(srcPointsList)
        val resultPoints = (0..lastPointIndex - 1).map {
            srcPointsList[it]
        }.copyToArray()

        Assert.assertArrayEquals(expectedPoints, resultPoints)

        val resultSortedPoints = srcPointsList.sort().copyToArray()
        val srcSorted = srcPoints.toArrayList().sort().copyToArray()

        Assert.assertArrayEquals(srcSorted, resultSortedPoints)
    }

    test fun testTriangle0() {
        assertConvexHullIsCorrect(
                array(
                        Point(0,0), Point(7,3), Point(1,7)
                ),
                array(
                        Point(1,7), Point(7,3), Point(0,0)
                )
        )
    }

    test fun testTriangle1() {
        assertConvexHullIsCorrect(
                array(
                        Point(0, 0), Point(7, 3), Point(1, 7)
                ),
                array(
                        Point(0, 0), Point(7, 3), Point(1, 7)
                )
        )
    }

    test fun testTriangle2() {
        assertConvexHullIsCorrect(
                array(
                        Point(0, 0), Point(1, -7), Point(7, -3)
                ),
                array(
                        Point(0, 0), Point(7, -3), Point(1, -7)
                )
        )
    }

    test fun testSquare() {
        assertConvexHullIsCorrect(
                array(
                        Point(0,0), Point(4,0), Point(4,4), Point(0,4)
                ),
                array(
                        Point(4,0), Point(1,2), Point(1,1), Point(1,1),
                        Point(0,0), Point(4,4), Point(0,4)
                )
        )
    }

    test fun testBigConvex0() {
        assertConvexHullIsCorrect(
                array(
                        Point(0,0), Point(9,-6), Point(12,3), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5)
                ),
                array(
                        Point(2,3), Point(4,3), Point(12,3), Point(6,6),
                        Point(7,7), Point(6,7), Point(7,6), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5), Point(9,-6),
                        Point(0,5), Point(0,0)
                )
        )
    }
    test fun testBigConvex1() {
        assertConvexHullIsCorrect(
                array(
                        Point(0,0), Point(4,-4), Point(9,-6), Point(11,-3),
                        Point(12,3), Point(11,5), Point(9,7), Point(2,8),
                        Point(0,5)
                ),
                array(
                        Point(2,3), Point(4,3), Point(12,3), Point(6,6),
                        Point(7,7), Point(6,7), Point(7,6), Point(11,5),
                        Point(9,7), Point(2,8), Point(0,5), Point(9,-6),
                        Point(0,5), Point(0,0), Point(4,-4), Point(4,-3),
                        Point(4,-4), Point(8,-1), Point(9, 0), Point(11,-3)
                )
        )
    }
}
