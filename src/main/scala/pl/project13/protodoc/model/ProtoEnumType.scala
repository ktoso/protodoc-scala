package pl.project13.protodoc.model

import scala.List.empty

case class ProtoEnumType(
    typeName: String,
    override val packageName: String = "",
    values: List[ProtoEnumValue] = empty,
    override var comment: String = "")
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

  override val fullName = if(packageName == "") typeName else packageName + "." + typeName
  override val representationOf = "enum"
  override def protoFields = values

  def moveToPackage(moveIntoHere: String) = {
    val newPackage = moveIntoHere // todo needs ".Something" checking

    copy(packageName = newPackage, comment = comment)
  }

//  override def toString = "ProtoEnumType '%s' in %s, with: %s".format(typeName, packageName, values)
  override def toString = "ProtoEnumType '%s'".format(typeName)
}
