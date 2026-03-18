package ru.naburnm8.queueapp.queueOperator.metrics.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.naburnm8.queueapp.queueOperator.metrics.request.StudentMetricsRequest
import ru.naburnm8.queueapp.queueOperator.metrics.response.StudentMetricsResponse
import java.util.UUID

interface StudentMetricsApi {
    companion object {
        const val URI_BASE = "/api/disciplines"
    }

    @GET("$URI_BASE/{disciplineId}/metrics")
    suspend fun getMetricsByDiscipline(@Path("disciplineId") disciplineId: UUID): Response<List<StudentMetricsResponse>>

    @POST("$URI_BASE/{disciplineId}/students/{studentId}/metrics")
    suspend fun createStudentMetrics(
        @Path("disciplineId") disciplineId: UUID,
        @Path("studentId") studentId: UUID,
        @Body req: StudentMetricsRequest
    ): Response<StudentMetricsResponse>


    @PUT("$URI_BASE/{disciplineId}/student/{studentId}/metrics")
    suspend fun updateStudentMetrics(
        @Path("disciplineId") disciplineId: UUID,
        @Path("studentId") studentId: UUID,
        @Body req: StudentMetricsRequest
    ): Response<StudentMetricsResponse>


    @GET("$URI_BASE/{disciplineId}/student/{studentId}/metrics")
    suspend fun getStudentMetrics(@Path("disciplineId") disciplineId: UUID, @Path("studentId") studentId: UUID): Response<StudentMetricsResponse>

    @GET("$URI_BASE/{disciplineId}/student/my")
    suspend fun myMetricsByDiscipline(@Path("disciplineId") disciplineId: UUID): Response<StudentMetricsResponse>

    @DELETE("$URI_BASE/student/metrics/{metricId}")
    suspend fun deleteMetrics(@Path("metricId") metricId: UUID): Response<Unit>

}