package ru.spbau.mit.compgeom.task01
import org.junit.Test as test
import org.junit.Assert
import ru.spbau.mit.compgeom.*

class Task01Test {
    test fun funcTest1() {
        val polygon = Polygon(array(
            Point(-2,-4),Point(7,0),Point(9,-4),Point(9,2),Point(12,5),
            Point(9,8),Point(7,4),Point(5,4),Point(4,7),Point(3,5),Point(2,5),
            Point(1,5),Point(0,5),Point(0,0),Point(2,0),Point(4,0)
        ))

        val pointsToCheck = array(
                // inside
                Point(10,4),Point(4,-1),Point(4,5),Point(1,3),Point(9,5),
                Point(6,0),Point(8,-1),

                // on edge
                Point(10, 7),Point(11, 6),Point(0,3),

                // outside
                Point(1,7),Point(6,7),Point(6,5),Point(16,5),Point(10,0),Point(10,-2),Point(6,-3),Point(1,-1)
        )

        val result = pointsToCheck.map { polygon.isPointInside(it) }.copyToArray()

        Assert.assertArrayEquals(
                array(
                        true, true, true, true, true, true, true, true, true, true,
                        false, false, false, false, false, false, false, false
                ),
                result
        )
    }

    test fun funcTest2() {
        val polygon = Polygon(array(
                Point(0, 0), Point(2, 0), Point(0, 2)
        ))

        val result = array(Point(1, 0), Point(1,1), Point(0, 1)).map { polygon.isPointInside(it) }.copyToArray()

        Assert.assertArrayEquals(array(true, true, true), result)
    }
}
