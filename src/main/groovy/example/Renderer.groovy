package example

import core.GameItem
import core.Window
import graphics.*
import graphics.lights.*
import org.joml.*

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

	private float specularPower = 10

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
			'cameraPos',
			'specularPower',
			'ambientLight'
		])

		shaderProgram.createPointLightUniform 'pointLight'
		shaderProgram.createMaterialUniform 'material'
		shaderProgram.createDirectionalLightUniform 'directionalLight'
	}

	def clear() {
		glClear ( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT )
	}

	def render(Window window, Camera camera, List<GameItem> gameItems, Vector3f ambientLight, PointLight pointLight, DirectionalLight directionalLight) {
		clear()

		//TODO only on resize
		glViewport(0, 0, window.width, window.height)

		shaderProgram.bind()

		// Projection
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR)
		shaderProgram.setUniform('projectionMatrix', projectionMatrix)

		// view matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera)

		//Light
		shaderProgram.setUniform("cameraPos", camera.position)
		shaderProgram.setUniform("ambientLight", ambientLight)
		shaderProgram.setUniform("specularPower", specularPower)
		shaderProgram.setUniform('texture_sampler', 0)
		def lightCopy = pointLight.clone()
		def lightPos = lightCopy.position
		def aux = new Vector4f(lightPos, 1)
		aux.mul(viewMatrix)
		lightPos.x = aux.x
		lightPos.y = aux.y
		lightPos.z = aux.z
		shaderProgram.setUniform('pointLight', lightCopy)
		def directionalLightCopy = directionalLight.clone()
		Vector4f dir = new Vector4f(directionalLightCopy.direction, 0)
		dir.mul (viewMatrix)
		directionalLightCopy.direction.set (dir.x, dir.y, dir.z)
		shaderProgram.setUniform ("directionalLight", directionalLightCopy)

		gameItems.each {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(it, viewMatrix)
			shaderProgram.setUniform('modelViewMatrix', modelViewMatrix)
			shaderProgram.setUniform('material', it.mesh.material)
			it.mesh.render()
		}

		shaderProgram.unbind()

	}

	def stop() {
		if (shaderProgram) shaderProgram.cleanup()
	}

}
