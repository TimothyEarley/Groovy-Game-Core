package example

import core.GameItem
import core.Window
import graphics.Camera
import graphics.ShaderProgram
import graphics.Transformation
import org.joml.Matrix4f

import static org.lwjgl.opengl.GL11.*

/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class Renderer {

	static final float FOV = Math.toRadians(60)
	static final float Z_NEAR = 0.01f, Z_FAR = 1000f


	private Transformation transformation

	private ShaderProgram shaderProgram

	def init(Window window) {

		shaderProgram = new ShaderProgram()
		shaderProgram.createVertexShader(this.class.getResource("/vertex.vs").text) // TODO create utility
		shaderProgram.createFragmentShader(this.class.getResource("/fragment.fs").text)
		shaderProgram.link()

		transformation = new Transformation()

		shaderProgram.createUniforms ([
			'projectionMatrix',
			'modelViewMatrix',
			'texture_sampler',
			'colour',
			'useColour',
		])
	}

	def clear() {
		glClear ( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT )
	}

	def render(Window window, Camera camera, List<GameItem> gameItems) {
		clear()
		glViewport(0, 0, window.width, window.height)

		shaderProgram.bind()

		// Projection
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR)
		shaderProgram.setUniform('projectionMatrix', projectionMatrix)

		// view matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera)

		shaderProgram.setUniform('texture_sampler', 0)

		gameItems.each {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(it, viewMatrix)
			shaderProgram.setUniform('modelViewMatrix', modelViewMatrix)

			// texture or colour
			shaderProgram.setUniform('colour', it.mesh.colour)
			shaderProgram.setUniform('useColour', it.mesh.isTextured() ? 0 : 1)


			it.mesh.render()
		}

		shaderProgram.unbind()

	}

	def stop() {
		if (shaderProgram) shaderProgram.cleanup()
	}

}
