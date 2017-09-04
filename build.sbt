name := "OpenNLPMosesFactored"

organization := "ie.tcd.slscs.itut"

version := "0.2-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies +=  "org.apache.opennlp" % "opennlp-tools" % "1.8.1"

mainClass in (Compile, run) := Some("ie.tcd.slscs.itut.OpenNLPMosesFactored")

libraryDependencies ++= {
        Seq(
            "org.scalatest" % "scalatest_2.10" % "2.0" % Test
        )
}
