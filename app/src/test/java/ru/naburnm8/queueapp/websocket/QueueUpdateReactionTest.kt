package ru.naburnm8.queueapp.websocket

import android.telecom.Call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.naburnm8.queueapp.fake.FakeTokenStorage
import ru.naburnm8.queueapp.fake.api.CallDescription
import ru.naburnm8.queueapp.fake.api.FakeQueuePlansApi
import ru.naburnm8.queueapp.fake.api.FakeQueuePlansShortApi
import ru.naburnm8.queueapp.fake.api.FakeQueueRulesApi
import ru.naburnm8.queueapp.fake.api.FakeQueuesApi
import ru.naburnm8.queueapp.fake.api.FakeSubmissionRequestsApi
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.api.QueuePlansShortApi
import ru.naburnm8.queueapp.queueConsumer.queue.queuePlans.repository.QueuePlansShortRepository
import ru.naburnm8.queueapp.queueConsumer.queue.viewmodel.QueueViewmodel
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.api.SubmissionRequestsApi
import ru.naburnm8.queueapp.queueConsumer.submissionRequests.repository.SubmissionRequestsRepository
import ru.naburnm8.queueapp.queueOperator.queues.api.QueuesApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.api.QueuePlansApi
import ru.naburnm8.queueapp.queueOperator.queues.queuePlans.repository.QueuePlansRepository
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.api.QueueRulesApi
import ru.naburnm8.queueapp.queueOperator.queues.queueRules.repository.QueueRulesRepository
import ru.naburnm8.queueapp.queueOperator.queues.repository.QueuesRepository
import ru.naburnm8.queueapp.queueOperator.queues.viewmodel.QueuesViewmodel
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class QueueUpdateReactionTest {

    lateinit var queueUpdatesManager: QueueUpdatesManager

    lateinit var queuesViewmodel: QueuesViewmodel

    lateinit var queueViewmodel: QueueViewmodel

    val callListsTeacher: MutableMap<String, MutableList<CallDescription>> = mutableMapOf()
    val callListsStudent: MutableMap<String, MutableList<CallDescription>> = mutableMapOf()

    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        queueUpdatesManager = QueueUpdatesManager(
            api = "localhost:8081",
            tokenStorage = FakeTokenStorage("test token", "refresh token")
        )

        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `queues (teacher) viewmodel loads queues on startup properly`() = runTest(dispatcher.scheduler) {
        setupTeacher()

        advanceUntilIdle()

        println("queues (teacher) viewmodel loads queues on startup properly")
        for (key in callListsTeacher.keys) {
            println("$key -> ${callListsTeacher[key]}")
        }
        println("---")

        for (key in callListsTeacher.keys) {
            if (key.contains("FakeQueuePlansApi")) {
                assertEquals("[GET_MY_QUEUE_PLANS]", callListsTeacher[key].toString())
            }
            if (key.contains("FakeQueuesApi")) {
                assertEquals("[VIEW:00000000-0000-0001-0000-000000000001]", callListsTeacher[key].toString())
            }
            if (key.contains("FakeSubmissionRequestsApi")) {
                assertEquals("[GET_ALL_SHORT:00000000-0000-0001-0000-000000000001]", callListsTeacher[key].toString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `queue (student) viewmodel loads queues on startup properly`() = runTest(dispatcher.scheduler) {
        setupStudent()

        advanceUntilIdle()

        println("queue (student) viewmodel loads queues on startup properly")
        for (key in callListsStudent.keys) {
            println("$key -> ${callListsStudent[key]}")
        }
        println("---")

        for (key in callListsStudent.keys) {
            if (key.contains("FakeQueuesApi")) {
                assertEquals("[VIEW:00000000-0000-0001-0000-000000000001]", callListsStudent[key].toString())
            }
            if (key.contains("FakeSubmissionRequestsApi")) {
                assertEquals("[GET_ALL_MY, GET_MY:00000000-0000-0001-0000-000000000001]", callListsStudent[key].toString())
            }
            if (key.contains("FakeQueuePlansShortApi")) {
                assertEquals("[GET_QUEUE_PLANS]", callListsStudent[key].toString())
            }
            if (key.contains("FakeQueueRulesApi")) {
                assertEquals("[]", callListsStudent[key].toString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `queues (teacher) viewmodel correctly reacts to an update being sent` () = runTest(dispatcher.scheduler) {
        setupTeacher()
        advanceUntilIdle()
        callListsTeacher.cleanEntries()

        queueUpdatesManager.emitUpdateForTest(UUID(1, 1), 17L)

        advanceUntilIdle()

        println("queues (teacher) viewmodel correctly reacts to an update being sent")
        for (key in callListsTeacher.keys) {
            println("$key -> ${callListsTeacher[key]}")
        }
        println("---")


        for (key in callListsTeacher.keys) {
            if (key.contains("FakeQueuePlansApi")) {
                assertEquals("[GET_QUEUE_PLAN_BY_ID:00000000-0000-0001-0000-000000000001]", callListsTeacher[key].toString())
            }
            if (key.contains("FakeQueuesApi")) {
                assertEquals("[VIEW:00000000-0000-0001-0000-000000000001]", callListsTeacher[key].toString())
            }
            if (key.contains("FakeSubmissionRequestsApi")) {
                assertEquals("[GET_ALL_SHORT:00000000-0000-0001-0000-000000000001]", callListsTeacher[key].toString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `queue (student) viewmodel correctly reacts to an update being sent`() = runTest (dispatcher.scheduler) {
        setupStudent()
        advanceUntilIdle()
        callListsStudent.cleanEntries()
        queueUpdatesManager.emitUpdateForTest(UUID(1, 1), 17L)
        advanceUntilIdle()

        println("queue (student) viewmodel correctly reacts to an update being sent")
        for (key in callListsStudent.keys) {
            println("$key -> ${callListsStudent[key]}")
        }
        println("---")

        for (key in callListsStudent.keys) {
            if (key.contains("FakeQueuesApi")) {
                assertEquals("[VIEW:00000000-0000-0001-0000-000000000001]", callListsStudent[key].toString())
            }
            if (key.contains("FakeSubmissionRequestsApi")) {
                assertEquals("[GET_MY:00000000-0000-0001-0000-000000000001]", callListsStudent[key].toString())
            }
            if (key.contains("FakeQueuePlansShortApi")) {
                assertEquals("[GET_SHORT_QUEUE_PLAN:00000000-0000-0001-0000-000000000001]", callListsStudent[key].toString())
            }
            if (key.contains("FakeQueueRulesApi")) {
                assertEquals("[]", callListsStudent[key].toString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun setupQueues(callLists: MutableMap<String, MutableList<CallDescription>>): QueuesApi {
        val fakeQueuesApi = FakeQueuesApi()
        callLists[fakeQueuesApi::class.simpleName.toString()] = fakeQueuesApi.callList
        return fakeQueuesApi
    }

    private fun setupQueuePlans(callLists: MutableMap<String, MutableList<CallDescription>>): QueuePlansApi {
        val fakeQueuePlansApi = FakeQueuePlansApi()
        callLists[fakeQueuePlansApi::class.simpleName.toString()] = fakeQueuePlansApi.callList
        return fakeQueuePlansApi
    }

    private fun setupSubmissionRequests(callLists: MutableMap<String, MutableList<CallDescription>>): SubmissionRequestsApi {
        val fakeSubmissionRequestsApi = FakeSubmissionRequestsApi()
        callLists[fakeSubmissionRequestsApi::class.simpleName.toString()] = fakeSubmissionRequestsApi.callList
        return fakeSubmissionRequestsApi
    }

    private fun setupQueueRules(callLists: MutableMap<String, MutableList<CallDescription>>): QueueRulesApi {
        val fakeQueueRulesApi = FakeQueueRulesApi()
        callLists[fakeQueueRulesApi::class.simpleName.toString()] = fakeQueueRulesApi.callList
        return fakeQueueRulesApi
    }

    private fun setupQueuePlansShort(callLists: MutableMap<String, MutableList<CallDescription>>): QueuePlansShortApi {
        val fakeQueuePlansShortApi = FakeQueuePlansShortApi()
        callLists[fakeQueuePlansShortApi::class.simpleName.toString()] = fakeQueuePlansShortApi.callList
        return fakeQueuePlansShortApi
    }



    private fun setupStudent() {
        val queuesRepository = QueuesRepository(setupQueues(callListsStudent))
        val submissionRequestsRepository = SubmissionRequestsRepository(setupSubmissionRequests(callListsStudent))
        val queuePlansShortRepository = QueuePlansShortRepository(setupQueuePlansShort(callListsStudent))
        val queueRulesRepository = QueueRulesRepository(setupQueueRules(callListsStudent))

        queueViewmodel = QueueViewmodel(
            queuePlansShortRepository,
            queuesRepository,
            submissionRequestsRepository,
            queueUpdatesManager,
            queueRulesRepository
        )
    }

    private fun setupTeacher() {
        val queuePlansRepository = QueuePlansRepository(setupQueuePlans(callListsTeacher))
        val queuesRepository = QueuesRepository(setupQueues(callListsTeacher))
        val submissionRequestsRepository = SubmissionRequestsRepository(setupSubmissionRequests(callListsTeacher))

        queuesViewmodel = QueuesViewmodel(
            queuePlansRepository,
            queuesRepository,
            submissionRequestsRepository,
            queueUpdatesManager
        )
    }

    private fun <K, V> MutableMap<K, MutableList<V>>.cleanEntries() {
        for (key in this.keys) {
            if (this[key] != null) {
                this[key]!!.clear()
            }
        }
    }
}