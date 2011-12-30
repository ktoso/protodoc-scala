package pl.project13.protodoc.model


trait Deprecatable {
  private var dprctd = false

  def deprecated = dprctd
  def deprecated_=(newState: Boolean) { dprctd = newState }
  def isDeprecated() = deprecated
}