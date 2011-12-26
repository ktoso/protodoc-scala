package pl.project13.protodoc
package templating

trait PrintToFile {

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Any) = {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}