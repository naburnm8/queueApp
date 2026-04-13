package ru.naburnm8.queueapp


import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue


import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.naburnm8.queueapp.authorization.session.SessionRepository
import ru.naburnm8.queueapp.authorization.session.SessionState
import ru.naburnm8.queueapp.fake.FakeTokenStorage

@RunWith(RobolectricTestRunner::class)
class SessionRepositoryTest {

    @Test
    fun resolveTeacherSession() = runBlocking {
        val teacherJwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwZGNhMzdjYi1mMTM0LTQ5OGEtOTM1NC00ZTAwNjE1Mzc3NzQiLCJyb2xlcyI6WyJST0xFX1FPUEVSQVRPUiJdLCJpc3MiOiJhZGFwdGl2ZS1xdWV1ZSIsImV4cCI6MTc3NjEwMTg3MSwiaWF0IjoxNzc2MTAwOTcxLCJlbWFpbCI6ImFsaW50QGJrLnJ1In0.hMZ9h37kiLNBV-vXxnWKq4alcJp8AFw3lAz4D4_Xntk"
        val repo = SessionRepository(
            tokenStorage = FakeTokenStorage(teacherJwt, "refresh token")
        )

        val result = repo.resolveSession()

        assertTrue(result is SessionState.Teacher)
    }

    @Test
    fun resolveStudentSession() = runBlocking {
        val studentJwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmNzAwZGQ0NS1jMTI5LTRlYjctYTljNy1lZGRlZjgzOGQxYzkiLCJyb2xlcyI6WyJST0xFX1FDT05TVU1FUiJdLCJpc3MiOiJhZGFwdGl2ZS1xdWV1ZSIsImV4cCI6MTc3NjEwMjA5NiwiaWF0IjoxNzc2MTAxMTk2LCJlbWFpbCI6Im1haWwxQG1haWwucnUifQ.UounK1VKYDcSF3iJwoa16K_q4pfdZFtfbcFcvKdikGs"

        val repo = SessionRepository(
            tokenStorage = FakeTokenStorage(studentJwt, "refresh token")
        )

        val result = repo.resolveSession()

        assertTrue(result is SessionState.Student)
    }

}