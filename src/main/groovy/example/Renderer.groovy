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

	static final int MAX_POINT_LIGHTS = 5, MAX_SPOT_LIGHTS = 5

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

		shaderProgram.createPointLightListUniform 'pointLights', MAX_POINT_LIGHTS
		shaderProgram.createSpotLightListUniform 'spotLights', MAX_SPOT_LIGHTS
		shaderProgram.createMaterialUniform 'material'
		shaderProgram.createDirectionalLightUniform 'directionalLight'
	}

	def clear() {
		glClear ( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT )
	}

	def render(Window window, Camera camera, List<GameItem> gameItems, Vector3f ambientLight, List<PointLight> pointLights, List<SpotLight> spotLights, DirectionalLight directionalLight) {
		clear()

		//TODO only on resize
		glViewport(0, 0, window.width, window.height)

		shaderProgram.bind()

		// Projection
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.width, window.height, Z_NEAR, Z_FAR)
		shaderProgram.setUniform('projectionMatrix', projectionMatrix)

		// view matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera)

		shaderProgram.setUniform("cameraPos", camera.position)

		//Light
		renderLights(viewMatrix, ambientLight, pointLights, spotLights, directionalLight)

		shaderProgram.setUniform('texture_sampler', 0)
		gameItems.each {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(it, viewMatrix)
			shaderProgram.setUniform('modelViewMatrix', modelViewMatrix)
			shaderProgram.setUniform('material', it.mesh.material)
			it.mesh.render()
		}

		shaderProgram.unbind()

	}

	def renderLights(Matrix4f viewMatrix, Vector3f ambientLight, List<PointLight> pointLights, List<SpotLight> spotLights, DirectionalLight directionalLight) {

		shaderProgram.setUniform("ambientLight", ambientLight)
		shaderProgram.setUniform("specularPower", specularPower)

		//Point lights
		pointLights?.eachWithIndex {
			PointLight pointLight, int i ->
				def lightCopy = pointLight.clone()
				def lightPos = lightCopy.position
				def aux = new Vector4f(lightPos, 1)
				aux.mul(viewMatrix)
				lightPos.set(aux.x, aux.y, aux.z)
				shaderProgram.setUniform('pointLights', lightCopy, i)
		}

		//Spot lights
		spotLights?.eachWithIndex {
			SpotLight spotLight, int i ->
				def lightCopy = spotLight.clone()
				def dir = new Vector4f(lightCopy.direction, 0)
				dir.mul(viewMatrix)
				lightCopy.direction.set(dir.x, dir.y, dir.z)
				def lightPos = lightCopy.position
				def aux = new Vector4f(lightPos, 1)
				aux.mul(viewMatrix)
				lightPos.set(aux.x, aux.y, aux.z)

				shaderProgram.setUniform ("spotLights", lightCopy, i)
		}


		def directionalLightCopy = directionalLight.clone()
		Vector4f dir = new Vector4f(directionalLightCopy.direction, 0)
		dir.mul (viewMatrix)
		directionalLightCopy.direction.set (dir.x, dir.y, dir.z)
		shaderProgram.setUniform ("directionalLight", directionalLightCopy)

	}

	def stop() {
		if (shaderProgram) shaderProgram.cleanup()
	}

}
