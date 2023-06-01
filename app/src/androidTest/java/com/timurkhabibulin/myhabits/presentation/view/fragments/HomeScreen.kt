package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.timurkhabibulin.myhabits.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher


object HomeScreen : KScreen<HomeScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val habits = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = { itemType(::HabitItemScreen) }
    )

    val makeNewHabitBtn = KButton { withId(R.id.make_new_habit_Btn) }

    val filtersOrderingTV = KTextView { withId(R.id.filters_ordering_TV) }

    class HabitItemScreen(matcher: Matcher<View>) : KRecyclerItem<HabitItemScreen>(matcher) {
        val habitNameTV = KTextView { withId(R.id.habit_name_TV) }
        val habitDescriptionTV = KTextView { withId(R.id.habit_description_TV) }

        val priorityTV = KTextView { withId(R.id.priority_TV) }
        val typeTV = KTextView { withId(R.id.type_TV) }
        val colorTV = KTextView { withId(R.id.color_TV) }
        val periodTV = KTextView { withId(R.id.period_TV) }

        val doneBtn = KButton { withId(R.id.done_btn) }
    }
}