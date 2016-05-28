package core

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.glfw.GLFWMouseButtonCallbackI

/**
 * Created 28/05/16
 * @author Timothy Earley
 */
class MouseInput {

	final Vector2d prevPos = new Vector2d(-1, -1), curPos = new Vector2d()

	final Vector2f displVec = new Vector2f()

	boolean inWindow

	Map<Integer, Boolean> btnPressed = [:]

	void init(Window window) {

		GLFW.glfwSetMouseButtonCallback window.window, {
			_window, int button, int action, int mods ->
				btnPressed[button] = action == GLFW.GLFW_PRESS
		} as GLFWMouseButtonCallbackI

			GLFW.glfwSetCursorEnterCallback window.window, {
			_window, boolean entered ->
				inWindow = entered
		}

		GLFW.glfwSetCursorPosCallback window.window, {
			_window, double xpos, double ypos ->
				curPos.x = xpos
				curPos.y = ypos
		} as GLFWCursorPosCallbackI
	}

	void input(Window window) {

		displVec.x = displVec.y = 0

		if (prevPos.x > 0 && prevPos.y > 0 && inWindow) {

			def deltaX = curPos.x - prevPos.x
			def deltaY = curPos.y - prevPos.y

			if (deltaX) displVec.y = deltaX
			if (deltaY) displVec.x = deltaY
		}

		prevPos.x = curPos.x
		prevPos.y = curPos.y

	}
}
