package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
case class ProtoMessage (messageName: String,
                         packageName: String,
                         fields: List[ProtoMessageField] = List(),
                         enums: List[ProtoEnumTypeField] = List(),
                         innerMessages: List[ProtoMessage] = List()) {

//  override def toString = "ProtoMessage '%s' in %s, with: %s".format(messageName, packageName, fields)
}