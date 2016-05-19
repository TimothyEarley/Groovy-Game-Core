package core

import core.Window
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI
import org.lwjgl.opengl.GL

import java.awt.*

import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.system.MemoryUtil.NULL
/**
 *
 * Handles GL (GLFW)
 *
 * Created 19/05/16
 * @author Timothy Earley
 */
class GLWindow implements Window {

	long window

	def bgColor = Color.BLACK

	String title = "Test please ignore"

	def init() {
		GLFWErrorCallback.createPrint(System.err).set() // sets the error output

		// init glfw
		if ( !glfwInit()) throw new IllegalStateException ( "GLFW could not be initialized" )

		// config for window
		glfwDefaultWindowHints()

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // invisible
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // resizable

		window = glfwCreateWindow(width, height, title, NULL, NULL)

		if (!window) throw new RuntimeException( "Window could not be created" )

		// Key listener
		glfwSetKeyCallback window, new Input()

		// Monitor resolution
		def vidmode = glfwGetVideoMode glfwGetPrimaryMonitor()
		// center
		glfwSetWindowPos (
				window,
				(vidmode.width() - width) / 2 as int,
				(vidmode.height() - height) / 2 as int
		)

		// Make the context current
		glfwMakeContextCurrent window

		// enable vsync
		glfwSwapInterval 1

		// set size callback
		glfwSetFramebufferSizeCallback(window, {
			window, width, height ->
				this.width = width
				this.height = height
		} as GLFWFramebufferSizeCallbackI )

		// make visible
		glfwShowWindow window

		// GL stuff
		GL.createCapabilities() // Binds GL to the one created in GLFW
		glClearColor(bgColor.red / 0xff, bgColor.green / 0xff, bgColor.blue / 0xff, bgColor.alpha / 0xff)

	}

	boolean shouldClose() {
		glfwWindowShouldClose window
	}

	@Override
	def nextRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
		glfwSwapBuffers window
	}

	def stop() {
		Callbacks.glfwFreeCallbacks window
		glfwDestroyWindow window
	}

	def poll() {
		glfwPollEvents()
	}

	def terminate() {
		glfwTerminate()
		glfwSetErrorCallback(null).free()
	}

}
