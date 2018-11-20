package com.readystatesoftware.chuck.internal.ui.error

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.readystatesoftware.chuck.R
import com.readystatesoftware.chuck.internal.data.RecordedThrowable

import java.text.DateFormat

/**
 * @author Olivier Perez
 */
class ErrorAdapter(private val context: Context, private val listener: ErrorListListener)
    : ListAdapter<RecordedThrowable, ErrorAdapter.ErrorViewHolder>(DiffError()) {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ErrorViewHolder {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.chuck_list_item_error, parent, false)
        return ErrorViewHolder(view)
    }

    override fun onBindViewHolder(errorViewHolder: ErrorViewHolder, position: Int) {
        val recordedThrowable = getItem(position)
        errorViewHolder.bind(recordedThrowable)
        errorViewHolder.listen(recordedThrowable)
    }

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tag: TextView = itemView.findViewById(R.id.tag)
        private val clazz: TextView = itemView.findViewById(R.id.clazz)
        private val message: TextView = itemView.findViewById(R.id.message)
        private val date: TextView= itemView.findViewById(R.id.date)

        fun bind(throwable: RecordedThrowable) {
            tag.text = throwable.tag
            clazz.text = throwable.clazz
            message.text = throwable.message
            date.text = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(throwable.date)
        }

        fun listen(throwable: RecordedThrowable) {
            itemView.setOnClickListener { this@ErrorAdapter.listener.onClick(throwable) }
        }
    }

    class DiffError : DiffUtil.ItemCallback<RecordedThrowable>() {

        override fun areItemsTheSame(t1: RecordedThrowable, t2: RecordedThrowable): Boolean {
            return t1.id == t2.id
        }

        override fun areContentsTheSame(t1: RecordedThrowable, t2: RecordedThrowable): Boolean {
            return t1 == t2
        }
    }

    interface ErrorListListener {
        fun onClick(throwable: RecordedThrowable)
    }
}
