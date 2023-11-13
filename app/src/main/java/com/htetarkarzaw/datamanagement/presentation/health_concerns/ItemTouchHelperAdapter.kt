package com.htetarkarzaw.datamanagement.presentation.health_concerns

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}