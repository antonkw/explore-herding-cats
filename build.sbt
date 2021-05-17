import Dependencies._

ThisBuild / organization := "io.github.antonkw"
ThisBuild / scalaVersion := "3.0.0"
ThisBuild / version := "0.0.1-SNAPSHOT"

//ThisBuild / scalacOptions ++= Seq(
//  "-deprecation",
//  "-feature",
//  "-language:_",
//  "-unchecked",
//  "-Xfatal-warnings",
//  "-Ymacro-annotations"
//)

lazy val `herding-herding-cats` =
  project
    .in(file("."))
    .settings(name := "herding herding cats")
    .settings(commonSettings: _*)
    .settings(dependencies: _*)

lazy val commonSettings = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    catsCore
  )
)
