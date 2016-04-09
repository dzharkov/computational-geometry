package ru.spbau.mit.compgeom.quickhull

import ru.spbau.mit.compgeom.Point
import java.awt.Color
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.*
import javax.swing.*
import java.awt.Point as awtPoint

fun main(args: Array<String>) {
    if (args.size > 0 && args[0] == "console") {
        consoleApp(args.copyOfRange(1, args.size))
        return
    }

    val frame = JFrame("Quick Hull")
    val quickHullCanvasHolder = QuickHullCanvas()

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(quickHullCanvasHolder)
    frame.setSize(1200, 600)
    frame.isResizable = false
    frame.isVisible = true

    frame.jMenuBar = JMenuBar().apply {
        add(buildMenuItem("Build CH") { quickHullCanvasHolder.runQuickHull() })
        add(buildMenuItem("Clear") { quickHullCanvasHolder.clearAll() })
        add(buildMenuItem("Dump") { quickHullCanvasHolder.dump() })
    }
}

private fun buildMenuItem(name: String, listener: (ActionEvent) -> Unit): JMenuItem {
    val x = JMenuItem(name)
    x.addActionListener(listener)
    return x
}

private class QuickHullCanvas : JPanel(), MouseListener by DoNothingMouseListener {
    private val points: ArrayList<Point> = arrayListOf()
    private var currentCHLast: Int? = null
    private var pointMenuLastLocation = Point(0, 0)
    private val pointMenu = JPopupMenu()

    init {
        addMouseListener(this)

        with(pointMenu) menu@{
            add(JMenuItem("Remove point").apply {
                addActionListener {
                    removePoint(pointMenuLastLocation)
                    this@menu.isVisible = false
                }
            })
        }
    }

    fun runQuickHull() {
        logMsg("before QH")
        logConfiguration()
        currentCHLast = runQuickHull(points)
        logMsg("after QH")
        logConfiguration()
        repaint()
    }

    fun clearAll() {
        logMsg("Clearing")
        logConfiguration()
        points.clear()
        currentCHLast = null
        repaint()
    }

    fun dump() {
        logMsg("Dump")
        logConfiguration()
    }

    override fun mouseClicked(e: MouseEvent) {
        pointMenu.isVisible = false
        when (e.button) {
            MouseEvent.BUTTON1 -> {
                points.add(e.locationOnScreen.toCartesian())
                repaint()
            }
            MouseEvent.BUTTON3 -> {
                pointMenuLastLocation = e.point.toCartesian()
                pointMenu.location = e.point
                pointMenu.isVisible = true
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        g.clearRect(0, 0, width, height)
        g.color = Color.BLACK
        points.forEach { p ->
            g.drawOval(p.displayX, p.displayY, 1, 1)
        }

        paintConvex(g)
    }

    private fun logMsg(msg: String) {
        println("================= $msg ==================")
    }

    private fun awtPoint.toCartesian() = convertToCartesian(x, y)
    private fun convertToCartesian(x: Int, y: Int) = Point(x, height - y)

    private val Point.displayX: Int get() = this.x
    private val Point.displayY: Int get() = height - this.y

    private fun paintConvex(g: Graphics) {
        val currentLast = currentCHLast
        if (currentLast != null) {
            for (i in 0..currentLast - 1) {
                g.drawLine(
                        points[i].displayX, points[i].displayY,
                        points[(i + 1) % currentLast].displayX, points[(i + 1) % currentLast].displayY
                )
            }
        }
    }

    private fun removePoint(location: Point) {
        val found = points.firstOrNull() { location.distance(it) < 7.0 } ?: return
        points.remove(found)
        currentCHLast = null

        repaint()
    }

    private fun logConfiguration() {
        for (p in points) {
            println(p)
        }
        println("currentCHLast = $currentCHLast")
    }
}

private object DoNothingMouseListener : MouseListener {
    override fun mouseClicked(e: MouseEvent) {
    }

    override fun mousePressed(e: MouseEvent) {
    }

    override fun mouseReleased(e: MouseEvent) {
    }

    override fun mouseEntered(e: MouseEvent) {
    }

    override fun mouseExited(e: MouseEvent) {
    }
}
