package pl.project13.protodoc.model

import ProtoModifier._

trait Commentable {
  var cmnt: String = ""
  def comment = cmnt
  def comment_=(newComment: String) = cmnt = newComment
}

/**
 *
 */


/**
 * Represent an Int property
 */


/**
 * Represent an Int property
 */


/**
 * Represent an String property
 */


/**
 * Represent an Float property
 */


/**
 * Represent an Double property
 */


/**
 * Represent an String property
 */


/**
 * Represent an String property
 */


/**
 * Represent a Message property, that is of course also defined as Protocol Buffers resource
 * todo actually use it to represent Message instance fields
 */


/**
 * Represent a Enum property, that is of course also defined as Protocol Buffers resource
 * todo actually use it to represent Enum instance fields
 */


/**
 * Companion object, serves as factory, will be used by parser
 */
