package ru.geekbrains.android2.myhealth.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.databinding.ItemHealthDataBinding
import ru.geekbrains.android2.myhealth.utils.ui.getColorInt
import ru.geekbrains.android2.myhealth.data.entity.Color as Colors

class DiaryRVAdapter(val onClickListener: ((HealthData) -> Unit)? = null) : RecyclerView.Adapter<DiaryRVAdapter.HealthDataViewHolder>() {

    var data: List<HealthData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemHealthDataBinding.inflate(inflater, parent, false)
        return HealthDataViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HealthDataViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class HealthDataViewHolder(val binding: ItemHealthDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(healthData: HealthData, position: Int) {
            with(healthData) {
                if (position > 0 && healthData.date != data[position - 1].date) {
                    binding.groupDate.visibility = View.VISIBLE
                }
                if (position == 0) binding.groupDate.visibility = View.VISIBLE
                binding.groupData.let {
                    val gradient = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        intArrayOf(
                            Color.WHITE,
                            getDataColor(upperBloodPressure, binding.root.context),
                            Color.WHITE
                        )
                    )

                    it.background = gradient
                }

                binding.data = healthData
                binding.executePendingBindings()

                itemView.setOnClickListener {
                    onClickListener?.invoke(healthData)
                }
            }
        }

        private fun getDataColor(upperBloodPressure: String, context: Context): Int {
            return try {
                when (upperBloodPressure.toInt()) {
                    in 0..99 -> Colors.BLUE.getColorInt(context)
                    in 100..119 -> Colors.DARK_GREEN.getColorInt(context)
                    in 120..129 -> Colors.GREEN.getColorInt(context)
                    in 130..139 -> Colors.LIGHT_GREEN.getColorInt(context)
                    in 140..159 -> Colors.YELLOW.getColorInt(context)
                    in 160..179 -> Colors.ORANGE.getColorInt(context)
                    in 180..1000 -> Colors.RED.getColorInt(context)
                    else -> Color.WHITE
                }
            } catch (e: Throwable) {
                println(e.message)
                Color.WHITE
            }
        }
    }
}
