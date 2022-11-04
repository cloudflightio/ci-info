package io.cloudflight.ci.info

/**
 * Singleton to detect the current Continuous Integration (CI) Server based on
 * the current [System.getenv]
 */
object CI {

    private val ciDetector = CIDetector(System.getenv())

    /**
     * @return the current [CIServer] or `null` if we are not running on a known [CIServer]
     */
    val server: CIServer? =
        ciDetector.ciServer

    /**
     * @return `true` is this code is running on a known [CIServer]
     */
    val isCI: Boolean = server != null

    /**
     * Use this property to detect if the current [CIServer] is building a pull request.
     *
     * @return `null` if the current [CIServer] does not detect pull requests from the environment
     */
    val isPR: Boolean? = ciDetector.pr
}