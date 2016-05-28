package graphics.obj

import org.joml.*
import graphics.*

abstract class OBJLoader {

  static Mesh loadMesh(String filename) {

    List<Vector3f> vertices = []
    List<Vector2f> textures = []
    List<Vector3f> normals = []
    List<Face> faces = []


    OBJLoader.class.getResource(filename).eachLine {

      def tokens = it.split ( /\s+/ )

      switch ( tokens[0] ) {

        case 'v':
          vertices << new Vector3f ( tokens[1].toFloat(), tokens[2].toFloat(), tokens[3].toFloat() )
          break
        case 'vt':
          textures << new Vector2f ( tokens[1].toFloat(), tokens[2].toFloat() )
          break
        case 'vn':
          normals << new Vector3f ( tokens[1].toFloat(), tokens[2].toFloat(), tokens[3].toFloat() )
          break
        case 'f':
          faces << new Face ( tokens[1..3] )
          break

        default: // ignore
          break
      }

    }

    return reorderLists(vertices, textures, normals, faces)

  }

  static private Mesh reorderLists(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Face> faces) {

    List<Integer> indices = []

    // create and fill the positions array
    def posArr = new float[vertices.size() * 3]
    def i = 0
    vertices.each {
      posArr[i * 3] = it.x
      posArr[i * 3 + 1] = it.y
      posArr[i * 3 + 2] = it.z
      i++
    }

    //create the texture array and normals array
    def texCoordsArr = new float[vertices.size() * 2]
    def normalsArr = new float[vertices.size() * 3]


    faces.each {
      face -> face.idxGroups.each {
        processFaceVertex it, textures, normals, indices, texCoordsArr, normalsArr
      }
    }

    // fill indices array
    int[] indicesArr = indices

    // create and return the Mesh
    return new Mesh( posArr, texCoordsArr, normalsArr, indicesArr )

  }

  static private void processFaceVertex ( IdxGroup indices, List<Vector2f> textCoordList, List<Vector3f> normList, List<Integer> indicesList, float[] texCoordArr, float[] normArr ) {

    int posIndex = indices.idxPos
    indicesList << posIndex

    if (indices.idxTexCoord >= 0) {

      Vector2f texCoord = textCoordList[indices.idxTexCoord]
      texCoordArr[posIndex * 2] = texCoord.x
      texCoordArr[posIndex * 2 + 1] = 1 - texCoord.y

    }

    if (indices.idxVecNormal >= 0) {

      Vector3f normal = normList[indices.idxVecNormal]
      normArr[posIndex * 3] = normal.x
      normArr[posIndex * 3 + 1] = normal.y
      normArr[posIndex * 3 + 2] = normal.z

    }

  }

}
