package graphics.obj

import org.joml.*


/**
* A face has the following format:
* f index/texture/normal index/texture/normal index/texture/normal
*/
protected class Face {

    private IdxGroup[] idxGroups = new IdxGroup[3]

    Face(ArrayList v) {
      3.times {
        idxGroups[it] = parse(v[it])
      }
    }

    private IdxGroup parse(String line) {

      def idxGroup = new IdxGroup()

      def tokens = line.split ("/")
      def length = tokens.length
      idxGroup.idxPos = tokens[0].toInteger() - 1

      if (length > 1) {

        def texCoord = tokens[1]

        idxGroup.idxTexCoord = texCoord.length() > 0 ? texCoord.toInteger() - 1 : IdxGroup.NO_VALUE

        if (length > 2) {
          idxGroup.idxVecNormal = tokens[2].toInteger() - 1
        }
      }

      return idxGroup
    }

}
