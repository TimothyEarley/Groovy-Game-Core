package graphics

import de.matthiasmann.twl.utils.PNGDecoder

import java.nio.ByteBuffer

import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.opengl.GL30.glGenerateMipmap


/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class Texture {

	final id

	Texture(String filename) {

		PNGDecoder decoder = new PNGDecoder( Texture.class.getResourceAsStream(filename) )

		def buffer = ByteBuffer.allocateDirect(4 * decoder.width * decoder.height)

		decoder.decode(buffer, decoder.width * 4, PNGDecoder.Format.RGBA)

		buffer.flip()

		id = glGenTextures()

		glBindTexture(GL_TEXTURE_2D, id)

		// each component (RGBA) has one byte
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

		//upload texture
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.width,  decoder.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)


		//mipmaps
		glGenerateMipmap(GL_TEXTURE_2D)
	}

}
