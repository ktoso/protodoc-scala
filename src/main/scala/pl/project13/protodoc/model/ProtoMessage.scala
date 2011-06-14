package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
class ProtoMessage (val messageName: String,
                    val packageName: String,
                    val fields: List[ProtoMessageField],
                    val enums: List[ProtoEnumTypeField],
                    val innerMessages: List[ProtoMessage]) {

  override def toString = "ProtoMessage '%s' in %s, with: %s".format(messageName, packageName, fields)
}