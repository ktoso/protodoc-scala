package pl.project13.protodoc.model

/**
 *
 * @author Konrad Malawski
 */

case class ProtoEnumType(typeName: String, packageName: String = "",
                         values: List[ProtoEnumValue] = List())
                         extends ProtoType
                            with Commentable {

  def asScalaSourceCode() {
    """
    /* THIS FILE HAS BEEN GENERATED AUTOMATICALY, DONT MODIFY IT BY HAND */

    package replace.me.plz

    import scala.Enumeration
    import %s._

    object %s extends Enumeration {
      type %s
      val %s = Value

      // def self reader
      // def self writer
    }
    """ format(typeName, typeName, typeName,
               values.map(_.valueName).reduceLeft(_ + ", " + _))
  }

  override val fullName = packageName + "." + typeName
  override val representationOf = "enum"
  override def protoFields = values
}
