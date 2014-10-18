package ru.spbau.mit.compgeom.quickhull

import ru.spbau.mit.compgeom.Point
import java.awt.event.MouseListener
import java.awt.event.MouseEvent
import javax.swing.JPanel
import java.awt.Graphics
import java.awt.Color
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.awt.event.WindowFocusListener
import java.awt.event.WindowEvent
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JFrame
import java.util.ArrayList
import java.awt.event.ActionEvent

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
    {
        setVisible(false)
        val removePointItem = JMenuItem("Remove point")
        removePointItem.addActionListener {
            quickHullCanvasHolder.removePoint()
            setVisible(false)
        }
        add(removePointItem)
    }


    override fun windowGainedFocus(e: WindowEvent?) {
    }

    override fun windowLostFocus(e: WindowEvent?) {
        setVisible(false)
    }
}

private object quickHullCanvasHolder: JPanel(), MouseListener by basicMouseListener {
    val points: ArrayList<Point> = arrayListOf()
    var currentCHLast: Int? = null
    var currentPointMenuLocation: Point? = null

    {
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
        pointMenu.setVisible(false)
        when (e.getButton()) {
            MouseEvent.BUTTON1 -> {
                points.add(convertToCartesian(e.getX(), e.getY()))
                repaint()
            }
            MouseEvent.BUTTON3 -> {
                pointMenu.setLocation(e.getPoint())
                currentPointMenuLocation = convertToCartesian(e.getX(), e.getY())
                pointMenu.setVisible(true)
            }
        }
    }

    private fun convertToCartesian(x: Int, y: Int) = Point(x, getHeight() - y)

    val Point.displayX: Int get() = this.x
    val Point.displayY: Int get() = getHeight() - this.y

    override fun paintComponent(g: Graphics) {
        g.clearRect(0, 0, getWidth(), getHeight())
        g.setColor(Color.BLACK)
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
    {
        add(buildMenuUtem("Build CH") { quickHullCanvasHolder.runQuickHull() })
        add(buildMenuUtem("Clear") { quickHullCanvasHolder.clearAll() })
        add(buildMenuUtem("Dump") { quickHullCanvasHolder.dump() })
    }
}

fun main(args: Array<String>) {
    val frame = JFrame("Quick Hull")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.add(quickHullCanvasHolder)
    frame.addWindowFocusListener(pointMenu)
    frame.setSize(1200, 600)
    frame.setResizable(false)
    frame.setVisible(true)
    frame.setJMenuBar(menuBar)
}
