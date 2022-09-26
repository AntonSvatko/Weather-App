package com.test.weather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.weather.data.entity.City
import com.test.weather.databinding.ItemCityBinding

class CitiesAdapter(private val callBack: (City) -> Unit) :
    ListAdapter<City, CitiesAdapter.RecordViewHolder>(RecordsDiffUtilVoiceRec()) {

    fun submitList(listCities: List<City>, subListFrom: Int, subListTo: Int) {
        super.submitList(kotlin.runCatching { listCities.subList(subListFrom, subListTo) }
            .getOrDefault(listCities))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bindVoiceRec(getItem(position))
    }

    inner class RecordViewHolder(
        private val binding: ItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindVoiceRec(city: City) {
            binding.name = city.name
            binding.position = city.primaryKey

            itemView.setOnClickListener {
                callBack(city)
            }
            binding.executePendingBindings()
        }
    }


    class RecordsDiffUtilVoiceRec : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(
            oldItemVoiceRec: City,
            newItemVoiceRec: City
        ): Boolean {
            return oldItemVoiceRec.id == newItemVoiceRec.id
        }

        override fun areContentsTheSame(
            oldIteVoiceRec: City,
            newItemVoiceRec: City
        ): Boolean {
            return oldIteVoiceRec.name == newItemVoiceRec.name
        }
    }
}