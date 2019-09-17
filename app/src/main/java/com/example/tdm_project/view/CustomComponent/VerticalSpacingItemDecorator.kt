package com.example.tdm_project.view.CustomComponent

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecorator( var verticalSpaceHeight : Int)  : RecyclerView.ItemDecoration ( ){




     @Override fun itemOffsets(@NonNull outRect : Rect, @NonNull view : View, @NonNull parent : RecyclerView , @NonNull state : RecyclerView.State ) {

            outRect.top = verticalSpaceHeight
        }



}