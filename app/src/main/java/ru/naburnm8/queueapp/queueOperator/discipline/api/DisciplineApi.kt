package ru.naburnm8.queueapp.queueOperator.discipline.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddOwnersRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.AddWorkTypesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.CreateNewDisciplineRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateDisciplinesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.request.UpdateWorkTypesRequest
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplinesResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.WorkTypesResponse
import ru.naburnm8.queueapp.request.DeleteRequest
import java.util.UUID

interface DisciplineApi {

    companion object {
        private const val URI_BASE = "/api/discipline"
    }

    @GET("$URI_BASE/{disciplineId}/owners")
    suspend fun getOwners(@Path("disciplineId") disciplineId: UUID): Response<List<TeacherResponse>>

    @POST("$URI_BASE/{disciplineId}/addOwners")
    suspend fun addOwners(
        @Path("disciplineId") disciplineId: UUID,
        @Body req: AddOwnersRequest
    ): Response<Unit>

    @GET("$URI_BASE/my")
    suspend fun getMyDisciplines() : Response<DisciplinesResponse>


    @GET(URI_BASE)
    suspend fun getDisciplines() : Response<DisciplinesResponse>

    @POST(URI_BASE)
    suspend fun createDiscipline(@Body req: CreateNewDisciplineRequest): Response<DisciplinesResponse>

    @GET("$URI_BASE/{disciplineId}/workTypes")
    suspend fun getWorkTypesById(@Path("disciplineId") disciplineId: UUID): Response<WorkTypesResponse>

    @POST("$URI_BASE/workTypes")
    suspend fun addWorkTypes(@Body req: AddWorkTypesRequest): Response<WorkTypesResponse>

    @DELETE(URI_BASE)
    suspend fun deleteDisciplines(@Body req: DeleteRequest): Response<Unit>

    @DELETE("$URI_BASE/workTypes")
    suspend fun deleteWorkTypes(@Body req: DeleteRequest): Response<Unit>


    @PUT(URI_BASE)
    suspend fun updateDisciplines(@Body req: UpdateDisciplinesRequest): Response<DisciplinesResponse>

    @PUT("$URI_BASE/workTypes")
    suspend fun updateWorkTypes(@Body req: UpdateWorkTypesRequest): Response<WorkTypesResponse>

}