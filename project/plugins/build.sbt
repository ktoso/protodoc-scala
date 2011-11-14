// https://github.com/siasia/xsbt-proguard-plugin/issues/9 in order to use proguard 4.7 (Java7 compatible)
//libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-proguard-plugin" % "0.1-SNAPSHOT")

// stable version (not for java 7):
libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-proguard-plugin" % (v+"-0.1.1"))
