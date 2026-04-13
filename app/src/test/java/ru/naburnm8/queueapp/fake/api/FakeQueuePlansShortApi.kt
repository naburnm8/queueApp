package ru.naburnm8.queueapp.fake.api

import retrofit2.Response
import ru.naburnm8.queueapp.profile.response.TeacherResponse
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api.QueuePlansShortApi
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.response.QueuePlanShortResponse
import ru.naburnm8.queueapp.queueOperator.discipline.response.DisciplineDto
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.request.QueueStatus
import java.util.UUID

class FakeQueuePlansShortApi : QueuePlansShortApi {

    companion object {
        enum class CallName {
            GET_QUEUE_PLANS, GET_SHORT_QUEUE_PLAN
        }

    }

    val callList: MutableList<CallDescription> = mutableListOf()

    override suspend fun getQueuePlans(): Response<List<QueuePlanShortResponse>> {
        callList.add(
            CallDescription(
                CallName.GET_QUEUE_PLANS.toString(),
                ""
            )
        )
        return Response.success(
            listOf(
                QueuePlanShortResponse(
                    id = UUID(1, 1),
                    title = "Queue plan",
                    discipline = DisciplineDto(
                        id = UUID(1,1),
                        name = "Discipline",
                        personalAchievementsScoreLimit = 100,
                    ),
                    status = QueueStatus.DRAFT,
                    teacher = TeacherResponse(
                        id = UUID(1,1),
                        firstName = "",
                        lastName = "",
                        patronymic = "",
                        department = "",
                        telegram = "",
                        avatarUrl = ""
                    ),
                    slotDurationMinutes = 15,
                    useTime = true,
                    wTime = 1.0,
                    useDebts = true,
                    wDebts = 1.0,
                    useAchievements = true,
                    wAchievements = 1.0
                )
            )
        )
    }

    override suspend fun getShortQueuePlan(queuePlanId: UUID): Response<QueuePlanShortResponse> {
        callList.add(
            CallDescription(
                CallName.GET_SHORT_QUEUE_PLAN.toString(),
                queuePlanId.toString()
            )
        )
        return Response.success(
            QueuePlanShortResponse(
                id = UUID(1, 1),
                title = "Queue plan",
                discipline = DisciplineDto(
                    id = UUID(1,1),
                    name = "Discipline",
                    personalAchievementsScoreLimit = 100,
                ),
                status = QueueStatus.DRAFT,
                teacher = TeacherResponse(
                    id = UUID(1,1),
                    firstName = "",
                    lastName = "",
                    patronymic = "",
                    department = "",
                    telegram = "",
                    avatarUrl = ""
                ),
                slotDurationMinutes = 15,
                useTime = true,
                wTime = 1.0,
                useDebts = true,
                wDebts = 1.0,
                useAchievements = true,
                wAchievements = 1.0
            )
        )
    }
}