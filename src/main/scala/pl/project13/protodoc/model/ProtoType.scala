package pl.project13.protodoc.model

/**
 * @author Konrad Malawski
 */
abstract class ProtoType {
  /**
   * Use this field to declare which Protocol Buffers element
   * the extending class represents. It could be "message" for example.
   */
  val representationOf: String
  val packageName: String = ""
  val fullName: String
  def protoFields: List[ProtoField]

  /**
   * Used to create a new instance of the ProtoType,
   * which has the packageName updated (newPackage is prepended to the name)
   */
  def moveToPackage(newPackage: String): ProtoType
}