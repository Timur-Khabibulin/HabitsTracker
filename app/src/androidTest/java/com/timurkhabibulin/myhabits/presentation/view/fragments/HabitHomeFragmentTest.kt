package com.timurkhabibulin.myhabits.presentation.view.fragments

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.interceptors.watcher.testcase.impl.screenshot.ScreenshotStepWatcherInterceptor
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.timurkhabibulin.myhabits.presentation.view.MainActivity
import com.timurkhabibulin.myhabits.presentation.view.fragments.HomeScreen.filtersOrderingTV
import com.timurkhabibulin.myhabits.presentation.view.fragments.HomeScreen.habits
import com.timurkhabibulin.myhabits.presentation.view.fragments.HomeScreen.makeNewHabitBtn
import org.junit.Rule
import org.junit.Test


internal class HabitHomeFragmentTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple().apply {
        stepWatcherInterceptors.add(ScreenshotStepWatcherInterceptor(screenshots))
    }
) {
    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun checkHomeScreen() = run {
        val testContext = this
        HomeScreen {
            checkHabitItem(testContext)
            checkFAB(testContext)
            checkFilter(testContext)
        }
    }

    private fun checkHabitItem(testContext: TestContext<Unit>) {
        testContext.step("1. Check habit item elements") {
            habits {
                childAt<HomeScreen.HabitItemScreen>(0) {
                    HomeScreen.HabitItemScreen::class.java.typeParameters.forEach { _ ->
                        isVisible()
                        priorityTV.containsText("Приоритет")
                        typeTV.containsText("Тип")
                        colorTV.containsText("Цвет")
                        periodTV.containsText("Период")
                        doneBtn.isClickable()
                    }
                }
            }
        }
    }

    private fun checkFAB(testContext: TestContext<Unit>) {
        testContext.step("2. Check FAB") {
            makeNewHabitBtn.isVisible()
            makeNewHabitBtn.isClickable()
        }
    }

    private fun checkFilter(testContext: TestContext<Unit>) {
        testContext.step("3. Check Filter") {
            filtersOrderingTV.isVisible()
            filtersOrderingTV.containsText("Фильтры и сортировка")
        }
    }
}