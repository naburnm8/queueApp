package ru.naburnm8.queueapp.modules

import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.repository.AuthorizationRepository
import ru.naburnm8.queueapp.authorization.repository.IntegrationRepository
import ru.naburnm8.queueapp.authorization.repository.RegistrationRepository
import ru.naburnm8.queueapp.profile.repository.ProfileRepository
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository
import ru.naburnm8.queueapp.queueOperator.discipline.repository.StudentDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.discipline.repository.TeacherDisciplineRepository
import ru.naburnm8.queueapp.queueOperator.metrics.repository.StudentMetricsRepository
import ru.naburnm8.queueapp.queueOperator.queues.invitations.repository.InvitationsRepository
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api.QueuePlansApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository.QueuePlansRepository
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository.QueueRulesRepository

val repositoryModule = module {
    single {
        AuthorizationRepository(get())
    }

    single {
        IntegrationRepository(get())
    }

    single {
        RegistrationRepository(get())
    }

    single {
        ProfileRepository(get())
    }

    single<StudentDisciplineRepository> {
        StudentDisciplineRepository(get())
    }

    single<TeacherDisciplineRepository> {
        TeacherDisciplineRepository(get())
    }

    single {
        StudentMetricsRepository(get())
    }
    single {
        QueueRulesRepository(get())
    }
    single {
        QueuePlansRepository(get())
    }

    single {
        InvitationsRepository(get())
    }
    single {
        SubmissionRequestsRepository(get())
    }
}