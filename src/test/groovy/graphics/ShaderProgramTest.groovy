package graphics

import spock.lang.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import static org.lwjgl.system.MemoryUtil.NULL

class ShaderProgramTest extends Specification {

  def setupSpec() {
      GLFW.glfwInit()
      GLFW.glfwWindowHint( GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE )
      def offscreen_context = GLFW.glfwCreateWindow( 16, 16, "headless", NULL, NULL )
      GLFW.glfwMakeContextCurrent( offscreen_context )
      GL.createCapabilities()
  }

  def "load a shader"() {

    given: " a vertex and fragment shader, plus gl capabilities"
    def vertexShaderSource = '''
      #version 330
      void main() {

      }
    '''
    def fragmentShaderSource = '''
      #version 330
      void main() {

      }
    '''

    expect: "the shader gets created without errors"
    def shader = new ShaderProgram()
    shader.createVertexShader(vertexShaderSource)
    shader.createFragmentShader(fragmentShaderSource)
    shader.link()
  }

}
