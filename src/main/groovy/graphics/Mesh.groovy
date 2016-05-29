package graphics

import org.lwjgl.BufferUtils
import org.joml.Vector3f

import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.opengl.GL13.*
import static org.lwjgl.opengl.GL15.*
import static org.lwjgl.opengl.GL20.*
import static org.lwjgl.opengl.GL30.*
/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class Mesh {


	final int vaoID, vertexCount
	final List vboList = []

	Material material

	Mesh(float[] positions, float[] texCoords, float[] normals, int[] indices) {

		vertexCount = indices.length
		// Create VAO
		vaoID = glGenVertexArrays()
		glBindVertexArray(vaoID)

		// Positions vbo
		int vboID = glGenBuffers()
		vboList << vboID
		def verticesBuffer = BufferUtils.createFloatBuffer(positions.length)
		verticesBuffer.put(positions).flip()
		glBindBuffer(GL_ARRAY_BUFFER, vboID)
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

		// Texture coordinates vbo
		vboID = glGenBuffers()
		vboList << vboID
		def texCoordsBuffer = BufferUtils.createFloatBuffer(texCoords.length)
		texCoordsBuffer.put(texCoords).flip()
		glBindBuffer(GL_ARRAY_BUFFER, vboID)
		glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW)
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

		// normals vbo
		vboID = glGenBuffers()
		vboList << vboID
		def normalsBuffer = BufferUtils.createFloatBuffer(normals.length)
		normalsBuffer.put(normals).flip()
		glBindBuffer(GL_ARRAY_BUFFER, vboID)
		glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW)
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)

		// indices vbo
		vboID = glGenBuffers()
		vboList << vboID
		def indicesBuffer = BufferUtils.createIntBuffer(indices.length)
		indicesBuffer.put(indices).flip()
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID)
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)


		// unbind
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		glBindVertexArray(0)
	}

	void render() {

		def texture = material.texture

		if (texture) {
			// Change the texture
			glActiveTexture(GL_TEXTURE0)
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.id)
		}

		//  Bind the VAO
		glBindVertexArray(vaoID)
		glEnableVertexAttribArray 0
		glEnableVertexAttribArray 1
		glEnableVertexAttribArray 2

		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

		glDisableVertexAttribArray 0
		glDisableVertexAttribArray 1
		glDisableVertexAttribArray 2

		glBindVertexArray(0)
	}

	void cleanup() {
		glDisableVertexAttribArray(0)


		if (material) material.cleanup()

		// Delete VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		vboList.each {
			glDeleteBuffers it
		}

		// Delete VAO
		glBindVertexArray(0)
		glDeleteVertexArrays(vaoID)
	}

}
