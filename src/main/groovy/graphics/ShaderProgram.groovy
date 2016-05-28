package graphics

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

		if (uniformLocation < 0) throw new Exception("Could not find uniform: $uniformName")

		uniforms[uniformName] = uniformLocation
	}

	void setUniform(String uniformName, Matrix4f value) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16)
		value.get(fb)
		glUniformMatrix4fv(uniforms[uniformName], false, fb)
	}

	void setUniform(String uniformName, int value) {
		glUniform1i(uniforms[uniformName], value)
	}

	void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms[uniformName], value.x, value.y, value.z)
	}


}
