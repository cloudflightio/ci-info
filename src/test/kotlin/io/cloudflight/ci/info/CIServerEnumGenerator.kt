package io.cloudflight.ci.info

import java.io.File

fun main() {
    val enumFile = File("src/main/kotlin/io/cloudflight/ci/info/CIServer.kt")
    if (enumFile.exists()) {
        enumFile.delete()
    }
    enumFile.appendLine("package io.cloudflight.ci.info")
    enumFile.appendLine("")
    enumFile.appendLine("enum class CIServer(val serverName: String) {")
    Vendor.readList().sortedBy { it.constant }.forEach { v ->
        enumFile.appendLine("    ${v.constant}(\"${v.name}\"),")
    }
    enumFile.appendLine("}")
}

private fun File.appendLine(line: String) {
    this.appendText(line)
    this.appendText(System.lineSeparator())
}