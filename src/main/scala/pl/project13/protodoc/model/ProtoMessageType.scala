package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
case class ProtoMessageType (messageName: String,
                         packageName: String = "",
                         fields: List[ProtoMessageField] = List(),
                         enums: List[ProtoEnumType] = List(),
                         innerMessages: List[ProtoMessageType] = List())
                         extends ProtoType
                            with Commentable
                            with HasFullName {

  override val fullName = packageName + "." + messageName
  override val representationOf = "message"
  
//  override def toString = "ProtoMessageType '%s' in %s, with: %s".format(messageName, packageName, fields)
}