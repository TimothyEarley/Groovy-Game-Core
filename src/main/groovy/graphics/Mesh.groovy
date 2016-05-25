package graphics

import org.lwjgl.BufferUtils

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


	final int vaoID, posVboID, idxVboID, texCoordsVboID, vertexCount

	final Texture texture

	Mesh(float[] positions, float[] texCoords, int[] indices, Texture texture) {

		this.texture = texture

		vertexCount = indices.length

		vaoID = glGenVertexArrays()
		glBindVertexArray(vaoID)

		def verticesBuffer = BufferUtils.createFloatBuffer(positions.length)
		verticesBuffer.put(positions).flip()
		posVboID = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, posVboID)
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

		def texCoordsBuffer = BufferUtils.createFloatBuffer(texCoords.length)
		texCoordsBuffer.put(texCoords).flip()
		texCoordsVboID = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, texCoordsVboID)
		glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW)
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

		def indicesBuffer = BufferUtils.createIntBuffer(indices.length)
		indicesBuffer.put(indices).flip()
		idxVboID = glGenBuffers()
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboID)
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)


		// unbind
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		glBindVertexArray(0)
	}

	void render() {
		// Change the texture
		glActiveTexture(GL_TEXTURE0)
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, texture.id)

		//  Bind the VAO
		glBindVertexArray(vaoID)
		glEnableVertexAttribArray(0)
		glEnableVertexAttribArray(1)

		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

		glDisableVertexAttribArray(0)
		glDisableVertexAttribArray(1)
		glBindVertexArray(0)
	}

	void cleanup() {
		glDisableVertexAttribArray(0)

		glBindBuffer(GL_ARRAY_BUFFER, 0)
		glDeleteBuffers(posVboID)
		glDeleteBuffers(idxVboID)
		glDeleteBuffers(texCoordsVboID)

		glBindVertexArray(0)
		glDeleteVertexArrays(vaoID)
	}
}
