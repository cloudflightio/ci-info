package io.cloudflight.ci.info

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CIDetectorTest {

    @Test
    fun `single env matches`() {
        val detector = CIDetector(mapOf("BUILDKITE" to "true"))
        assertEquals(CIServer.BUILDKITE, detector.ciServer)
    }

    @Test
    fun `all env variables need to match - negative test`() {
        val detector = CIDetector(mapOf("JENKINS_URL" to "true"))
        assertNull(detector.ciServer)
    }

    @Test
    fun `all env variables need to match - positive test`() {
        val detector = CIDetector(mapOf("JENKINS_URL" to "true", "BUILD_ID" to "true"))
        assertEquals(CIServer.JENKINS, detector.ciServer)
    }

    @Test
    fun `env can be an object - value does not match`() {
        val detector = CIDetector(mapOf("CI" to "something"))
        assertNull(detector.ciServer)
    }

    @Test
    fun `env can be an object - value matches`() {
        val detector = CIDetector(mapOf("CI" to "woodpecker"))
        assertEquals(CIServer.WOODPECKER, detector.ciServer)
    }

    @Test
    fun `env can be an array - first value matches`() {
        val detector = CIDetector(mapOf("NOW_BUILDER" to "true"))
        assertEquals(CIServer.VERCEL, detector.ciServer)
    }

    @Test
    fun `env can be an array - second value matches`() {
        val detector = CIDetector(mapOf("VERCEL" to "true"))
        assertEquals(CIServer.VERCEL, detector.ciServer)
    }

    @Test
    fun `PR can be null`() {
        val detector = CIDetector(mapOf("DSARI" to "true"))
        assertEquals(CIServer.DSARI, detector.ciServer)
        assertNull(detector.pr)
    }

    @Test
    fun `PR is env`() {
        val detector = CIDetector(mapOf("CI" to "woodpecker", "CI_BUILD_EVENT" to "pull_request"))
        assertEquals(CIServer.WOODPECKER, detector.ciServer)
        assertEquals(true, detector.pr)
    }

    @Test
    fun `PR is env with value that matches`() {
        val detector = CIDetector(mapOf("BUILDKITE" to "true", "BUILDKITE_PULL_REQUEST" to "true"))
        assertEquals(CIServer.BUILDKITE, detector.ciServer)
        assertEquals(true, detector.pr)
    }

    @Test
    fun `PR is env with value that does not match`() {
        val detector = CIDetector(mapOf("BUILDKITE" to "true", "BUILDKITE_PULL_REQUEST" to "false"))
        assertEquals(CIServer.BUILDKITE, detector.ciServer)
        assertEquals(false, detector.pr)
    }

    @Test
    fun `PR should match any env - match`() {
        val detector = CIDetector(mapOf("CF_BUILD_ID" to "123", "CF_PULL_REQUEST_NUMBER" to "123"))
        assertEquals(CIServer.CODEFRESH, detector.ciServer)
        assertEquals(true, detector.pr)
    }

    @Test
    fun `PR should match any env - other match`() {
        val detector = CIDetector(mapOf("CF_BUILD_ID" to "123", "CF_PULL_REQUEST_ID" to "123"))
        assertEquals(CIServer.CODEFRESH, detector.ciServer)
        assertEquals(true, detector.pr)
    }

    @Test
    fun `PR should match any env - no match`() {
        val detector = CIDetector(mapOf("CF_BUILD_ID" to "123"))
        assertEquals(CIServer.CODEFRESH, detector.ciServer)
        assertEquals("Codefresh", detector.ciServer!!.serverName)
        assertEquals(false, detector.pr)
    }
}