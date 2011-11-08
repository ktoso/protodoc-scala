package pl.project13.protodoc.model

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
case class ProtoMessageType (messageName: String,
                             override val packageName: String = "",
                             fields: List[ProtoMessageField] = List(),
                             enums: List[ProtoEnumType] = List(),
                             innerMessages: List[ProtoMessageType] = List())
                             extends ProtoType
                                with Commentable {


  override val fullName = if(packageName == "") messageName else packageName+"."+messageName
  override val representationOf = "message"
  override def protoFields = fields

  def moveToPackage(moveToHere: String) = {
    val newPackage = moveToHere // todo needs ".Something" checking
    val msg = ProtoMessageType(messageName = messageName,
                               packageName = newPackage,
                               fields = fields,
                               enums = enums,
                               innerMessages = innerMessages)
    msg.comment = comment

    msg
  }

  override def toString = "ProtoMessageType '%s' in %s, with: %s".format(messageName, packageName, fields)
}