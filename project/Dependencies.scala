import sbt._

object Dependencies {


  def cats(artifact: String): ModuleID    = "org.typelevel" %% artifact % "2.6.1"
//  val effect = "org.typelevel" %% "cats-effect" % "3.1.0"


  val catsCore   = cats("cats-core")

}
