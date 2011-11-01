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
  val fullName: String
  def protoFields: List[ProtoField]
}