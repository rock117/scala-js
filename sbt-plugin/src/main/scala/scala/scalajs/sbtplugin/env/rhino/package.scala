/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js sbt plugin        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */


package scala.scalajs.sbtplugin.env

import org.mozilla.javascript._

import scala.scalajs.tools.io._

package object rhino {

  implicit class ContextOps(val self: Context) extends AnyVal {
    def evaluateFile(scope: Scriptable, file: VirtualJSFile,
        securityDomain: AnyRef = null): Any = {
      self.evaluateString(scope, file.content, file.path, 1, securityDomain)
    }
  }

  implicit class ScriptableObjectOps(val self: ScriptableObject) {
    def addFunction(name: String, function: Array[AnyRef] => Unit) = {
      val rhinoFunction =
        new BaseFunction {
          override def call(context: Context, scope: Scriptable,
              thisObj: Scriptable, args: Array[AnyRef]): AnyRef = {
            function(args)
            Undefined.instance
          }
        }

      ScriptableObject.putProperty(self, name, rhinoFunction)
    }
  }
}
