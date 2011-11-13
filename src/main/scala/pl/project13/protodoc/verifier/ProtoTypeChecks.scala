package pl.project13.protodoc.verifier

import pl.project13.protodoc.model.ProtoType

class ProtoTypeChecks(protoType: ProtoType) {
  def isMessageType = protoType.representationOf == "message"
  def isEnumType = protoType.representationOf == "enum"
}