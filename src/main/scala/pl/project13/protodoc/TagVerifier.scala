package pl.project13.protodoc

import model._

/**
 * @author Konrad Malawski
 */
object TagVerifier extends Logger {

  def validateTagUniqueness(context: ProtoType, tags: List[ProtoTag]): List[DuplicateTagVerifierError] = {
    var errors: List[DuplicateTagVerifierError] = List()

    // has duplicate tags?
    val distinctTags = tags.distinct
    val duplicatesCount = tags.lengthCompare(distinctTags.length)
    
    if (duplicatesCount != 0) {
      error("Found "+duplicatesCount+" duplicated tags")

      for(duplicateTag <- tags.diff(distinctTags)) {
        error("Duplicate tag ["+duplicateTag+"] in context: "+b(context.fullName))

        val duplicates = context.protoFields.filter(_.tag.tagNumber == duplicateTag.tagNumber)

        val err = DuplicateTagVerifierError(duplicates.mkString(", "),
                                            "These fields all have the Tag Number equal to " + duplicateTag.tagNumber)
        errors ::= err
      } 
    }

    errors
  }
}