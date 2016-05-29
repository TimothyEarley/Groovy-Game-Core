package graphics.obj

import spock.lang.Specification
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import static org.lwjgl.system.MemoryUtil.NULL

class OBJLoaderTest extends Specification {

  def setupSpec() {
      GLFW.glfwInit()
      GLFW.glfwWindowHint( GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE )
      def offscreen_context = GLFW.glfwCreateWindow( 16, 16, "headless", NULL, NULL )
      GLFW.glfwMakeContextCurrent( offscreen_context )
      GL.createCapabilities()
  }

  def "load a mesh"() {
    given: "a file to load"
    def file = "/bunny.obj"
    expect: "the mesh can be loaded"
    OBJLoader.loadMesh(file)
  }

}
