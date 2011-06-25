package pl.project13.protodoc.model

import ProtoModifier._

trait Commentable {
  protected var cmnt: String = ""
  def comment = cmnt
  def comment_=(newComment: String) = cmnt = newComment
}