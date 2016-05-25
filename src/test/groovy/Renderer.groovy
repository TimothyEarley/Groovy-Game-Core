import core.GameItem
import core.Transformation
import core.Window
import graphics.ShaderProgram

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

		shaderProgram.createUniform('projectionMatrix')
		shaderProgram.createUniform('worldMatrix')
		shaderProgram.createUniform('texture_sampler')
	}

	def clear() {
		glClear ( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT )
	}

	def render(Window window, List<GameItem> gameItems) {
		clear()
		glViewport(0, 0, window.width, window.height)

		shaderProgram.bind()

		// Projection
		shaderProgram.setUniform('projectionMatrix', transformation.getProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR))

		shaderProgram.setUniform('texture_sampler', 0)

		gameItems.each {
			shaderProgram.setUniform('worldMatrix', transformation.getWorldMatrix(it.position, it.rotation, it.scale))

			it.mesh.render()
		}

		shaderProgram.unbind()

	}

	def stop() {
		if (shaderProgram) shaderProgram.cleanup()
	}

}
