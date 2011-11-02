package pl.project13.protodoc

import model._

/**
 * @author Konrad Malawski
 */
object TagVerifier extends Logger {

  def validateTags(context: ProtoType, tags: List[ProtoTag]): List[TagVerifierError] = {
    info("Verifying Proto Tag uniqueness of fields in context "+b(context))
    var errors: List[TagVerifierError]= List()

    // has duplicate tags?
    errors :::= validateDuplicateTags(context, tags)
    
    if(errors.isEmpty) ok("No errors in tags were detected in "+b(context))

    errors
  }
  
  def validateDuplicateTags(context: ProtoType, tags: List[ProtoTag]): List[DuplicateTagVerifierError] = {
    val distinctTags = tags.distinct
    val duplicatesCount = tags.lengthCompare(distinctTags.length)

    if (duplicatesCount == 0) return List.empty

    error("Found "+duplicatesCount+" duplicated tags...")
    for(duplicateTag <- tags.diff(distinctTags)) yield duplicatedTagError(context, duplicateTag)
  }
  
  private def duplicatedTagError(context: ProtoType, duplicate: ProtoTag) = {
    error("Duplicate tag ["+duplicate+"] in context: "+b(context))
    
    val duplicates = context.protoFields.filter(_.tag.tagNumber == duplicate.tagNumber)
    
    DuplicateTagVerifierError(duplicates.mkString(", "),
                              "These fields all have the Tag Number equal to " + duplicate.tagNumber)
  }
}