package core

import org.lwjgl.glfw.GLFWKeyCallback

import static org.lwjgl.glfw.GLFW.*

/**
 * Created 19/05/16
 * @author Timothy Earley
 */
class Input extends GLFWKeyCallback {

	void invoke(long window, int key, int scancode, int action, int mods) {
		//TODO pass/store the input
		println "$window, $key, $scancode, $action, $mods"
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose window, true // detected in main loop
		}
	}

}
