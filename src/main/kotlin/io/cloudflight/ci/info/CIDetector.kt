package io.cloudflight.ci.info

internal class CIDetector(env: Map<String, String>) {

    val ciServer: CIServer?
    val pr: Boolean?

    init {
        Vendor.env = env
        val vendors = Vendor.readList()
        val v = vendors.firstOrNull { it.envMatches }
        ciServer = v?.let { CIServer.valueOf(it.constant) }
        pr = v?.isPR
    }
}