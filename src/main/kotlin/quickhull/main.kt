package ru.spbau.mit.compgeom.quickhull

import ru.spbau.mit.compgeom.Point
import java.awt.Color
import java.awt.Graphics
import java.awt.event.*
import java.util.*
import javax.swing.*

private object basicMouseListener: MouseListener {
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

private object pointMenu : JPopupMenu(""), WindowFocusListener {
    init {
        isVisible = false
        val removePointItem = JMenuItem("Remove point")
        removePointItem.addActionListener {
            quickHullCanvasHolder.removePoint()
            isVisible = false
        }
        add(removePointItem)
    }


    override fun windowGainedFocus(e: WindowEvent?) {
    }

    override fun windowLostFocus(e: WindowEvent?) {
        isVisible = false
    }
}

private object quickHullCanvasHolder: JPanel(), MouseListener by basicMouseListener {
    val points: ArrayList<Point> = arrayListOf()
    var currentCHLast: Int? = null
    var currentPointMenuLocation: Point? = null

    init {
        add(pointMenu)
        addMouseListener(this)
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

    private fun logMsg(msg: String) {
        println("================= $msg ==================")
    }

    override fun mouseClicked(e: MouseEvent) {
        pointMenu.isVisible = false
        when (e.button) {
            MouseEvent.BUTTON1 -> {
                points.add(convertToCartesian(e.x, e.y))
                repaint()
            }
            MouseEvent.BUTTON3 -> {
                pointMenu.location = e.point
                currentPointMenuLocation = convertToCartesian(e.x, e.y)
                pointMenu.isVisible = true
            }
        }
    }

    private fun convertToCartesian(x: Int, y: Int) = Point(x, height - y)

    val Point.displayX: Int get() = this.x
    val Point.displayY: Int get() = height - this.y

    override fun paintComponent(g: Graphics) {
        g.clearRect(0, 0, width, height)
        g.color = Color.BLACK
        points.forEach { p ->
            g.drawOval(p.displayX, p.displayY, 1, 1)
        }

        paintConvex(g)
    }

    fun paintConvex(g: Graphics) {
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

    fun removePoint() {
        val target = currentPointMenuLocation!!
        val found = points.firstOrNull() { target.distance(it) < 7.0 } ?: return
        points.remove(found)
        currentCHLast = null

        repaint()
    }

    fun logConfiguration() {
        for (p in points) {
            println(p)
        }
        println("currentCHLast = ${currentCHLast}")
    }

    fun dump() {
        logMsg("Dump")
        logConfiguration()
    }
}

private fun buildMenuUtem(name: String, listener: (ActionEvent) -> Unit): JMenuItem {
    val x = JMenuItem(name)
    x.addActionListener(listener)
    return x
}

private object menuBar : JMenuBar() {
    init {
        add(buildMenuUtem("Build CH") { quickHullCanvasHolder.runQuickHull() })
        add(buildMenuUtem("Clear") { quickHullCanvasHolder.clearAll() })
        add(buildMenuUtem("Dump") { quickHullCanvasHolder.dump() })
    }
}

fun main(args: Array<String>) {
    if (args.size > 0 && args[0] == "console") {
        consoleApp(args.copyOfRange(1, args.size))
        return
    }

    val frame = JFrame("Quick Hull")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(quickHullCanvasHolder)
    frame.addWindowFocusListener(pointMenu)
    frame.setSize(1200, 600)
    frame.isResizable = false
    frame.isVisible = true
    frame.jMenuBar = menuBar
}
