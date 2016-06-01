package graphics

import graphics.lights.*
import org.joml.*
import org.lwjgl.BufferUtils

import java.nio.FloatBuffer

import static org.lwjgl.opengl.GL20.*
/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class ShaderProgram {

	private int programID, vertexShaderID, fragmentShaderID

	final Map<String, Integer> uniforms

	ShaderProgram() {

		programID = glCreateProgram()

		if (!programID) {
			throw new Exception("Could not create Shader")
		}

		uniforms = [:]

	}

	void createVertexShader(String shaderCode) {
		vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER)
	}

	void createFragmentShader(String shaderCode) {
		fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER)
	}

	private createShader(String shaderSource, int type) {
		int shaderID = glCreateShader(type)

		if (!shaderID) {
			throw new Exception("Could not create $type shader: $shaderSource")
		}

		glShaderSource shaderID, shaderSource
		glCompileShader shaderID

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling shader, error code: ${glGetShaderInfoLog(shaderID, 1024)}")
		}

		glAttachShader programID, shaderID

		return shaderID
	}

	void link() {

		glLinkProgram programID
		if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: ${glGetShaderInfoLog(programID, 1024)}")
		}

		glValidateProgram programID
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: ${glGetShaderInfoLog(programID, 1024)}") //TODO replace with log
		}

	}


	void bind() {
		glUseProgram programID
	}

	void unbind() {
		glUseProgram 0
	}

	void cleanup() {
		unbind()
		if (programID) {

			if (vertexShaderID) glDetachShader programID, vertexShaderID
			if (fragmentShaderID) glDetachShader programID, fragmentShaderID

			glDeleteProgram programID

		}
	}

	void createUniforms(List<String> uniformNames) {
		uniformNames.each {
			createUniform it
		}
	}

	void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programID, uniformName)

		if (uniformLocation < 0) throw new Exception("Could not find uniform: '$uniformName'")

		uniforms[uniformName] = uniformLocation
	}

	void createUniforms(String prefix, List<String> uniforms) {
		createUniforms ( uniforms.collect { "$prefix.$it" } )
	}

	void createPointLightUniform(String uniformName) {
		createUniforms (uniformName, ['colour', 'position', 'intensity'])
		createUniforms ("${uniformName}.att", ['constant', 'linear', 'exponent'])
	}

	void createDirectionalLightUniform(String uniformName) {
		createUniforms (uniformName, ['colour', 'direction', 'intensity'])
	}

	void createMaterialUniform(String uniformName) {
		createUniforms (uniformName, ['colour', 'useColour', 'reflectance'])
	}

	void setUniform(String uniformName, Matrix4f value) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16)
		value.get(fb)
		glUniformMatrix4fv(uniforms[uniformName], false, fb)
	}

	void setUniform(String uniformName, int value) {
		glUniform1i(uniforms[uniformName], value)
	}

	void setUniform(String uniformName, float value) {
		glUniform1f(uniforms[uniformName], value)
	}

	void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms[uniformName], value.x, value.y, value.z)
	}

	void setUniform(String uniformName, PointLight pointLight) {
		setUniform ("${uniformName}.colour", pointLight.colour)
		setUniform ("${uniformName}.position", pointLight.position)
		setUniform ("${uniformName}.intensity", pointLight.intensity)
		setUniform ("${uniformName}.att.constant", pointLight.att.constant)
		setUniform ("${uniformName}.att.linear", pointLight.att.linear)
		setUniform ("${uniformName}.att.exponent", pointLight.att.exponent)
	}

	void setUniform(String uniformName, Material mat) {
		setUniform ("${uniformName}.colour", mat.colour)
		setUniform ("${uniformName}.useColour", mat.isTextured() ? 0 : 1)
		setUniform ("${uniformName}.reflectance", mat.reflectance)
	}

	void setUniform(String uniformName, DirectionalLight directionalLight) {
		setUniform ("${uniformName}.colour", directionalLight.colour)
		setUniform ("${uniformName}.direction", directionalLight.direction)
		setUniform ("${uniformName}.intensity", directionalLight.intensity)
	}


}
