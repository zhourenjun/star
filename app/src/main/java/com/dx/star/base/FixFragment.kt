package com.dx.star.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.ArrayDeque
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentTransaction
import com.dx.star.util.LogUtil
import java.lang.StringBuilder


@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(context: Context, manager: FragmentManager, containerId: Int) :
    FragmentNavigator(context, manager, containerId) {
    private val context: Context = context
    private val manager: FragmentManager = manager
    private val containerId: Int = containerId
    private val mBackStack = ArrayDeque<String>()

    @Nullable
    override fun navigate(
        destination: Destination,
        @Nullable args: Bundle?,
        @Nullable navOptions: NavOptions?,
        @Nullable navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag = destination.id.toString()
        val transaction: FragmentTransaction = manager.beginTransaction()
        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            fragment.arguments = args
            transaction.add(containerId, fragment, tag)
            initialNavigate = true
            mBackStack.add(tag)
        } else {
            fragment.arguments = args
            transaction.show(fragment)
        }
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()
        return if (initialNavigate) destination else null
    }

    override fun popBackStack(): Boolean {
        if (mBackStack.isEmpty()) {
            return false
        }
        //        if (manager.getBackStackEntryCount() > 0) {
//            manager.popBackStack(
//                    generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
//                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        } // else, we're on the first Fragment, so there's nothing to pop from FragmentManager
        val removeTag = mBackStack.removeLast()
        return doNavigate(removeTag)
    }

    /**
     * ???????????????A ?????? B, B ?????? C???C ??????????????????????????? ???????????????????????? ?????????????????????????????????????????? B ???????????????????????? C ??????????????? A???
     *
     * @param destId ?????? Navigation ????????? keep_state_fragment ?????? id ??????????????? action ??? id?????????
     * @return  true ???????????????false ????????????
     */
    fun closeMiddle(destId: Int): Boolean {
        val removeTag = destId.toString()
        val sb = StringBuilder("All stack is : [ ")
        for (s in mBackStack) {
            sb.append(s).append(" ")
        }
        sb.append("]").append(". Waiting for close is ").append(removeTag)
        LogUtil.i( sb.toString())
        val remove = mBackStack.remove(removeTag)
        return if (remove) {
            doNavigate(removeTag)
        } else {
            false
        }
    }

    /**
     * ?????? Fragment ????????????????????? Fragment ???????????????
     *
     * @param removeTag ????????? Fragment tag
     * @return true ???????????????false ????????????
     */
    private fun doNavigate(removeTag: String): Boolean {
        val transaction: FragmentTransaction = manager.beginTransaction()
        val removeFrag = manager.findFragmentByTag(removeTag)
        if (removeFrag != null) {
            transaction.remove(removeFrag)
        } else {
            return false
        }
        val showTag = mBackStack.last
        val showFrag = manager.findFragmentByTag(showTag)
        if (showFrag != null) {
            transaction.show(showFrag)
            transaction.setPrimaryNavigationFragment(showFrag)
            transaction.setReorderingAllowed(true)
            val stateSaved = manager.isStateSaved
            LogUtil.i("popBackStack: ?????????????????????????????????$stateSaved")
            if (stateSaved) {
                transaction.commitNowAllowingStateLoss()
            } else {
                transaction.commitNow()
            }
        } else {
            return false
        }
        return true
    }
}