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

	String title = "Test please ignore"

	def init() {
		GLFWErrorCallback.createPrint(System.err).set() // sets the error output

		// init glfw
		if ( !glfwInit()) throw new IllegalStateException ( "GLFW could not be initialized" )

		// config for window
		glfwDefaultWindowHints()

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // invisible
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // resizable

		// Needed for OSX
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);


		window = glfwCreateWindow(width, height, title, NULL, NULL)

		if (!window) throw new RuntimeException( "Window could not be created" )

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

		GL.createCapabilities() // Binds GL to the one created in GLFW

		// enable depth
		glEnable GL_DEPTH_TEST

		// Debug show traingles
		// glPolygonMode GL_FRONT_AND_BACK, GL_LINE

		println "GLFW ${glfwGetVersionString()} \n OpenGL ${glGetString GL_VERSION}"
	}

	boolean shouldClose() {
		glfwWindowShouldClose window
	}

	@Override
	def nextRender() {
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

	@Override
	boolean isKeyPressed(int keycode) {
		glfwGetKey( window, keycode ) == GLFW_PRESS
	}

	@Override
	def setClearColor(color) {
		switch (color) {
			case Color:
				Color c = color
				glClearColor(c.red / 0xff, c.green / 0xff, c.blue / 0xff, c.alpha / 0xff)
				break
			case Number:
				Number n = color
				glClearColor((n >> 16) & 0xff, (n >> 8) & 0xff, n & 0xff, (n >> 24) & 0xff)
		}
	}
}
