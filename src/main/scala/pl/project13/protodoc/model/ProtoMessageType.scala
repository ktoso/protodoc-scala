package pl.project13.protodoc.model

import scala.List.empty

/**
 * Represents a proto message type, such as:
 * <pre>
 * <code>
 *   message MyMessage {
 *     required string name = 1;
 *   }
 * </code>
 * </pre>
 * @author Konrad Malawski
 */
case class ProtoMessageType(
    messageName: String,
    override val packageName: String = "",
    fields: List[ProtoMessageField] = empty,
    var enums: List[ProtoEnumType] = empty,
    var innerMessages: List[ProtoMessageType] = empty,
    override var comment: String = "")
  extends ProtoType
  with Commentable {


  override val fullName = if(packageName == "") messageName else packageName+"."+messageName
  override val representationOf = "message"
  override def protoFields = fields

  // map inner messages to contain this message as "package"
  innerMessages = innerMessages map { _.moveToPackage(fullName) }
  // enum to proper "package"
  enums = enums map { _.moveToPackage(fullName) }

  def moveToPackage(moveToHere: String) = {
    val newPackage = moveToHere // todo needs ".Something" checking

    copy(packageName = newPackage, comment = comment)
  }

//  override def toString = "ProtoMessageType [%s] in package: [%s], with fields: [%s] and inner messages: [%s]".format(messageName, packageName, fields, innerMessages)
  override def toString = "ProtoMessageType [%s] in package: [%s]".format(messageName, packageName)
}