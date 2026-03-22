package ru.naburnm8.queueapp.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.naburnm8.queueapp.authorization.session.SessionRepository
import ru.naburnm8.queueapp.authorization.viewmodel.AuthorizationViewmodel
import ru.naburnm8.queueapp.authorization.viewmodel.RegistrationViewmodel
import ru.naburnm8.queueapp.navigaton.viewmodel.NavigationViewmodel
import ru.naburnm8.queueapp.profile.viewmodel.ProfileViewmodel
import ru.naburnm8.queueapp.queueOperator.discipline.viewmodel.DisciplineViewmodel
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.invitations.viewmodel.InvitationsViewmodel
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.viewmodel.QueuePlansViewmodel
import ru.naburnm8.queueapp.viewmodel.InterViewmodelBridge


val viewmodelModule = module {
    viewModel{
        NavigationViewmodel(
            get(),
            get()
        )
    }

    viewModel {
        AuthorizationViewmodel(get(),
            get(),
            get()
        )
    }

    viewModel {
        RegistrationViewmodel(
            get(),
            get(),
            get(),
            get()
        )
    }

    single {
        InterViewmodelBridge()
    }

    single {
        SessionRepository(get())
    }

    viewModel {
        ProfileViewmodel(
            get(),
            get(),
            get(),
        )
    }

    viewModel {
        DisciplineViewmodel(
            get(),
            get()
        )
    }

    viewModel {
        StudentMetricsViewmodel(
            get(),
            get(),
            get()
        )
    }
    viewModel {
        QueuePlansViewmodel(
            get(),
            get(),
            get()
        )
    }
    viewModel {
        InvitationsViewmodel(
            get(),
            get()
        )
    }
}