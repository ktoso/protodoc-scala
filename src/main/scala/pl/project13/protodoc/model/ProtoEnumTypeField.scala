package pl.project13.protodoc.model

/**
 *
 * @author Konrad Malawski
 */

case class ProtoEnumTypeField(typeName: String,
                              values: List[ProtoEnumValue]) {
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
}

case class ProtoEnumValue(valueName: String)